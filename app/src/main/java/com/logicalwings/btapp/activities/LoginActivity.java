package com.logicalwings.btapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static android.Manifest.permission_group.CAMERA;
import static android.Manifest.permission_group.SMS;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ApiInterface apiInterface;

    Button buttonNext;
    EditText email;
    private String emailStr;
    private User user;
    LinearLayout linearEdit, linearButton;
    ImageView imageLogo;
    TextView textBharatBig, textBharatSmall, forgotPass;
    protected LibFile libFile;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();

        initUi();


    }

    private void checkValidation() {
        user = new User();
        emailStr = email.getText().toString().trim();

        if (!emailStr.equals("")) {
            if (emailStr.contains("@")) {
                user.setEmail(emailStr);
            } else {
                if (emailStr.matches("^[0-9]*$") && emailStr.length() > 2) {
                    user.setMobileNo(emailStr);
                } else {
                    user.setEmail(emailStr);
                }
            }
            if (user != null) {
                if (AppUtils.hasInternet(LoginActivity.this)) {
                    userLoginApi(user);
                } else {
                    AppUtils.showToast(LoginActivity.this, getResources().getString(R.string.no_internet_connection));
                }
            } else {
                AppUtils.showToast(LoginActivity.this, "something went wrong! pls try again!");
            }
        } else {
//            stopAnim();
            cancelProgressDialog();
            AppUtils.showToast(LoginActivity.this, "Please enter Email or Phone number!");
        }
    }

    @Override
    protected void initData() {
        super.initData();

        libFile = LibFile.getInstance(getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }
        apiInterface = ApiClient.getApiClient();
        AppUtils.hasInternet(LoginActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    protected void initUi() {
        super.initUi();
        buttonNext = findViewById(R.id.button_next);
        email = findViewById(R.id.edit_email);
        linearEdit = findViewById(R.id.linear_edit);
        linearButton = findViewById(R.id.linear_button);
        imageLogo = findViewById(R.id.image_logo);
        textBharatBig = findViewById(R.id.text_bharat_big);
        textBharatSmall = findViewById(R.id.text_bharat_small);
        forgotPass = findViewById(R.id.text_forgot_pass);

        buttonNext.setOnClickListener(this);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    startAnim();
                    showProgressDialog("Please wait!");
                    checkValidation();
                    AppUtils.hideKeyboard(LoginActivity.this, email);
                    handled = true;
                }
                return handled;
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    public void userLoginApi(final User user) {
        Log.e("checkValidation", user.getEmail() + "/" + user.getMobileNo());
        Call<LoginResponce> call = apiInterface.userLogin(user);

        call.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                Log.e("mainResponse", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.e("userResponse", response.body().getMessage());
                    User loginUser = response.body().getData();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
//                        stopAnim();
                        cancelProgressDialog();
                        libFile.setString(AppConstants.SAP_CUSTOMER_NAME, loginUser.getSapCustomerName().toString());
                        libFile.setString(AppConstants.EMAIl, loginUser.getEmail());
                        libFile.setString(AppConstants.MOBILE_NO, loginUser.getMobileNo());
                        Intent i = new Intent(LoginActivity.this, PasswordActivity.class);
                        startActivity(i);
                    } else if (response.body().getMessage().equals(AppConstants.USER_NOT_EXIST)) {
//                        stopAnim();
                        cancelProgressDialog();
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getMessage().equals(AppConstants.USER_EXIST_PASSWORD_NOT_SET)) {
//                        stopAnim();
                        cancelProgressDialog();
                        libFile.setString(AppConstants.SAP_CUSTOMER_NAME, loginUser.getSapCustomerName().toString());
                        libFile.setString(AppConstants.EMAIl, loginUser.getEmail());
                        libFile.setString(AppConstants.MOBILE_NO, loginUser.getMobileNo());
                        userSendVerification(user);
                    } else {
//                        stopAnim();
                        cancelProgressDialog();
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
//                stopAnim();
                cancelProgressDialog();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void userSendVerification(User user) {
        Log.e("checkUser", user.getEmail() + "/" + user.getMobileNo() + "/" + user.toString());
        Call<LoginResponce> call2 = apiInterface.userSendVerify(user);
        call2.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call2, Response<LoginResponce> response) {
                Log.e("sendVerifiResponse", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
//                    stopAnim();
                    cancelProgressDialog();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(i);
                    }
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
//                    stopAnim();
                    cancelProgressDialog();
                    Log.e("response2", new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call2, Throwable t) {
                cancelProgressDialog();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next:
                showProgressDialog("Please wait!");
                checkValidation();
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
