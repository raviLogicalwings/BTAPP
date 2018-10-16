package com.logicalwings.btapp.adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.HomeActivity;
import com.logicalwings.btapp.activities.MyOrderDetailsActivity;
import com.logicalwings.btapp.fragments.FavouritesFragment;
import com.logicalwings.btapp.fragments.ViewOrderFragment;
import com.logicalwings.btapp.model.CartResponse;
import com.logicalwings.btapp.model.GetOrdersResponse;
import com.logicalwings.btapp.utils.AppUtils;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<GetOrdersResponse> getOrdersResponses;
    private GetOrdersResponse getOrdersResponse;

    public MyOrdersAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<GetOrdersResponse> getOrdersResponses) {
        this.getOrdersResponses = getOrdersResponses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_layout, parent, false);
        MyOrdersAdapter.ViewHolder viewHolder = new MyOrdersAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrdersAdapter.ViewHolder holder, final int position) {

        getOrdersResponse = getOrdersResponses.get(position);

        holder.textOrderPrice.setText("₹" + getOrdersResponse.getTotalAmount());
        holder.textOrderDate.setText(getOrdersResponse.getSapOrderDate());
        holder.textOrderNo.setText(String.valueOf(getOrdersResponse.getSapOrderNo()));

        holder.linearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MyOrderDetailsActivity.class);
                i.putExtra("sapOrderNo", getOrdersResponses.get(position).getSapOrderNo());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getOrdersResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderNo, textOrderDate, textOrderPrice, textSeeMore;
        LinearLayout linearOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            textOrderNo = itemView.findViewById(R.id.text_order_no);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            textOrderPrice = itemView.findViewById(R.id.text_order_price);
//            textSeeMore = itemView.findViewById(R.id.text_see_more_les);
            linearOrder = itemView.findViewById(R.id.linear_order);
        }
    }
}
