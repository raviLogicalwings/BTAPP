package com.logicalwings.btapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.callbacks.UnauthorizedUser;
import com.logicalwings.btapp.model.LoginResponce;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.model.User;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    protected LibFile libFile;
    private ProgressDialog progressDialog;

    protected void initData() {
        libFile = LibFile.getInstance(this);
    }

    protected void initUi() {
        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
    }

    private void hideKeyboard() {

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void updateQtyTODb(ProductItem productItem) {
        hideKeyboard();
        String mobile = libFile.getString(AppConstants.MOBILE_NO);
        String email = libFile.getString(AppConstants.EMAIl);
        Database base = new Database(BaseActivity.this);
        base.open();
        if (!mobile.equals("")) {
            base.createCartEntry(mobile, String.valueOf(productItem.getItemId()), productItem.getItemName(), productItem.getSapItemCode(), String.valueOf(productItem.getFkItemGroupId()), productItem.getSize(), productItem.getPattern(), productItem.getSegmentType(), String.valueOf(productItem.getFkTubeItemId()), String.valueOf(productItem.getFkFlapItemId()), String.valueOf(productItem.getQty() != null ? productItem.getQty() : 0));
        } else {
            base.createCartEntry(email, String.valueOf(productItem.getItemId()), productItem.getItemName(), productItem.getSapItemCode(), String.valueOf(productItem.getFkItemGroupId()), productItem.getSize(), productItem.getPattern(), productItem.getSegmentType(), String.valueOf(productItem.getFkTubeItemId()), String.valueOf(productItem.getFkFlapItemId()), String.valueOf(productItem.getQty() != null ? productItem.getQty() : 0));
        }
        base.close();
    }

    protected void onTokenRefreshSuccess(int code) {
    }

    protected void onTokenRefreshFail(int code) {
    }

    protected void onTokenRefreshBadResponse(int code) {

    }

    public void authenticateUser(final int code) {
        final User user = new User();

        String mobile = libFile.getString(AppConstants.MOBILE_NO);
        String email = libFile.getString(AppConstants.EMAIl);
        String password = libFile.getString(AppConstants.PASSWORD);
        user.setPassword(password);
        if (!mobile.equals("")) {
            user.setMobileNo(mobile);
        } else {
            user.setEmail(email);
        }

        Log.e("userData", user.getMobileNo()+"/"+user.getPassword()+"/"+user.getEmail());
        ApiInterface apiInterface = ApiClient.getApiClient();
        Call<LoginResponce> passwordCall = apiInterface.userLoginPassword(user);

        passwordCall.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                Log.d("passwordResponse", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        if (response.body().getData() != null) {
                            User responceUser = response.body().getData();
                            libFile.setString(AppConstants.TOKEN, responceUser.getToken());
                            libFile.setInt(AppConstants.CUSTOMER_ID, responceUser.getCustomerId());
                            libFile.setBoolean(AppConstants.IS_LOGIN, true);
                            libFile.setString(AppConstants.PASSWORD, user.getPassword());
                            Log.e("token", responceUser.getToken());
                            AppUtils.showToast(BaseActivity.this, "Login successfully!");
                        }
                        onTokenRefreshSuccess(code);
                    } else {
                        onTokenRefreshBadResponse(code);
                        Log.e("loginFailed1", response.body().getMessage());
                        Toast.makeText(BaseActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BaseActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                Toast.makeText(BaseActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                onTokenRefreshFail(code);
                Log.e("loginFailed2", t.getMessage());
            }
        });
    }

    /**
     * Created to open and show progressDialog box.
     *
     * @param message
     */
    protected void showProgressDialog(String message) {

        if (progressDialog != null){
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


    }

    /**
     * Created to close opened progress dialog.
     */
    protected void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
