package zipper;

import java.util.zip.*;
import java.io.*;
import java.util.Scanner;


public class Zipper {
	private File[] sourAdd;//用于标记源文件，首先需要判断源文件是否存在
	private File destAdd;//用于创建路径
	private boolean zoru;  // zip or unzip. True for unzip, false for zip.
	
	public Zipper(String[] args) throws Exception { //用输入参数创建zipper类
		int length = args.length;
		if(length == 0) {
			System.err.println("Input cant be empty!");
			throw new Exception();
		}
		zoru = args[0].endsWith(".zip");  // 判断是不是zip文件
		if(zoru) {
			if(args[length - 1].endsWith(".zip")) {  // 如果是，那么需要创建的文件的尾部是zip格式，执行的操作是压缩
				sourAdd = new File[length];	//创建FILE数组
				for(int i = 0; i < length; i++)	
					sourAdd[i] = new File(args[i]);	//用第i个参数创建file对象，是其文件名
				destAdd = new File("");   //不需要
			}
			else {
				sourAdd = new File[length - 1];
				for(int i = 0; i < length - 1; i++)
					sourAdd[i] = new File(args[i]);
				if(!args[length - 1].endsWith("\\"))  // 
					args[length - 1] += "\\";
				destAdd = new File(args[length - 1]);
			}
		}
		else {
			if(args[length - 1].endsWith(".zip")) {  //执行的操作是解压缩
				sourAdd = new File[length - 1];
				for(int i = 0; i < length - 1; i++)
					sourAdd[i] = new File(args[i]);
				destAdd = new File(args[length - 1]);
			}
			else {
				sourAdd = new File[length];
				for(int i = 0; i < length; i++)
					sourAdd[i] = new File(args[i]);
				destAdd = new File("untitled.zip");  // 在根目录下生成一个untitled.zip文件
			}
		}
	}
	
	private File reName(File f) {  // 自定义的重命名
		if(!f.exists())
			return f;
		int no = 1, index, i;
		String name = f.getPath();
		for(i = name.length() - 1; i > -1; i--)
			if(name.charAt(i) == '.')  // ÕÒµ½ÎÄ¼þÀàÐÍºó×ºµÄ'.'£¬ÒÔËüÎª½ç´´½¨Ç°×ººó×º×Ö·û´®
				break;
		index = i;
		String prefix = name.substring(0, index);
		String postfix = name.substring(index);
		File g;
		while((g = new File(prefix + "_" + no + postfix)).exists())  // ÈôÒÀ¾ÉÖØ¸´£¬Ôòno++
			no++;
		return g;
	}
	
	private void doZip(File f, String dir, ZipOutputStream out, int indent) throws IOException {  // 完成zip操作
		String fileName = f.getName();
		String dirName = dir + fileName;  //
		if(!f.exists()) {
			System.err.println("Cant find " + fileName);
			return;
		}
		if(f.isFile()) { 
			for(int i = 0; i < indent; i++)
				System.out.print("\t");
			System.out.println("Writing file " + fileName + "...");
			out.putNextEntry(new ZipEntry(dirName));
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(dirName));
			int c;
			while((c = in.read()) != -1)
				out.write(c);
			in.close();
			return;
		}
		if(f.isDirectory()) {  //
			for(int i = 0; i < indent; i++)
				System.out.print("\t");
			System.out.println("Writing directory " + fileName + ":");
			File[] fs = f.listFiles();
			out.putNextEntry(new ZipEntry(dirName + "/"));
			for(int i = 0; i < fs.length; i++)  
				doZip(fs[i], dirName + "/", out, indent + 1); //完成压缩
		}
	}
	
	private void doUnZip(ZipInputStream in) throws Exception {
		File temp;
		ZipEntry ze;
		while((ze = in.getNextEntry()) != null) {
			temp = new File(destAdd + ze.getName());
			if(ze.isDirectory()) {
				if(!temp.exists())  // Èô¸ÃÎÄ¼þ¼Ð²»´æÔÚ£¬Ôò´´½¨
					temp.mkdirs();
			}
			else {
				if(temp.exists()) {
					Scanner sc = new Scanner(System.in);
					System.out.println("The file " + temp + " has already existed.");  // ÈôÎÄ¼þÃûÓÐ³åÍ»£¬ÔòÌá¹©Ò»ÏÂÈýÖÖ²Ù×÷
					System.out.println("Which operation do you want?('r': rewrite, 'n': neglect, 'b': save both)");
					String op = sc.nextLine();
					switch(op.charAt(0)) {
					case 'r':  // ¸²¸Ç
						temp.delete();
						break;
					case 'n':  // Ìø¹ý
						continue;
					case 'b':  // Í¬Ê±±£Áô
						temp = reName(temp);
						break;
					}
				}
				FileOutputStream out = new FileOutputStream(temp);
				byte[] doc = new byte[512];
				int n;
				while((n = in.read(doc, 0, 512)) != -1)
					out.write(doc, 0, n);
				out.close();
			}
		}
	}
	
	public void start() throws Exception{
		if(zoru) {  // 
			if(!destAdd.exists())  // 
				destAdd.mkdirs();
			ZipInputStream in;
			System.out.println("Destination: " + destAdd);
			for(int i = 0; i < sourAdd.length; i++) {
				System.out.println("Unzipping file " + sourAdd[i]);
				if(!sourAdd[i].exists()) {
					System.err.println("Cant find " + sourAdd[i]);
					continue;
				}
				in = new ZipInputStream(new FileInputStream(sourAdd[i]));
				doUnZip(in); //解压缩
				in.close();
			}
			System.out.println("Done.");
		}
		else{  
			if(destAdd.exists()) {
				Scanner sc = new Scanner(System.in);
				System.out.println("Destination is occupied.");  // 发生了文件覆盖
				System.out.println("Which operation do you want?('r': rewrite, 'n': neglect, 'b': save both)");
				String op = sc.nextLine();
				switch(op.charAt(0)) {
				case 'r':
					destAdd.delete();
					break;
				case 'n':
					System.out.println("Done.");
					return;
				case 'b':
					destAdd = reName(destAdd);
					break;
				}
			}
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destAdd)));
			System.out.println("Destination: " + destAdd);
			for(int i = 0; i < sourAdd.length; i++)
				doZip(sourAdd[i], "", out, 0); //压缩
			out.close();
			System.out.println("Done.");
		}
	}
	
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		/*
		 *Zip:
		 *    Legal input format:
		 *        more than one ".*" + 0 or 1 ".zip"
		 *    Output:
		 *        0 ".zip": zip all ".*" to default address(./untitled.zip)
		 *        1 ".zip": zip all ".*" to ".zip"
		 *        
		 *Unzip:
		 *    Legal input format:
		 *        more than one ".zip" + 0 or 1 destination address 
		 *    Output:
		 *        0 dest add: unzip all ".zip" to default address(./)
		 *        1 dest add: unzip all ".zip" to dest add
		 */
		while(true) {
			String [] input = sc.nextLine().split(" "); //用空格当作文件的分隔符
			Zipper zp = new Zipper(input);
			zp.start();
		}
	}
}
