package com.logicalwings.btapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.logicalwings.btapp.R;
import com.logicalwings.btapp.adapters.TruckProductsAdapter;
import com.logicalwings.btapp.base.Database;
import com.logicalwings.btapp.callbacks.FavouriteUnfavouriteOpration;
import com.logicalwings.btapp.callbacks.UpdateProductToDbOpration;
import com.logicalwings.btapp.model.Cart;
import com.logicalwings.btapp.model.FavouriteResponse;
import com.logicalwings.btapp.model.ItemGroupPost;
import com.logicalwings.btapp.model.ItemPatternPost;
import com.logicalwings.btapp.model.ItemSizePost;
import com.logicalwings.btapp.model.ListItemGroupResponse;
import com.logicalwings.btapp.model.ListItemPatternResponse;
import com.logicalwings.btapp.model.ListItemSizeResponse;
import com.logicalwings.btapp.model.ListSearchItemsResponse;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.model.SearchItemPost;
import com.logicalwings.btapp.retofit.ApiClient;
import com.logicalwings.btapp.retofit.ApiInterface;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorySecondActivity extends BaseActivity {

    private LinearLayout linearSearchItemGroup, linearSearchPattern, linearSearchSize, linearSearch;
    private AutoCompleteTextView textItemGroup, textSize, textPattern;
    private Button buttonSearch;
    private RecyclerView searchProductRecycler;

    private TruckProductsAdapter truckProductsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar loader;

    private ApiInterface apiInterface;
    private String token;

    private ItemGroupPost itemGroupPost;
    private ItemSizePost itemSizePost;
    private ItemPatternPost itemPatternPost;
    private SearchItemPost searchItemPost;
    private List<ProductItem> productItems;
    ArrayList<String> listItemGroupsId = new ArrayList<>();

    private Database base;
    private HashSet<String> hashSet;
    private ArrayList<String> listItemGroups;
    private ArrayList<String> listsizes;
    private ArrayList<String> listPatterns;
    private List<Cart> cartList;

    Toolbar toolbar;

    private static final int CODE_GET_ITEM_GROUP = 101;
    private static final int CODE_GET_ITEM_SIZE = 102;
    private static final int CODE_GET_ITEM_PATTERN = 103;
    private static final int CODE_GET_ITEM_SEARCH = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_second);

        initData();

        initUi();

        updateUi();

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
        hashSet = new HashSet<String>();
        listItemGroups = new ArrayList<>();
        listsizes = new ArrayList<>();
        listPatterns = new ArrayList<>();
        cartList = new ArrayList<>();

        base = new Database(CategorySecondActivity.this);
        base.open();
        cartList = base.getCartItem();
        base.close();
    }

    @Override
    protected void initUi() {
        super.initUi();

        linearSearchItemGroup = findViewById(R.id.linear_search_item_group);
        linearSearchPattern = findViewById(R.id.linear_search_pattern);
        linearSearchSize = findViewById(R.id.linear_search_size);
        linearSearch = findViewById(R.id.linear_search);
        textItemGroup = findViewById(R.id.text_item_group);
        textPattern = findViewById(R.id.text_pattern);
        textSize = findViewById(R.id.text_size);
        searchProductRecycler = findViewById(R.id.search_product_recycler);
        buttonSearch = findViewById(R.id.button_search);
        loader = findViewById(R.id.loader);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Non-Truck Products");
        loadState();

        textItemGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Toast.makeText(getContext(), s.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count >= 2) {
                itemGroupPost.setKeyword(s.toString());
                itemGroupPost.setCategoryId(2);
                if (!textSize.getText().toString().equals("")) {
                    itemGroupPost.setSize(textSize.getText().toString());
                }
                if (!textPattern.getText().toString().equals("")) {
                    itemGroupPost.setPattern(textPattern.getText().toString());
                }
                getItemGroupData(itemGroupPost);
            }
//            }

            @Override
            public void afterTextChanged(Editable s) {
//                Toast.makeText(getContext(), s.toString(), Toast.LENGTH_LONG).show();

            }
        });

        textSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count >= 2) {
                itemSizePost.setKeyword(s.toString());
                itemSizePost.setCategoryId("2");
//                    itemSizePost.setItemGroupId(1);
                if (!textPattern.getText().toString().equals("")) {
                    itemSizePost.setPattern(textPattern.getText().toString());
                }
                if (!textItemGroup.getText().toString().equals("")) {
                    String string = textItemGroup.getText().toString();

                    for (int i = 0; i < listItemGroupsId.size(); i++) {
                        Log.e("ids", listItemGroupsId.get(i));
                        String string2 = listItemGroupsId.get(i);
                        if (string2.contains("/")) {
                            String[] parts = string2.split("/");
                            String part1 = parts[0];
                            String part2 = parts[1];
                            Log.e("ids2", part2);
                            itemSizePost.setItemGroupId(Integer.parseInt(part2));
                        } else {
                            AppUtils.showToast(CategorySecondActivity.this, "Please select itemGroup from list!");
                        }
                    }
//                    if (string.contains("/")) {
//                        String[] parts = string.split("/");
//                        String part2 = parts[1];
//                        itemSizePost.setItemGroupId(Integer.parseInt(part2));
//                    } else {
//                        AppUtils.showToast(CategorySecondActivity.this, "Please select itemGroup from list!");
//                    }
                }
                getItemSizeData(itemSizePost);
            }
//            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textPattern.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count >= 2) {
                itemPatternPost.setKeyword(s.toString());
                itemPatternPost.setCategoryId("2");
                if (!textSize.getText().toString().equals("")) {
                    itemPatternPost.setSize(textSize.getText().toString());
                }
                if (!textItemGroup.getText().toString().equals("")) {
                    String string = textItemGroup.getText().toString();

                    for (int i = 0; i < listItemGroupsId.size(); i++) {
                        Log.e("ids", listItemGroupsId.get(i));
                        String string2 = listItemGroupsId.get(i);
                        if (string2.contains("/")) {
                            String[] parts = string2.split("/");
                            String part1 = parts[0];
                            String part2 = parts[1];
                            Log.e("ids3", part2);
                            itemPatternPost.setItemGroupId(Integer.parseInt(part2));
                        } else {
                            AppUtils.showToast(CategorySecondActivity.this, "Please select itemGroup from list!");
                        }
                    }
//                    if (string.contains("/")) {
//                        String[] parts = string.split("/");
//                        String part2 = parts[1];
//                        AppUtils.showToast(CategorySecondActivity.this, part2);
//                        itemPatternPost.setItemGroupId(Integer.parseInt(part2));
//                    } else {
//                        AppUtils.showToast(CategorySecondActivity.this, "Please select itemGroup from list!");
//                    }
                }
                getItemPatternData(itemPatternPost);
            }
//            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!textItemGroup.getText().toString().equals("") || !textPattern.getText().toString().equals("") || !textSize.getText().toString().equals("")) {
                    startAnim();
                    if (!textSize.getText().toString().equals("")) {
                        searchItemPost.setSize(textSize.getText().toString());
                    }
                    if (!textPattern.getText().toString().equals("")) {
                        searchItemPost.setPattern(textPattern.getText().toString());
                    }
                    if (!textItemGroup.getText().toString().equals("")) {
                        String string = textItemGroup.getText().toString();

                        for (int i = 0; i < listItemGroupsId.size(); i++) {
                            Log.e("ids", listItemGroupsId.get(i));
                            String string2 = listItemGroupsId.get(i);
                            if (string2.contains("/")) {
                                String[] parts = string2.split("/");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                Log.e("ids3", part2);
                                searchItemPost.setItemGroupId(part2);
                            } else {
                                AppUtils.showToast(CategorySecondActivity.this, "Please select itemGroup from list!");
                            }
                        }
//                        if (string.contains("/")) {
//                            String[] parts = string.split("/");
//                            String part2 = parts[1];
//                            searchItemPost.setItemGroupId(part2);
//                        } else {
//                            AppUtils.showToast(CategorySecondActivity.this, "Please select itemGroup from list!");
//                        }
                    }
                    if (searchItemPost != null) {
                        linearSearch.setVisibility(View.GONE);
                        searchProductRecycler.setVisibility(View.VISIBLE);
                        getSearchItemsData(searchItemPost);
                    }
                } else {
                    AppUtils.showToast(CategorySecondActivity.this, "Please enter atLeast 1 item");
                }
            }
        });

        truckProductsAdapter = new TruckProductsAdapter(CategorySecondActivity.this, productItems, new FavouriteUnfavouriteOpration() {
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
        searchProductRecycler.setAdapter(truckProductsAdapter);

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

        if (code == CODE_GET_ITEM_GROUP) {
            getItemGroupData(itemGroupPost);
        } else if (code == CODE_GET_ITEM_SIZE) {
            getItemSizeData(itemSizePost);
        } else if (code == CODE_GET_ITEM_PATTERN) {
            getItemPatternData(itemPatternPost);
        } else if (code == CODE_GET_ITEM_SEARCH) {
            getSearchItemsData(searchItemPost);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CategorySecondActivity.this, HomeActivity.class);
        ActivityCompat.finishAffinity(CategorySecondActivity.this);
        startActivity(i);
    }

    private void loadState() {
        stopAnim();
        linearLayoutManager = new LinearLayoutManager(CategorySecondActivity.this);
        token = LibFile.getInstance(CategorySecondActivity.this).getString(AppConstants.TOKEN);
        apiInterface = ApiClient.getApiClient();

        itemGroupPost = new ItemGroupPost();
        itemSizePost = new ItemSizePost();
        itemPatternPost = new ItemPatternPost();
        searchItemPost = new SearchItemPost();
        itemGroupPost.setCategoryId(1);

        AppUtils.hasInternet(CategorySecondActivity.this);
        libFile = LibFile.getInstance(CategorySecondActivity.this);

        textItemGroup.setThreshold(2);
        textSize.setThreshold(2);
        textPattern.setThreshold(2);
    }

    void startAnim() {
        loader.setVisibility(View.VISIBLE);
    }

    void stopAnim() {
        loader.setVisibility(View.GONE);
    }

    /**
     * @param itemGroupPost
     */
    private void getItemGroupData(ItemGroupPost itemGroupPost) {

        Call<ListItemGroupResponse> call = apiInterface.apiGetItemGroup(itemGroupPost, token);
        call.enqueue(new Callback<ListItemGroupResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListItemGroupResponse> call, Response<ListItemGroupResponse> response) {
                Log.d("itemgroupResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    stopAnim();
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        hashSet.clear();
                        listItemGroups.clear();
                        listItemGroupsId.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            listItemGroups.add(response.body().getData().get(i).getItemGroupName());
                            listItemGroupsId.add(response.body().getData().get(i).getItemGroupName() + "/" + response.body().getData().get(i).getItemGroupId());
                        }

                        hashSet.addAll(listItemGroups);
                        listItemGroups.clear();
                        listItemGroups.addAll(hashSet);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (CategorySecondActivity.this, android.R.layout.select_dialog_item, listItemGroups);
                        textItemGroup.setAdapter(adapter);
                    } else if (response.code() == AppConstants.ERROR_CODE) {
                        authenticateUser(CODE_GET_ITEM_GROUP);
                    } else {
                        AppUtils.showToast(CategorySecondActivity.this, "Data not available!");
                    }
                } else {
                    stopAnim();
                    Toast.makeText(CategorySecondActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListItemGroupResponse> call, Throwable t) {
                stopAnim();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(CategorySecondActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * @param itemSizePost
     */
    private void getItemSizeData(ItemSizePost itemSizePost) {
        Call<ListItemSizeResponse> call = apiInterface.apiGetItemSize(itemSizePost, token);
        call.enqueue(new Callback<ListItemSizeResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListItemSizeResponse> call, Response<ListItemSizeResponse> response) {
                Log.e("itemSizeResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        hashSet.clear();
                        listsizes.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            listsizes.add(response.body().getData().get(i).getSize());
                        }
                        hashSet.addAll(listsizes);
                        listsizes.clear();
                        listsizes.addAll(hashSet);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (CategorySecondActivity.this, android.R.layout.select_dialog_item, listsizes);
                        textSize.setAdapter(adapter);
                    } else if (response.code() == AppConstants.ERROR_CODE) {
                        authenticateUser(CODE_GET_ITEM_SIZE);
                    } else {
//                        AppUtils.showToast(CategorySecondActivity.this, "Data not available!");
                    }
                } else {
                    Toast.makeText(CategorySecondActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListItemSizeResponse> call, Throwable t) {
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(CategorySecondActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * @param itemPatternPost
     */
    private void getItemPatternData(ItemPatternPost itemPatternPost) {
        Log.d("toke", token);
        Call<ListItemPatternResponse> call = apiInterface.apiGetItemPattern(itemPatternPost, token);
        call.enqueue(new Callback<ListItemPatternResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListItemPatternResponse> call, Response<ListItemPatternResponse> response) {
                Log.d("itemgroupResponse", new Gson().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        hashSet.clear();
                        listPatterns.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            listPatterns.add(response.body().getData().get(i).getPattern());
                        }

                        hashSet.addAll(listPatterns);
                        listPatterns.clear();
                        listPatterns.addAll(hashSet);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (CategorySecondActivity.this, android.R.layout.select_dialog_item, listPatterns);
                        textPattern.setAdapter(adapter);
                    } else if (response.code() == AppConstants.ERROR_CODE) {
                        authenticateUser(CODE_GET_ITEM_PATTERN);
                    } else {
//                        AppUtils.showToast(CategorySecondActivity.this, "Data not available!");
                    }
                } else {
                    Toast.makeText(CategorySecondActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListItemPatternResponse> call, Throwable t) {
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(CategorySecondActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * @param searchItemPost
     */
    private void getSearchItemsData(SearchItemPost searchItemPost) {

        Call<ListSearchItemsResponse> call = apiInterface.apiGetSearchItems(searchItemPost, token);

        call.enqueue(new Callback<ListSearchItemsResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ListSearchItemsResponse> call, Response<ListSearchItemsResponse> response) {
                Log.d("searchResponse", new Gson().toJson(response));

                if (response.isSuccessful()) {

                    stopAnim();

                    if (response.body().getStatuscode() == AppConstants.RESULT_SUCCESSFUL) {
                        productItems.clear();
                        productItems.addAll(response.body().getData());
                    }
                } else if (response.code() == AppConstants.ERROR_CODE) {
                    authenticateUser(CODE_GET_ITEM_SEARCH);
                } else {

                    stopAnim();
                    Toast.makeText(CategorySecondActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                }

                updateUi();
            }

            @Override
            public void onFailure(retrofit2.Call<ListSearchItemsResponse> call, Throwable t) {
                stopAnim();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(CategorySecondActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                        Toast.makeText(CategorySecondActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    if (response.body().getStatuscode() == AppConstants.RESULT_FAIL) {
                        productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                    }

                } else {
                    Toast.makeText(CategorySecondActivity.this, "something went wrong!", Toast.LENGTH_LONG).show();
                    productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                }

                truckProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<FavouriteResponse> call, Throwable t) {
                productItem.setIsFavourite(productItem.getIsFavourite() == 1 ? 0 : 1);
                truckProductsAdapter.notifyDataSetChanged();
                if (t.getMessage().equals(AppConstants.NO_INTERNET)) {
                    Toast.makeText(CategorySecondActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
