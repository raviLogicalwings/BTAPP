package com.logicalwings.btapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.CartActivity;
import com.logicalwings.btapp.activities.CategoryFirstActivity;
import com.logicalwings.btapp.activities.CategorySecondActivity;
import com.logicalwings.btapp.activities.HomeActivity;
import com.logicalwings.btapp.model.ListCategoryResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    TextView placeOrder, myOrders, favourites, textToolbar, textCartCounter;
    ImageView toolSearch, toolCart, toolSave;
    FrameLayout frameLayout;
    private String token;
    private ApiInterface apiInterface;
    String firstCategory, secondCategory;
    ProgressBar loader;
    NavigationView navigationView;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_home, container, false);

        initData();

        initUi();

        return fView;
    }

    @Override
    protected void initData() {
        super.initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppUtils.hasInternet(Objects.requireNonNull(getActivity()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartCounter();
    }

    @Override
    protected void initUi() {
        super.initUi();

        placeOrder = fView.findViewById(R.id.text_place_order);
        myOrders = fView.findViewById(R.id.text_orders);
        favourites = fView.findViewById(R.id.text_favourites);
        loader = fView.findViewById(R.id.loader);
        textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        frameLayout = getActivity().findViewById(R.id.frame_layout_cart);
        textCartCounter = getActivity().findViewById(R.id.text_cart_counter);
        navigationView = getActivity().findViewById(R.id.nav_view);

        textToolbar.setText("BHARAT TYRES");

        navigationView.getMenu().getItem(0).setChecked(true);
        setCartCounter();
        loadState();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new PlaceOrderFragment()).commit();
                startAnim();
                placeOrder.setEnabled(false);
                getCategories();
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new OrdersFragment()).commit();
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new FavouritesFragment()).commit();
            }
        });
    }

    private void showCategoryDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.product_category_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        final TextView textCategoryFirst = (TextView) dialogView.findViewById(R.id.text_category_first);
        final TextView textCategorySecond = (TextView) dialogView.findViewById(R.id.text_category_second);
        final TextView textCancel = (TextView) dialogView.findViewById(R.id.text_cancel);

        if (!firstCategory.isEmpty() && !secondCategory.isEmpty()) {
            textCategoryFirst.setText(firstCategory);
            textCategorySecond.setText(secondCategory);
        }
        textCategoryFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                Intent i = new Intent(getActivity(), CategoryFirstActivity.class);
                startActivity(i);
            }
        });

        textCategorySecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                Intent i = new Intent(getActivity(), CategorySecondActivity.class);
                startActivity(i);
            }
        });

        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                placeOrder.setEnabled(true);
            }
        });

        b.show();
    }

    private void getCategories() {
        Log.d("token", token);
        Call<ListCategoryResponse> call = apiInterface.apiGetCategory(token);
        call.enqueue(new Callback<ListCategoryResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListCategoryResponse> call, Response<ListCategoryResponse> response) {
                if (response.isSuccessful()) {
                    placeOrder.setEnabled(true);
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        firstCategory = response.body().getData().get(0).getCategoryName();
                        secondCategory = response.body().getData().get(1).getCategoryName();
                        showCategoryDialog();

                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    placeOrder.setEnabled(true);
                    stopAnim();
                    Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListCategoryResponse> call, Throwable t) {
                placeOrder.setEnabled(true);
                stopAnim();
                if(t.getMessage().equals(AppConstants.NO_INTERNET)){
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadState() {
        stopAnim();
        toolSearch.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        toolSave.setVisibility(View.GONE);
        token = LibFile.getInstance(getActivity()).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();
    }

    void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
    }

    private void setCartCounter() {
        int cartCounter = AppUtils.getCartCounter(getActivity());
        Log.e("cartListSize", String.valueOf(cartCounter));
        if (cartCounter != 0 && cartCounter > 0) {
            textCartCounter.setText(String.valueOf(cartCounter));
        } else {
            textCartCounter.setVisibility(View.GONE);
        }
    }
}
