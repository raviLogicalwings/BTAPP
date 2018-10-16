package com.logicalwings.btapp.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.CartActivity;
import com.logicalwings.btapp.adapters.MyOrdersAdapter;
import com.logicalwings.btapp.model.GetOrdersResponse;
import com.logicalwings.btapp.model.ListGetOrdersResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    RecyclerView orderRecycler;
    MyOrdersAdapter myOrdersAdapter;
    ImageView toolSearch, toolCart, toolSave;
    private ApiInterface apiInterface;
    private String token;
    List<GetOrdersResponse> getOrdersResponses;
    ProgressBar loader;
    LinearLayout linearOrderNoData, linearOrdersNoInternet;
    Button btOrdersNoInternet;
    SwipeRefreshLayout swipeRefreshLayout;
    FrameLayout frameLayout;
    NavigationView navigationView;
    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        orderRecycler = view.findViewById(R.id.order_recycler);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        btOrdersNoInternet = view.findViewById(R.id.button_orders_retry);
        linearOrdersNoInternet = view.findViewById(R.id.linear_orders_no_internet);
        TextView textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        frameLayout = frameLayout = getActivity().findViewById(R.id.frame_layout_cart);
        loader = view.findViewById(R.id.loader);
        linearOrderNoData = view.findViewById(R.id.linear_order_noData);
        navigationView = getActivity().findViewById(R.id.nav_view);
        textToolbar.setText("My Orders");

        navigationView.getMenu().getItem(2).setChecked(true);
        loadState();
        getOrders();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadState();
                getOrders();
            }
        });

        btOrdersNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearOrdersNoInternet.setVisibility(View.GONE);
                btOrdersNoInternet.setVisibility(View.GONE);
                startAnim();
                loadState();
                getOrders();
            }
        });
        return view;
    }

    private void getOrders() {
        swipeRefreshLayout.setRefreshing(false);
        Call<ListGetOrdersResponse> call = apiInterface.apiGetOrders(token);
        call.enqueue(new Callback<ListGetOrdersResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListGetOrdersResponse> call, Response<ListGetOrdersResponse> response) {
                Log.e("getOrdersResponse", new Gson().toJson(response));
                Log.e("errorCodeOrder", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
//                        for (int i = 0; i < response.body().getData().size(); i++) {
//                            Log.e("getSapOrdersResponse", response.body().getData().get(i).getSapOrderNo());
//                        }
                        getOrdersResponses = response.body().getData();
                        setAdapter();
                    } else {
                        if (getActivity() != null){
                            AppUtils.showToast(getActivity(), response.body().getMessage());
                        }
                        linearOrderNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    linearOrderNoData.setVisibility(View.VISIBLE);
                    linearOrdersNoInternet.setVisibility(View.GONE);
                    btOrdersNoInternet.setVisibility(View.GONE);
                    stopAnim();
                    Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListGetOrdersResponse> call, Throwable t) {
                stopAnim();
                if(t.getMessage().equals(AppConstants.NO_INTERNET)){
                    linearOrdersNoInternet.setVisibility(View.VISIBLE);
                    btOrdersNoInternet.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAdapter() {
        if (getActivity() != null) {
            myOrdersAdapter = new MyOrdersAdapter(getActivity());
            orderRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            myOrdersAdapter.setList(getOrdersResponses);
            orderRecycler.setAdapter(myOrdersAdapter);
        }
    }

    private void loadState() {
        toolSearch.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        toolSave.setVisibility(View.GONE);
        token = LibFile.getInstance(getActivity()).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();
    }

    private void startAnim(){
        loader.setVisibility(View.VISIBLE);
    }
    private void stopAnim() {
        loader.setVisibility(View.GONE);
    }
}
