package com.gastromanager.mainwindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

public class GMTreeItem extends DefaultMutableTreeNode {

	public String m_name = new String();
	public String m_value = new String();
	public String m_xmlname = new String(); 
	public HashMap<String, String> m_attributes = new HashMap<String, String>();
	public Set<GMTreeItem> m_children = new HashSet<GMTreeItem>();
	public DrillDownButton button;
	public GMTree treeParent;
	public String id, buttonAssociatedId;
	
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
	
	public void setTree(GMTree parent) {
		this.treeParent = parent;
	}
	
	public GMTree getTree() {
		return this.treeParent;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setButton(DrillDownButton btn) {
		this.button = btn;
//		this.buttonAssociatedId;
		//
	}
	
	public DrillDownButton getButton() {
		return this.button;
	}
	
	public void setBtnAssociatedId(String id) {
		this.buttonAssociatedId = id;
	}
	public String getBtnAssociatedId() {
		return this.buttonAssociatedId;
	}
	
	public void setChildren(GMTreeItem[] children) {
		for (int i = 0; i < children.length; i++) {
			this.m_children.add(children[i]);
		}
	}
	
	public HashMap<String, String> getAttributes() {
		return m_attributes;
	}
	
	public void addAttributes(String name, String value) {
		this.m_attributes.put(name, value);
		this.id = GMTreeItem.this.getAttribute("uuid");
	}
	
	public String getAttribute(String attr) {
		return this.m_attributes.get(attr);
	}

	public void setUUID(String uuid) {
		this.m_attributes.putIfAbsent("uuid", uuid);
	}
	
	public String getValue() {
		return this.m_value;
	}
	public void setValue(String value) {
		this.m_value = value;
	}
	
	public String getName() {
		return this.m_name;
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
	}

	public GMTreeItem(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

}
