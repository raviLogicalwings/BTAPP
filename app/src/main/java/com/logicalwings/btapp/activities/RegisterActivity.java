package com.logicalwings.btapp.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.model.LoginResponce;
import com.logicalwings.btapp.model.MainResponce;
import com.logicalwings.btapp.model.User;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.AutoReadSMSReceiver;
import com.logicalwings.btapp.utils.LibFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editVerificationCode, editPass, editConfirmPass;
    private TextView textCodeResend, textTimer, textOtpNotReceived;
    private User user;
    private String verification, pass, confirmPass;
    private ApiInterface apiInterface;
    private LinearLayout linearOtpNotReceived, linearWaitFor;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        AppUtils.hasInternet(RegisterActivity.this);

        user = new User();
        String mobile = LibFile.getInstance(RegisterActivity.this).getString(AppConstants.MOBILE_NO);
        String email = LibFile.getInstance(RegisterActivity.this).getString(AppConstants.EMAIl);

        if (!mobile.isEmpty()) {
            user.setMobileNo(mobile);
        } else {
            user.setEmail(email);
        }

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
    }

    @Override
    protected void initUi() {
        super.initUi();
        buttonRegister = findViewById(R.id.button_register);
        editVerificationCode = findViewById(R.id.edit_verification_code);
        editPass = findViewById(R.id.edit_pass);
        editConfirmPass = findViewById(R.id.edit_confirm_pass);
        textCodeResend = findViewById(R.id.text_code_resend);
        textTimer = findViewById(R.id.text_timer);
        textOtpNotReceived = findViewById(R.id.text_otp_not_received);
        linearOtpNotReceived = findViewById(R.id.linear_otp_not_received);
        linearWaitFor = findViewById(R.id.linear_wait_for);

        buttonRegister.setOnClickListener(RegisterActivity.this);

        textCodeResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearOtpNotReceived.setVisibility(View.GONE);
                linearWaitFor.setVisibility(View.VISIBLE);
                showProgressDialog("Please wait!");
                userResendVerification(user);
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");
                    String otp = message.replaceAll("[^0-9]+", "");
                    String otp2 = otp.substring(0, otp.length() - 1);
                    editVerificationCode.setText(otp2);
                    editVerificationCode.setSelection(editVerificationCode.getText().length());
                    textCodeResend.setEnabled(true);
                    textCodeResend.setTextColor(getResources().getColor(R.color.textColorBlack));
                    textTimer.setVisibility(View.GONE);
                }
            }
        }
    };

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void checkValidation() {
        user = new User();

        verification = editVerificationCode.getText().toString().trim();
        pass = editPass.getText().toString().trim();
        confirmPass = editConfirmPass.getText().toString().trim();

        if (!verification.equals("") && !pass.equals("") && !confirmPass.equals("")) {
            if (!pass.equals(confirmPass)) {
                Toast.makeText(RegisterActivity.this, "password not match", Toast.LENGTH_LONG).show();
            } else {
                user.setVfcCode(verification);
                user.setPassword(pass);
                user.setConfirmPassword(confirmPass);
            }

            if (user != null) {
                showProgressDialog("Please wait!");
                apiUserRegistrationCall(user);
            } else {
                Toast.makeText(RegisterActivity.this, "something went wrong! pls try again!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_LONG).show();
        }
    }

    public void apiUserRegistrationCall(User user) {
        Log.d("response", "apiUserRegistrationCall() called with: user = [" + new Gson().toJson(user) + "]");
        Call<MainResponce> call = apiInterface.apiUserRegistration(user);
        call.enqueue(new Callback<MainResponce>() {

            @Override
            public void onResponse(Call<MainResponce> call, Response<MainResponce> response) {
                Log.e("response", "onResponse() called with: call = [" + call + "], response = [" + new Gson().toJson(response) + "]");
                if (response.isSuccessful()) {
                    cancelProgressDialog();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Intent intentHome = new Intent(RegisterActivity.this, PasswordActivity.class);
                        ActivityCompat.finishAffinity(RegisterActivity.this);
                        startActivity(intentHome);
                    } else {
//                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    cancelProgressDialog();
                    Toast.makeText(RegisterActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponce> call, Throwable t) {
                Log.d("registerResponse", "onFailure() called with: call = [" + call + "], t = [" + new Gson().toJson(t) + "]");
                Log.e("registerResponseError", t.getMessage());
                cancelProgressDialog();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(RegisterActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void userResendVerification(User user) {
        Call<LoginResponce> call2 = apiInterface.userSendVerify(user);
        call2.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call2, Response<LoginResponce> response) {
                Log.e("response", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    cancelProgressDialog();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startTimer();
                    }
                } else {
                    cancelProgressDialog();
                    Log.e("response2", new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call2, Throwable t) {
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(RegisterActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        ActivityCompat.finishAffinity(RegisterActivity.this);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                checkValidation();
                break;
        }
    }

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setVisibility(View.VISIBLE);
                textTimer.setText("" + millisUntilFinished / 1000 + "secs");
            }

            public void onFinish() {
                linearOtpNotReceived.setVisibility(View.VISIBLE);
                linearWaitFor.setVisibility(View.GONE);
            }
        };
        cTimer.start();
    }

}
