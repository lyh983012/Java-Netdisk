package main_interface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.tree.*;
import communication.Communicator;
import communication.FileTree;
import communication.filenode;
import javax.swing.border.MatteBorder;

/**
 * 
这个类写的是一般用户的操作界面
 *
 */

public class Main_general extends JFrame{

	/**
	 * verson.2.0
	 */
	
	private static final long serialVersionUID = 1L;
	JTree filetree;
	Main_general mainFrame;
	public String user_name;
	
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

	public Main_general(String n) {
		//用缩进的方式大概区分了各个panel的层次
		super(n);
		user_name=n;
		Communicator sender =new Communicator(n);
		mainFrame=this;
		    mainFrame.setSize(800,400);
		    mainFrame.setResizable(false);
		    mainFrame.getContentPane().setLayout(null);
	    JPanel Tree=new JPanel();
	    JPanel tool=new JPanel();
		    tool.setLayout(new GridLayout(4,1)); 
			    JButton upload = new JButton("上传文件");
			    upload.setSize(40,20);
			    JButton download = new JButton("下载文件");
			    download.setSize(40,20);
			    JButton infor = new JButton("信息查看");
			    infor.setSize(40,20);
			    JButton help = new JButton("一些帮助");
			    help.setSize(40,20);
		    tool.add(upload);
		    tool.add(download);
		    tool.add(infor);
		    tool.add(help); 
		    	tool.setBackground(Color.PINK);
		    	tool.setBorder(BorderFactory.createLoweredBevelBorder());
		    tool.setBounds(51, 91, 111, 211); 
		    mainFrame.getContentPane().add(tool);
		    JButton exit = new JButton("退出");
		    exit.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		        	 mainFrame.dispose();
		         }
		    });
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
	    JTree creattree=null;
		try {
			creattree = sender.reTree(n);
			filetree=creat_file_tree(Tree,creattree,200,50,textPane);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
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
		    JButton refresh = new JButton("刷新文件目录");
		    refresh.setBounds(51, 50, 108, 29);
		    getContentPane().add(refresh);
	    //手动刷新文件目录
	    refresh.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 try {
	        		 filetree=sender.reTree(n);
	        		 refresh_file_tree(Tree,filetree,200,50,textPane);
	     		} catch (Exception e1) {
	     			e1.printStackTrace();
	     		}
	         }
	    });
	    //进入文件上传
	    upload.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 try {	 
	        		 uploadWin.main(filetree, user_name,textPane,Tree);
	     		} catch (Exception e1) {
	     			e1.printStackTrace();
	     		}
	         }
	    });
	    //进入帮主界面
	    help.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 helpWin.main(user_name);
	         }
	    });
	    //进入文件下载
	    download.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 downloadWin.main(filetree, user_name);
	         }
	    });
	    //一些基本的设置修改
	    infor.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 inforWin.main(user_name);
	         }
	    });
	    //重命名文件
	    rename.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 filenode  node =(filenode) filetree.getLastSelectedPathComponent();
	        	 File file=node.thisfile;
	        	 String aimadress=node.path;
	        	 String newname=JOptionPane.showInputDialog( "输入新名字" );
	        	 sender.rename_file(file, aimadress, newname);
	         }
	    });
	    //删除文件
	    delete.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 filenode  node =(filenode) filetree.getLastSelectedPathComponent();
	        	 System.out.println("delete"+node.path);
	        	 File file=node.thisfile;
	        	 String aimadress=node.path;
	        	 sender.delet_file(file, aimadress);
	         }
	    });
	    //退出并清理
	    exit.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 mainFrame.dispose();
	         }
	    });
	   
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setVisible(true);

	}

	//通过受到的文件树创建
	static public  JTree creat_file_tree(JPanel panel,JTree filetree, int x,int y,JTextPane file_pro){

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
	            		filenode  node =(filenode) filetree.getLastSelectedPathComponent();
	                if (node == null)
	                    return;
	                if (node.isLeaf()) {
	                	file_pro.setText(node.toString2());
	                }
	            }
	        });
	        return filetree;
	}
	//刷新文件树
	static public JTree refresh_file_tree(JPanel panel,JTree filetree, int x,int y,JTextPane file_pro){

		System.out.println("refreshing...");
		panel.removeAll();
		panel.repaint();

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
			panel.revalidate();
	        filetree.addTreeSelectionListener(new TreeSelectionListener() {
	            @Override
	            public void valueChanged(TreeSelectionEvent e) {
	            		filenode  node =(filenode) filetree.getLastSelectedPathComponent();
	                if (node == null)
	                    return;
	                if (node.isLeaf()) {
	                	file_pro.setText(node.toString2());
	                }
	            }
	        });
	        return filetree;
}
	//创建文件树，实际上只是封装了另外一个类的方法
	static public  JTree creat_tree(String username) {
        return FileTree.creat_filetree(Communicator.des,username) ;
	}

}


