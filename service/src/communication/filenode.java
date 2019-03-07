package communication;

import java.io.File;
import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;

public class filenode extends DefaultMutableTreeNode {

	boolean isDirect;
	public File thisfile;
	public String filename;
	public String path;
	public String size;
	public long _size;
	
}