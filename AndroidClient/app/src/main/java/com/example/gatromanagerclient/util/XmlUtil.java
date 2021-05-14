package com.example.gatromanagerclient.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XmlUtil {

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String readFileToString(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static List<String> getDrillDownMenuTypes(String drillDownMenuTag, Document xmlDoc) {
        List<String> drillDownMenuTypeList = null;
        Element root = xmlDoc.getDocumentElement();
        NodeList children = root.getChildNodes();
        for(int i=0; i< children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if(child.getNodeName().equals("drilldownmenus")) {
                    NodeList childNodeChildren = child.getChildNodes();
                    for(int j=0; j<childNodeChildren.getLength(); j++) {
                        Node childNodeChild = childNodeChildren.item(j);
                        if(childNodeChild.getNodeType() == Node.ELEMENT_NODE
                                && childNodeChild.getNodeName().equals("drilldownmenu")) {
                            if(drillDownMenuTypeList == null) {
                                drillDownMenuTypeList = new ArrayList<>();
                            }
                            drillDownMenuTypeList.add(childNodeChild.getAttributes()
                                    .getNamedItem("name").getNodeValue());

                        }
                    }
                }
            }
        }

        return drillDownMenuTypeList;
    }



    public static String assignUUID() {
        Random rd = new Random(); // creating Random object
        String uuid = String.valueOf(rd.nextLong());
        return uuid;
    }
}
