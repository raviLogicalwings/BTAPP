package com.logicalwings.btapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

public class PasswordActivity extends BaseActivity {
    Button buttonLogin;
    EditText editPassword;
    User user;
    String password;
    private ApiInterface apiInterface;
    protected LibFile libFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        initData();

        initUi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PasswordActivity.this, LoginActivity.class);
        ActivityCompat.finishAffinity(PasswordActivity.this);
        startActivity(i);
    }

    @Override
    protected void initData() {
        super.initData();
//        stopAnim();
        libFile = LibFile.getInstance(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }
        apiInterface = ApiClient.getApiClient();
        AppUtils.hasInternet(PasswordActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    protected void initUi() {
        super.initUi();
        buttonLogin = findViewById(R.id.button_login);
        editPassword = findViewById(R.id.edit_password);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkValidation();
                    AppUtils.hideKeyboard(PasswordActivity.this, editPassword);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void checkValidation() {
        user = new User();
        password = editPassword.getText().toString().trim();
        String mobile = libFile.getString(AppConstants.MOBILE_NO);
        String email = libFile.getString(AppConstants.EMAIl);

        if (!password.isEmpty()) {
            user.setPassword(password);
            if (!mobile.equals("")) {
                user.setMobileNo(mobile);
            } else {
                user.setEmail(email);
            }

            if (user != null) {
                if (AppUtils.hasInternet(PasswordActivity.this)) {
//                    startAnim();
                    showProgressDialog("Please wait!");
                    userLoginPasswordApi(user);
                } else {
                    AppUtils.showToast(PasswordActivity.this, getResources().getString(R.string.no_internet_connection));
                }
            } else {
                Toast.makeText(PasswordActivity.this, "something went wrong! pls try again!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(PasswordActivity.this, "please enter password!", Toast.LENGTH_LONG).show();
        }
    }

    private void userLoginPasswordApi(final User user) {
        Call<LoginResponce> passwordCall = apiInterface.userLoginPassword(user);

        passwordCall.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                Log.d("passwordResponse", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
//                    stopAnim();
                    cancelProgressDialog();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        if (response.body().getData() != null) {
                            User responceUser = response.body().getData();
                            libFile.setString(AppConstants.TOKEN, responceUser.getToken());
                            libFile.setInt(AppConstants.CUSTOMER_ID, responceUser.getCustomerId());
                            libFile.setBoolean(AppConstants.IS_LOGIN, true);
                            libFile.setString(AppConstants.PASSWORD, user.getPassword());
                            Log.e("token", responceUser.getToken());
                            AppUtils.showToast(PasswordActivity.this, "Login successfully!");
                        }
                        Intent i = new Intent(PasswordActivity.this, HomeActivity.class);
                        ActivityCompat.finishAffinity(PasswordActivity.this);
                        startActivity(i);
                    } else {
                        Toast.makeText(PasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cancelProgressDialog();
                    Toast.makeText(PasswordActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
//                stopAnim();
                cancelProgressDialog();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(PasswordActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
