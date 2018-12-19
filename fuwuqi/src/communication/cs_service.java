package communication;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.Color;
import java.beans.XMLEncoder;
import java.io.*;

public class cs_service 
{

	
    public static void main(String[] args) throws IOException 
    {
    

    		ServerSocket ss = new ServerSocket(30000);//是一个能够接受其他通信实体请求的类
        System.out.println("---waiting----");
        //用一个while循环可以同时响应多个客户端的请求
        while(true){
             Socket sk= ss.accept();//服务器监听对应端口的输入
             ServerThread  st = new ServerThread(sk);//创建一个线程，用线程创建一个套接字
         	try {
             st.start(); 
        		}catch(Exception e) {
        			BufferedWriter bw=null;
		        bw= new BufferedWriter(new OutputStreamWriter(sk.getOutputStream()));
		        bw.write("Illegal request");
		        bw.close();
        		}
        } 
    }
}
//服务器线程类
	
class ServerThread extends Thread implements instruction_lyh
	{
	  	Socket sk;
		static String local_file_address  ="/home/pi/Desktop/netdisk/" ; 
		static String local_tree_address  ="/home/pi/Desktop/tree/" ;
		//static String local_file_address  ="/Users/lyh/Desktop/test/" ; 
		//static String local_tree_address  ="/Users/lyh/Desktop/tree/" ;
		int wait_time=1000;
		int port=30000;
		
		private boolean wrong_flag=false;
		public String ip_address="ordinarabbit.imwork.net";

		public ServerThread(Socket sk){
		  	this.sk= sk;
	  		}

		@Override
	  	public void run() {     
		  try { 
		  dataPack redata=new dataPack();
		  InputStream in = sk.getInputStream();  
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  PrintStream  ps = new PrintStream(sk.getOutputStream());
		  String re=read_mass(br);
		  redata.toDatapack(re);
	      String checkcode=redata.getcheckCode();
	      String username = redata.getBody_nth(0);    
	      System.out.println("来自客户端的数据："+redata.getBody()); 
	      System.out.println("命令是："+checkcode);
	      
	      if (checkcode.compareTo(time_statement)==0) { 		//时间
	    	  		re_time(ps,username);
	      }else if (checkcode.compareTo(help_statement)==0) {//帮助
	    	  		re_help(ps,username);
	      }else if (checkcode.compareTo(web_statement)==0) {//网站
	    	  		re_web(ps,username);
	      }else if(checkcode.compareTo(sign_statement)==0){ //注册
	    	  		re_sign(redata,ps,username);
	      }else if(checkcode.compareTo(log_statement)==0){  //登录
	    	  		re_log(redata,ps,username);	
	      }else if (checkcode.compareTo(text_statement)==0) { //任意文本
	    	  		re_text(ps,username);
	      }else if(checkcode.compareTo(upload_statement)==0) {//接收文件
	    	  		re_file(redata,ps,in,username);
	      }else if(checkcode.compareTo(updatetree_statement)==0) {//更新目录
	    	  		se_tree(ps,username,redata,this.sk);
	      }else if(checkcode.compareTo(download_statement)==0) {//发送文件
	    	  		se_File(redata,username,this.sk);
	      }else if(checkcode.compareTo(delete_statement)==0) {//删除文件
  	  			del_File(redata,username);
	      }else if(checkcode.compareTo(rename_statement)==0) {//重命名文件
	    	  		Ren_File(redata,username);
	      }
  	  		br.close();
  	  		ps.close();
	      }
	      catch(	Exception e){
	          e.printStackTrace();
	      }
	  }
		//完成：基础操作——读取信息——请勿裸露使用
		public String read_mass(BufferedReader br) {
			String mass2="";
			try {
		        int flag=1;
		        long begin_time = System.currentTimeMillis();
		        while( !br.ready() ) {
		        		if (System.currentTimeMillis()-begin_time>wait_time){
		        			flag=0;
		        			break;
		        		}
		        };
		        if(flag==1){
		        	mass2=br.readLine();
		        }else {
		        br.close();
		        	mass2=wrong_statement;
		        	System.out.println("服务器无响应");
		        }
			}catch(Exception e) {}
				return mass2;
		}
		//完成：基础操作——发送信息——请勿裸露使用	
		public void send_mass(PrintStream ps,String mass) {
			
			String mass2="";
			try{   
					ps.println(mass);   
					ps.close();//关闭输出流包装
		        }catch(Exception e) {
		        		e.printStackTrace();
		        mass2=wrong_statement;
		        }
			
		}
		//完成：内部方法，获取本机时间 
	  	private void re_time(PrintStream ps,String username) { 
	  		dataPack data=new dataPack();
	  		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式  
	  		data.addBody(df.format(new Date()));
	  		data.setcheckCode(time_statement);
	  		send_mass(ps,data.toString());
	  } 
	  	//完成：内部方法，获取本机帮助
	  	private void re_help(PrintStream ps,String username) {
		  	dataPack data=new dataPack();
	  		data.addBody("please call 18801302019");
	  		data.setcheckCode(help_statement);
	  		send_mass(ps,data.toString());
	  	} 
	  	//完成：内部方法，获取本机网页地址
	  	private void re_web(PrintStream ps,String username) {

		dataPack data=new dataPack();
	  	data.addBody("ordinarabbit.imwork.net");
	  	data.addBody(" if you want to visit my website, use the 55649 port");
	 	send_mass(ps,data.toString());
	  }
	  	//完成：内部方法，针对sign命令进行回应 
	  	private void re_sign(dataPack redata,PrintStream ps,String username) throws IOException {
		  dataPack data=new dataPack();
  	  	String name=redata.getBody_nth(3);
  	  	String password=redata.getBody_nth(4);
		
  	  	if (name.compareTo("admin")==0 && password.compareTo("admin")==0) {
  	  		data.setcheckCode(right_statement);
  	  		data.addBody("admin_user");
  	  		File start=new File(local_file_address+username);
  	  		if(!start.exists())
  	  			start.createNewFile(); 		
  	  	}else if (name.compareTo("general")==0 && password.compareTo("general")==0) {
  	  		data.setcheckCode(right_statement);
  	  		data.addBody("general_user");
  	  		File start=new File(local_file_address+username);
  	  			if(!start.exists())
  	  				start.createNewFile();
	  		
  	  	}else {
  	  		data.setcheckCode(wrong_statement);
  	  		data.addBody("no_user");
  	  	}
  	  	send_mass(ps,data.toString());
	  }
	  	//完成：内部方法，针对log命令进行回应 
	  	private void re_log(dataPack redata,PrintStream ps,String username) throws IOException {
		dataPack data=new dataPack();
  	  	String name=redata.getBody_nth(3);
  	  	String password=redata.getBody_nth(4);  
  	  	if (name.compareTo("admin")==0 && password.compareTo("admin")==0) {
  	  		data.setcheckCode(right_statement);
  	  		data.addBody("admin_user");
  	  		File start=new File(local_file_address  +username);
  	  		if(!start.exists())
  	  			start.createNewFile(); 		
  	  	}else if (name.compareTo("general")==0 && password.compareTo("general")==0) {
  	  		data.setcheckCode(right_statement);
  	  		data.addBody("general_user");
  	  		File start=new File(local_file_address  +username);
  	  		if(!start.exists())
  	  			start.createNewFile(); 		
  	  	}else {
  	  		data.setcheckCode(wrong_statement);
  	  		data.addBody("no_this_user");
  	  	}
  	  	send_mass(ps,data.toString());
	  } 
	  	//完成：内部方法，针对文本命令进行回应 
	  	private void re_text(PrintStream ps,String username) {
		  	dataPack data=new dataPack();
	  		data.addBody(" hello ");
	  		data.addBody(" your text has been received by manager  ");
	  		send_mass(ps,data.toString());
	  }
	  	//完成：内部方法，针对上传文件命令进行回应 
	  	private void re_file(dataPack redata,PrintStream ps, InputStream in,String username) throws IOException {
		  	  dataPack data=new dataPack();
	  		  Socket fileLoaderSocket = sk;  
		      File _file=new File(redata.getBody_nth(3)+File.separator+redata.getBody_nth(4));
		      if(!_file.exists()){    
		    	  	_file.createNewFile();
		      }
		      OutputStream fileSave = new FileOutputStream(_file);  
		      byte[] buf = new byte[1024];  
		      int len = 0;  //判断是否读到文件末尾
		         while((len=in.read(buf)) != -1) 
		           {  
		            fileSave.write(buf, 0, len);  
		            fileSave.flush();   //刷新
		           }  
		        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fileLoaderSocket.getOutputStream())); 
		        data.addBody("文件上传成功");
		        data.setcheckCode(upload_statement);
		        out.flush();  		          //刷新
		        fileLoaderSocket.close();  		          //资源关闭  
		        fileSave.close();  
	  }  
	  	//测试：内部方法，针对下载文件命令进行回应 
	  	public boolean se_File(dataPack redata,String user_name,Socket filesk) throws IOException {
	  		 
	  		  String address=redata.getBody_nth(3);
	  		  File _file=new File(address);
		      if(!_file.exists()){    
		    	  	_file= new File("no this file.");
		      }
		      InputStream fileRead=null;
		      try {
				 OutputStream out = filesk.getOutputStream();   //传送文件的os
				 fileRead = new FileInputStream(_file);  
				 PrintStream ps = new PrintStream(out);
	
				 byte[] buf = new byte[1024];  
				 int len = 0; 
				 while((len=fileRead.read(buf)) != -1)
				          {  
				           out.write(buf, 0, len); 
				          }  
				 filesk.shutdownOutput();  
				 filesk.close();  
				 ps.close();
				 fileRead.close();
			}catch(Exception e) {
			
			}
			return wrong_flag;  
		   }
	  	//完成：内部方法，针对更新文件目录命令进行回应   	  
	  	private void se_tree(PrintStream ps,String username,dataPack redata,Socket filesk)  {
	  		File TREE=new File(local_tree_address+username);
		    JTree user_tree=FileTree.creat_filetree(local_file_address,username) ;
		    if(!TREE.exists()) {
		    		try {
						TREE.createNewFile();
					} catch (IOException e1) {
					}
		    }
		    //将所得的JTree写入XML文件
		    XMLEncoder e=null;
			try {
				e = new XMLEncoder( new BufferedOutputStream(new FileOutputStream(local_tree_address+username+".xml")));
			} catch (FileNotFoundException e2) {
			}
		    e.writeObject(user_tree);
		    e.close();
		    //将所得的XML文件用逐字传输	
	  		File _file=new File(local_tree_address+username+".xml");
			InputStream fileRead=null;
			try {
			 OutputStream out = filesk.getOutputStream();   //传送文件的os
			 fileRead = new FileInputStream(_file);  
			 PrintStream ps1 = new PrintStream(out);
			 byte[] buf = new byte[1024];  
			 int len = 0; 
			 while((len=fileRead.read(buf)) != -1)
			          {  
			           out.write(buf, 0, len); 
			          }  
			 filesk.shutdownOutput();  
			      //获取从服务端反馈的信息  
			 BufferedReader in = new BufferedReader(new InputStreamReader(filesk.getInputStream()));  
			 String serverBack = in.readLine();  
			 System.out.println(serverBack);    
			      //资源关闭  
			 filesk.close();  
			 ps1.close();
			 fileRead.close();
			}catch(Exception e1) {
				
			} 
	  }
	  	//待check：删除文件
	  	private void del_File(dataPack redata,String username) {
		  	  String address=redata.getBody_nth(3);
	  		  File _file=new File(address);
	  		  System.out.println(username+" want to delete "+address);
	  		  if(_file.exists() && _file.getName()!=username)
	  			  _file.delete();
	  	}	
	  	//待check：重命名文件
	  	private void Ren_File(dataPack redata,String username) {
	  	  String address=redata.getBody_nth(3);
	  	  String newname=redata.getBody_nth(4);
  		  File _file=new File(address);
  		  String suffix = _file.getName().substring(_file.getName().lastIndexOf(".") + 1);
  		  System.out.println(_file.getParent()+File.separator+newname+"."+suffix);
  		  if(_file.getName()!=username)
  		  _file.renameTo(new File(_file.getParent()+File.separator+newname+"."+suffix));
	  	}
	  	//没完成： 检查名字
	  	private boolean check_name(String line) {
		   return true;
	   }
	 	//没完成： 检查密码
	  	private boolean check_password(String line) {
		   return true;
	   }
		//完成：内部方法，获取本机ip
	  	private String Get_IP() {
			String thisIP = null;
			try
			{ 
				thisIP=""+InetAddress.getLocalHost();
			} catch (UnknownHostException e)
			{ 
			e.printStackTrace();
			}
			return thisIP;
	  	}
	  	//完成：内部方法，获取本机时间
		private String Get_TIME() {
			String thisTIME = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			thisTIME=df.format(new Date());
			return thisTIME;
		}	
	}	
