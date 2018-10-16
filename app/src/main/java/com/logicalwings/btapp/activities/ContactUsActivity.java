package com.logicalwings.btapp.activities;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.ContactUsAdapter;
import com.logicalwings.btapp.callbacks.UnauthorizedUser;
import com.logicalwings.btapp.model.ContactUs;
import com.logicalwings.btapp.model.ListContactusResponse;
import com.logicalwings.btapp.model.User;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity {

    RecyclerView contactRecycler;
    Button buttonSendMail, buttonContactNoInternet;
    LinearLayout linearContactNoInternet;
    ContactUsAdapter contactUsAdapter;
    List<ContactUs> contactUsList;
    ProgressBar loader;
    private ApiInterface apiInterface;
    private String token;
    User user;
    public static final int CODE_CONTACT = 440;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Contact Us");
        }

        initData();

        initUi();

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
        token = LibFile.getInstance(ContactUsActivity.this).getString(AppConstants.TOKEN);
        Log.e("contactActivity", token);
        user = new User();
    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);

        if (code == CODE_CONTACT) {
            AppUtils.startAnim(loader);
            AppUtils.showToast(ContactUsActivity.this, "onAuthorized");
            initData();
            getContactUsData();
        } else {
            AppUtils.stopAnim(loader);
        }
    }

    @Override
    protected void onTokenRefreshFail(int code) {
        super.onTokenRefreshFail(code);
        AppUtils.showToast(ContactUsActivity.this, "onFailed");
    }

    @Override
    public void authenticateUser(int code) {
        super.authenticateUser(code);
    }

    @Override
    protected void initUi() {
        super.initUi();

        contactRecycler = findViewById(R.id.contact_us_recycler);
        buttonSendMail = findViewById(R.id.button_send_mail);
        loader = findViewById(R.id.loader);
        buttonContactNoInternet = findViewById(R.id.button_contact_retry);
        linearContactNoInternet = findViewById(R.id.linear_contact_no_internet);

        getContactUsData();

        buttonSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactUsActivity.this, SendContactMailActivity.class);
                startActivity(i);
            }
        });

        buttonContactNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startAnim(loader);
                linearContactNoInternet.setVisibility(View.GONE);
                buttonContactNoInternet.setVisibility(View.GONE);
                getContactUsData();
            }
        });

    }

    private void setAdapter(List<ContactUs> contactUsList) {
        contactUsAdapter = new ContactUsAdapter(ContactUsActivity.this, contactUsList);
        contactRecycler.setLayoutManager(new LinearLayoutManager(ContactUsActivity.this));
        contactRecycler.setAdapter(contactUsAdapter);
    }

    private void getContactUsData() {
        Log.e("token", token);
//        AppUtils.showToast(ContactUsActivity.this, "getContacts");
        Call<ListContactusResponse> call = apiInterface.apiGetContactUsDetails(token);
        call.enqueue(new Callback<ListContactusResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListContactusResponse> call, Response<ListContactusResponse> response) {
//                Toast.makeText(getContext(), new Gson().toJson(response), Toast.LENGTH_LONG).show();
                Log.e("contactResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    buttonSendMail.setVisibility(View.VISIBLE);
                    AppUtils.stopAnim(loader);
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
//                        Toast.makeText(getActivity(), response.body().getData().toString(), Toast.LENGTH_LONG).show();
                        contactUsList = response.body().getData();
                        setAdapter(contactUsList);
                    }
                } else if (response.code() == AppConstants.ERROR_CODE) {
                    AppUtils.showToast(ContactUsActivity.this, "working");
                    authenticateUser(CODE_CONTACT);
                } else {
                    Log.e("errorCode", String.valueOf(response.code()));
                    AppUtils.stopAnim(loader);
                    Toast.makeText(ContactUsActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListContactusResponse> call, Throwable t) {
                AppUtils.stopAnim(loader);
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    buttonSendMail.setVisibility(View.GONE);
                    linearContactNoInternet.setVisibility(View.VISIBLE);
                    buttonContactNoInternet.setVisibility(View.VISIBLE);
                    Toast.makeText(ContactUsActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
