package main_interface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import communication.Communicator;

public class helpWin extends JFrame {

	private JFrame helpFrame_1;

	public static void main(String user_name) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					helpWin frame = new helpWin(user_name);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public helpWin(String user_name) {
		   JFrame helpFrame=this ;
		   JPanel controlPanel2;		 
		   Communicator sender=new Communicator(user_name);
		   JComboBox<String> comboBox=new JComboBox<String>();
		   JPanel statupane1=new JPanel();
		   JLabel label=new JLabel("消息类型:");
		   JButton clear = new JButton("清空消息");
		   helpFrame_1 = new JFrame("帮助");
		   helpFrame_1.setVisible(true);
		   helpFrame_1.setResizable(false);
		   helpFrame_1.setSize(400,600);
		   helpFrame_1.getContentPane().setLayout(new GridLayout(3, 1));
		   statupane1.setBorder(new EmptyBorder(5,5,5,5));
		   statupane1.setLayout(new FlowLayout(FlowLayout.CENTER,5,60));
		   statupane1.add(label);
		   
		   comboBox.addItem("需要更多帮助");
		   comboBox.addItem("作者个人网站");
		   comboBox.addItem("获取服务器时间");
		   comboBox.addItem("检查存储空间");
		   comboBox.addItem("给作者留言");
		   statupane1.add(comboBox);
		   
		    controlPanel2 = new JPanel();
		    FlowLayout fl_controlPanel2 = new FlowLayout();
		    fl_controlPanel2.setVgap(60);
		    controlPanel2.setLayout(fl_controlPanel2);
		  
		   helpFrame_1.getContentPane().add(statupane1);
		   helpFrame_1.getContentPane().add(controlPanel2);
		   
		   JScrollPane scrollPane = new JScrollPane();
		   helpFrame_1.getContentPane().add(scrollPane);
		   JTextPane statusLabel2 = new JTextPane();
		   scrollPane.setViewportView(statusLabel2);
		   statusLabel2.setSize(200,30);
		   
		   JScrollBar scrollBar = new JScrollBar();
		   scrollPane.setRowHeaderView(scrollBar);
		   JButton sendButton = new JButton("确定");
		   controlPanel2.add(sendButton);
		   sendButton.setVerticalAlignment(SwingConstants.TOP);
		   sendButton.setSize(40,30);
		   sendButton.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		            String send0=(String)comboBox.getSelectedItem();
		            String tauch = null;
		            if(send0.compareTo("需要更多帮助")==0) {
		            	tauch=sender.help();
		            }else if(send0.compareTo("作者个人网站")==0) {
		            	tauch=sender.check_web();
		            }else if(send0.compareTo("获取服务器时间")==0) {
		            	tauch=sender.check_time();
		            }else if(send0.compareTo("检查存储空间")==0) {
		            	tauch=sender.check_space();
		            }else if(send0.compareTo("space")==0) {
		            	tauch=sender.check_space();
		            }else {
		            	send0=JOptionPane.showInputDialog( "输入留言" );
		            	sender.any_text(send0);
			        }
		            if (tauch != null) {            	   
		               statusLabel2.setText("来自服务器的响应 :" + tauch);
		            }
		            else{
		               statusLabel2.setText("no responce" );           
		            }      
		         }
		      });
		   
		   controlPanel2.add(clear);
	}

}
