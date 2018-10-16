package com.logicalwings.btapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.HomeActivity;
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

public class ChangePasswordFragment extends Fragment {

    TextView textToolbar;
    ImageView toolSearch, toolCart, toolSave;
    Button buttonCancel, buttonSubmit;
    ProgressBar loader;
    private ApiInterface apiInterface;
    EditText editOldPassword, editNewPassword, editConfirmPassword;
    private String oldPasswordStr, newPasswordStr, confirmPasswordStr, tokenStr;
    private User user;
    private int customerId;
    protected LibFile libFile;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonSubmit = view.findViewById(R.id.button_submit);
        editConfirmPassword = view.findViewById(R.id.edit_confirm_pass);
        editOldPassword = view.findViewById(R.id.edit_old_pass);
        editNewPassword = view.findViewById(R.id.edit_new_pass);
        loader = view.findViewById(R.id.loader);

        textToolbar.setText(R.string.change_password);

        initData();
        loadState();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                ActivityCompat.finishAffinity(getActivity());
                startActivity(i);
            }
        });
        return view;
    }

    private void changePassword() {
        confirmPasswordStr = editConfirmPassword.getText().toString();
        newPasswordStr = editNewPassword.getText().toString();
        oldPasswordStr = editOldPassword.getText().toString();

        user.setOldPassword(oldPasswordStr);
        user.setPassword(newPasswordStr);
        user.setConfirmPassword(confirmPasswordStr);
        user.setCustomerId(customerId);

        if (!confirmPasswordStr.isEmpty() && !newPasswordStr.isEmpty() && !oldPasswordStr.isEmpty()) {
            if (!newPasswordStr.equals(confirmPasswordStr)) {
                Toast.makeText(getActivity(), "password not match!", Toast.LENGTH_LONG).show();
            } else {
                startAnim();
                apiResetPasswordCall(user, tokenStr);
            }
        } else {
            Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_LONG).show();
        }
    }

    private void loadState() {
        toolSearch.setVisibility(View.VISIBLE);
        toolCart.setVisibility(View.VISIBLE);
        toolSave.setVisibility(View.GONE);
    }

    private void initData() {
        stopAnim();
        apiInterface = ApiClient.getApiClient();
        libFile = LibFile.getInstance(getContext());
        user = new User();
        tokenStr = LibFile.getInstance(getActivity()).getString(AppConstants.TOKEN);
        customerId = LibFile.getInstance(getActivity()).getInt(AppConstants.CUSTOMER_ID);

        if (getActivity() != null) {
            AppUtils.hasInternet(getActivity());
        }

    }

    public void apiResetPasswordCall(User user, String token) {
        Call<LoginResponce> call = apiInterface.apiResetPassword(user, token);
        call.enqueue(new Callback<LoginResponce>() {
            @Override
            public void onResponse(retrofit2.Call<LoginResponce> call, Response<LoginResponce> response) {
                Log.d("resetPassResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getContext(), HomeActivity.class);
                        ActivityCompat.finishAffinity(getActivity());
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    stopAnim();
                    Toast.makeText(getActivity(), "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LoginResponce> call, Throwable t) {
                stopAnim();
                if(t.getMessage().equals(AppConstants.NO_INTERNET)){
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
    }
}
