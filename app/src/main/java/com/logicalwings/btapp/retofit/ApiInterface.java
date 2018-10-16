package com.logicalwings.btapp.retofit;

import com.logicalwings.btapp.model.FavouriteResponse;
import com.logicalwings.btapp.model.ItemGroupPost;
import com.logicalwings.btapp.model.ItemPatternPost;
import com.logicalwings.btapp.model.ItemSizePost;
import com.logicalwings.btapp.model.ListCartCreateOrderPost;
import com.logicalwings.btapp.model.ListCartPost;
import com.logicalwings.btapp.model.ListCartResponse;
import com.logicalwings.btapp.model.ListCategoryResponse;
import com.logicalwings.btapp.model.ListContactusResponse;
import com.logicalwings.btapp.model.ListGetFavouriteResponse;
import com.logicalwings.btapp.model.ListGetOrderDetailResponse;
import com.logicalwings.btapp.model.ListGetOrdersResponse;
import com.logicalwings.btapp.model.ListItemGroupResponse;
import com.logicalwings.btapp.model.ListItemPatternResponse;
import com.logicalwings.btapp.model.ListItemSizeResponse;
import com.logicalwings.btapp.model.ListSearchItemsResponse;
import com.logicalwings.btapp.model.LoginResponce;
import com.logicalwings.btapp.model.MainResponce;
import com.logicalwings.btapp.model.OrderResponse;
import com.logicalwings.btapp.model.ProductItem;
import com.logicalwings.btapp.model.SearchGloballyPost;
import com.logicalwings.btapp.model.SearchItemPost;
import com.logicalwings.btapp.model.SendContactUs;
import com.logicalwings.btapp.model.SendContactUsResponse;
import com.logicalwings.btapp.model.User;
import com.logicalwings.btapp.utils.AppConstants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Pandit on 05/09/2018.
 */

public interface ApiInterface {

    @POST(AppConstants.USER_LOGIN)
    Call<LoginResponce> userLogin(@Body User user);

    @POST(AppConstants.USER_LOGIN_PASSWORD)
    Call<LoginResponce> userLoginPassword(@Body User user);

    @POST(AppConstants.USER_SEND_VERIFICATION)
    Call<LoginResponce> userSendVerify(@Body User user);

    @POST(AppConstants.USER_REGISTRATION)
    Call<MainResponce> apiUserRegistration(@Body User user);

    @POST(AppConstants.RESET_PASSWORD)
    Call<LoginResponce> apiResetPassword(@Body User user, @Header("Authorization") String token);

    @GET(AppConstants.GET_CATEGORY)
    Call<ListCategoryResponse> apiGetCategory(@Header("Authorization") String token);

    @POST(AppConstants.GET_ITEM_GROUP)
    Call<ListItemGroupResponse> apiGetItemGroup(@Body ItemGroupPost itemGroupPost, @Header("Authorization") String token);

    @POST(AppConstants.GET_ITEM_SIZE)
    Call<ListItemSizeResponse> apiGetItemSize(@Body ItemSizePost itemSizePost, @Header("Authorization") String token);

    @POST(AppConstants.GET_ITEM_PATTERN)
    Call<ListItemPatternResponse> apiGetItemPattern(@Body ItemPatternPost itemPatternPost, @Header("Authorization") String token);

    @POST(AppConstants.GET_SEARCH_ITEMS)
    Call<ListSearchItemsResponse> apiGetSearchItems(@Body SearchItemPost searchItemPost, @Header("Authorization") String token);

    @POST(AppConstants.GET_SEARCH_ITEMS_GLOBALLY)
    Call<ListSearchItemsResponse> apiGetSearchItemsGlobally(@Body SearchGloballyPost searchGloballyPost, @Header("Authorization") String token);

    @GET(AppConstants.GET_CONTACTUS_DETAILS)
    Call<ListContactusResponse> apiGetContactUsDetails(@Header("Authorization") String token);

    @POST(AppConstants.GET_CART_ITEM_DETAILS)
    Call<ListCartResponse> apiGetCartDetails(@Header("Authorization") String token, @Body ListCartPost listCartPosts);

    @POST(AppConstants.MAKE_ITEM_FAVOURITE)
    Call<FavouriteResponse> apiMakeFavourite(@Body ProductItem productItem, @Header("Authorization") String token);

    @GET(AppConstants.GET_ITEM_FAVOURITE)
    Call<ListGetFavouriteResponse> apiGetFavourite(@Header("Authorization") String token);

    @POST(AppConstants.CREATE_ORDER)
    Call<OrderResponse> apiCreateOrder(@Header("Authorization") String token, @Body ListCartCreateOrderPost listCartCreateOrderPost);

    @GET(AppConstants.GET_ORDERS)
    Call<ListGetOrdersResponse> apiGetOrders(@Header("Authorization") String token);

    @POST(AppConstants.SEND_CONTACTUS_DETAILS)
    Call<SendContactUsResponse> apiSendContactDetails(@Body SendContactUs sendContactUs, @Header("Authorization") String token);

    @GET(AppConstants.GET_ORDER_DETAILS)
    Call<ListGetOrderDetailResponse> apiGetOrderDetails(@Header("Authorization") String token, @Query("SapOrderNo") String sapOrderNo);
}
