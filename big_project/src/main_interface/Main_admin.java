package main_interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.tree.*;

import communication.Communicator;
import communication.FileTree;

import javax.swing.border.MatteBorder;

public class Main_admin extends JFrame {


	private File _file;
	private File _address;
	public String user_name;
	private JPanel contentPane;

	public static void main(String args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_general frame = new Main_general(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Main_admin(String n) {
		super(n);
		getContentPane().setBackground(UIManager.getColor("Button.select"));
		user_name=n;
		Main_admin mainFrame=this;
	    mainFrame.setSize(800,400);//Set the frame size
	    mainFrame.setResizable(false);
	    mainFrame.getContentPane().setLayout(null);
	    JPanel tool=new JPanel();
	    JPanel Tree=new JPanel();
	    JToolBar toolbar = new JToolBar();
	    
	    tool.setLayout(new GridLayout(4,1));
	    { 
		    JButton upload = new JButton("上传文件");
		    upload.setSize(40,20);
		    
		    JButton download = new JButton("下载文件");
		    download.setSize(40,20);
		    
		    JButton setting = new JButton("设置");
		    setting.setSize(40,20);
		    
		    JButton help = new JButton("一些帮助");
		    help.setSize(40,20);
		    
		    
		    tool.add(upload);
		    tool.add(download);
		    tool.add(setting);
		    tool.add(help); 
		    	tool.setBackground(Color.PINK);
		    	tool.setBorder(BorderFactory.createLoweredBevelBorder());
		    upload.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		        	 uploadWin.main(creat_test_tree(user_name), user_name,null, Tree);
		         }
		    });
		    help.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		        	 helpWin.main(user_name);
		         }
		    });
	    }
	   
 
	    tool.setBounds(51, 79, 111, 211); 
	    mainFrame.getContentPane().add(tool);
	    
	    JButton exit = new JButton("退出");
	    exit.setBounds(51, 321, 117, 29);
	    getContentPane().add(exit);
	    
	    JPanel panel = new JPanel();
	    panel.setBorder(new MatteBorder(4, 2, 2, 2, (Color) new Color(192, 192, 192)));
	    panel.setBounds(626, 50, 153, 300);
	    getContentPane().add(panel);
	    panel.setLayout(null);
	    
	    JTextPane textPane = new JTextPane();
	    textPane.setEditable(false);
	    textPane.setBackground(new Color(255, 250, 250));
	    textPane.setBounds(6, 81, 141, 213);
	    panel.add(textPane);
	    
	    creat_file_tree(Tree,creat_test_tree(user_name),200,50,textPane);
	    mainFrame.getContentPane().add(Tree);
	    
	    JButton delete = new JButton("删除所选文件");
	    delete.setBounds(6, 40, 141, 29);
	    JButton rename = new JButton("重命名所选文件");
	    rename.setBounds(6, 8, 141, 29);
	    panel.add(delete);
	    panel.add(rename);
	    
	    Scrollbar scrollbar = new Scrollbar();
	    scrollbar.setBounds(132, 87, 15, 207);
	    panel.add(scrollbar);
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setVisible(true);

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
	
	//通过受到的文件树创建
	protected void creat_file_tree(JPanel panel,JTree filetree, int x,int y,JTextPane file_pro){

	        panel.setLayout(new BorderLayout());	        
	        filetree.setShowsRootHandles(false);
	        filetree.setEditable(true);
	        filetree.addTreeSelectionListener(new TreeSelectionListener() {
	            @Override
	            public void valueChanged(TreeSelectionEvent e) {
	                System.out.println("当前被选中的节点: " + e.getPath());
	            }
	        });
	        JScrollPane scrollPane = new JScrollPane(filetree);
	        panel.add(scrollPane, BorderLayout.CENTER);
	        panel.setBounds(x, y, 400, 300); 
	        filetree.addTreeSelectionListener(new TreeSelectionListener() {
	            @Override
	            public void valueChanged(TreeSelectionEvent e) {
	                DefaultMutableTreeNode node = (DefaultMutableTreeNode) filetree.getLastSelectedPathComponent();
	                if (node == null)
	                    return;
	                Object object = node.getUserObject();
	                if (node.isLeaf()) {
	                	file_pro.setText(object.toString());
	                }
	            }
	        });
	}

	protected JTree creat_test_tree(String username) {
		 return FileTree.creat_filetree(Communicator.des,username) ;
	}
}


