package communication;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.*;

class dataPack implements Serializable{

	private String checkCode;
    private String textLength;
    private Integer numOfCommand;
    private String body;
    private String cutterString;

    public String getcheckCode() {
        return checkCode;
    }
    public String getBody() {
        return body;
    }

    public void addBody(String body) {
    	if(this.numOfCommand==0) {
    		this.body=body+cutterString;
    		this.numOfCommand++;
    	} 	else{
        this.body += (body+cutterString);
        this.numOfCommand++;
        textLength=this.numOfCommand.toString();
    	}
    }
    public void setcheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

 
   public String getBody_nth(int n) {
	   StringTokenizer cutter=new StringTokenizer(body);
	   String temp="";
	   for(	int i=0;i<=n;i++) {
			temp=cutter.nextToken(cutterString);			
		}
	   return temp;
   }
    
   public void toDatapack(String command) {
	   StringTokenizer cutter=new StringTokenizer(command);
	   this.checkCode=cutter.nextToken(this.cutterString);
	   this.textLength=cutter.nextToken(this.cutterString);
	   this.numOfCommand=Integer.valueOf(this.textLength);
	   this.body="";
	   while(cutter.hasMoreTokens())
		   this.body+=(cutter.nextToken(this.cutterString)+this.cutterString);
   }
   
   public String toString(){
        return this.checkCode+this.cutterString+this.textLength+this.cutterString+this.body;
    } 
   
   public String toString(dataPack o){
	   return o.checkCode+cutterString+o.textLength+cutterString+o.body;
   }
    
   public dataPack(String cu){
	   this.numOfCommand=0;
	   this.cutterString=cu;
   }
  
   public dataPack(){
	   this.numOfCommand=0;
	   this.cutterString=";";
	   
   }
   

}
