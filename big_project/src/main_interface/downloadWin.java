package main_interface;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.print.attribute.standard.PrinterLocation;
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
这个类写的主界面下属的下载文件窗口
 *
 */

public class downloadWin extends JFrame {
	
	/**
	 * version 2.0
	 */
	long len=1;
	private static final long serialVersionUID = 1L;
	private File _file;
	private File _address;

	public static void main(JTree Filetree,String name) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					downloadWin frame = new downloadWin(Filetree,name);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public downloadWin(JTree Filetree,String name) {
		//使用了插件进行布局调整。
		   JFrame submainFrame;
		   JLabel headerLabel;
		   JLabel statusLabel;
		   JLabel statusLabel2;
		   JPanel controlPanel;
		   JPanel controlPanel2;
		   Communicator sender =new Communicator(name);
		   JButton AddressButton = new JButton("选择接收地址");
		   JButton showFileDialogButton = new JButton("选择文件");
		   JButton send = new JButton("下载");
		   submainFrame = new JFrame("文件下载窗口");
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
		   
		   headerLabel.setText("选择你需要下载的文件");
		   
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
		   upProgress.setString("下载进度");
		   upProgress.setStringPainted(true);
		   upProgress.setIndeterminate(false);
		   upProgress.setSize(227,35);

		   //选择文件的对话窗口
		   showFileDialogButton.addActionListener(new ActionListener() {
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
		     	            _file=new File(node.path);
		     	           	len= node._size;
		     	            if (!_file.isDirectory()) {
			            		statusLabel.setText("选择了文件 :"+ _file);
		     	            }
		     	            else{
			            		statusLabel.setText("选择了文件  : 没选到文件");           
		     	            }    
		     	             j.dispose();
		     	             
		     			}
		     		});
		     		
		     		j.setVisible(true);
		         }
		      });
		  
		   //选择地址的对话窗口
		   AddressButton.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
			        	 JFileChooser fc=new JFileChooser();
			        	 fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
			        	 fc.showOpenDialog(submainFrame);
			        	 _address=fc.getSelectedFile();
		           if (_address!=null && _address.isDirectory()) {
		               statusLabel2.setText("选择了地址 :"+ _address);
		            }
		            else{
		               statusLabel2.setText("选择了地址 : 没选到文件夹");           
		            }    
		         }
		      });
		   //确定传输的按钮
		   send.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		           if(_file!= null && _address!=null) {
		        	   try {
		        		    System.out.println(_address.getPath()+_file.length());
						sender.re_file(_file, _address.getPath(), upProgress,send,len);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		           }
		         }
		      });

		   submainFrame.setVisible(true);  
		}
}

