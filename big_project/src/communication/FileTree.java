package communication;

import java.awt.Color;
import java.io.*;
import javax.swing.JTree;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileTree implements Serializable{

	public JTree FileTree;
	
	static private filenode link_node(filenode rootNode) {
		
		File temp=rootNode.thisfile;
		if(temp.isDirectory())
		{
			for(File sonfile : temp.listFiles()) {
				filenode SonFile = new filenode();
				SonFile.filename=sonfile.getName();
				SonFile.path=sonfile.getPath();
				SonFile.isDirect=sonfile.isDirectory();
				long size=sonfile.length();
				if(0<=size && size<=1024) {
					SonFile.size=size+ "B";
				}else if(1024<=size && size<=1024*1024) {
					SonFile.size=size/1024.0+ "KB";
				}else if(1024*1024<=size && size<=1024*1024*1024) {
					SonFile.size=size/(1024.0*1024.0)+ "MB";
				}else if(1024*1024*1024<=size && size<=1024*1024*1024*1024) {
					SonFile.size=size/(1024.0*1024*1024)+ "GB";
				}	
				SonFile.thisfile=sonfile;
				rootNode.add(link_node(SonFile));
			}
			return rootNode;
		}else {
			return rootNode;
		}	
	}
	static public JTree creat_filetree(String des,String user) {
	
		File start=new File(des+user);
		filenode rootNode = new filenode();
		rootNode.filename=user;
		rootNode.filename=start.getName();
		rootNode.path=start.getPath();
		rootNode.isDirect=start.isDirectory();
		long size=start.length();
		System.out.println(size);
			if(0<=size && size<=1024) {
					rootNode.size=size+ "B";
			}else if(1024<=size && size<=1024*1024) {
				rootNode.size=size/1024.0+ "KB";
			}else if(1024*1024<=size && size<=1024*1024*1024) {
				rootNode.size=size/(1024.0*1024.0)+ "MB";
			}else if(1024*1024*1024<=size && size<=1024*1024*1024*1024) {
				rootNode.size=size/(1024.0*1024*1024)+ "GB";
			}	
		rootNode.thisfile=start;
		rootNode=link_node(rootNode);
		JTree tree = new JTree(rootNode);
	    tree.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(192, 192, 192)));
	    tree.setBackground(new Color(240, 255, 240));
		return tree;
	}

	private void writeObject(ObjectOutputStream out) throws IOException{ 
	    out.defaultWriteObject();// 使定制的writeObject()方法可以利用自动序列化中内置的逻辑。 
	    out.writeObject(FileTree);    // 若要保存“Object对象”，则使用writeObject()
	}
	
	private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException{ 
	    in.defaultReadObject();       // 使定制的readObject()方法可以利用自动序列化中内置的逻辑。 
	    this.FileTree = (JTree) in.readObject(); // 若要读取“Object对象”，则使用readObject()
	}
}
