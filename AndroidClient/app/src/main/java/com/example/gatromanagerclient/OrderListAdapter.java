package com.example.gatromanagerclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.gastromanager.models.OrderItem;
import com.gastromanager.models.OrderItemInfo;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {


    private List<OrderItemInfo> orderItems;
    private OrderItemClickListener orderItemClickListener;

    public OrderListAdapter(List<OrderItemInfo> orderItems, OrderItemClickListener orderItemClickListener) {
        this.orderItems = orderItems;
        this.orderItemClickListener = orderItemClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateOrderItemList(List<OrderItemInfo> orderItemInfoList) {
        orderItems.clear();
        if(orderItemInfoList != null && orderItemInfoList.size() > 0) {
            orderItems.addAll(orderItemInfoList);
            orderItems.sort(new OrderItemComparator());
        }
        notifyDataSetChanged();
    }

    public List<OrderItemInfo> getOrderItems() {
        return orderItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View listItemView = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new holder instance
        ViewHolder listItemViewHolder = new ViewHolder(listItemView, orderItemClickListener);
        return listItemViewHolder;
    }

    // Involves populating data into the item through holder
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        OrderItemInfo orderItem = (OrderItemInfo) orderItems.get(position);
        // Set item views based on your views and data model
        holder.itemTextView.setText(orderItem.getXmlText());
        holder.itemTextView.setBackgroundColor(R.color.white);
        if(orderItem.getPrintStatus() == 1) {
            holder.itemTextView.setBackgroundColor(0xFF12FF45); //0xFF12FF45 = GREEN
        }
        /*holder.relativeLayout.setOnClickListener(view -> {
            Toast.makeText(view.getContext(),"click on item: "+orderItem.getItemId(),Toast.LENGTH_LONG).show();
            showMessage("Do you wish to remove item "+orderItem.getItemId(), orderItem, view);
        });*/
    }

    public void showMessage(String message, OrderItemInfo orderItem, View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        alert.setTitle(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int whichButton) {
                orderItems.remove(orderItem);
                updateOrderItemList(orderItems);
            }

        });

       alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            //what you need to do after click "Cancel"
        }
    });
            alert.show();
}


    @Override
    public int getItemCount() {
        return (orderItems == null) ? 0 :orderItems.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView itemTextView;
        public RelativeLayout relativeLayout;
        public OrderItemClickListener orderItemClickListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, OrderItemClickListener orderItemClickListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
            this.itemTextView = (TextView) itemView.findViewById(R.id.orderItemRowView);
            this.orderItemClickListener = orderItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orderItemClickListener.onOrderItemClick(getAdapterPosition());
        }
    }

    public interface OrderItemClickListener {
        void onOrderItemClick(int position);
    }
}
