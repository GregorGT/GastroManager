package com.example.gatromanagerclient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.gatromanagerclient.socket.Client;
import com.example.gatromanagerclient.util.Util;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gatromanagerclient.databinding.ActivityMain2Binding;

import org.w3c.dom.Text;

import java.util.Arrays;

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
}