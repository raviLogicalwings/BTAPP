package com.logicalwings.btapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.MyOrderDetailsAdapter;
import com.logicalwings.btapp.model.GetOrderDetailsResponse;
import com.logicalwings.btapp.model.ItemsItem;
import com.logicalwings.btapp.model.ListGetOrderDetailResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.LibFile;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderDetailsActivity extends BaseActivity {

    private ApiInterface apiInterface;
    private String token;
    ProgressBar loader;
    List<GetOrderDetailsResponse> getOrderDetailsResponses;
    MyOrderDetailsAdapter myOrderDetailsAdapter;
    RecyclerView orderDetailsRecycler;
    List<ItemsItem> itemsItems;
    private static final int CODE_GET_ORDER = 123;
    private String sapOrderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        loader = findViewById(R.id.loader);
        orderDetailsRecycler = findViewById(R.id.order_details_recycler);
        setSupportActionBar(toolbar);

        initData();

        getOrderDetails();
    }

    private void getOrderDetails() {
        Call<ListGetOrderDetailResponse> call = apiInterface.apiGetOrderDetails(token, sapOrderNo);
        call.enqueue(new Callback<ListGetOrderDetailResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListGetOrderDetailResponse> call, Response<ListGetOrderDetailResponse> response) {
//                Toast.makeText(getContext(), new Gson().toJson(response), Toast.LENGTH_LONG).show();
//                Log.e("fullOrderDetailsRespons", response.body().getData().toString());
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        itemsItems = response.body().getData().getItems();
                        setAdapter();
                        Log.e("orderDetailsResponse", response.body().getData().toString());
                    }
                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_GET_ORDER);
                } else {
                    stopAnim();
                    Toast.makeText(MyOrderDetailsActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListGetOrderDetailResponse> call, Throwable t) {
                stopAnim();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(MyOrderDetailsActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);

        if (code == CODE_GET_ORDER) {
            getOrderDetails();
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
    protected void initData() {
        super.initData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Order Details");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }
        token = LibFile.getInstance(MyOrderDetailsActivity.this).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();

        Bundle bundle = getIntent().getExtras();
        sapOrderNo = bundle.getString("sapOrderNo");

//        Log.e("sapIdd", sapOrderNo);
    }

    private void setAdapter() {
        myOrderDetailsAdapter = new MyOrderDetailsAdapter(MyOrderDetailsActivity.this);
        orderDetailsRecycler.setLayoutManager(new LinearLayoutManager(MyOrderDetailsActivity.this));
        myOrderDetailsAdapter.setList(itemsItems);
        orderDetailsRecycler.setAdapter(myOrderDetailsAdapter);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
    }
}
