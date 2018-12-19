package main_interface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import communication.Communicator;
import communication.filenode;

/**
 * 
这个类写的主界面下属的上传文件窗口
 *
 */

public class uploadWin extends JFrame {
	
	/**
	 * version 2.0
	 */
	private static final long serialVersionUID = 1L;
	private File _file;
	private File _address;
	public static void main(JTree Filetree,String name, JTextPane textPane,JPanel tree) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					uploadWin frame = new uploadWin(Filetree,name,textPane,tree);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public uploadWin(JTree Filetree,String name,JTextPane textPane,JPanel tree) {
		//使用了插件进行布局调整。
		   JFrame submainFrame;
		   JLabel headerLabel;
		   JLabel statusLabel;
		   JLabel statusLabel2;
		   JPanel controlPanel;
		   JPanel controlPanel2;
		   Communicator sender =new Communicator(name);
		   JButton AddressButton = new JButton("选择地址");
		   JButton showFileDialogButton = new JButton("选择文件");
		   JButton send = new JButton("发送");
		   
		   submainFrame = new JFrame("文件上传窗口");
		   submainFrame.setSize(400,600);
		   submainFrame.getContentPane().setLayout(new GridLayout(6, 1));
		   headerLabel = new JLabel("", JLabel.CENTER);
		   headerLabel.setBackground(new Color(224, 255, 255));
		   statusLabel = new JLabel("选择的文件：",JLabel.CENTER); 
		   statusLabel2 = new JLabel("选择的地址：",JLabel.CENTER); 
		   
		   statusLabel.setSize(350,30);
		   statusLabel2.setSize(350,30);
		   
		   controlPanel = new JPanel();
		   FlowLayout fl_controlPanel = new FlowLayout();
		   fl_controlPanel.setVgap(30);
		   controlPanel.setLayout(fl_controlPanel);
		   
		   controlPanel2 = new JPanel();
		   FlowLayout fl_controlPanel2 = new FlowLayout();
		   fl_controlPanel2.setVgap(30);
		   controlPanel2.setLayout(fl_controlPanel2);
		   
		   headerLabel.setText("选择你需要上传的文件");
		   
		   controlPanel.add(showFileDialogButton);
		   controlPanel2.add(AddressButton);
		   controlPanel2.add(send);
		   
		   submainFrame.getContentPane().add(headerLabel);
		   submainFrame.getContentPane().add(controlPanel);
		   submainFrame.getContentPane().add(statusLabel);
		   submainFrame.getContentPane().add(controlPanel2);
		   submainFrame.getContentPane().add(statusLabel2);
		   
		   JPanel panel = new JPanel();
		   submainFrame.getContentPane().add(panel);
		   panel.setLayout(null);
		   
		   JProgressBar upProgress = new JProgressBar();
		   upProgress.setLocation(85, 30);
		   panel.add(upProgress);
		   upProgress.setValue(1);
		   upProgress.setToolTipText("0");
		   upProgress.setString("上传进度");
		   upProgress.setStringPainted(true);
		   upProgress.setIndeterminate(false);
		   upProgress.setSize(227,35);
		   
		   JFileChooser  fileDialog = new JFileChooser();
		   //选择文件的对话窗口
		   showFileDialogButton.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		            int return1 = fileDialog.showOpenDialog(submainFrame);
		            if (return1 == JFileChooser.APPROVE_OPTION) {
		            	   _file = fileDialog.getSelectedFile();//进入java io file 缓存流
		               statusLabel.setText("选择了文件 :" + _file.getName());
		            }
		            else{
		               statusLabel.setText("选择了文件  : 没选到文件" );           
		            }      
		         }
		      });
		   //选择地址的对话窗口
		   AddressButton.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		        	 	JDialog j = new JDialog();
		        	 	j.setSize(400,400);
		        	 	j.setResizable(false);
			     	j.getContentPane().setLayout(new FlowLayout());
			     	j.getContentPane().add(Filetree);
		     		JButton button = new JButton("选中");
		     		j.getContentPane().add(button);
		     		button.addActionListener(new ActionListener() {
		     			@Override
		     			public void actionPerformed(ActionEvent e) {	
		     				 filenode node = (filenode) Filetree.getLastSelectedPathComponent();
		     	             if (node == null)
			                    return;
		     	           
		     	             _address=new File(node.path);
		     	            System.out.println(node.path.contains("."));   //路径

		 		            if (!node.path.contains(".")) {
		 		               statusLabel2.setText("选择了地址 :"+ _address);
			     	           j.dispose();
		 		            }
		 		            else{
		 		               statusLabel2.setText("选择了地址 : 没选到文件夹");           
		 		            }    
		     			}
		     		});
		     	
		     		j.setVisible(true);
		         }
		      });
		   //确定传输的按钮
		   send.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		           if(_file!= null && _address!=null) {
		        	   sender.se_File(_file, _address.getPath(), upProgress,send);
		        	   //发送之后刷新目录
		        		try {	 
			        		 JTree filetree=sender.reTree(name);
			        		 Main_general.refresh_file_tree(tree,filetree,200,50,textPane);
			     		} catch (Exception e1) {
			     			e1.printStackTrace();
			     		}
		           }
		         }
		      });

		   submainFrame.setVisible(true);  
		}
}

