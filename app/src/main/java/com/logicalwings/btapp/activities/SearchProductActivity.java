package com.logicalwings.btapp.activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.TruckProductsAdapter;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.callbacks.FavouriteUnfavouriteOpration;
import com.logicalwings.btapp.callbacks.UpdateProductToDbOpration;
import com.logicalwings.btapp.model.FavouriteResponse;
import com.logicalwings.btapp.model.ListSearchItemsResponse;
import com.logicalwings.btapp.model.SearchGloballyPost;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductActivity extends BaseActivity {

    private SearchView searchView;
    private ApiInterface apiInterface;
    private ProgressBar loader, scrollLoader;
    private String token;
    private TextView textNoData;
    private RecyclerView searchProductRecycler;

    private SearchGloballyPost searchGloballyPost;
    private List<ProductItem> productItems;
    private LinearLayoutManager linearLayoutManager;
    private TruckProductsAdapter truckProductsAdapter;
    private Database base;
    private static final int CODE_GET_SEARCH_ITEMS = 140;
    Toolbar toolbar;
    Boolean isScrolling = false;
    int currrentItems, totalItems, scrolledOutItems, pageCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        initData();

        initUi();

        updateUi();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SearchProductActivity.this, HomeActivity.class);
        ActivityCompat.finishAffinity(SearchProductActivity.this);
        startActivity(i);
    }

    @Override
    protected void initData() {
        super.initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorBlack));
        }
        productItems = new ArrayList<>();
        apiInterface = ApiClient.getApiClient();
        token = LibFile.getInstance(SearchProductActivity.this).getString(AppConstants.TOKEN);
        searchGloballyPost = new SearchGloballyPost();
        AppUtils.hasInternet(SearchProductActivity.this);

    }

    @Override
    protected void initUi() {
        super.initUi();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Search Here");
        }

        searchView = findViewById(R.id.search);
        searchProductRecycler = findViewById(R.id.search_product_recycler);
        loader = findViewById(R.id.loader);
        scrollLoader = findViewById(R.id.scroll_loader);
        textNoData = findViewById(R.id.text_no_data_available);

        linearLayoutManager = new LinearLayoutManager(SearchProductActivity.this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(SearchProductActivity.this, query, Toast.LENGTH_LONG).show();
                if (!query.equals("")) {
                    searchGloballyPost.setKeyword(query);
                }
                if (searchGloballyPost != null) {
                    startAnim();
                    getSearchItems(searchGloballyPost);
                } else {
                    AppUtils.showToast(SearchProductActivity.this, "please enter something to search!");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        truckProductsAdapter = new TruckProductsAdapter(SearchProductActivity.this, productItems, new FavouriteUnfavouriteOpration() {
            @Override
            public void makeFavourite(ProductItem productItem) {
                makeFavouriteunFavouriteProduct(productItem);
            }
            @Override
            public void makeUnfavourite(ProductItem productItem) {
                makeFavouriteunFavouriteProduct(productItem);
            }
        }, new UpdateProductToDbOpration() {
            @Override
            public void updateProduct(ProductItem productItem) {
                updateQtyTODb(productItem);
            }
        });
        searchProductRecycler.setLayoutManager(linearLayoutManager);
        truckProductsAdapter.setSearchView(searchView);
        searchProductRecycler.setAdapter(truckProductsAdapter);

        searchProductRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    Log.e("scrolling", "fine");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currrentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                Log.e("scrolled", String.valueOf(currrentItems+ "/" +totalItems+ "/" +scrolledOutItems));
                if (isScrolling && currrentItems + scrolledOutItems == totalItems) {
                    pageCount++;
                    isScrolling = false;
                }
            }
        });

        stopAnim();
    }

    private void updateUi() {
        if (productItems != null && productItems.size() > 0) {
            searchProductRecycler.setVisibility(View.VISIBLE);
        } else {
            searchProductRecycler.setVisibility(View.GONE);
        }
        truckProductsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);

        if (code == CODE_GET_SEARCH_ITEMS) {
            getSearchItems(searchGloballyPost);
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

    private void getSearchItems(SearchGloballyPost searchGloballyPost) {
        base = new Database(SearchProductActivity.this);
        Call<ListSearchItemsResponse> call = apiInterface.apiGetSearchItemsGlobally(searchGloballyPost, token);
        call.enqueue(new Callback<ListSearchItemsResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListSearchItemsResponse> call, Response<ListSearchItemsResponse> response) {
                Log.e("resetPassResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    stopAnim();
                    searchProductRecycler.setVisibility(View.VISIBLE);

                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        productItems.clear();
                        productItems.addAll(response.body().getData());

                    } else {
                        textNoData.setVisibility(View.VISIBLE);
                        textNoData.setText(response.body().getMessage());
                        searchProductRecycler.setVisibility(View.GONE);
                    }

                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_GET_SEARCH_ITEMS);
                } else {

                    stopAnim();

                    AppUtils.showToast(SearchProductActivity.this, "something went wrong!");
                }

                updateUi();
            }

            @Override
            public void onFailure(retrofit2.Call<ListSearchItemsResponse> call, Throwable t) {
                stopAnim();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(SearchProductActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
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

    /**
     * Make product as un-Favourite
     *
     * @param productItem
     */
    public void makeFavouriteunFavouriteProduct(final ProductItem productItem) {
        String token = libFile.getString(AppConstants.TOKEN);

        ApiInterface apiInterface = ApiClient.getApiClient();

        Call<FavouriteResponse> call = apiInterface.apiMakeFavourite(productItem, token);
        call.enqueue(new Callback<FavouriteResponse>() {
            @Override
            public void onResponse(retrofit2.Call<FavouriteResponse> call, Response<FavouriteResponse> response) {

                Log.d("favouriteResponse", new Gson().toJson(response));

                if (response.isSuccessful()) {

                    if (response.body().getMessage() != null)
                        Toast.makeText(SearchProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    if (response.body().getStatuscode() == AppConstants.RESULT_FAIL) {
                        productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                    }

                } else {
                    Toast.makeText(SearchProductActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                    productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                }

                truckProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<FavouriteResponse> call, Throwable t) {
                productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                truckProductsAdapter.notifyDataSetChanged();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(SearchProductActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
