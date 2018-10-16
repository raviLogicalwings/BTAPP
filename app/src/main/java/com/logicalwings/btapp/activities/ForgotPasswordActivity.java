package com.logicalwings.btapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.model.LoginResponce;
import com.logicalwings.btapp.model.User;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    EditText editFrogotEmailMobile;
    Button btForgotNext;
    private ApiInterface apiInterface;
    User user;
    String emailStr;
    LibFile libFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
        AppUtils.hasInternet(ForgotPasswordActivity.this);

        apiInterface = ApiClient.getApiClient();
        libFile = LibFile.getInstance(getApplication());
    }

    @Override
    protected void initUi() {
        super.initUi();
        editFrogotEmailMobile = findViewById(R.id.edit_forgot_email_mobile);
        btForgotNext = findViewById(R.id.button_forgot_next);

        btForgotNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        editFrogotEmailMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkValidation();
                    AppUtils.hideKeyboard(ForgotPasswordActivity.this, editFrogotEmailMobile);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void checkValidation() {
        user = new User();
        emailStr = editFrogotEmailMobile.getText().toString().trim();

        if (!emailStr.equals("")) {
            if (emailStr.contains("@")) {
                user.setEmail(emailStr);
            } else {
                if (emailStr.matches("^[0-9]*$") && emailStr.length() > 2) {
                    user.setMobileNo(emailStr);
                    libFile.setString(AppConstants.MOBILE_NO, user.getMobileNo());
                } else {
                    user.setEmail(emailStr);
                    libFile.setString(AppConstants.EMAIl, user.getEmail());
                }
            }
            if (user != null) {
                if (AppUtils.hasInternet(ForgotPasswordActivity.this)) {
                    showProgressDialog("Please wait!");
                    doVerify(user);
                } else {
                    AppUtils.showToast(ForgotPasswordActivity.this, getResources().getString(R.string.no_internet_connection));
                }
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "something went wrong! pls try again!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter Email or Phone number!", Toast.LENGTH_LONG).show();
        }
    }

    private void doVerify(User user) {
        Call<LoginResponce> call2 = apiInterface.userSendVerify(user);
        call2.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call2, Response<LoginResponce> response) {
                Log.e("ForgotPasswordResponse", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    cancelProgressDialog();
                    User loginUser = response.body().getData();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Log.e("forgotResponse", response.body().getData().toString());
                        libFile.setString(AppConstants.SAP_CUSTOMER_NAME, loginUser.getSapCustomerName().toString());
                        libFile.setString(AppConstants.EMAIl, loginUser.getEmail());
                        libFile.setString(AppConstants.MOBILE_NO, loginUser.getMobileNo());
                        Intent i = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
                        startActivity(i);
                    }
                    Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    cancelProgressDialog();
                    Log.e("response2", new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call2, Throwable t) {
                cancelProgressDialog();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
