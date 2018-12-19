package communication;

import java.io.File;
import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;

public class filenode extends DefaultMutableTreeNode implements Serializable{


	boolean isDirect;
	public File thisfile;
	public String filename;
	public String path;
	public String size;
	public long _size;
	
	public String toString2() {
		return "文件名: "+filename+System.getProperty("line.separator")+  
				"路径: "+path+System.getProperty("line.separator")+  
				"文件大小: "+size+System.getProperty("line.separator");
	}
	public String toString() {
		return "文件名: "+filename+System.getProperty("line.separator");
	}
	
}
