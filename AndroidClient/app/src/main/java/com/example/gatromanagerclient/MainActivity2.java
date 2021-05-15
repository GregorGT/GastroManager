package com.example.gatromanagerclient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.gatromanagerclient.model.DrillDownMenuType;
import com.example.gatromanagerclient.model.MenuDetail;
import com.example.gatromanagerclient.socket.Client;
import com.example.gatromanagerclient.util.SaxParserForGastromanager;
import com.example.gatromanagerclient.util.Util;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gatromanagerclient.databinding.ActivityMain2Binding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMain2Binding binding;
    private TextView orderDetailsView;
    private Button fetchOrderDetailsButton;

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
        //fetch data from server
        new LoadMenuTask().execute("menu", this, drilldownMenuTypeSpinner);
        drilldownMenuTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                String selecteditem =  adapter.getItemAtPosition(i).toString();
                System.out.println("selected "+selecteditem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

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
        System.out.println("loading menu details for "+response);
        SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
        return parser.parseXml(response, true);
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

    private class LoadMenuTask extends AsyncTask<Object, Void, String> {
        MainActivity2 mainActivity2;
        String requestIdentifier;
        Spinner menuTypeSpinner;

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
                    case 1 : mainActivity2 = (MainActivity2) param;
                        break;
                    case 2 : menuTypeSpinner = (Spinner) param;
                        break;
                   }
                paramCount++;
            }
            Client client = new Client();//Client.getInstance();
            String serverResponse = client.getResponse(requestIdentifier);
            System.out.println("Requesting for "+requestIdentifier);
            //System.out.println("Received "+serverResponse);
            client = null;
            return serverResponse;
        }
        @Override
        protected void onPostExecute(String result) {
            //txtOne.setText(response);
            System.out.println("Received on postExecute "+result);
            MenuDetail menuDetail = mainActivity2.loadMenuDetails(result);
            mainActivity2.loadMenuTypes(menuTypeSpinner, menuDetail);
            super.onPostExecute(result);
        }
    }
}