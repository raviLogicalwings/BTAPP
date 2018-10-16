package com.logicalwings.btapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.ContactUsAdapter;
import com.logicalwings.btapp.base.Database;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactFragment extends BaseFragment {
    RecyclerView contactRecycler;
    ContactUsAdapter contactUsAdapter;
    ImageView toolSearch, toolCart, toolSave;
    private ApiInterface apiInterface;
    private String token;
    List<ContactUs> contactUsList;
    SharedPreferences sharedPreferences;
    Button buttonSendMail;
    protected LibFile libFile;
    ProgressBar loader;
    Database base;
    UnauthorizedUser unauthorizedUser;
    public static final int CODE = 101;
    User user;
    NavigationView navigationView;
    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        contactRecycler = view.findViewById(R.id.contact_us_recycler);
        buttonSendMail = view.findViewById(R.id.button_send_mail);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        navigationView = getActivity().findViewById(R.id.nav_view);
        loader = view.findViewById(R.id.loader);
        TextView textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        textToolbar.setText("Contact Us");

        navigationView.getMenu().getItem(5).setChecked(true);
        iniData();
        getContactUsData();

        buttonSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new SendContactUsMail()).commit();
            }
        });
        return view;
    }

    private void setAdapter(List<ContactUs> contactUsList)
    {
        if(getActivity() != null)
        {
            contactUsAdapter = new ContactUsAdapter(getContext(), contactUsList);
            contactRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            contactRecycler.setAdapter(contactUsAdapter);
        }
    }
    private void iniData()
    {
        base = new Database(getActivity());
        libFile = LibFile.getInstance(getContext());
//        List<ProductItem> searchItemsList2 = new ArrayList<>();
        token = LibFile.getInstance(getActivity()).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();
        user = new User();

        String mobile = libFile.getString(AppConstants.MOBILE_NO);
        String email = libFile.getString(AppConstants.EMAIl);
        String password = libFile.getString(AppConstants.PASSWORD);
        user.setPassword(password);
        if(!mobile.equals(""))
        {
            user.setMobileNo(mobile);
        }
        else
        {
            user.setEmail(email);
        }

    }

    private void getContactUsData()
    {
//        Toast.makeText(getContext(), token, Toast.LENGTH_LONG).show();
        Log.e("token", token);
        Call<ListContactusResponse> call = apiInterface.apiGetContactUsDetails(token);
        call.enqueue(new Callback<ListContactusResponse>()
        {
            @Override
            public void onResponse(retrofit2.Call<ListContactusResponse> call, Response<ListContactusResponse> response)
            {
//                Toast.makeText(getContext(), new Gson().toJson(response), Toast.LENGTH_LONG).show();
                Log.e("contactResponse", new Gson().toJson(response));
                if(response.isSuccessful())
                {
                    stopAnim();
                    if(response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL)
                    {
//                        Toast.makeText(getActivity(), response.body().getData().toString(), Toast.LENGTH_LONG).show();
                        contactUsList = response.body().getData();
                        setAdapter(contactUsList);
                    }
                }
                else
                {
                    stopAnim();
                    Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_LONG).show();
//                    unauthorizedUser.onUnauthorized(CODE);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListContactusResponse> call, Throwable t)
            {
                stopAnim();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    void stopAnim()
    {
        loader.setVisibility(View.GONE);
    }
}
