package com.gastromanager.util;


import com.gastromanager.mainwindow.GMTreeItem;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XmlUtil {

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public static String readFileToString(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static void treeBuild(GMTreeItem rootNode, Node xmlNode, DefaultTreeModel model ,com.gastromanager.mainwindow.MenuElement menuElement) {
		if (xmlNode.getNodeName().contains("#"))
			return;
		boolean isRootNode;
		GMTreeItem newNode;
		
		if (xmlNode.getNodeName() == "root") {
			newNode = rootNode;
			isRootNode = true;
		} else {
			newNode = new GMTreeItem();
			isRootNode = false;
		}
		
		assignAttributes(xmlNode, newNode);

		if (xmlNode.getChildNodes().getLength() == 1) {
			if (xmlNode.getFirstChild().hasChildNodes() == false) {
				String nodeName = xmlNode.getFirstChild().getNodeName();
				if (nodeName == "#text") {
					newNode.setValue(xmlNode.getFirstChild().getTextContent());
				}
			}
		}

		if (xmlNode.getNodeValue() != null && xmlNode.getNodeValue().length() > 0)
			newNode.setValue(xmlNode.getNodeValue());
		
		if (rootNode.toString().contains("menues")) {
			menuElement.addMenuElement(newNode);
		}
		
		newNode.setXmlName(xmlNode.getNodeName());
		newNode.setUserObject(newNode.getDisplayString());

		if (newNode.getXmlName() == "button") {			
			createButton(newNode, model, rootNode, menuElement);
		}
		
		NodeList children = xmlNode.getChildNodes();
		
		newNode.setTree(rootNode.getTree());
		
		if (isRootNode) {
			rootNode.setUserObject("Restaurant name");
		} else {
			rootNode.add(newNode);
			rootNode.children.add(newNode);
		}
		for (int i = 0; i < children.getLength(); ++i) {
			treeBuild(newNode, children.item(i), model, menuElement);
		}
	}
    
    private static void assignAttributes(Node xmlNode, GMTreeItem newNode) {
    	NamedNodeMap attributes = xmlNode.getAttributes();	
		if(attributes != null) {
			for(int k = 0; k<attributes.getLength(); ++k) {
				String name = attributes.item(k).getNodeName();
				String value = attributes.item(k).getNodeValue();
				newNode.addAttributes(name, value);			
				newNode.setUUID(assignUUID());
			}
		}
    }
    
    private static void createButton(GMTreeItem newNode, DefaultTreeModel model, GMTreeItem rootNode, com.gastromanager.mainwindow.MenuElement mElement) {
		newNode.addMenuElements(mElement);
		HashMap<String, String> btnAttrs = newNode.getAttributes();
		GMTreeItem newH = new GMTreeItem(); 
		GMTreeItem newW = new GMTreeItem();
		GMTreeItem newX = new GMTreeItem(); 
		GMTreeItem newY = new GMTreeItem();
		model.insertNodeInto(newNode, rootNode, 0);
		model.insertNodeInto(newH, newNode, 0);
		model.insertNodeInto(newW, newNode, 1);
		model.insertNodeInto(newX, newNode, 2);
		model.insertNodeInto(newY, newNode, 3);
	
		newH.setUserObject("Height: "  + btnAttrs.get("height"));
		newW.setUserObject("Width: " + btnAttrs.get("width"));
		newX.setUserObject("X: " + btnAttrs.get("x-position"));
		newY.setUserObject("Y: " + btnAttrs.get("y-position"));
	}


    public static void parseXmlDocument(Document doc, GMTreeItem root, com.gastromanager.mainwindow.MenuElement mElement) {
        try {
        	
            treeBuild(root, doc.getFirstChild(),(DefaultTreeModel) root.getTree().getModel() , mElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String writeTreeIntoString(GMTreeItem treeItem) {

        String result = "";  
        List<GMTreeItem> children = treeItem.children;
        
        if (treeItem.getXmlName().isBlank()) {} 
        else {
        	result += "<" + treeItem.getXmlName() ;

        	HashMap<String, String> attrs = treeItem.getAttributes();
        	Iterator it = treeItem.getAttributes().entrySet().iterator();
    		while (it.hasNext()) {
    			Map.Entry pair = (Map.Entry)it.next();
    	        result += " " + pair.getKey() + "=" + "\"" + pair.getValue() + "\"";
    	    }
    		
    		if (treeItem.getChildCount() == 0) {
    			result += "/>" + "\n";
    		} else {
    			result += ">" + "\n";
    		}
    		
        }
        Iterator it = children.iterator();
        
        while (it.hasNext()) {
        	result += writeTreeIntoString((GMTreeItem) it.next());
        }
        if (children.size() > 0 || treeItem.getXmlName() == "button") {		// node.length > 0 || treeItem.m_value.length() > 0) {
			result += "</" + treeItem.getXmlName() + "> \n";
		}
        
        return result;
    }

    public static String assignUUID() {
        Random rd = new Random(); // creating Random object
        String uuid = String.valueOf(rd.nextLong());
        return uuid;
    }
}
