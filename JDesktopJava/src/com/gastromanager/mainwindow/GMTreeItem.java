package com.gastromanager.mainwindow;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

public class GMTreeItem extends DefaultMutableTreeNode {

	public String m_name = new String();
	public String m_value = new String();
	public String m_xmlname = new String(); 
	public HashMap < String, String > m_attributes = new HashMap<String, String>();
	
	
	public String getDisplayString() {
		
		String result = new String();
		
		if ( m_name.length() > 0 )
			result = m_name;
		else if (m_attributes.containsKey("name") == true)
			result = m_attributes.getOrDefault("name", result);
		
		else if ( m_xmlname.length() > 0) 
			result = m_xmlname;
		
		else if ( m_value.length() > 0 ) {
			result += "=";
			result += m_value;
		}
		
		return result;
		
	}
	
	public GMTreeItem() {
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(Object userObject) {
		super(userObject);
		// TODO Auto-generated constructor stub
	}

	public GMTreeItem(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

}
