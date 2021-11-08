/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.mainwindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class GMTreeItem extends DefaultMutableTreeNode implements Serializable {

	public String m_name = new String();
	public String m_value = new String();
	public String m_xmlname = new String(); 
	public HashMap<String, String> m_attributes = new HashMap<String, String>();
	public List<GMTreeItem> children = new ArrayList();
	public DrillDownButton button;
	public GMTree treeParent;
	public MenuElement menuElement;
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
	
	public GMTreeItem(TreePath path) {
	}

	public GMTreeItem(Object userObject) {
		super(userObject);
	}

	public GMTreeItem(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public void addMenuElements(MenuElement newMElement) {
		this.menuElement = newMElement;
	}
	
	public MenuElement getMenuElements() {
		return this.menuElement;
	}

}
