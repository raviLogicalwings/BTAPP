package com.logicalwings.btapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.CartAdapter;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.model.Cart;
import com.logicalwings.btapp.model.CartPost;
import com.logicalwings.btapp.model.CartResponse;
import com.logicalwings.btapp.model.ListCartCreateOrderPost;
import com.logicalwings.btapp.model.ListCartPost;
import com.logicalwings.btapp.model.ListCartResponse;
import com.logicalwings.btapp.model.OrderResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {

    private RecyclerView cartRecycler;
    private CartAdapter cartAdapter;
    private ApiInterface apiInterface;
    private String token;
    protected LibFile libFile;
    private ListCartPost listCartPost = new ListCartPost();
    private List<CartPost> cartPosts = new ArrayList<>();
    private List<CartResponse> cartResponses;
    private ListCartCreateOrderPost listCartCreateOrderPost = new ListCartCreateOrderPost();
    private TextView textProductTotalAmount, textProductItemsCount, textClearCart;
    private ProgressBar loader;
    private LinearLayout linearCartNodata, linearCartProceed, linearProceed, linearCartNoInternet;
    private Button btCartRetry;
    double totalAmount = 0;
    double totalPrice = 0;
    int totalGST = 0;
    DecimalFormat convertDecimal = new DecimalFormat("0.00");
    private static final int CODE_CART = 120;
    private static final int CODE_CREATE_ORDER = 121;
    String sapOrderId = "";
    boolean serverStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initData();

        initUi();

        linearProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listCartCreateOrderPost.setItems(cartResponses);
                Gson gson = new Gson();
                String json = gson.toJson(listCartCreateOrderPost);
                Log.e("createOrderJson", json);
                showProgressDialog("Please wait!");
                if (AppUtils.hasInternet(CartActivity.this)) {
                    createOrder(listCartCreateOrderPost);
                } else {
                    AppUtils.showToast(CartActivity.this, "something went wrong! but your product might processed, pls check your orders");
                }
            }
        });

        textClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Database base = new Database(CartActivity.this);
                base.open();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        CartActivity.this).create();
                alertDialog.setTitle("Delete Cart Data!");
                alertDialog.setMessage("Your cart will get clear! Are you sure want to Delete?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mobile = libFile.getString(AppConstants.MOBILE_NO);
                        String email = libFile.getString(AppConstants.EMAIl);
                        if (!mobile.equals("")) {
                            base.deleteCart(mobile);
                            base.close();
                        } else {
                            base.deleteCart(email);
                            base.close();
                        }
                        Intent intentLogin = new Intent(CartActivity.this, CartActivity.class);
                        ActivityCompat.finishAffinity(CartActivity.this);
                        startActivity(intentLogin);
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        btCartRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                linearCartNoInternet.setVisibility(View.GONE);
                btCartRetry.setVisibility(View.GONE);
                getCartData();
            }
        });
    }

    @Override
    protected void initUi() {
        super.initUi();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Cart");
        cartRecycler = findViewById(R.id.cart_recycler);
        textProductItemsCount = findViewById(R.id.text_product_items_count);
        textProductTotalAmount = findViewById(R.id.text_product_total_amount);
        textClearCart = findViewById(R.id.text_clear_cart);
        loader = findViewById(R.id.loader);
        linearCartNodata = findViewById(R.id.linear_cart_nodata);
        linearCartProceed = findViewById(R.id.linear_cart_proceed);
        linearProceed = findViewById(R.id.linear_proceed);
        linearCartNoInternet = findViewById(R.id.linear_cart_no_internet);
        btCartRetry = findViewById(R.id.button_cart_retry);
        linearCartProceed.setVisibility(View.GONE);

        getCartData();
    }

    @Override
    protected void initData() {
        super.initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }

        apiInterface = ApiClient.getApiClient();
        token = LibFile.getInstance(CartActivity.this).getString(AppConstants.TOKEN);


        libFile = LibFile.getInstance(CartActivity.this);
    }

    private void updateUi() {
        if (cartResponses != null && cartResponses.size() > 0) {
            cartRecycler.setVisibility(View.VISIBLE);
        } else {
            cartRecycler.setVisibility(View.GONE);

        }

//        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CartActivity.this, HomeActivity.class);
        ActivityCompat.finishAffinity(CartActivity.this);
        startActivity(i);
    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);

        if (code == CODE_CART) {
            getCartData();
        } else if (code == CODE_CREATE_ORDER) {
            createOrder(listCartCreateOrderPost);
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

    private void getCartData() {
        Database base = new Database(CartActivity.this);
        base.open();
        List<Cart> cartList = new ArrayList<>();
        cartList = base.getCartItem();
        base.close();
        cartPosts.clear();
        for (int i = 0; i < cartList.size(); i++) {
            CartPost cartPost = new CartPost(cartList.get(i).getSegmentType(), cartList.get(i).getPattern(), cartList.get(i).getSize(), Long.parseLong(cartList.get(i).getQuantity()), cartList.get(i).getItemName(), Integer.valueOf(cartList.get(i).getFkItemGroupId()), Integer.valueOf(cartList.get(i).getFkFlapItemId()), Integer.valueOf(cartList.get(i).getItemId()), Integer.valueOf(cartList.get(i).getFkTubeItemId()), cartList.get(i).getSapItemCode());
            cartPosts.add(cartPost);
        }

        listCartPost.setItems(cartPosts);

        Gson gson = new Gson();
        String json = gson.toJson(listCartPost);
        Log.e("token", token);
        Log.e("jsonData", json);
        Call<ListCartResponse> call = apiInterface.apiGetCartDetails(token, listCartPost);
        call.enqueue(new Callback<ListCartResponse>() {
            @Override
            public void onResponse(Call<ListCartResponse> call, Response<ListCartResponse> response) {
                Log.e("cartResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        cartResponses = response.body().getData();
                        textProductItemsCount.setText("Items: " + cartResponses.size());

                        for (int i = 0; i < cartResponses.size(); i++) {
                            totalAmount += cartResponses.get(i).getTotalAmount();
                            totalPrice += cartResponses.get(i).getTotalPrice();
                            totalGST += cartResponses.get(i).getGSTAmount();
                            Log.e("totalPrice", String.valueOf(cartResponses.get(i).getGSTAmount()));
                        }
                        textProductTotalAmount.setText(String.valueOf(convertDecimal.format(totalAmount)));
                        setAdapter();
                        Log.e("serverCartReceived", response.body().getMessage());
                    } else if (response.body().getStatuscode() == AppConstants.RESULT_FAIL) {
                        linearCartProceed.setVisibility(View.GONE);
                        linearCartNoInternet.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_CART);
                } else {
                    stopAnim();
                    linearCartNodata.setVisibility(View.VISIBLE);
                    linearCartProceed.setVisibility(View.GONE);
                    textClearCart.setVisibility(View.GONE);
                    linearCartNoInternet.setVisibility(View.GONE);
                    btCartRetry.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "Your cart is Empty!", Toast.LENGTH_LONG).show();
                }

                updateUi();
            }

            @Override
            public void onFailure(Call<ListCartResponse> call, Throwable t) {
                stopAnim();
                linearCartProceed.setVisibility(View.GONE);
                textClearCart.setVisibility(View.GONE);
                linearCartNoInternet.setVisibility(View.VISIBLE);
                btCartRetry.setVisibility(View.VISIBLE);
                Toast.makeText(CartActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();

//                Log.e("log", t.getCause().getMessage());
//                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
//                    linearCartProceed.setVisibility(View.GONE);
//                    textClearCart.setVisibility(View.GONE);
//                    linearCartNoInternet.setVisibility(View.VISIBLE);
//                    btCartRetry.setVisibility(View.VISIBLE);
//                    Toast.makeText(CartActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
//                } else if(t.getMessage().equals(AppConstants.TIMEOUT_ERROR)) {
//                    linearCartProceed.setVisibility(View.GONE);
//                    textClearCart.setVisibility(View.GONE);
//                    linearCartNoInternet.setVisibility(View.VISIBLE);
//                    btCartRetry.setVisibility(View.VISIBLE);
//                    Toast.makeText(CartActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
//                }
//                else {
//
//                }
//                Log.e("failedMsg", t.getMessage());

            }
        });
    }

    private void createOrder(ListCartCreateOrderPost listCartCreateOrderPost) {
        Call<OrderResponse> call = apiInterface.apiCreateOrder(token, listCartCreateOrderPost);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                Log.e("createOrderResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    cancelProgressDialog();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        serverStatus = true;
                        sapOrderId = response.body().getData();
                        if (sapOrderId != null && !sapOrderId.isEmpty()) {
                            Intent i = new Intent(CartActivity.this, OrderReviewActivity.class);
                            i.putExtra("SAPORDERID", sapOrderId);
                            i.putExtra("TOTALAMOUNT", String.valueOf(totalAmount));
                            i.putExtra("TOTALPRICE", String.valueOf(totalPrice));
                            i.putExtra("GSTAMOUNT", String.valueOf(totalGST));
                            ActivityCompat.finishAffinity(CartActivity.this);
                            startActivity(i);
                            Toast.makeText(CartActivity.this, response.body().getData(), Toast.LENGTH_LONG).show();
                        } else {
                            AppUtils.showToast(CartActivity.this, "Please check your internet connection!");
                        }
                    } else if (response.body().getStatuscode() == AppConstants.RESULT_FAIL) {
                        AppUtils.showToast(CartActivity.this, response.body().getMessage());
                    }
                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_CREATE_ORDER);
                } else {
                    //                    stopAnim();
                    cancelProgressDialog();
                    Toast.makeText(CartActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
//                stopAnim();
                cancelProgressDialog();
                if (serverStatus) {
                    Intent i = new Intent(CartActivity.this, OrderReviewActivity.class);
                    i.putExtra("SAPORDERID", sapOrderId);
                    i.putExtra("TOTALAMOUNT", String.valueOf(totalAmount));
                    i.putExtra("TOTALPRICE", String.valueOf(totalPrice));
                    i.putExtra("GSTAMOUNT", String.valueOf(totalGST));
                    ActivityCompat.finishAffinity(CartActivity.this);
                    startActivity(i);
                    Toast.makeText(CartActivity.this, "Order generated successfully", Toast.LENGTH_LONG).show();
                } else {
                    linearCartProceed.setVisibility(View.GONE);
                    cartRecycler.setVisibility(View.GONE);
                    textClearCart.setVisibility(View.GONE);
                    btCartRetry.setVisibility(View.VISIBLE);
                    linearCartNoInternet.setVisibility(View.VISIBLE);
                    Toast.makeText(CartActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void setAdapter() {
        cartAdapter = new CartAdapter(CartActivity.this);
        cartRecycler.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        cartAdapter.setList(cartResponses);
        cartRecycler.setAdapter(cartAdapter);
    }

    void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
        linearCartProceed.setVisibility(View.VISIBLE);
        textClearCart.setVisibility(View.VISIBLE);
    }
}
