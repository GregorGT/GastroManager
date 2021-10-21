package com.gastromanager.util;

import com.gastromanager.mainwindow.GMTreeItem;

/**
 *
 * This class contains public variables that are need in the whole project. Be careful when altering these values!!!!
 * 
 * @author panagiotis
 *
 */
public class PublicVariables {

	private GMTreeItem tree;
	
	private static PublicVariables instance = null;
	
	private PublicVariables() {
		
	}
	
	public static PublicVariables getInstance() {
		if (instance == null) {
			instance = new PublicVariables();
		}
		return instance;
	}

	public GMTreeItem getTree() {
		return tree;
	}

	public void setTree(GMTreeItem tree) {
		this.tree = tree;
	}
}
