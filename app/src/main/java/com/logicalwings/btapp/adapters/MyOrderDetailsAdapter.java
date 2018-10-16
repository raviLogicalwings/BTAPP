package com.logicalwings.btapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.model.ItemsItem;

import java.util.List;

public class MyOrderDetailsAdapter extends RecyclerView.Adapter<MyOrderDetailsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<ItemsItem> itemsItems;
    private ItemsItem itemsItem;

    public MyOrderDetailsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<ItemsItem> itemsItems) {
        this.itemsItems = itemsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyOrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.order_details_layout, parent, false);
        MyOrderDetailsAdapter.ViewHolder viewHolder = new MyOrderDetailsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderDetailsAdapter.ViewHolder holder, int position) {
        itemsItem = itemsItems.get(position);
        Log.e("itemsItemSize", String.valueOf(itemsItems.size()));

        holder.textSapItemCode.setText(itemsItem.getSapItemCode());
        holder.textItemName.setText(itemsItem.getItemName());
        holder.textQty.setText(String.valueOf(itemsItem.getQty()));
        holder.textTotalPrice.setText(String.valueOf(itemsItem.getTotalPrice()));
        holder.textGst.setText(String.valueOf(itemsItem.getGSTAmount()));
        holder.textTotalAmount.setText(String.valueOf(itemsItem.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return itemsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textSapItemCode, textItemName, textQty, textTotalPrice, textGst, textTotalAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            textSapItemCode = itemView.findViewById(R.id.text_sapItem_code);
            textItemName = itemView.findViewById(R.id.text_order_item_name);
            textQty = itemView.findViewById(R.id.text_order_qty);
            textTotalPrice = itemView.findViewById(R.id.text_order_total_price);
            textGst = itemView.findViewById(R.id.text_order_gst);
            textTotalAmount = itemView.findViewById(R.id.text_order_total_amount);
        }
    }
}
