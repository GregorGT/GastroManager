package com.example.gatromanagerclient.ui.payment;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gatromanagerclient.R;
import com.gastromanager.models.OrderItemInfo;

import java.util.List;

public class SelectedMenuItemsAdapter extends RecyclerView.Adapter<SelectedMenuItemsAdapter.ViewHolder> {

    private List<OrderItemInfo> selectedMenuItems;
    public final MenuItemClickListener myListener;
    private Dialog dialog;

    public SelectedMenuItemsAdapter(List<OrderItemInfo> selectedMenuItems, MenuItemClickListener menuItemClickListener) {
        this.selectedMenuItems = selectedMenuItems;
        this.myListener = menuItemClickListener;
    }

    public double getTotalAmount(List<OrderItemInfo> list) {
        Double totalPrice = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            totalPrice = list.stream().mapToDouble(OrderItemInfo::getPrice).sum();
        }
        return totalPrice;
    }

    public List<OrderItemInfo> getOrderItems() {
        return selectedMenuItems;
    }


    public void clearList() {
        selectedMenuItems.clear();
        notifyDataSetChanged();
    }

    public void removeItem(OrderItemInfo orderItemInfo) {
        selectedMenuItems.remove(orderItemInfo);
        notifyDataSetChanged();
    }

    public void addItemToList(OrderItemInfo orderItemInfo) {
        selectedMenuItems.add(orderItemInfo);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItemView = inflater.inflate(R.layout.menu_row_item, parent, false);
        ViewHolder listItemViewHolder = new ViewHolder(listItemView);
        return listItemViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderItemInfo orderItem = selectedMenuItems.get(position);
        String xmlString = orderItem.getXmlText();
        String[] splitString = xmlString.split("\\s+");

        holder.ivPushBtn.setVisibility(View.GONE);

//        holder.tvItemName.setText(splitString[0]);

        holder.tvItemName.setText(xmlString);

        holder.tvItemPrice.setText(String.format("%s EURO", orderItem.getPrice().toString()));

        holder.ivDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onMenuItemClickListener(orderItem, true);
            }
        });

        holder.ivEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(splitString[0], orderItem.getPrice(), position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (selectedMenuItems == null) ? 0 : selectedMenuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName;
        public TextView tvItemPrice;
        public LinearLayout llRootLayout;
        public ImageView ivPushBtn;
        public ImageView ivEditBtn;
        public ImageView ivDeleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvItemName = itemView.findViewById(R.id.tv_item_name);
            this.tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            this.llRootLayout = itemView.findViewById(R.id.root_layout);
            this.ivPushBtn = itemView.findViewById(R.id.iv_push_btn);
            this.ivEditBtn = itemView.findViewById(R.id.iv_edit_btn);
            this.ivDeleteBtn = itemView.findViewById(R.id.iv_delete_btn);
            dialog = new Dialog(itemView.getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        }
    }

    private void showDialog(String itemName, Double itemPrice, int position) {
        dialog.setContentView(R.layout.custom_price_dialog);
        dialog.show();

        TextView tvItemName = dialog.findViewById(R.id.tv_item_name);
        EditText etItemPrice = dialog.findViewById(R.id.et_item_price);
        AppCompatButton btnUpdatePrice = dialog.findViewById(R.id.btn_update_price);

        if (itemName != null && itemPrice != null) {
            tvItemName.setText(String.format("Item Name: %s", itemName));
            etItemPrice.setText(itemPrice.toString());
        }
        btnUpdatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etItemPrice.getText().toString() != null && !etItemPrice.getText().toString().trim().isEmpty()) {
                    Double updatePrice = Double.valueOf(etItemPrice.getText().toString());
                    if (updatePrice != null && updatePrice > 0) {
                        etItemPrice.setError(null);
                        updateItemPrice(position, updatePrice);
                        dialog.dismiss();
                    } else {
                        etItemPrice.setError("Please enter a valid price");
                    }
                } else {
                    etItemPrice.setError("Please enter price");
                }
            }
        });
    }

    public void updateItemPrice(int position, Double price) {
        selectedMenuItems.get(position).setPrice(price);
        notifyDataSetChanged();
        myListener.updateTotalAmount();
    }

}
