package com.gastromanager.mainwindow;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

public class GMTreeItem extends DefaultMutableTreeNode {

	public String m_name = new String();
	public String m_value = new String();
	public String m_xmlname = new String(); 
	public HashMap < String, String > m_attributes = new HashMap<String, String>();
	
	String getDisplayString() {
		
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
	
	public HashMap<String, String> getAttributes() {
		
		return m_attributes;
	}
	
	public void setAttributes(String name, String value) {
		this.m_attributes.put(name, value);
	}
	

	public void setUUID(String uuid) {
		this.m_attributes.putIfAbsent("uuid", uuid);
	}
	
	public void setValue(String value) {
		this.m_value = value;
	}
	
	public void setName(String name) {
		this.m_name = name;
	}
	
	public String getXmlName() {
		return this.m_xmlname;
	}
	public void setXmlName(String xmlName) {
		this.m_xmlname = xmlName;
	}
	public GMTreeItem() {
	
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
