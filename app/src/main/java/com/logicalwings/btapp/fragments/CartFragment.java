package com.logicalwings.btapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.OrderReviewActivity;
import com.logicalwings.btapp.adapters.CartAdapter;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.model.Cart;
import com.logicalwings.btapp.model.CartPost;
import com.logicalwings.btapp.model.CartResponse;
import com.logicalwings.btapp.model.ListCartCreateOrderPost;
import com.logicalwings.btapp.model.ListCartPost;
import com.logicalwings.btapp.model.ListCartResponse;
import com.logicalwings.btapp.model.OrderResponse;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.retofit.CartCallBackInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    RecyclerView cartRecycler;
    CartAdapter cartAdapter;
    ImageView toolSearch, toolCart, toolSave;
    private ApiInterface apiInterface;
    private String token;
    protected LibFile libFile;
    ListCartPost listCartPost = new ListCartPost();
    List<CartPost> cartPosts = new ArrayList<>();
    List<ListCartPost> listCartPosts = new ArrayList<>();
    List<CartResponse> cartResponses;
    ListCartCreateOrderPost listCartCreateOrderPost = new ListCartCreateOrderPost();
    TextView textProductTotalAmount, textProductItemsCount;
    ProgressBar loader;
    CartCallBackInterface cartCallBackInterface;
    FrameLayout frameLayout;
    LinearLayout linearCartNodata, linearCartProceed, linearProceed;
    double totalAmount = 0;
    double totalPrice = 0;
    int totalGST = 0;
    DecimalFormat convertDecimal = new DecimalFormat("0.00");
//    SwipeRefreshLayout swipeRefreshLayout;

    public CartFragment() {
        // Required empty public constructor
    }

    public CartFragment(CartCallBackInterface cartCallBackInterface) {
        // Required empty public constructor
        this.cartCallBackInterface = cartCallBackInterface;
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRecycler = view.findViewById(R.id.cart_recycler);
        textProductItemsCount = view.findViewById(R.id.text_product_items_count);
        textProductTotalAmount = view.findViewById(R.id.text_product_total_amount);
//        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        loader = view.findViewById(R.id.loader);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        frameLayout = getActivity().findViewById(R.id.frame_layout_cart);
        linearCartNodata = view.findViewById(R.id.linear_cart_nodata);
        linearCartProceed = view.findViewById(R.id.linear_cart_proceed);
        linearProceed = view.findViewById(R.id.linear_proceed);
        TextView textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        textToolbar.setText("My Cart");

        loadState();
        getCartData();

        linearProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listCartCreateOrderPost.setItems(cartResponses);
                Gson gson = new Gson();
                String json = gson.toJson(listCartCreateOrderPost);
                Log.e("createOrderJson", json);
                startAnim();
                createOrder(listCartCreateOrderPost);
            }
        });

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadState();
//                if(cartResponses.size() < 1)
//                {
//                    linearCartNodata.setVisibility(View.VISIBLE);
//                    linearCartProceed.setVisibility(View.GONE);
//                }
//                else
//                {
//                    getCartData();
//                }
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        return view;
    }

    private void getCartData() {
        Database base = new Database(getActivity());
        base.open();
        List<Cart> cartList = new ArrayList<>();
        cartList = base.getCartItem();
        base.close();

        for (int i = 0; i < cartList.size(); i++) {
            CartPost cartPost = new CartPost(cartList.get(i).getSegmentType(), cartList.get(i).getPattern(), cartList.get(i).getSize(), Long.parseLong(cartList.get(i).getQuantity()), cartList.get(i).getItemName(), Integer.valueOf(cartList.get(i).getFkItemGroupId()), Integer.valueOf(cartList.get(i).getFkFlapItemId()), Integer.valueOf(cartList.get(i).getItemId()), Integer.valueOf(cartList.get(i).getFkTubeItemId()), cartList.get(i).getSapItemCode());
            cartPosts.add(cartPost);
        }

        listCartPost.setItems(cartPosts);

        Gson gson = new Gson();
        String json = gson.toJson(listCartPost);
        Log.e("token", token);
        Log.e("jsonData", json);
        Call<ListCartResponse> call = apiInterface.apiGetCartDetails(token, listCartPost);
        call.enqueue(new Callback<ListCartResponse>() {
            @Override
            public void onResponse(Call<ListCartResponse> call, Response<ListCartResponse> response) {
                Log.e("cartResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        cartResponses = response.body().getData();
                        textProductItemsCount.setText("Items: " + cartResponses.size());

                        for (int i = 0; i < cartResponses.size(); i++) {
                            totalAmount += cartResponses.get(i).getTotalAmount();
                            totalPrice += cartResponses.get(i).getTotalPrice();
                            totalGST += cartResponses.get(i).getGSTAmount();
                        }
                        textProductTotalAmount.setText(String.valueOf(convertDecimal.format(totalAmount)));
                        setAdapter();
                        Log.d("serverCartReceived", response.body().getMessage());
                    }
                } else {
                    stopAnim();
                    linearCartNodata.setVisibility(View.VISIBLE);
                    linearCartProceed.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "your cart is empty!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ListCartResponse> call, Throwable t) {
                stopAnim();
                if(t.getMessage().equals(AppConstants.NO_INTERNET)){
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createOrder(ListCartCreateOrderPost listCartCreateOrderPost) {
        Call<OrderResponse> call = apiInterface.apiCreateOrder(token, listCartCreateOrderPost);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                Log.e("createOrderResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    stopAnim();
                    String sapOrderId = response.body().getData();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        Intent i = new Intent(getActivity(), OrderReviewActivity.class);
                        i.putExtra("SAPORDERID", sapOrderId);
                        i.putExtra("TOTALAMOUNT", String.valueOf(totalAmount));
                        i.putExtra("TOTALPRICE", String.valueOf(totalPrice));
                        i.putExtra("GSTAMOUNT", String.valueOf(totalGST));
                        ActivityCompat.finishAffinity(getActivity());
                        startActivity(i);
                        Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    stopAnim();
                    Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                stopAnim();
                if(t.getMessage().equals(AppConstants.NO_INTERNET)){
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAdapter() {
        cartAdapter = new CartAdapter(getContext());
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter.setList(cartResponses);
        cartRecycler.setAdapter(cartAdapter);
    }

    private void loadState() {
        toolSearch.setVisibility(View.VISIBLE);
        toolSave.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        linearCartNodata.setVisibility(View.GONE);
        AppUtils.hasInternet(getActivity());
        libFile = LibFile.getInstance(getContext());
        token = LibFile.getInstance(getActivity()).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();
    }

    void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
    }

    public void updateProductQuantity(int position, List<CartResponse> cartResponses, String quantity, Context context) {
        Log.e("receivedData", String.valueOf(cartResponses.get(position).getItemId()));
        String itemId = String.valueOf(cartResponses.get(position).getItemId());
        Database base = new Database(context);
        base.open();
        base.updateCartQuantity(itemId, quantity);
        base.close();
        cartCallBackInterface.cartData();
    }

    public void deleteCartFromSqlite(int position, List<CartResponse> cartResponses, Context context) {
        String itemId = String.valueOf(cartResponses.get(position).getItemId());
        Database base = new Database(context);
        base.open();
        base.deleteCartData(itemId);
        base.close();
        cartCallBackInterface.deleteCart();
    }
}
