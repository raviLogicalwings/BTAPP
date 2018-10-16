package com.logicalwings.btapp.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.base.Database;
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

public class BaseFragment extends Fragment {

    protected LibFile libFile;
    protected View fView;
    private ApiInterface apiInterface;

    protected void initData() {
        libFile = LibFile.getInstance(getActivity());
        apiInterface = ApiClient.getApiClient();
    }

    protected void initUi() {
    }

    public void updateQtyTODb(ProductItem productItem) {
        String mobile = libFile.getString(AppConstants.MOBILE_NO);
        String email = libFile.getString(AppConstants.EMAIl);
        Database base = new Database(getActivity());
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
                            AppUtils.showToast(getActivity(), "Login successfully!");
                        }
                        onTokenRefreshSuccess(code);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    onTokenRefreshBadResponse(code);
                    Toast.makeText(getActivity(), "something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponce> call, Throwable t) {
                onTokenRefreshFail(code);
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
