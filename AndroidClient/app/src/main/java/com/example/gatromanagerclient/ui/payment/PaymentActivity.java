package com.example.gatromanagerclient.ui.payment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gatromanagerclient.R;
import com.example.gatromanagerclient.socket.Client;
import com.example.gatromanagerclient.util.Constants;
import com.example.gatromanagerclient.util.Util;
import com.gastromanager.models.OrderDetailQuery;
import com.gastromanager.models.OrderItemInfo;
import com.gastromanager.models.OrderItemTransactionInfo;
import com.gastromanager.models.TransactionInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, MenuItemClickListener {

    private TextInputEditText etFloorId, etTableId, etOrderId;
    private TextInputLayout tlfFloor, tlfTable, tlfOrder;
    private LinearLayout llMenuItemsEmpty, llSelectedMenuItemsEmpty;
    private ImageView ivFloorLeft, ivFloorRight, ivTableLeft, ivTableRight, ivOrderLeft, ivOrderRight;
    private AppCompatButton btnClear, btnSubmit, btnSelectOrder;
    private TextView tvTotalAmount;
    private RecyclerView rvMenuItem, rvSelectedMenuItem;
    private Dialog progressDialog;
    private MenuItemsAdapter menuItemsAdapter;
    private SelectedMenuItemsAdapter selectedMenuItemsAdapter;
    private List<OrderItemInfo> menuItemInfoList = new ArrayList<>();
    private List<OrderItemInfo> selectedMenuItemsList = new ArrayList<>();
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        // initializing views
        initViews();

        //Listening when table Id changed calling socket to get orderId and set in the field
        etTableId.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (etFloorId.getText() != null &&
                        etTableId.getText() != null) {
                    String floorId = etFloorId.getText().toString();
                    String tableId = etTableId.getText().toString();
                    if (!floorId.trim().equals(Constants.EMPTY_RESULT)
                            && !tableId.trim().equals(Constants.EMPTY_RESULT)) {
                        setProgressbar(true);
                        new GetOrderID().execute();
                    }
                }
            }
        });
    }

    private void initViews() {
        progressDialog = new Dialog(this);

        etFloorId = findViewById(R.id.et_floor_Id);
        etTableId = findViewById(R.id.et_table_Id);
        etOrderId = findViewById(R.id.et_order_Id);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        tlfFloor = findViewById(R.id.text_layout_floor);
        tlfTable = findViewById(R.id.text_layout_table);
        tlfOrder = findViewById(R.id.text_layout_order_id);

        llMenuItemsEmpty = findViewById(R.id.ll_menuItems_empty);
        llSelectedMenuItemsEmpty = findViewById(R.id.ll_selectedItems_empty);

        rvMenuItem = findViewById(R.id.rv_menu_items);
        menuItemsAdapter = new MenuItemsAdapter(menuItemInfoList, this);
        rvMenuItem.setHasFixedSize(true);
        rvMenuItem.setLayoutManager(new LinearLayoutManager(this));
        rvMenuItem.setAdapter(menuItemsAdapter);

        rvSelectedMenuItem = findViewById(R.id.rv_selected_menu_items);
        selectedMenuItemsAdapter = new SelectedMenuItemsAdapter(selectedMenuItemsList, this);
        rvSelectedMenuItem.setHasFixedSize(true);
        rvSelectedMenuItem.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedMenuItem.setAdapter(selectedMenuItemsAdapter);


        ivFloorLeft = findViewById(R.id.iv_left_floor_id);
        ivFloorRight = findViewById(R.id.iv_right_floor_id);
        ivFloorLeft.setOnClickListener(this);
        ivFloorRight.setOnClickListener(this);

        ivTableLeft = findViewById(R.id.iv_left_table_id);
        ivTableRight = findViewById(R.id.iv_right_table_id);
        ivTableLeft.setOnClickListener(this);
        ivTableRight.setOnClickListener(this);

        ivOrderLeft = findViewById(R.id.iv_left_order_id);
        ivOrderRight = findViewById(R.id.iv_right_order_id);
        ivOrderLeft.setOnClickListener(this);
        ivOrderRight.setOnClickListener(this);

        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        btnSelectOrder = findViewById(R.id.btn_select_order);
        btnSelectOrder.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_floor_id:
                System.out.println("MyAppLogs left floor");
                break;
            case R.id.iv_right_floor_id:
                System.out.println("MyAppLogs right floor");
                break;
            case R.id.btn_select_order:
                if (validateInputFields()) {
                    getSelectOrderId();
                }
                break;
            case R.id.iv_left_order_id:
                System.out.println("MyAppLogs left order");
                break;
            case R.id.iv_right_order_id:
                break;
            case R.id.iv_left_table_id:
                System.out.println("MyAppLogs left table");
                break;
            case R.id.iv_right_table_id:
                System.out.println("MyAppLogs right table");
                break;
            case R.id.btn_clear:
                onClearClicked();
                break;
            case R.id.btn_submit:
                payForItemsOnSubmitClicked();
                break;
        }
    }

    private void payForItemsOnSubmitClicked() {
        if (selectedMenuItemsAdapter.getOrderItems() == null || selectedMenuItemsAdapter.getOrderItems().isEmpty()) {
            setEmptyIconsVisibility();
        } else {
            OrderItemTransactionInfo request = new OrderItemTransactionInfo();
            request.setOrderItemInfo(selectedMenuItemsAdapter.getOrderItems());
            request.setAddTransaction(true);
            request.setTransactionInfo(new TransactionInfo());
            setProgressbar(true);
            new payForItems().execute(request);
        }
    }

    private void getSelectOrderId() {
        setProgressbar(true);
        String orderId = (etOrderId.getText() != null) ?
                etOrderId.getText().toString() : null;
        if (orderId != null && orderId.length() >= 0) {
//            int currentOrderId = Integer.parseInt(orderId);
//            etOrderId.setText(String.valueOf(currentOrderId + 1));
//            orderId = (etOrderId.getText() != null) ?
//                    etOrderId.getText().toString() : null;
            etOrderId.setError(null);
            OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
            orderDetailQuery.setHumanreadableId(orderId);

            if (etFloorId.getText() != null)
                orderDetailQuery.setFloorId(etFloorId.getText().toString());

            if (etTableId.getText() != null)
                orderDetailQuery.setTableId(etTableId.getText().toString());

            if (orderId != null && !orderId.isEmpty()) {
                new GetOrderDetails().execute(orderDetailQuery, this);
            }
        } else {
            etOrderId.setError("Enter order id ");
        }
    }

    private boolean validateInputFields() {
        tlfFloor.setError(null);
        tlfTable.setError(null);
        tlfOrder.setError(null);

        if (Util.isEmptyOrNull(etFloorId)) {
            tlfFloor.setError("Enter Floor id");
            return false;
        }
        if (Util.isEmptyOrNull(etTableId)) {
            tlfTable.setError("Enter Table id");
            return false;
        }

        if (Util.isEmptyOrNull(etOrderId)) {
            tlfOrder.setError("Enter Order id");
            return false;
        }
        return true;
    }

    public void setProgressbar(boolean showStatus) {
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        progressDialog.setCancelable(false);
        if (!showStatus) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }
    }

    public void onMenuItemClickListener(OrderItemInfo orderItemInfo, boolean isSelectedMenuList) {
        if (isSelectedMenuList) {
            menuItemsAdapter.addItemToList(orderItemInfo);
            selectedMenuItemsAdapter.removeItem(orderItemInfo);
            menuItemInfoList = menuItemsAdapter.getOrderItems();
        } else {
            selectedMenuItemsAdapter.addItemToList(orderItemInfo);
            menuItemsAdapter.removeItem(orderItemInfo);
            selectedMenuItemsList = selectedMenuItemsAdapter.getOrderItems();
        }
        updateTotalAmountUI();
        setEmptyIconsVisibility();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClearClicked() {
        if (selectedMenuItemsAdapter.getOrderItems() != null) {
            List<OrderItemInfo> list = selectedMenuItemsAdapter.getOrderItems();
            menuItemInfoList.addAll(list);
            menuItemsAdapter.updateOrderItemList(menuItemInfoList);
            selectedMenuItemsAdapter.clearList();
            updateTotalAmountUI();
        }
        setEmptyIconsVisibility();
    }

    private void setEmptyIconsVisibility() {
        if (selectedMenuItemsAdapter.getOrderItems() != null && selectedMenuItemsAdapter.getOrderItems().isEmpty()) {
            llSelectedMenuItemsEmpty.setVisibility(View.VISIBLE);
        } else {
            llSelectedMenuItemsEmpty.setVisibility(View.GONE);
        }

        if (menuItemsAdapter.getOrderItems() != null && menuItemsAdapter.getOrderItems().isEmpty()) {
            llMenuItemsEmpty.setVisibility(View.VISIBLE);
        } else {
            llMenuItemsEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateTotalAmount() {
        updateTotalAmountUI();
    }

    private void updateTotalAmountUI() {
        Double totalAmount = selectedMenuItemsAdapter.getTotalAmount(selectedMenuItemsList);
        tvTotalAmount.setText(String.format("Total: %s Euro", totalAmount));
        Util.hideKeyboard(PaymentActivity.this);
    }

    // async task for calling socket
    private class GetOrderID extends AsyncTask<Void, Void, Integer> {
        Integer response;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(Void... voids) {
            Client client = new Client();
            response = client.getHumanReadableOrderId(
                    etFloorId.getText().toString(),
                    etTableId.getText().toString()
            );
            return response;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Integer result) {
            if (result != null) etOrderId.setText(result.toString());
            else Util.showToast(PaymentActivity.this, "No existing orders");
            setProgressbar(false);
            super.onPostExecute(result);
        }
    }

    private class GetOrderDetails extends AsyncTask<Object, Void, Void> {
        PaymentActivity paymentActivity;
        OrderDetailQuery requestIdentifier;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Object... request) {
            Iterator iterator = Arrays.stream(request).iterator();
            int paramCount = 0;
            while (iterator.hasNext()) {
                Object param = iterator.next();
                switch (paramCount) {
                    case 0:
                        requestIdentifier = (OrderDetailQuery) param;
                        break;
                    case 1:
                        paymentActivity = (PaymentActivity) param;
                        break;
                }
                paramCount++;
            }
            Client client = new Client();
            menuItemInfoList = client.getOrderInfo(requestIdentifier);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void result) {
            if (menuItemInfoList != null)
                menuItemsAdapter.updateOrderItemList(menuItemInfoList);
            else
                menuItemsAdapter.updateOrderItemList(new ArrayList<OrderItemInfo>());
            selectedMenuItemsAdapter.clearList();
            updateTotalAmountUI();
            setEmptyIconsVisibility();
            Util.hideKeyboard(PaymentActivity.this);
            setProgressbar(false);
            super.onPostExecute(result);
        }
    }

    private class payForItems extends AsyncTask<Object, Void, Void> {
        PaymentActivity paymentActivity;
        OrderItemTransactionInfo requestBody;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Object... request) {
            Iterator iterator = Arrays.stream(request).iterator();
            int paramCount = 0;
            while (iterator.hasNext()) {
                Object param = iterator.next();
                switch (paramCount) {
                    case 0:
                        requestBody = (OrderItemTransactionInfo) param;
                        break;
                    case 1:
                        paymentActivity = (PaymentActivity) param;
                        break;
                }
                paramCount++;
            }
            Client client = new Client();
            menuItemInfoList = client.payOrderItems(requestBody);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void result) {
            menuItemsAdapter.updateOrderItemList(menuItemInfoList);
            selectedMenuItemsAdapter.clearList();
            updateTotalAmountUI();
            setEmptyIconsVisibility();
            setProgressbar(false);
            super.onPostExecute(result);
        }
    }

}