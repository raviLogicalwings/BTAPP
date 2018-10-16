package com.logicalwings.btapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.CartActivity;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.model.CartResponse;
import com.logicalwings.btapp.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<CartResponse> cartResponses;
    private CartResponse cartResponse = new CartResponse();
    private boolean seeMore = true;
    private DecimalFormat convertDecimal = new DecimalFormat("0.00");
    private Database base;

    public CartAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<CartResponse> cartResponses) {
        this.cartResponses = cartResponses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {
        cartResponse = cartResponses.get(position);

        final String qty = String.valueOf(cartResponse.getQty());
        double productPrice = cartResponse.getTotalPrice() / Double.parseDouble(qty);
        holder.textProductName.setText(cartResponse.getItemName());
        holder.textProductCode.setText(cartResponse.getSapItemCode());
        holder.textTotalPrice.setText("₹" + convertDecimal.format(cartResponse.getTotalPrice()));
        holder.textProductQuantity.setText(String.valueOf("Qty: " + cartResponse.getQty()));
        holder.textProductGstAmount.setText("₹" + convertDecimal.format(cartResponse.getGSTAmount()));
        holder.textProductTotalAmount.setText("₹" + convertDecimal.format(cartResponse.getTotalAmount()));
        holder.textProductPrice.setText("₹" + convertDecimal.format(productPrice));

        holder.linearProductQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantityDialog(cartResponses, position, qty);
            }
        });

        holder.linearProductSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seeMore) {
                    holder.linearProductGetMore.setVisibility(View.VISIBLE);
                    holder.textSeeMoreLess.setText("See Less");
                    seeMore = false;
                } else {
                    holder.linearProductGetMore.setVisibility(View.GONE);
                    holder.textSeeMoreLess.setText("See More");
                    seeMore = true;
                }
            }
        });

        holder.imageProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position, cartResponses, context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textProductCode, textProductPrice, textProductQuantity, textProductGstAmount, textProductTotalAmount, textSeeMoreLess, textTotalPrice;
        LinearLayout linearProductQuantity, linearProductSeeMore, linearProductGetMore;
        ImageView imageProductDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            textProductCode = itemView.findViewById(R.id.text_product_code);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);
            textProductQuantity = itemView.findViewById(R.id.text_product_quantity);
            textProductTotalAmount = itemView.findViewById(R.id.text_product_total_amount);
            textProductGstAmount = itemView.findViewById(R.id.text_product_gst_amount);
            textSeeMoreLess = itemView.findViewById(R.id.text_see_more_less);
            linearProductQuantity = itemView.findViewById(R.id.linear_product_quantity);
            linearProductSeeMore = itemView.findViewById(R.id.linear_product_seemore);
            linearProductGetMore = itemView.findViewById(R.id.linear_product_getmore);
            imageProductDelete = itemView.findViewById(R.id.image_product_delete);
            textTotalPrice = itemView.findViewById(R.id.text_total_price);
        }
    }

    private void updateQuantityDialog(final List<CartResponse> cartResponses, final int position, String qty) {

        base = new Database(context);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editQuantity = (EditText) dialogView.findViewById(R.id.edit_quantity);
        editQuantity.setText(qty);
        editQuantity.setSelection(editQuantity.getText().length());
        dialogBuilder.setTitle(R.string.update_quantity);
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String quantity = editQuantity.getText().toString().trim();
                if (!quantity.equals("") && !quantity.equals("0")) {
                    Database base = new Database(context);
                    base.open();
                    base.updateCartQuantity(String.valueOf(cartResponses.get(position).getItemId()), quantity);
                    base.close();
                    Intent i = new Intent(context, CartActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
//                    cartFragment.updateProductQuantity(position, cartResponses, quantity, context);
                } else {
                    AppUtils.showToast(context, "please enter quantity");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void showDeleteDialog(final int position, final List<CartResponse> cartResponses, final Context context) {
        base = new Database(context);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.delete_product);
        dialogBuilder.setMessage("Are you sure want to delete product?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                base.open();
                base.deleteCartData(String.valueOf(cartResponses.get(position).getItemId()));
                base.close();
                Intent i = new Intent(context, CartActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
