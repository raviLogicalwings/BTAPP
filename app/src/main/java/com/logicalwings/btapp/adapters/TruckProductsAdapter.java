package com.logicalwings.btapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.SearchProductActivity;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.callbacks.FavouriteUnfavouriteOpration;
import com.logicalwings.btapp.callbacks.UpdateProductToDbOpration;
import com.logicalwings.btapp.model.Cart;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class TruckProductsAdapter extends RecyclerView.Adapter<TruckProductsAdapter.ViewHolder> {

    private Context context;
    private List<ProductItem> productItems;
    private List<Cart> cartList;

    private FavouriteUnfavouriteOpration favouriteUnfavouriteOpration;
    private UpdateProductToDbOpration updateProductToDbOpration;

    private SearchView searchView;
    private Database base;
    private String itemId;

    public TruckProductsAdapter(Context context, List<ProductItem> productItems, FavouriteUnfavouriteOpration favouriteUnfavouriteOpration, UpdateProductToDbOpration updateProductToDbOpration) {
        this.context = context;
        this.favouriteUnfavouriteOpration = favouriteUnfavouriteOpration;
        this.updateProductToDbOpration = updateProductToDbOpration;
        this.productItems = productItems;

        cartList = new ArrayList<>();
        getSqliteData();
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    @NonNull
    @Override
    public TruckProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orders_layout, parent, false);
        return new TruckProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TruckProductsAdapter.ViewHolder holder, final int position) {
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

        holder.imageMakeFavourite.setImageResource((
                productItem.getIsFavourite() != null && productItem.getIsFavourite() == 1 ?
                        R.drawable.ic_favorite :
                        R.drawable.ic_favorite_border));

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

        holder.imageMakeFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productItem.setIsFavourite((productItem.getIsFavourite() == 1 ? 0 : 1));

                notifyItemChanged(position);

                if (productItem.getIsFavourite() == 1) {

                    if (favouriteUnfavouriteOpration != null)
                        favouriteUnfavouriteOpration.makeFavourite(productItem);

                } else {
                    if (favouriteUnfavouriteOpration != null)
                        favouriteUnfavouriteOpration.makeUnfavourite(productItem);
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

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textProductName, textProductCode, textAddToCart;
        ImageView imageMakeFavourite;

        public ViewHolder(View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductCode = itemView.findViewById(R.id.text_product_code);
            textAddToCart = itemView.findViewById(R.id.text_add_to_cart);
            imageMakeFavourite = itemView.findViewById(R.id.image_make_favourite);
        }
    }

    /**
     * @param productItem
     */
    private void showChangeLangDialog(final ProductItem productItem, final TruckProductsAdapter.ViewHolder holder) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
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
                        AppUtils.showToast(context, "please select valid quantity!");
                    } else {
                        b.dismiss();
                        holder.textAddToCart.setText("Update Cart");
                        productItem.setQty(Integer.valueOf(quantity));

                        if (updateProductToDbOpration != null)
                            updateProductToDbOpration.updateProduct(productItem);
                        AppUtils.showToast(context, "Added to cart successfully!");

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
                    }
                    else {
                        b.dismiss();
                        base.open();
                        base.updateCartQuantity(itemId, quantity);
                        base.close();
                        AppUtils.showToast(context, "Quantity Updated");
                    }

//                    hideSoftKeyboard();
                } else {
                    AppUtils.showToast(context, "please enter quantity");
//                    hideSoftKeyboard();
                }
            }
        });
    }

}
