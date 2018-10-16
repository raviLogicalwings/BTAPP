package com.logicalwings.btapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.ReviewOrderAdapter;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.model.Cart;
import com.logicalwings.btapp.model.CartPost;
import com.logicalwings.btapp.model.CartResponse;
import com.logicalwings.btapp.model.ListCartPost;
import com.logicalwings.btapp.model.ListCartResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.LibFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderReviewActivity extends BaseActivity {

    TextView orderDate, orderId, orderTotal;
    Button btContinueShopping;
    Database base;
    protected LibFile libFile;
    List<CartPost> cartPosts = new ArrayList<>();
    ListCartPost listCartPost = new ListCartPost();
    List<CartResponse> cartResponses;
    private ApiInterface apiInterface;
    private String token;
    RecyclerView reviewOrderRecycler;
    ReviewOrderAdapter reviewOrderAdapter;
    ProgressBar loader;
    private static final int CODE_ORDER_REVIEW = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Order Summary");
        }

        orderDate = findViewById(R.id.text_order_date);
        orderId = findViewById(R.id.text_order_sapid);
        orderTotal = findViewById(R.id.text_order_total);
        btContinueShopping = findViewById(R.id.button_continue_shopping);
        loader = findViewById(R.id.loader);
        reviewOrderRecycler = findViewById(R.id.review_order_recycler);

        initData();

        setData();

        btContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderReviewActivity.this, HomeActivity.class);
                ActivityCompat.finishAffinity(OrderReviewActivity.this);
                startActivity(i);
            }
        });
    }

    private void setData() {
        base.open();
        List<Cart> cartList = base.getCartItem();
        base.close();

        Bundle bundle = getIntent().getExtras();
        String sapOrderId = bundle.getString("SAPORDERID");
        String totalAmount = bundle.getString("TOTALAMOUNT");
        String totalPrice = bundle.getString("TOTALPRICE");
        String totalGst = bundle.getString("GSTAMOUNT");

        String items = String.valueOf(cartList.size());
        String date = getCurrentDate();

        orderId.setText(sapOrderId);
        orderTotal.setText("₹" + totalAmount);
        orderDate.setText(date);

        Log.e("sapid", sapOrderId);
        Log.e("totalAmount", totalAmount);
        Log.e("totalPrice", totalPrice);
        Log.e("totalGst", totalGst);

        getCartData();

    }

    private void clearCart() {
        base.open();
        String mobile = libFile.getString(AppConstants.MOBILE_NO);
        String email = libFile.getString(AppConstants.EMAIl);
        if (!mobile.equals("")) {
            base.deleteCart(mobile);
            base.close();
        } else {
            base.deleteCart(email);
            base.close();
        }
    }

    @Override
    protected void initData() {
        super.initData();

        base = new Database(OrderReviewActivity.this);
        libFile = LibFile.getInstance(OrderReviewActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }
        token = LibFile.getInstance(OrderReviewActivity.this).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();
    }


    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        String currentDate = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);

        return currentDate;
    }

    private void getCartData() {
        Database base = new Database(OrderReviewActivity.this);
        base.open();
        List<Cart> cartList = new ArrayList<>();
        cartList = base.getCartItem();
        base.close();

        for (int i = 0; i < cartList.size(); i++) {
            CartPost cartPost = new CartPost(cartList.get(i).getSegmentType(), cartList.get(i).getPattern(), cartList.get(i).getSize(), Integer.valueOf(cartList.get(i).getQuantity()), cartList.get(i).getItemName(), Integer.valueOf(cartList.get(i).getFkItemGroupId()), Integer.valueOf(cartList.get(i).getFkFlapItemId()), Integer.valueOf(cartList.get(i).getItemId()), Integer.valueOf(cartList.get(i).getFkTubeItemId()), cartList.get(i).getSapItemCode());
            cartPosts.add(cartPost);
        }

        listCartPost.setItems(cartPosts);

        Gson gson = new Gson();
        String json = gson.toJson(listCartPost);
        Log.e("jsonData", json);
        Call<ListCartResponse> call = apiInterface.apiGetCartDetails(token, listCartPost);
        call.enqueue(new Callback<ListCartResponse>() {
            @Override
            public void onResponse(Call<ListCartResponse> call, Response<ListCartResponse> response) {
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        cartResponses = response.body().getData();
                        clearCart();
                        setAdapter();
                        Log.e("serverCartReceived", response.body().getMessage());
                    }
                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_ORDER_REVIEW);
                } else {
                    stopAnim();
                    Toast.makeText(OrderReviewActivity.this, "your cart is empty!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListCartResponse> call, Throwable t) {
                stopAnim();
                clearCart();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(OrderReviewActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAdapter() {
        reviewOrderAdapter = new ReviewOrderAdapter(OrderReviewActivity.this);
        reviewOrderRecycler.setLayoutManager(new LinearLayoutManager(OrderReviewActivity.this));
        reviewOrderAdapter.setList(cartResponses);
        reviewOrderRecycler.setAdapter(reviewOrderAdapter);
    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);

        if (code == CODE_ORDER_REVIEW) {
            getCartData();
        }

    }

    @Override
    protected void onTokenRefreshFail(int code) {
        super.onTokenRefreshFail(code);
    }

    @Override
    public void authenticateUser(int code) {
        super.authenticateUser(code);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog() {
        Intent i = new Intent(OrderReviewActivity.this, HomeActivity.class);
        ActivityCompat.finishAffinity(OrderReviewActivity.this);
        startActivity(i);
    }

    void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
    }
}
