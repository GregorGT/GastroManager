package com.gastromanager.util;


import com.gastromanager.mainwindow.GMTreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void treeBuild(GMTreeItem rootNode, Node xmlNode) {
        if (xmlNode.getNodeName().contains("#"))
            return;

        GMTreeItem newNode = new GMTreeItem(rootNode);
        rootNode.add(newNode);

        NamedNodeMap attributes = xmlNode.getAttributes();

        if(attributes != null) {
            for(int k = 0; k<attributes.getLength(); ++k) {
                String name = attributes.item(k).getNodeName();
                String value = attributes.item(k).getNodeValue();

                newNode.m_attributes.put(name,  value);
                newNode.m_attributes.putIfAbsent("uuid", assignUUID());
                //if it's already assigned, do not assign
            }
        }

        if (xmlNode.getNodeName() == "x") {
            String emptyNode = xmlNode.getTextContent();
        }

        if (xmlNode.getChildNodes().getLength() == 1) {
            if (xmlNode.getFirstChild().hasChildNodes() == false) {
                String nodeName = xmlNode.getFirstChild().getNodeName();
                if (nodeName == "#text") {
                    newNode.m_value = xmlNode.getFirstChild().getTextContent();
                }
            }
        }

        if (xmlNode.getNodeValue() != null && xmlNode.getNodeValue().length() > 0)
            newNode.m_value = xmlNode.getNodeValue();

        newNode.m_xmlname = xmlNode.getNodeName();
        newNode.setUserObject(newNode.getDisplayString());
        NodeList children = xmlNode.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            treeBuild(newNode, children.item(i));
        }
    }

    public static void parseXmlDocument(Document doc, GMTreeItem root) {
        try {
            treeBuild(root, doc.getFirstChild());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String writeTreeIntoString(GMTreeItem treeItem) {

        String result = "";
        //Write saving function
        return result;
    }

    public static String assignUUID() {
        Random rd = new Random(); // creating Random object
        String uuid = String.valueOf(rd.nextLong());
        return uuid;
    }
}
