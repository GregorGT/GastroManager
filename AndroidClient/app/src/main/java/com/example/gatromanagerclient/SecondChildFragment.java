package com.example.gatromanagerclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.gatromanagerclient.model.DrillDownMenuItemDetail;
import com.example.gatromanagerclient.model.DrillDownMenuItemOptionChoiceDetail;
import com.example.gatromanagerclient.model.DrillDownMenuItemOptionDetail;
import com.example.gatromanagerclient.socket.Client;
import com.example.gatromanagerclient.util.XmlUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SecondChildFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_secondchild, container, false);
        new LoadDrillDownMenuTask().execute("menu", this, container);
        return returnView;
    }

    public void createOrderDrillDown(String response, ViewGroup container) {
        System.out.println("inside creation of drill down "+response);
        LinearLayout linearLayout = (LinearLayout) container.findViewById(R.id.orderDrillDownLayout);
        if(null == response) {
            TextView msgView = new TextView(container.getContext());
            msgView.setText("No data found");
            linearLayout.addView(msgView);
        } else {
            try {
                Document menuXml = XmlUtil.loadXMLFromString(response);
                NodeList drillDownMenusList = menuXml.getElementsByTagName("drilldownmenus"); //TODO fix this as its not returning all elements
                NodeList drillDownMenuList = drillDownMenusList.item(0).getChildNodes();//menuXml.getElementById("drilldownmenus");//getElementsByTagName("drilldownmenu"); //TODO fix this as its not returning all elements
                System.out.println("drill down menu types count " + drillDownMenuList.getLength());
                for (int i = 0; i < drillDownMenuList.getLength(); i++) {
                    Node node = drillDownMenuList.item(i);
                    if (node.hasAttributes()) {
                        System.out.println("has attributes");
                        Node nameAttributeNode = node.getAttributes().getNamedItem("name");
                        System.out.println("NodeValue = " + nameAttributeNode.getNodeValue());
                        if (null != nameAttributeNode.getNodeValue() &&
                                nameAttributeNode.getNodeValue().equals("Android Tablet")) {
                            System.out.println("Tablets");
                            NodeList childNodes = node.getChildNodes();
                            for (int j = 0; j < childNodes.getLength(); j++) {
                                Node childNode = childNodes.item(j);
                                if (childNode.getNodeName().equals("button")) {
                                    String buttonText = childNode.getAttributes().getNamedItem("name").getNodeValue();
                                    String buttonHeight = null;
                                    String buttonWidth = null;
                                    NodeList childNodeChildren = childNode.getChildNodes();
                                    for (int k = 0; k < childNodeChildren.getLength(); k++) {
                                        Node node1 = childNodeChildren.item(k);
                                        if (node1.getNodeName().equals("height")) {
                                            buttonHeight = node1.getTextContent();
                                        } else if (node1.getNodeName().equals("width")) {
                                            buttonWidth = node1.getTextContent();
                                        }
                                    }

                                    System.out.println("adding button for " + buttonText + " " + buttonHeight + " " + buttonWidth);
                                    Button myNewButton = new Button(container.getContext());
                                    myNewButton.setText(buttonText);
                                    myNewButton.setHeight(Integer.parseInt(buttonHeight));
                                    myNewButton.setWidth(Integer.parseInt(buttonWidth));
                                    myNewButton.setVisibility(View.VISIBLE);
                                    myNewButton.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            // your handler code here
                                            DrillDownMenuItemDetail menuItemDetail = loadMenuItemDetail(buttonText, menuXml);
                                            System.out.println("Got here "+menuItemDetail);
                                        }
                                    });
                                    linearLayout.addView(myNewButton);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private DrillDownMenuItemDetail loadMenuItemDetail(String menuItem, Document menuXml) {
        DrillDownMenuItemDetail menuItemDetail = null;
        Map<String, DrillDownMenuItemOptionDetail> optionsMap = null;
        List<DrillDownMenuItemDetail> subItems = null;
        NodeList menuItemNodeList = menuXml.getElementsByTagName("item");
        for(int i=0; i< menuItemNodeList.getLength(); i++) {
            Node menuItemNode  = menuItemNodeList.item(i);
            String menuItemNodeName = menuItemNode.getAttributes().getNamedItem("name").getNodeValue();
            if(menuItemNodeName.equals(menuItem)) {
                menuItemDetail = loadMenuItemDetails(menuItemNode);
                /*NodeList menuItemNodeChildren = menuItemNode.getChildNodes();
                for(int j=0;j<menuItemNodeChildren.getLength(); j++) {
                    Node menuItemNodeChild = menuItemNodeChildren.item(j);
                    if(menuItemNodeChild.getNodeType() == Node.ELEMENT_NODE) {
                        menuItemDetail = loadMenuItemDetails(menuItemNodeChild);
                    }
                }*/
            }

        }
        return menuItemDetail;
    }

    private DrillDownMenuItemDetail loadMenuItemDetails(Node menuNode) {
        DrillDownMenuItemDetail menuItemDetail = null;
        Map<String, DrillDownMenuItemOptionDetail> optionsMap = null;
        List<DrillDownMenuItemDetail> subItems = null;
        String menuItemNodeName = menuNode.getAttributes().getNamedItem("name").getNodeValue(); //check and avoid by taking it from the calling function
        NodeList menuItemNodeChildren = menuNode.getChildNodes();
        for (int j = 0; j < menuItemNodeChildren.getLength(); j++) {
            Node menuItemNodeChild = menuItemNodeChildren.item(j);
            if(menuItemNodeChild.getNodeType() == Node.ELEMENT_NODE) {
                String menuItemNodeChildName = menuItemNodeChild.getNodeName();
                switch (menuItemNodeChildName) {
                    case "option":
                        if (optionsMap == null) {
                            optionsMap = new HashMap<>();
                        }
                        String optionName = menuItemNodeChild.getAttributes().getNamedItem("name").getNodeValue();
                        DrillDownMenuItemOptionDetail option = loadDrillDownMenuOption(menuItemNodeChild);
                        optionsMap.put(optionName, option);
                        break;
                    case "item":
                        DrillDownMenuItemDetail childDrillDownDetail = loadMenuItemDetails(menuItemNodeChild);
                        if (subItems == null) {
                            subItems = new ArrayList<>();
                        }
                        subItems.add(childDrillDownDetail);
                        break;
                }
            }
        }
        menuItemDetail = new DrillDownMenuItemDetail(menuItemNodeName, optionsMap, subItems);

        return menuItemDetail;
    }

    private DrillDownMenuItemOptionDetail loadDrillDownMenuOption(Node optionNode) {
        DrillDownMenuItemOptionDetail option = null;
        List<DrillDownMenuItemOptionChoiceDetail> optionChoices = null;
        option =  new DrillDownMenuItemOptionDetail();
        option.setId(optionNode.getAttributes().getNamedItem("id").getNodeValue());
        option.setName(optionNode.getAttributes().getNamedItem("name").getNodeValue());
        option.setPrice(Double.valueOf((optionNode.getAttributes().getNamedItem("price") != null ?
                optionNode.getAttributes().getNamedItem("price"):optionNode.getAttributes().getNamedItem("overwrite_price")).getNodeValue()));
        if(optionNode.hasChildNodes()) {
            NodeList optionNodeChildren = optionNode.getChildNodes();
            for (int i=0; i < optionNodeChildren.getLength(); i++) {
                Node optionNodeChild = optionNodeChildren.item(i);
                if(optionNodeChild.getNodeType() == Node.ELEMENT_NODE && optionNodeChild.getNodeName().equals("choice")) {
                    if(optionChoices == null) {
                        optionChoices = new ArrayList<>();
                    }
                    DrillDownMenuItemOptionChoiceDetail optionChoice =  new DrillDownMenuItemOptionChoiceDetail();
                    optionChoice.setName(optionNodeChild.getAttributes().getNamedItem("name").getNodeValue());
                    optionChoice.setPrice(Double.valueOf(optionNodeChild.getAttributes().getNamedItem("price").getNodeValue()));
                    optionChoices.add(optionChoice);
                }
            }
        }
        if(optionChoices != null && optionChoices.size() > 0) {
            option.setChoice(optionChoices.get(0));
        }

        return option;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }

    private static class LoadDrillDownMenuTask extends AsyncTask<Object, Void, String> {
        SecondChildFragment secondChildFragment;
        String requestIdentifier;
        @SuppressLint("StaticFieldLeak")
        ViewGroup container;
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(Object... request) {
            Iterator iterator = Arrays.stream(request).iterator();
            int paramCount = 0;
            while(iterator.hasNext()) {
                Object param = iterator.next();
                switch (paramCount) {
                    case 0 : requestIdentifier = (String) param;
                             break;
                    case 1 : secondChildFragment = (SecondChildFragment) param;
                             break;
                    case 2: container = (ViewGroup) param;
                }
                paramCount++;
            }
            Client client = new Client();//Client.getInstance();
            String serverResponse = client.getResponse(requestIdentifier);
            System.out.println("Requesting for "+requestIdentifier);
            System.out.println("Received "+serverResponse);
            return serverResponse;
        }
        @Override
        protected void onPostExecute(String result) {
            //txtOne.setText(response);
            //System.out.println("Received "+result);
            secondChildFragment.createOrderDrillDown(result, container);
            super.onPostExecute(result);
        }
    }
}
