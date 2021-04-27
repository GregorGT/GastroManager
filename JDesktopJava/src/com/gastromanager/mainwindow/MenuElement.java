package com.gastromanager.mainwindow;

import java.util.HashSet;

public class MenuElement {
	
	public int numberOfElements = 0;
	public HashSet<GMTreeItem> elements = new HashSet<GMTreeItem>();
	
	public void addMenuElement(GMTreeItem item) {
		this.elements.add(item);
		this.numberOfElements++;
	}
	
	public HashSet<GMTreeItem> getMenuElements() {
		return this.elements;
	}
	
	public MenuElement() {
	}
	
}
