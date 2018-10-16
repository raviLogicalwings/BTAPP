package com.logicalwings.btapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.callbacks.FavouriteUnfavouriteOpration;
import com.logicalwings.btapp.callbacks.UpdateProductToDbOpration;
import com.logicalwings.btapp.model.Cart;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ProductItem> productItems;
    private List<Cart> cartList;
    private FavouriteUnfavouriteOpration favouriteUnfavouriteOpration;
    private UpdateProductToDbOpration updateProductToDbOpration;
    private Database base;
    private String itemId;

    public FavouritesAdapter(Context context, List<ProductItem> productItems, FavouriteUnfavouriteOpration favouriteUnfavouriteOpration, UpdateProductToDbOpration updateProductToDbOpration) {
        this.context = context;
        this.favouriteUnfavouriteOpration = favouriteUnfavouriteOpration;
        this.productItems = productItems;
        this.inflater = LayoutInflater.from(this.context);
        this.updateProductToDbOpration = updateProductToDbOpration;
        cartList = new ArrayList<>();
        getSqliteData();
    }

    public void setList(List<ProductItem> productItems) {
        this.productItems = productItems;
        notifyDataSetChanged();
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    @NonNull
    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.favourite_layout, parent, false);
        FavouritesAdapter.ViewHolder viewHolder = new FavouritesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouritesAdapter.ViewHolder holder, final int position) {
        final ProductItem productItem = productItems.get(position);

        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getSapItemCode().equals(productItem.getSapItemCode())) {
                holder.textAddToCart.setText("Update Cart");
                break;
            } else {
                holder.textAddToCart.setText("Add To Cart");
            }
        }

        holder.textProductName.setText(productItem.getItemName());
        holder.textProductCode.setText(productItem.getSapItemCode());

        holder.imageFavourite.setImageResource((
                productItem.getIsFavourite() != null && productItem.getIsFavourite() == 1 ?
                        R.drawable.ic_favorite :
                        R.drawable.ic_favorite_border));

        holder.imageFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productItem.setIsFavourite((productItem.getIsFavourite() == 1 ? 0 : 1));
                notifyDataSetChanged();
                if (favouriteUnfavouriteOpration != null)
                    favouriteUnfavouriteOpration.makeUnfavourite(productItem);
            }
        });

        holder.textAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.textAddToCart.getText().equals("Update Cart")) {
                    updateChangeLangDialog(productItem.getSapItemCode());
                } else {
                    showChangeLangDialog(productItem, holder);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textProductCode, textAddToCart;
        ImageView imageFavourite;

        ViewHolder(View itemView) {
            super(itemView);
            textProductCode = itemView.findViewById(R.id.text_product_code);
            textProductName = itemView.findViewById(R.id.text_product_name);
            imageFavourite = itemView.findViewById(R.id.image_product_favourite);
            textAddToCart = itemView.findViewById(R.id.text_add_to_cart);
        }
    }


    private void showChangeLangDialog(final ProductItem productItem, final FavouritesAdapter.ViewHolder holder) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editQuantity = (EditText) dialogView.findViewById(R.id.edit_quantity);

        dialogBuilder.setTitle(R.string.enter_quantity);
        dialogBuilder.setPositiveButton("Add", null);
        dialogBuilder.setNegativeButton("Cancel", null);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        Button button = b.getButton(AlertDialog.BUTTON_POSITIVE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = editQuantity.getText().toString().trim();

                if (!quantity.equals("") && !quantity.equals("0")) {
                    if (quantity.length() > 7) {
                        AppUtils.showToast(context, "Please select valid quantity!");
                    } else {
                        b.dismiss();
                        holder.textAddToCart.setText("Update Cart");
                        productItem.setQty(Integer.valueOf(quantity));

                        if (updateProductToDbOpration != null)
                            updateProductToDbOpration.updateProduct(productItem);
                        AppUtils.showToast(context, "Added to cart successfully!");
                        notifyDataSetChanged();
                    }
                } else {
                    AppUtils.showToast(context, "please enter quantity");
                }
            }
        });

    }

    private void updateChangeLangDialog(String sapCode) {
        base = new Database(context);
        base.open();
        cartList = base.getCartItem();
        base.close();
        String qty = null;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editQuantity = (EditText) dialogView.findViewById(R.id.edit_quantity);

        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getSapItemCode().equals(sapCode)) {
                qty = cartList.get(i).getQuantity();
                itemId = cartList.get(i).getItemId();
                editQuantity.setText(qty);
            }
        }

        editQuantity.setSelection(editQuantity.getText().length());
        dialogBuilder.setTitle(R.string.enter_quantity);
        dialogBuilder.setPositiveButton("Update", null);
        dialogBuilder.setNegativeButton("Cancel", null);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        Button button = b.getButton(AlertDialog.BUTTON_POSITIVE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = editQuantity.getText().toString().trim();

                if (!quantity.equals("") && !quantity.equals("0")) {
                    if (quantity.length() > 7) {
                        AppUtils.showToast(context, "Please select valid quantity!");
                    } else {
                        base.open();
                        base.updateCartQuantity(itemId, quantity);
                        base.close();
                        AppUtils.showToast(context, "Quantity Updated");
                    }
                } else {
                    AppUtils.showToast(context, "please enter quantity");
                }
            }
        });
    }

    private void getSqliteData() {
        base = new Database(context);
        base.open();
        cartList = base.getCartItem();
        base.close();
    }
}
