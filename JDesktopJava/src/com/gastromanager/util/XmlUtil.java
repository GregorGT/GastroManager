package com.gastromanager.util;


import com.gastromanager.mainwindow.GMTreeItem;
import com.gastromanager.models.MenuDetail;
import com.gastromanager.models.OrderItem;
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
import java.util.*;

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

    private static void treeBuild(GMTreeItem rootNode, Node xmlNode, DefaultTreeModel model ,com.gastromanager.mainwindow.MenuElement menuElement) {
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
    	
    	if (treeItem.getXmlName().equals("layout")) {
    		int c=0;
    	}
    	
        String result = "";  
        List<GMTreeItem> children = treeItem.children;

        
        if (treeItem.getXmlName().length() == 0) 
        	{} 
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
    			if (treeItem.getXmlName() == "button") {
    				result += "/> \n"; 
    			} else {
    				result += ">" + "\n";
    			}
    		}
        }
        Iterator it = children.iterator();
        
        while (it.hasNext()) {
        	result += writeTreeIntoString((GMTreeItem) it.next());
        }
        if (children.size() > 0) {
			result += "</" + treeItem.getXmlName() + "> \n";
		}
        
        return result;
    }

    public static String assignUUID() {
        Random rd = new Random(); // creating Random object
        String uuid = String.valueOf(rd.nextLong());
        return uuid;
    }

    public MenuDetail getMenuDetail() {
		String xmlContent = null;
		MenuDetail menuDetail = null;
		try {
			xmlContent = XmlUtil.readFileToString(
//					"C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml",
					"/home/panagiotis/repos/GastroManager/JDesktopJava/data/sample_tempalte.xml",
					Charset.defaultCharset());
			SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
			menuDetail = parser.parseXml(xmlContent);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return menuDetail;
	}

	public static String formatOrderText(OrderItem orderItem) {
		StringBuilder orderDetailsBuilder = new StringBuilder();

				//Main Item
				Node item = orderItem.getXml().getDocumentElement();
				if (item.getNodeName() == "item") {
					orderDetailsBuilder.append(item.getAttributes().getNamedItem("name").getNodeValue() + GastroManagerConstants.PRICE_SPACING + orderItem.getQuantity() + "\n");
					//addOptionOrderInfo(item, orderDetailsBuilder);
					//Linked items
					addChildItemInfo(item.getChildNodes(), orderDetailsBuilder);
					//addChildItems(item, orderDetailsBuilder);
					orderDetailsBuilder.append("\n");

				}

		System.out.println(orderDetailsBuilder.toString());
		return orderDetailsBuilder.toString().trim();
	}

	public static String getMainOrderItem(OrderItem orderItem) {
    	String orderItemInfo = formatOrderText(orderItem);
    	if(orderItemInfo != null && orderItemInfo.length() > 0) {
    		orderItemInfo = orderItemInfo.substring(0, orderItemInfo.indexOf(" "));
		}
    	return orderItemInfo;
	}

	private static void addChildItemInfo(NodeList children, StringBuilder orderDetailsBuilder) {
		for (int childId = 0; childId < children.getLength(); childId++) {
			Node child = children.item(childId);
			String childItemName  = child.getNodeName();
			if(childItemName == "item") {
				orderDetailsBuilder.append(GastroManagerConstants.FOUR_SPACES + child.getAttributes().getNamedItem("name").getNodeValue());
				addOptionOrderInfo(child, orderDetailsBuilder);
				//orderDetailsBuilder.append("\n");
				if(child.hasChildNodes()) {
					addChildItemInfo(child.getChildNodes(), orderDetailsBuilder);
				}
			}
		}
	}

	private static void addOptionOrderInfo(Node node, StringBuilder orderDetailsBuilder) {
		if(node != null && node.hasChildNodes()) {
			NodeList childNodes  = node.getChildNodes();
			if (childNodes != null) {
				for (int childId = 0; childId < childNodes.getLength(); childId++) {
					Node child = childNodes.item(childId);
					if (child.getNodeName() == "option") {
						System.out.println("adding option information " + child.getAttributes().getNamedItem("name").getNodeValue());
						orderDetailsBuilder.append(GastroManagerConstants.FOUR_SPACES + child.getAttributes().getNamedItem("name").getNodeValue());
						if (child.hasChildNodes()) {
							NodeList optionChildNodes = child.getChildNodes();
							for (int optionChildId = 0; optionChildId < optionChildNodes.getLength(); optionChildId++) {
								Node optionChild = optionChildNodes.item(optionChildId);
								if (optionChild.getNodeName() == "choice") {
									orderDetailsBuilder.append(GastroManagerConstants.FOUR_SPACES + optionChild.getAttributes().getNamedItem("name").getNodeValue());
									break;
								}
							}
						}
						orderDetailsBuilder.append("\n");
						break;
					}

				}
			}
		}

	}

}
