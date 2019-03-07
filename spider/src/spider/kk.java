package spider;

import connection.ConnectionUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class kk{
	
	static int counts[][] = new int[25][20];
	private static String getHtml(String url)throws Exception{
	    URL url1=new URL(url);//使用java.net.URL
	    URLConnection connection=url1.openConnection();//打开链接
	    InputStream in=connection.getInputStream();//获取输入流
	    InputStreamReader isr=new InputStreamReader(in,"gbk");//流的包装
	    BufferedReader br=new BufferedReader(isr);
	    String line;
	    StringBuffer sb=new StringBuffer();
	    while((line=br.readLine())!=null){//整行读取
	        sb.append(line,0,line.length());//添加到StringBuffer中
	        sb.append('\n');//添加换行符
	    }
	    //关闭各种流，先声明的后关闭
	    br.close();
	    isr.close();
	    in.close();
	    return sb.toString();
	}
    static private void regexMain(int year, int page,String name) throws Exception {
    	String url=null;
    	if(page<10) {
        url="http://kde.cnki.net/KDEService/Search/Brief/CJFD/?PYKM="+name+"&Year="+year+"&Issue=0"+page;
    	}else {
    	url="http://kde.cnki.net/KDEService/Search/Brief/CJFD/?PYKM="+name+"&Year="+year+"&Issue="+page;
    	}
        String result = getHtml(url);
        getHouseInfo(result, year-1995);
    }
    static  private void getHouseInfo(String targetStr,int year) {
        StringBuilder lastInfo=new StringBuilder();
        Pattern[] patterns=new Pattern[20];
        Matcher[] matchers=new Matcher[20];
        patterns[0]=Pattern.compile("计算机控制");
        patterns[1]=Pattern.compile("微机电");
        patterns[2]=Pattern.compile("光电子");
        patterns[3]=Pattern.compile("虚拟仪器");
        patterns[4]=Pattern.compile("智能");
        patterns[5]=Pattern.compile("神经网络");
        patterns[6]=Pattern.compile("光盘");
        patterns[7]=Pattern.compile("高性能");
        patterns[8]=Pattern.compile("互联网");
        patterns[9]=Pattern.compile("物联网");
        patterns[10]=Pattern.compile("大数据");
        patterns[11]=Pattern.compile("芯片");
        patterns[12]=Pattern.compile("导航");
        patterns[13]=Pattern.compile("嵌入式");
        patterns[14]=Pattern.compile("信息光学");
        patterns[15]=Pattern.compile("激光");
        patterns[16]=Pattern.compile("非线性光学");      
        for(int i=0;i<17;i++) {
        	matchers[i] = patterns[i].matcher(targetStr);
        }
        for(int i=0;i<17;i++) {
        while (matchers[i].find()) {
        	counts[year][i]+=1;
        }
        }	
    }

    static public void main(String arg[]) throws Exception {
    	String names[]= {
    			"YBJI",
    			"XDYQ",
    			"YQXB",
    			"YBJS",
    			"ZYQB",
    			"QXSW",
    			"GYZD",
    			"DZIY",
    			"DCYQ",
    			"FXYQ",
    			"ZDYY"};
    		for(int i=0;i<=10;i++) {
    			 System.out.println(names[i]);
        	for(int year=1995;year<2020;year++) {
        		for(int page=0;page<12;page++) {
        			regexMain(year,page,names[i]);
        			}
            	}
        	}
   
        	System.out.print("year: ");
        	for(int year=1995;year<2020;year++) {
        		System.out.print(year+" ");
        	}
        	System.out.println(" ");
        	for(int kind=0;kind<17;kind++) {
        		System.out.print("count: ");
        		for(int year=1995-1995;year<2020-1995;year++) {
        			System.out.print(counts[year][kind]+" ");
        		}
        		System.out.println(" ");
        	}
        }
    
    }


