package computer;
import java.util.zip.*;
import java.io.*;
import java.util.Scanner;


public class Zipper {
	private File[] sourAdd;
	private File destAdd;
	private boolean zoru;  // zip or unzip. True for unzip, false for zip.
	
	public Zipper(String[] args) throws Exception {
		int length = args.length;
		if(length == 0) {
			System.err.println("Input cant be empty!");
			throw new Exception();
		}
		zoru = args[0].endsWith(".zip");  // ����һ������Ϊzip������Ϊ�˴β���Ϊ��ѹ�������ǣ�����Ϊ��ѹ������
		if(zoru) {
			if(args[length - 1].endsWith(".zip")) {  // ���һ������������zip������Ϊ��Ŀ���ַ������Ŀ���ַΪĬ��(���������ڵ�ַ)
				sourAdd = new File[length];
				for(int i = 0; i < length; i++)
					sourAdd[i] = new File(args[i]);
				destAdd = new File("");
			}
			else {
				sourAdd = new File[length - 1];
				for(int i = 0; i < length - 1; i++)
					sourAdd[i] = new File(args[i]);
				if(!args[length - 1].endsWith("\\"))  // ����ַ����'\'��β����֮
					args[length - 1] += "\\";
				destAdd = new File(args[length - 1]);
			}
		}
		else {
			if(args[length - 1].endsWith(".zip")) {  // ���һ����������zip������Ϊ��ѹ����Ŀ���ַ������ΪĬ��
				sourAdd = new File[length - 1];
				for(int i = 0; i < length - 1; i++)
					sourAdd[i] = new File(args[i]);
				destAdd = new File(args[length - 1]);
			}
			else {
				sourAdd = new File[length];
				for(int i = 0; i < length; i++)
					sourAdd[i] = new File(args[i]);
				destAdd = new File("untitled.zip");  // ����Ŀ���ַ��������Ϊ���ص�untitled.zip
			}
		}
	}
	
	private File reName(File f) {  // �����ļ��Ѵ��ڣ�����ô˺����Զ�����
		if(!f.exists())
			return f;
		int no = 1, index, i;
		String name = f.getPath();
		for(i = name.length() - 1; i > -1; i--)
			if(name.charAt(i) == '.')  // �ҵ��ļ����ͺ�׺��'.'������Ϊ�紴��ǰ׺��׺�ַ���
				break;
		index = i;
		String prefix = name.substring(0, index);
		String postfix = name.substring(index);
		File g;
		while((g = new File(prefix + "_" + no + postfix)).exists())  // �������ظ�����no++
			no++;
		return g;
	}
	
	private void doZip(File f, String dir, ZipOutputStream out, int indent) throws IOException {  // fΪ��ǰ�ļ���dirΪ��ǰ�ļ�����һ��·����indentΪ��ӡʱ������
		String fileName = f.getName();
		String dirName = dir + fileName;  // ��ǰ��·��
		if(!f.exists()) {
			System.err.println("Cant find " + fileName);
			return;
		}
		if(f.isFile()) {  // �����ļ�����ѹ��
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
		if(f.isDirectory()) {  // �����ļ��У���ݹ�ѹ����������ݣ�����dir��������һ���ַ
			for(int i = 0; i < indent; i++)
				System.out.print("\t");
			System.out.println("Writing directory " + fileName + ":");
			File[] fs = f.listFiles();
			out.putNextEntry(new ZipEntry(dirName + "/"));
			for(int i = 0; i < fs.length; i++)  // �ݹ���ô˺�����ע��ZipEntry������'/'ʶ���ļ��ж�����'\'
				doZip(fs[i], dirName + "/", out, indent + 1);
		}
	}
	
	private void doUnZip(ZipInputStream in) throws Exception {
		File temp;
		ZipEntry ze;
		while((ze = in.getNextEntry()) != null) {
			temp = new File(destAdd + ze.getName());
			if(ze.isDirectory()) {
				if(!temp.exists())  // �����ļ��в����ڣ��򴴽�
					temp.mkdirs();
			}
			else {
				if(temp.exists()) {
					Scanner sc = new Scanner(System.in);
					System.out.println("The file " + temp + " has already existed.");  // ���ļ����г�ͻ�����ṩһ�����ֲ���
					System.out.println("Which operation do you want?('r': rewrite, 'n': neglect, 'b': save both)");
					String op = sc.nextLine();
					switch(op.charAt(0)) {
					case 'r':  // ����
						temp.delete();
						break;
					case 'n':  // ����
						continue;
					case 'b':  // ͬʱ����
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
		if(zoru) {  // ��ѹ
			if(!destAdd.exists())  // ��Ŀ���ַ�����ڣ��򴴽�
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
				doUnZip(in);
				in.close();
			}
			System.out.println("Done.");
		}
		else{  // ѹ��
			if(destAdd.exists()) {
				Scanner sc = new Scanner(System.in);
				System.out.println("Destination is occupied.");  // ��Ŀ���ļ��Ѵ��ڣ��������ǰ
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
				doZip(sourAdd[i], "", out, 0);
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
			String [] input = sc.nextLine().split(" ");
			Zipper zp = new Zipper(input);
			zp.start();
		}
	}
}
