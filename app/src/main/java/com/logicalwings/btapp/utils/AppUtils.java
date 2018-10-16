package com.logicalwings.btapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.model.Cart;

import java.util.List;

public class AppUtils {

    public static boolean hasInternet(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (!(networkInfo != null && networkInfo.isConnectedOrConnecting())) {
            showToast(context, context.getString(R.string.no_internet_connection));
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static int getCartCounter(Context context) {
        Database base = new Database(context);
        base.open();
        List<Cart> cartList = base.getCartItem();
        base.close();
        return cartList.size();
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void startAnim(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void stopAnim(View view) {
        view.setVisibility(View.GONE);
    }
}
