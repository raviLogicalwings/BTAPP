package com.logicalwings.btapp.fragments;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import com.logicalwings.btapp.adapters.FavouritesAdapter;
import com.logicalwings.btapp.callbacks.FavouriteUnfavouriteOpration;
import com.logicalwings.btapp.callbacks.UpdateProductToDbOpration;
import com.logicalwings.btapp.model.FavouriteResponse;
import com.logicalwings.btapp.model.ListGetFavouriteResponse;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesFragment extends BaseFragment {

    private static final int CODE_GET_FAVOURITE = 230;
    NavigationView navigationView;
    private RecyclerView favouriteRecycler;
    private ImageView toolSearch, toolCart, toolSave;
    private ApiInterface apiInterface;
    private String token;
    private ProgressBar loader;
    private LinearLayout linearFavouriteNoData, linearFavouriteNoInternet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FrameLayout frameLayout;
    private FavouritesAdapter favouritesAdapter;
    private Button btFavouriteNoInternet;
    private List<ProductItem> productItems;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_favourites, container, false);

        initData();

        initUi();

        updateUi();

        return fView;
    }

    @Override
    protected void initData() {
        super.initData();
        productItems = new ArrayList<>();
    }

    @Override
    protected void initUi() {
        super.initUi();

        favouriteRecycler = fView.findViewById(R.id.favourite_recycler);
        swipeRefreshLayout = fView.findViewById(R.id.swipeRefresh);
        linearFavouriteNoData = fView.findViewById(R.id.linear_favourite_noData);
        linearFavouriteNoInternet = fView.findViewById(R.id.linear_favourite_no_internet);
        btFavouriteNoInternet = fView.findViewById(R.id.button_favourite_retry);
        loader = fView.findViewById(R.id.loader);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        frameLayout = frameLayout = getActivity().findViewById(R.id.frame_layout_cart);
        TextView textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        navigationView = getActivity().findViewById(R.id.nav_view);
        textToolbar.setText("My Favourites");

        navigationView.getMenu().getItem(4).setChecked(true);

        loadState();
        getFavourites();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadState();
                getFavourites();

            }
        });

        btFavouriteNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                linearFavouriteNoInternet.setVisibility(View.GONE);
                btFavouriteNoInternet.setVisibility(View.GONE);
                loadState();
                getFavourites();
            }
        });
        favouritesAdapter = new FavouritesAdapter(getContext(), productItems, new FavouriteUnfavouriteOpration() {
            @Override
            public void makeFavourite(ProductItem productItem) {

            }

            @Override
            public void makeUnfavourite(ProductItem productItem) {

                if (productItem != null) {
                    unFavouriteProducts(productItem);
                } else {
                    linearFavouriteNoData.setVisibility(View.VISIBLE);
                }

            }
        }, new UpdateProductToDbOpration() {
            @Override
            public void updateProduct(ProductItem productItem) {
                updateQtyTODb(productItem);
            }
        });
        favouriteRecycler.setAdapter(favouritesAdapter);

    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);

        if (code == CODE_GET_FAVOURITE) {
            getFavourites();
        }
    }

    @Override
    protected void onTokenRefreshFail(int code) {
        super.onTokenRefreshFail(code);
    }

    @Override
    public void authenticateUser(int code) {
        super.authenticateUser(code);
    }

    private void updateUi() {
        if (productItems != null && productItems.size() > 0) {
            favouriteRecycler.setVisibility(View.VISIBLE);
            linearFavouriteNoData.setVisibility(View.GONE);

        } else {
            favouriteRecycler.setVisibility(View.GONE);
//            linearFavouriteNoData.setVisibility(View.VISIBLE);
        }
    }

    private void loadState() {
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        toolSearch.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        toolSave.setVisibility(View.GONE);
        linearFavouriteNoData.setVisibility(View.GONE);
        token = libFile.getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();
    }

    private void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    private void stopAnim() {
        loader.setVisibility(View.GONE);
    }

    /**
     * Get all favourite Products from server
     */
    private void getFavourites() {

        Log.d("token", token);
        swipeRefreshLayout.setRefreshing(false);
        Call<ListGetFavouriteResponse> call = apiInterface.apiGetFavourite(token);

        call.enqueue(new Callback<ListGetFavouriteResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListGetFavouriteResponse> call, Response<ListGetFavouriteResponse> response) {

                if (getActivity() == null)
                    return;

                Log.d("getFavouritesResponse", new Gson().toJson(response));

                stopAnim();
                Log.e("errorCodeFav", String.valueOf(response.code()));
                if (response.isSuccessful()) {

                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {

                        AppUtils.showToast(getActivity(), String.valueOf(response.body().getData().size()) + " Favourite items found");
                        productItems.clear();
                        productItems.addAll(response.body().getData());

                    } else {
                        linearFavouriteNoData.setVisibility(View.VISIBLE);
                        AppUtils.showToast(getActivity(), "No Favourite items available!");
                    }

                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_GET_FAVOURITE);
                } else {

                    linearFavouriteNoData.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "something went wrong!", Toast.LENGTH_LONG).show();
                }

                updateUi();
            }

            @Override
            public void onFailure(retrofit2.Call<ListGetFavouriteResponse> call, Throwable t) {
                stopAnim();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    linearFavouriteNoInternet.setVisibility(View.VISIBLE);
                    btFavouriteNoInternet.setVisibility(View.VISIBLE);
                    linearFavouriteNoData.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Make product as un-Favourite
     *
     * @param productItem
     */
    public void unFavouriteProducts(final ProductItem productItem) {
        String token = libFile.getString(AppConstants.TOKEN);

        ApiInterface apiInterface = ApiClient.getApiClient();

        Call<FavouriteResponse> call = apiInterface.apiMakeFavourite(productItem, token);
        call.enqueue(new Callback<FavouriteResponse>() {
            @Override
            public void onResponse(retrofit2.Call<FavouriteResponse> call, Response<FavouriteResponse> response) {

                Log.d("favouriteResponse", new Gson().toJson(response));

                if (getActivity() == null)
                    return;

                if (response.isSuccessful()) {

                    if (response.body().getMessage() != null)
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    if (response.body().getStatuscode() == AppConstants.RESULT_FAIL) {
                        productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                    } else {
                        favouritesAdapter.getProductItems().remove(productItem);
                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().detach(FavouritesFragment.this).attach(FavouritesFragment.this).commit();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "something went wrong!", Toast.LENGTH_LONG).show();
                    productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                }

                favouritesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<FavouriteResponse> call, Throwable t) {
                productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                favouritesAdapter.notifyDataSetChanged();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
