package com.logicalwings.btapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.model.ListContactusResponse;
import com.logicalwings.btapp.model.SendContactUs;
import com.logicalwings.btapp.model.SendContactUsResponse;
import com.logicalwings.btapp.model.User;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendContactUsMail extends Fragment {

    EditText editMessage;
    Button buttonSendEmail;
    protected LibFile libFile;
    private ApiInterface apiInterface;
    private String tokenStr, userEmail;
    SendContactUs sendContactUs;

    public SendContactUsMail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_contact_us_mail, container, false);
        editMessage = view.findViewById(R.id.edit_message);
        buttonSendEmail = view.findViewById(R.id.button_send_email);

        initData();
        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();

                if(!message.equals(""))
                {
                    sendContactUs.setMessage(message);
                    sendContactUs.setEmailId(userEmail);
                }
                else
                {
                    AppUtils.showToast(getActivity(), userEmail);
                    AppUtils.showToast(getActivity(), "please type your message!");
                }
                if(sendContactUs != null)
                {
                    sendMail(sendContactUs);
                }
            }
        });
        return view;
    }

    private void sendMail(SendContactUs sendContactUs)
    {
        Call<SendContactUsResponse> call = apiInterface.apiSendContactDetails(sendContactUs, tokenStr);
        call.enqueue(new Callback<SendContactUsResponse>()
        {
            @Override
            public void onResponse(retrofit2.Call<SendContactUsResponse> call, Response<SendContactUsResponse> response)
            {
//                Toast.makeText(getContext(), new Gson().toJson(response), Toast.LENGTH_LONG).show();
                Log.e("sendMailResponse", new Gson().toJson(response));
                if(response.isSuccessful())
                {
                    if(response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL)
                    {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SendContactUsResponse> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initData()
    {
        apiInterface = ApiClient.getApiClient();
        libFile = LibFile.getInstance(getContext());
        sendContactUs = new SendContactUs();
        tokenStr = libFile.getString(AppConstants.TOKEN);
        userEmail = libFile.getString(AppConstants.MOBILE_NO);
        AppUtils.hasInternet(getActivity());
    }
}
