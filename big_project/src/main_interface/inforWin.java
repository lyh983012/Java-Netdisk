package main_interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import communication.Communicator;
import communication.dataPack;

import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import java.awt.Color;

public class inforWin extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String user_name) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					inforWin frame = new inforWin(user_name);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public inforWin(String username) {
		Communicator sender=new Communicator(username);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText(username+"的信息");
		textField.setEditable(false);
		panel.add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		textPane = new JTextPane();
		textPane.setBackground(Color.PINK);
		textPane.setEditable(false);
		panel.add(textPane, BorderLayout.CENTER);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式  
		String thistime=	df.format(new Date());
		String thisIP = null;
		try
		{ 
			thisIP=""+InetAddress.getLocalHost();
		} catch (UnknownHostException e){ thisIP="Internet connection error";}
		
		String hostIP;
		try {
			hostIP = ""+ InetAddress.getByName(Communicator.ip_address);
		} catch (UnknownHostException e) {
			hostIP="Host connection error";
		}
		String author=null;
		if(username=="admin")
			author="admin";
		else
			author="general";
		String capacity=null;
		capacity=sender.check_space();
		String all=
				System.getProperty("line.separator")+  
				System.getProperty("line.separator")+  
				System.getProperty("line.separator")+  
		"本机时间    :"+thistime+System.getProperty("line.separator")+  
		"本机IP     : "+thisIP+System.getProperty("line.separator")+  
		"服务器IP    :"+hostIP+System.getProperty("line.separator")+
		"本机权限    :"+author+System.getProperty("line.separator")+ 
		"容量       :"+capacity+System.getProperty("line.separator") ;
		textPane.setText(all);
		textPane.setEditable(false);
	}

}
