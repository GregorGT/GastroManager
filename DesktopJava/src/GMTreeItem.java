import java.util.HashMap;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class GMTreeItem extends TreeItem {
	
	
	public String m_name;
	public String m_value;
	public String m_xmlname;
	public HashMap < String, String > m_attributes;
	
	public GMTreeItem(Tree parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(TreeItem parentItem, int style) {
		super(parentItem, style);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(Tree parent, int style, int index) {
		super(parent, style, index);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(TreeItem parentItem, int style, int index) {
		super(parentItem, style, index);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void checkSubclass() {
	    //  allow subclass
	    //	System.out.println("info   : checking menu subclass");
	}
	
	String getDisplayString() {
		
		String result = new String();
		
		if ( m_name.length() > 0 )  
			result = m_name;
		else 
			if ( m_xmlname.length() > 0) 
				result = m_xmlname;
		if ( m_value.length() > 0 ) {
			result += "=";
			result += m_value;
		}
		
		return result;
		
	}

}
