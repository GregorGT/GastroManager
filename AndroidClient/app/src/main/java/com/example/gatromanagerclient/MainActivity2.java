package com.example.gatromanagerclient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.gastromanager.models.DrillDownMenuButton;
import com.gastromanager.models.DrillDownMenuType;
import com.gastromanager.models.MenuDetail;
import com.example.gatromanagerclient.socket.Client;
import com.example.gatromanagerclient.util.SaxParserForGastromanager;
import com.example.gatromanagerclient.util.Util;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.gatromanagerclient.databinding.ActivityMain2Binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;
    private TextView orderDetailsView;
    private Button fetchOrderDetailsButton;
    private MenuDetail menuDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        orderDetailsView = findViewById(R.id.textView2);
        System.out.println("Current order details :" + orderDetailsView.getText());
        EditText orderIdInputTextField = findViewById(R.id.orderIdInputTextField);
        fetchOrderDetailsButton = findViewById(R.id.fetchOrderInfoButton);
        fetchOrderDetailsButton.setOnClickListener(v -> {
                    System.out.println("inside get order");
                    orderDetailsView.setText("");
            //EditText orderIdInputTextField = findViewById(R.id.orderIdInputTextField);
            String inputOrderId = (orderIdInputTextField.getText() != null) ?
                    orderIdInputTextField.getText().toString():null;
                    System.out.println("get details for order id "+inputOrderId);
            if(inputOrderId != null && !inputOrderId.isEmpty()) {
                new GetOrderDetailsTask().execute(inputOrderId);
            }
        }
        );

        //Previous
        Button prevOverButton = findViewById(R.id.prevOrderButton);
        prevOverButton.setOnClickListener(v -> {
                    System.out.println("inside get previous order");
                    orderDetailsView.setText("");
                    //EditText orderIdInputTextField = findViewById(R.id.orderIdInputTextField);
                    String inputOrderId = (orderIdInputTextField.getText() != null) ?
                            orderIdInputTextField.getText().toString():null;
                    if(Util.isNumeric(inputOrderId)) {
                        Integer currOrderId = Integer.valueOf(inputOrderId);
                        currOrderId = (currOrderId-1 < 0) ? 0: currOrderId-1;
                        orderIdInputTextField.setText(String.valueOf(currOrderId));
                        inputOrderId = (orderIdInputTextField.getText() != null) ?
                                orderIdInputTextField.getText().toString():null;
                    }
                    System.out.println("get details for order id "+inputOrderId);
                    if(inputOrderId != null && !inputOrderId.isEmpty()) {
                        new GetOrderDetailsTask().execute(inputOrderId.trim());
                    }
                }
        );

        //Next
        Button nextOverButton = findViewById(R.id.nextOrderButton);
        nextOverButton.setOnClickListener(v -> {
                    System.out.println("inside get previous order");
                    orderDetailsView.setText("");
                    //EditText orderIdInputTextField = findViewById(R.id.orderIdInputTextField);
                    String inputOrderId = (orderIdInputTextField.getText() != null) ?
                            orderIdInputTextField.getText().toString():null;
                    if(Util.isNumeric(inputOrderId)) {
                        Integer currOrderId = Integer.valueOf(inputOrderId);
                        orderIdInputTextField.setText(String.valueOf(currOrderId+1));
                        inputOrderId = (orderIdInputTextField.getText() != null) ?
                                orderIdInputTextField.getText().toString():null;
                    }
                    System.out.println("get details for order id "+inputOrderId);
                    if(inputOrderId != null && !inputOrderId.isEmpty()) {
                        new GetOrderDetailsTask().execute(inputOrderId.trim());
                    }
                }
        );
        //Drill down menu
        Spinner drilldownMenuTypeSpinner = findViewById(R.id.spinner2);
        //Menu space
        LinearLayout menuView = findViewById(R.id.menuItemsView);
        //fetch data from server
        try {
            menuDetail = new LoadMenuTask().execute("menu", this, drilldownMenuTypeSpinner).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        drilldownMenuTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem =  adapter.getItemAtPosition(i).toString();
                System.out.println("selected "+selecteditem+ " "+menuDetail);
                menuView.removeAllViews();
                loadMenuView(selecteditem, menuDetail, menuView);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

    }

    private void loadMenuItems(DrillDownMenuType menuType, MenuDetail menuDetail, LinearLayout view) {
        List<DrillDownMenuButton> menuButtons  = menuType.getButtons();
        for(DrillDownMenuButton menuButton:menuButtons) {
            Button menuButtonView = new Button(this);
            menuButtonView.setWidth(Integer.valueOf(menuButton.getWidth()));
            menuButtonView.setHeight(Integer.valueOf(menuButton.getHeight()));
            menuButtonView.setText(menuButton.getName());
            menuButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // your handler code here
                    System.out.println("Got here ");
                }
            });
            view.addView(menuButtonView);

        }
    }

    private void loadMenuView(String selectedMenuType, MenuDetail menuDetail, LinearLayout view) {
        List<DrillDownMenuType> menuTypeList = menuDetail.getDrillDownMenus().getDrillDownMenuTypes();
        for(DrillDownMenuType menuType: menuTypeList) {
            if(menuType.getName().equals(selectedMenuType)) {
                loadMenuItems(menuType, menuDetail, view);
                break;
            }
        }
        /*Button myNewButton = new Button(container.getContext());
        myNewButton.setText(buttonText);
        myNewButton.setHeight(Integer.parseInt(buttonHeight));
        myNewButton.setWidth(Integer.parseInt(buttonWidth));
        myNewButton.setVisibility(View.VISIBLE);
        myNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                List<String> drillDownMenuTupes  = XmlUtil.getDrillDownMenuTypes("drilldownmenus", menuXml);
                System.out.println(drillDownMenuTupes);
                DrillDownMenuItemDetail menuItemDetail = loadMenuItemDetail(buttonText, menuXml);
                System.out.println("Got here "+menuItemDetail);
            }
        });
        linearLayout.addView(myNewButton);*/
    }

    private void loadMenuTypes(Spinner spinner,MenuDetail menuDetail) {
        if(menuDetail != null && spinner != null) {
            List<String> menuTypes = new ArrayList<>();
            for (DrillDownMenuType menuType : menuDetail.getDrillDownMenus().getDrillDownMenuTypes()) {
                menuTypes.add(menuType.getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, menuTypes);
            arrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }
    }

    private MenuDetail loadMenuDetails(String response) {
        MenuDetail responseMenuDetail = null;
        System.out.println("loading menu details for "+response);
        SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
        responseMenuDetail = parser.parseXml(response, true);
        menuDetail = responseMenuDetail;
        return responseMenuDetail;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetOrderDetailsTask extends AsyncTask<String, Void, Void> {
        String response;
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(String... request) {
            Client client = new Client();//.getInstance();
            response = client.getResponse(Arrays.stream(request).findFirst().get());
            System.out.println("Requesting for "+Arrays.stream(request).findFirst().get());
            System.out.println("Response "+response);
            client = null;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            orderDetailsView.setText(response);
            super.onPostExecute(result);
        }
    }

    private class LoadMenuTask extends AsyncTask<Object, Void, MenuDetail> {
        MainActivity2 mainActivity2;
        String requestIdentifier;
        Spinner menuTypeSpinner;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected MenuDetail doInBackground(Object... request) {
            Iterator iterator = Arrays.stream(request).iterator();
            int paramCount = 0;
            while(iterator.hasNext()) {
                Object param = iterator.next();
                switch (paramCount) {
                    case 0 : requestIdentifier = (String) param;
                        break;
                    case 1 : mainActivity2 = (MainActivity2) param;
                        break;
                    case 2 : menuTypeSpinner = (Spinner) param;
                        break;
                   }
                paramCount++;
            }
            Client client = new Client();//Client.getInstance();
            //String serverResponse = client.getResponse(requestIdentifier);
            MenuDetail serverResponse = client.getMenuDetails(requestIdentifier);
            System.out.println("Requesting for "+requestIdentifier);
            //System.out.println("Received "+serverResponse);
            client = null;
            return serverResponse;
        }
        @Override
        protected void onPostExecute(MenuDetail result) {
            //txtOne.setText(response);
            System.out.println("Received on postExecute "+result);
            //MenuDetail menuDetail = mainActivity2.loadMenuDetails(result);
            mainActivity2.loadMenuTypes(menuTypeSpinner, result);
            super.onPostExecute(result);
        }
    }
}