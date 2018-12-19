package login;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import communication.Communicator;
import communication.instruction_lyh;
import main_interface.Main_general;

public class Logwin extends JFrame {
	

/**
 * 
这个类写的是一开始的登陆界面，包括附属的两个子窗口。
由于大量的信息共享，所以写在同一个public类中，用函数的方式建立窗口
 *
 */
	
	private static final long serialVersionUID = 1L;
	public String user_name;
	private JFrame mainFrame;
	public int  login_state;
	public int  sign_in_state;

	//完成：调用方法
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Logwin frame = new Logwin();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Logwin(){
			//设置主帧
			mainFrame = new JFrame("登录界面");
			mainFrame.setSize(400,500);
			mainFrame.setResizable(false);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//标题
			JLabel headline=new JLabel("223A工作室 林逸晗荣誉开发",JLabel.CENTER);
			headline.setBackground(new Color(245, 255, 250));
			headline.setFont(new Font("Copperplate", Font.BOLD, 15));
			headline.setSize(100,20);
			ImageIcon icon =new ImageIcon(get_icon_resource("/resource/cool.png"));
			JLabel commentlabel = new JLabel("", icon,JLabel.CENTER);
		    
			JPanel temp=new JPanel();
			temp.setBackground(new Color(245, 245, 245));temp.setLayout(null);//新建1级panel来控制几个组件的布局

		    JButton jbtlogin = new JButton("登陆");
		    JButton jbtsign_in = new JButton("注册");
		    JButton jbtexit = new JButton("退出");
		    jbtexit.setBounds(296, 100, 60, 35); 
		    jbtsign_in.setBounds(40, 100, 60, 35); 
		    jbtlogin.setBounds(171, 100, 60, 35);  
		    
		    temp.add(jbtlogin);
	        temp.add(jbtsign_in);
	        temp.add(jbtexit);
		    
		    JPanel p2 = new JPanel(new GridLayout(3,1)); //新建2级的panel来控制几个组件的布局
		    p2.add(headline);
		    p2.add(commentlabel);
		    p2.add(temp);

		    mainFrame.getContentPane().add(p2);
	        mainFrame.setVisible(true);
		        
	        //三个按钮的相应动作
	        jbtlogin.addActionListener(new ActionListener(){
		        		@Override
		            public void actionPerformed(ActionEvent e) {
		        			creat_login();
		            }
		         });
		    jbtsign_in.addActionListener(new ActionListener(){
	        		@Override
	            public void actionPerformed(ActionEvent e) {
	        			creat_sign_in();
	            }
	         });
		    jbtexit.addActionListener(new ActionListener(){
	        		@Override
	            public void actionPerformed(ActionEvent e) {
	        			System.exit(0);
	            }
	         });
		    
		    System.out.println("end");
		        
		 }
		
	private int creat_login() {
			
			
			JFrame login = new JFrame("登陆");
			login.setSize(400,200);
			
			login.setResizable(false);
			login.getContentPane().setLayout(new GridLayout(3,2,5,5));//行列元件数目，水平竖直间隔
			
			//第1行
			login.getContentPane().add(new JLabel("用户名"));
			JTextField name=new JTextField(8);
			name.setSize(80,40);
			login.getContentPane().add(name);
			
			//第2行
			login.getContentPane().add(new JLabel("密码"));
			JTextField password=new JTextField(8);
			password.setSize(80,40);
			login.getContentPane().add(password);
			
			//第3行 添加按钮
			JButton yes =new JButton("确认");
			yes.setSize(40,20);
			JButton cancel =new JButton("取消");
			cancel.setSize(40,20);
			login.getContentPane().add(yes);
			login.getContentPane().add(cancel);
			
	        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        login.setVisible(true);
			//设置按钮动作
			yes.addActionListener(new ActionListener(){
	    		@Override
	    		public void actionPerformed(ActionEvent e) {
	    			Communicator sender= new Communicator(name.getText());
	    			sender.log(name.getText(),password.getText());
	    			boolean flag=(sender.redata.getcheckCode().compareTo(instruction_lyh.right_statement)==0);
	    			System.out.println(flag);
	    			System.out.println(sender.redata.getBody_nth(0));
        			if(flag && sender.redata.getBody_nth(0).compareTo("general_user")==0) {
        				 creat_success_window();
        				 user_name=name.getText();
        				 Main_general.main(user_name);
        				 mainFrame.dispose();
        			}else if(flag && sender.redata.getBody_nth(0).compareTo("admin_user")==0) {
        				 creat_success_window();
       				 user_name=name.getText();
       				 Main_general.main(user_name);
       				 mainFrame.dispose(); 
        			}else{
        				creat_wrong_window();//如果登陆failed，进入失败页面
        				};	
        			login.dispose();
        			}
	    		});
			cancel.addActionListener(new ActionListener(){
	    		@Override
	        public void actionPerformed(ActionEvent e) {
	    			login_state=0;
	    			login.dispose();}
	    		});
			return 1;
		 
		 }
		
	private int creat_sign_in() {
				

		JFrame sign_in = new JFrame("注册");
		sign_in.setSize(400,200);
		sign_in.setResizable(false);
	 	sign_in.getContentPane().setLayout(new GridLayout(4,2,5,5));
			 	//第1行
	 	JTextField name=new JTextField(8);
	 	sign_in.getContentPane().add(new JLabel("用户名"));
	 	sign_in.getContentPane().add(name);
	
			 	//第2行
		JTextField pass=new JTextField(8);
	 	sign_in.getContentPane().add(new JLabel("密码"));
	 	sign_in.getContentPane().add(pass);
			 	
			 	//第3行
		JTextField pass2=new JTextField(8);
	 	sign_in.getContentPane().add(new JLabel("确认密码"));
	 	sign_in.getContentPane().add(pass2);
			 	
			 	//第4行，	put buttons
		JButton yes =new JButton("确定");
		yes.setSize(40,20);
		JButton cancel =new JButton("取消");
		cancel.setSize(40,20);
		sign_in.getContentPane().add(yes);
		sign_in.getContentPane().add(cancel);
		sign_in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sign_in.setVisible(true);
				
		//set actions
			yes.addActionListener(new ActionListener(){
			    		@Override
			        public void actionPerformed(ActionEvent e) {
			    			try {
			    		String Users_name=name.getText();		
			    		String Pass_word=pass.getText();
		    			String Pass_word2=pass2.getText();
		    			Communicator sender= new Communicator(Users_name);
		    			sender.sign(Users_name,Pass_word);
		    			if(Users_name.compareTo("")!=0 && 
		    					Pass_word.compareTo(Pass_word2)==0 &&
		    					Pass_word2.compareTo("")!=0 && 
		    					sender.sign(name.getText(),pass.getText())){
			   				creat_success_window();
			   				sign_in_state=1;
			    			}else {
			    				sign_in_state=0;
			    			}
		    			
				   	if(sign_in_state==1) {
				    			creat_login();		    				
			 			}else if (sign_in_state==0){
			 				creat_wrong_window();
			   			};	
			   			sign_in.dispose();
			    		}catch(Exception e1){
			    			System.out.println("pleas retry");
			    		}
			    		}});
			cancel.addActionListener(new ActionListener(){
				    		@Override
				       public void actionPerformed(ActionEvent e) {
			    			sign_in_state=0;
			    			sign_in.dispose();}});
			
		return 0;
			 
		 }
	
 	private String get_icon_resource(String name) {
		File temp=new File("");
		System.out.println(temp.getAbsolutePath());
		return temp.getAbsolutePath()+name;
	}
	
	//创建成功窗口
	protected void creat_success_window(){
		JFrame sus =new JFrame("success");
		sus.setSize(200,20);
		sus.setVisible(false);
		JOptionPane.showMessageDialog(null, "success！");  
		try
		{
		Thread.currentThread().sleep(1000);//毫秒
		}
		catch(Exception e){}
		sus.dispose();
		
	}
	//创建失败窗口
	protected void creat_wrong_window(){
		JFrame sus =new JFrame("Wrong");
		sus.setSize(200,20);
		sus.setVisible(false);
		JOptionPane.showMessageDialog(null, "Wrong！");  
		try
		{
		Thread.currentThread().sleep(1000);//毫秒
		}
		catch(Exception e){}
		sus.dispose();
	}
	//创建任意提示窗口
	protected void creat_warning_window(String massage){

		JFrame sus =new JFrame("warning");
		sus.setSize(200,20);
		sus.setVisible(false);
		JOptionPane.showMessageDialog(null,massage);  
		try
		{
		Thread.currentThread().sleep(1000);//毫秒
		}
		catch(Exception e){}
		sus.dispose();
		
	}
	
}




