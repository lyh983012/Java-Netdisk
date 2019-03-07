package communication;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;

import javax.swing.JTree;
import javax.swing.border.MatteBorder;

public class FileTree {

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
				SonFile._size=size;
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

}