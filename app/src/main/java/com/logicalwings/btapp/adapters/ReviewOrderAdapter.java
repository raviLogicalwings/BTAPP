package com.logicalwings.btapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.fragments.CartFragment;
import com.logicalwings.btapp.model.CartResponse;
import com.logicalwings.btapp.retofit.CartCallBackInterface;
import com.logicalwings.btapp.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.List;

public class ReviewOrderAdapter extends RecyclerView.Adapter<ReviewOrderAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<CartResponse> cartResponses;
    private CartResponse cartResponse = new CartResponse();
    private DecimalFormat convertDecimal = new DecimalFormat("0.00");

    public ReviewOrderAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<CartResponse> cartResponses) {
        this.cartResponses = cartResponses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_order_layout, parent, false);
        ReviewOrderAdapter.ViewHolder viewHolder = new ReviewOrderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewOrderAdapter.ViewHolder holder, final int position) {
        cartResponse = cartResponses.get(position);

        final String qty = String.valueOf(cartResponse.getQty());
        Log.e("qty", qty);
        double productPrice = cartResponse.getTotalPrice() / Double.parseDouble(qty);
        holder.textProductName.setText(cartResponse.getItemName());
        holder.textProductCode.setText(cartResponse.getSapItemCode());
        holder.textProductGstAmount.setText("₹" + convertDecimal.format(cartResponse.getGSTAmount()));
        holder.textProductPrice.setText("₹" + convertDecimal.format(productPrice));
        holder.textProductQty.setText(qty);

    }

    @Override
    public int getItemCount() {
        return cartResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textProductCode, textProductPrice, textProductGstAmount, textProductQty;

        public ViewHolder(View itemView) {
            super(itemView);

            textProductCode = itemView.findViewById(R.id.text_order_code);
            textProductName = itemView.findViewById(R.id.text_order_name);
            textProductPrice = itemView.findViewById(R.id.text_order_price);
            textProductGstAmount = itemView.findViewById(R.id.text_order_tax);
            textProductQty = itemView.findViewById(R.id.text_order_quantity);
        }
    }

}
