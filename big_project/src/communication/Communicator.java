package communication;

import java.net.*;
import java.text.SimpleDateFormat;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;


public class Communicator implements instruction_lyh
{

	private dataPack data;
	public dataPack redata;
	static public String des;
	static public String ip_address="ordinarabbit.imwork.net";
	static public int outport=39169;
	//static int outport=30000;
	//static public String ip_address="127.0.0.1";	
	private long wait_time=1000;
	private boolean wrong_flag=false;
	private long _progress;
	private long _filesize;
	private String user_name;
	
	class Progress extends Thread{//自定义类progress
	    private JProgressBar progressBar;
	    public Progress(JProgressBar progressBar)
	    {
	        this.progressBar = progressBar;
	    }
	    public void run()
	    {
	    		double percent=(_progress*1.0)/(_filesize*1.0);
	        progressBar.setValue((int)(percent*100));  //进度值
	        progressBar.setIndeterminate(false);  //采用确定的进度条
	    }
	}
	
	public Communicator(String user_name) {  
		this.wrong_flag=false;
		this.user_name=user_name;
		File directory = new File("."); 
		try {
			des=directory.getCanonicalPath();
			System.out.println(des);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	//完成：内部方法，获取本机IP
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
	//完成：改变服务器地址
	public void Change_idadress(String address) {
	if(address.compareTo("default")==0) {
		 Communicator.ip_address="ordinarabbit.imwork.net";
	}else {
		if(address!=null && !address.isEmpty()) {
			Communicator.ip_address=address;
		}
	}
	}
	//完成：改变通信口
	public void Change_port(int port) {
		Communicator.outport=port;
	}
	//完成：获得错误标志
	public boolean get_wrongflag() {
		return wrong_flag;
	}
	//完成：内部方法，读取消息
	private String read_mass(BufferedReader br) {
	    redata=new dataPack();
	    data=new dataPack();
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
	        	mass2=wrong_statement;
	        	System.out.println("read mass time out "+flag+br.readLine());
	        }
		}catch(Exception e) {}
			return mass2;
	}
	//完成：内部方法，发送消息
	private void send_mass(PrintStream ps,String mass) {
	     redata=new dataPack();
	     data=new dataPack();
	   try{
	        ps.println(mass);   
	        }catch(Exception e) {
	        	System.out.println(">>>send die<<<");
	        	e.printStackTrace();
	        	wrong_flag=false;
	        }
	   }
	//完成：基础操作——读取信息——请勿裸露使用
	public String read_mass() {
	    redata=new dataPack();
	    data=new dataPack();
		String mass2="";
		try {
			Socket sk =new Socket( ip_address, outport);
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));		
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
	        	mass2=wrong_statement;
	        	System.out.println("read die"+flag+br.readLine());
	        }
		}catch(Exception e) {System.out.println("can not read mass");}	
		redata.toDatapack(mass2);
		return mass2;
	}
	//完成：基础操作——发送信息——请勿裸露使用
	public void send_mass(String mass) {
	    redata=new dataPack();
	    data=new dataPack();
	    data.setcheckCode(text_statement);
	    data.addBody(mass);
	    data.addBody(Get_IP());
		data.addBody(Get_TIME());
	   try{
		   Socket sk =new Socket( ip_address, outport);
			PrintStream ps = new PrintStream(sk.getOutputStream());
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
	        ps.println(data.toString());   
	        }catch(Exception e) {
	        	System.out.println(">>>send die<<<");
	        	e.printStackTrace();
	        	wrong_flag=false;
	        }
	   }
	//完成：检查存储空间
	public String check_space() {
	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(checkspace_statement);	
		data.addBody(user_name);//第一个参数改为username
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		try {
		Socket sk =new Socket( ip_address, outport);
		PrintStream ps = new PrintStream(sk.getOutputStream());
		BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
		send_mass(ps,data.toString());
		String re=read_mass(br);
		redata.toDatapack(re);
		ps.close();
		br.close();
		sk.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return redata.toString();
	}
	//完成：请求帮助
	public String help() {

	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(help_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		try {
		Socket sk =new Socket( ip_address,outport);
		PrintStream ps = new PrintStream(sk.getOutputStream());
		BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
		
		send_mass(ps,data.toString());
		String re=read_mass(br);
		redata.toDatapack(re);
		ps.close();
		br.close();
		sk.close();
		}catch(Exception e) {
			System.out.println("sign_in");
			e.printStackTrace();
		}
		return redata.getBody();
	}
	//完成：检查存储空间
	public String getmem() {
	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(getmem_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody("ineedwebsite");
		try {
			Socket sk =new Socket( ip_address, outport);
			PrintStream ps = new PrintStream(sk.getOutputStream());
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			data.addBody(Get_IP());
			data.addBody(Get_TIME());
			send_mass(ps,data.toString());
			String re=read_mass(br);
			redata.toDatapack(re);
			ps.close();
			br.close();
			sk.close();
		}catch(Exception e) {
			System.out.println("sign_in");
			e.printStackTrace();
		}
		return redata.getBody();
	}
	//完成：检查服务器
	public String check_time() {
		
	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(time_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		try {
		Socket sk =new Socket( ip_address, outport);
		PrintStream ps = new PrintStream(sk.getOutputStream());
		BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
		
		send_mass(ps,data.toString());
		String re=read_mass(br);
		redata.toDatapack(re);
		ps.close();
		br.close();
		sk.close();
		}catch(Exception e) {
			System.out.println("sign_in");
			e.printStackTrace();
		}
		return redata.getBody();
	}
	//完成：请求网址
	public String check_web() {

	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(web_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		String temp = null;
		try {
		Socket sk =new Socket( ip_address, outport);
		PrintStream ps = new PrintStream(sk.getOutputStream());
		BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
		send_mass(ps,data.toString());
		temp=read_mass(br);
		redata.toDatapack(temp);
		ps.close();
		br.close();
		sk.close();
		}catch(Exception e) {
			System.out.println("sign_in");
			e.printStackTrace();
		}
		return redata.getBody();
	}
	//完成：发送消息
	public String any_text(String temp) {
	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(text_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody("text is: "+temp);
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		try {
		Socket sk =new Socket( ip_address, outport);
		PrintStream ps = new PrintStream(sk.getOutputStream());
		BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
		send_mass(ps,data.toString());
		String re=read_mass(br);
		redata.toDatapack(re);
		ps.close();
		br.close();
		sk.close();
		}catch(Exception e) {
			System.out.println("sign_in");
			e.printStackTrace();
		}
		return redata.getBody();
	}
	//完成：注册
	public boolean sign(String name,String password) {
	    redata=new dataPack();
	    data=new dataPack();
		data.setcheckCode(sign_statement);
		data.addBody(name);
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		data.addBody(name);
		data.addBody(password);
		try {
			Socket sk =new Socket( ip_address, outport);
			PrintStream ps = new PrintStream(sk.getOutputStream());
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			
			send_mass(data.toString());		
			String re=read_mass(br);
			redata.toDatapack(re);
			ps.close();
			br.close();
			sk.close();
			
			if(redata.getcheckCode().compareTo(right_statement)==0) {
				return true;
			}else {
				return false;
				}
		}catch(Exception e) {
				wrong_flag=false;}
		return wrong_flag;
	}
	//完成：登录
	public boolean log(String name,String password) {

		try {
		    redata=new dataPack();
		    data=new dataPack();
			Socket sk =new Socket( ip_address, outport);
			PrintStream ps = new PrintStream(sk.getOutputStream());
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			data.setcheckCode(log_statement);
			data.addBody(name);
			data.addBody(Get_IP());
			data.addBody(Get_TIME());
			data.addBody(name);
			data.addBody(password);
			send_mass(ps,data.toString());
			String re=read_mass(br);
			redata.toDatapack(re);
			ps.close();
			br.close();
			sk.close();
		
			if(redata.getcheckCode().compareTo(right_statement)==0) {
				return true;
			}
			else {
				return false;
				}
			
			}catch(Exception e) {
				wrong_flag=false;
				e.printStackTrace();
			}
			return wrong_flag;
			}
	//完成:上传文件
	public boolean se_File(File _file,String aimadress,JProgressBar upProgress,JButton send) {
		
		redata=new dataPack();
		data=new dataPack();
		data.setcheckCode(upload_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		data.addBody(aimadress);
		data.addBody(_file.getName());
		 _filesize=_file.length();
		 _progress=0;
		send.setEnabled(false);
		System.out.println(data.toString());
		Socket filesk=null;
		InputStream fileRead=null;
		
		try {
			 filesk =new Socket( ip_address, outport);
			 OutputStream out = filesk.getOutputStream();  
			 fileRead = new FileInputStream(_file);  
			 PrintStream ps = new PrintStream(out);
			 ps.println(data.toString());
			 ps.flush();
			 Thread.sleep(200);
			 byte[] buf = new byte[100];  
			 int len = 0; 
			 Dimension d = upProgress.getSize();
			 Rectangle rect = new Rectangle(0, 0, d.width, d.height);
			 int dede=1;
			 double start=0;
		     double end=0;
		     float v=0;
			 while((len=fileRead.read(buf)) != -1){  
			           out.write(buf, 0, len); 
			           out.flush();   //刷新
			           _progress+=len;
			           if((int) (100*_progress/ _filesize)>=dede) {
				        		end= System.nanoTime()/1000000000.0;
				        		v=(float) (len/(end-start));
				        	   	upProgress.setValue((int) (100*_progress/ _filesize));  //进度值
				        	   	if(v<1024)
				        	   		upProgress.setString("上传进度="+(100*_progress/ _filesize)+"%"+"   速度为："+String.format("%.2f",v)+"kb/s");  //提示信息
				        	   	else
				        	   		upProgress.setString("上传进度="+(100*_progress/ _filesize)+"%"+"   速度为："+String.format("%.2f",v/1024)+"Mb/s");  //提示信息
				        	   	upProgress.paintImmediately(rect);
				        	   	dede+=1;
				        	    start= System.nanoTime()/1000000000.0;
			           }
			  }  
		 upProgress.setValue(100);  //进度值
	     upProgress.setString("上传进度="+100+"%");  //提示信息
         upProgress.paintImmediately(rect);
		 filesk.shutdownOutput();  
		      //获取从服务端反馈的信息  
		 BufferedReader in = new BufferedReader(new InputStreamReader(filesk.getInputStream()));  
		 String serverBack = in.readLine();  
		 System.out.println(serverBack);    
		      //资源关闭  
		 filesk.close();  
		 ps.close();
		 fileRead.close();
		send.setEnabled(true);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		return wrong_flag;  
	   }
	//测试：下载文件	
	public void re_file(File file,String aimadress,JProgressBar upProgress,JButton download,long lenth) throws IOException {
			redata=new dataPack();
			data=new dataPack();
			data.setcheckCode(download_statement);
			data.addBody(user_name);//第一个参数改为username
			data.addBody(Get_IP());
			data.addBody(Get_TIME());
			data.addBody(file.getPath());
			download.setEnabled(false);
			File _file=new File(aimadress+File.separator+file.getName());
		      if(!_file.exists()){    
		    	  	_file.createNewFile();
		      }
			 Socket filesk =new Socket( ip_address, outport);
			 OutputStream out = filesk.getOutputStream();  
			 PrintStream ps = new PrintStream(out);
			 ps.println(data.toString());///
			 upProgress.setIndeterminate(false);  //采用确定的进度条
			 InputStream in=filesk.getInputStream();  
			 OutputStream fileSave = new FileOutputStream(_file);  
			 _filesize=lenth;
			 _progress=0;
			 Dimension d = upProgress.getSize();
			 Rectangle rect = new Rectangle(0, 0, d.width, d.height);
			 byte[] buf = new byte[1024];  
		      int dede=1;
		      int len = 0;  //判断是否读到文件末尾
		      double start=0;
		      double end=0;
		      float v=0;
		         while((len=in.read(buf)) != -1) 
		           {  
		            fileSave.write(buf, 0, len);  
		            fileSave.flush();   //刷新
		            _progress+=len;
			        if((int) (100*_progress/ _filesize)>=dede) {
				     		end= System.nanoTime()/1000000000.0;
			        		v=(float) (len/(end-start));
			        	   	upProgress.setValue((int) (100*_progress/ _filesize));  //进度值
				        	   	if(v<1024)
				        	   		upProgress.setString("下载进度="+(100*_progress/ _filesize)+"%"+"   速度为："+String.format("%.2f",v)+"kb/s");  //提示信息
				        	   	else
				        	   		upProgress.setString("下载进度="+(100*_progress/ _filesize)+"%"+"   速度为："+String.format("%.2f",v/1024)+"Mb/s");  //提示信息
				        	   	upProgress.paintImmediately(rect);
			        	   	if(lenth>10000000)
			        	   		dede+=1;
			        	   	else
			        	   		dede+=5;
			        	    start= System.nanoTime()/1000000000.0;
		           }
		         }  
				 upProgress.setValue(100);  //进度值
			     upProgress.setString("下载进度="+100+"%");  //提示信息
		         upProgress.paintImmediately(rect);

		     download.setEnabled(true);
		     out.flush();  		          //刷新
		     fileSave.close();  
   }
	//完成：更新目录
	public JTree reTree(String username) throws IOException, ClassNotFoundException {

		    redata=new dataPack();
		    data=new dataPack();
		    Socket sk=new Socket( ip_address, outport);
			PrintStream ps = new PrintStream(sk.getOutputStream());
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			data.setcheckCode(updatetree_statement);
			data.addBody(username);
			data.addBody(Get_IP());
			data.addBody(Get_TIME());
			System.out.println("sending request...");
			send_mass(ps,data.toString());//发送请求
		  	InputStream in = sk.getInputStream();  
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式  
		    String filename=des+"temp_tree.xml";
		    File _file=new File(filename);
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
		     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sk.getOutputStream())); 
		     out.flush(); //刷新
		     fileSave.close();  
		     System.out.println("finished");
		     XMLDecoder decoder = new  XMLDecoder (new BufferedInputStream(new FileInputStream(_file)));
		     return (JTree)decoder.readObject();
	}
	//测试：删除文件
	public void delet_file(File file,String aimadress) {
			data=new dataPack();
			data.setcheckCode(delete_statement);
			data.addBody(user_name);//第一个参数改为username
			data.addBody(Get_IP());
			data.addBody(Get_TIME());
			data.addBody(aimadress);
			try {
				Socket sk =new Socket( ip_address, outport);
				PrintStream ps = new PrintStream(sk.getOutputStream());
				BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
				send_mass(ps,data.toString());//发送请求
				String re=read_mass(br);
				redata.toDatapack(re);
				ps.close();
				br.close();
				sk.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
	//测试：重命名文件
	public void rename_file(File file,String aimadress,String newname) {
		data=new dataPack();
		data.setcheckCode(rename_statement);
		data.addBody(user_name);//第一个参数改为username
		data.addBody(Get_IP());
		data.addBody(Get_TIME());
		data.addBody(aimadress);
		data.addBody(newname);
		try {
			Socket sk =new Socket( ip_address, outport);
			PrintStream ps = new PrintStream(sk.getOutputStream());
			BufferedReader br  = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			send_mass(ps,data.toString());//发送请求
			String re=read_mass(br);
			redata.toDatapack(re);
			ps.close();
			br.close();
			sk.close();
		}catch(Exception e) {
			System.out.println("sign_in");
			e.printStackTrace();
		}
		}
}
	


