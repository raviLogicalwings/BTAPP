package com.logicalwings.btapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.model.SendContactUs;
import com.logicalwings.btapp.model.SendContactUsResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendContactMailActivity extends BaseActivity {

    EditText editMessage;
    Button buttonSendEmail;
    private ApiInterface apiInterface;
    private String token, userEmail, userName, userPhone;
    SendContactUs sendContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_contact_mail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("Send Mail");

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
        sendContactUs = new SendContactUs();
        token = LibFile.getInstance(SendContactMailActivity.this).getString(AppConstants.TOKEN);
        userEmail = LibFile.getInstance(SendContactMailActivity.this).getString(AppConstants.EMAIl);
        userName = LibFile.getInstance(SendContactMailActivity.this).getString(AppConstants.SAP_CUSTOMER_NAME);
        userPhone = LibFile.getInstance(SendContactMailActivity.this).getString(AppConstants.MOBILE_NO);
        Log.e("contactMailActivity", token);
    }

    @Override
    protected void initUi() {
        super.initUi();
        editMessage = findViewById(R.id.edit_message);
        buttonSendEmail = findViewById(R.id.button_send_email);

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();

                showProgressDialog("Please wait!");
                if (!message.equals("")) {
                    sendContactUs.setMessage(message);
                    sendContactUs.setEmailId(userEmail);
                    sendContactUs.setMobileNo(userPhone);
                    sendContactUs.setCustomerName(userName);

                    if (sendContactUs != null) {
                        sendMail(sendContactUs);
                    }
                } else {
                    cancelProgressDialog();
                    AppUtils.showToast(SendContactMailActivity.this, "please type your message!");
                }

            }
        });
    }

    private void sendMail(SendContactUs sendContactUs) {
        Call<SendContactUsResponse> call = apiInterface.apiSendContactDetails(sendContactUs, token);
        call.enqueue(new Callback<SendContactUsResponse>() {
            @Override
            public void onResponse(retrofit2.Call<SendContactUsResponse> call, Response<SendContactUsResponse> response) {
                Log.e("sendMailResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    cancelProgressDialog();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Toast.makeText(SendContactMailActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SendContactMailActivity.this, HomeActivity.class);
                        ActivityCompat.finishAffinity(SendContactMailActivity.this);
                        startActivity(i);
                    }
                } else {
                    cancelProgressDialog();
                    Toast.makeText(SendContactMailActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SendContactUsResponse> call, Throwable t) {
                cancelProgressDialog();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(SendContactMailActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
