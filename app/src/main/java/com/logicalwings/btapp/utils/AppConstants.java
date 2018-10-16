package com.logicalwings.btapp.utils;

public class AppConstants {
    public static final int SPLASH_TIME_OUT = 5000;
    public static final int ERROR_CODE = 401;
    public static final boolean DEBUG = true;
    public static final String BASE_URL = "http://35.231.248.33/bharattyresapi/";
    public static final String USER_LOGIN = "login/verifyCustomer";
    public static final String USER_SET_PASSWORD = "login/setPassword";
    public static final String USER_SEND_VERIFICATION = "login/sendVerification";
    public static final String USER_LOGIN_PASSWORD = "login/login";
    public static final String USER_REGISTRATION = "login/setPassword";
    public static final String RESET_PASSWORD = "login/changePassword";
    public static final String GET_CATEGORY = "category/getCategory";
    public static final String GET_ITEM_GROUP = "items/itemGroup";
    public static final String GET_ITEM_SIZE = "items/itemSize";
    public static final String GET_ITEM_PATTERN = "items/itemPattern";
    public static final String GET_SEARCH_ITEMS = "items/searchItems";
    public static final String GET_CART_ITEM_DETAILS = "items/getItemDetails";
    public static final String GET_SEARCH_ITEMS_GLOBALLY = "items/searchItemsGlobally";
    public static final String CREATE_ORDER = "order/createOrder";
    public static final String GET_ORDERS = "order/getOrders";
    public static final String GET_ORDER_DETAILS = "order/getOrderDetails";
    public static final String MAKE_ITEM_FAVOURITE = "favourite/makeItemfavourite";
    public static final String GET_ITEM_FAVOURITE = "favourite/getFavouriteItems";
    public static final String GET_CONTACTUS_DETAILS= "contactus/getContactusdetails";
    public static final String SEND_CONTACTUS_DETAILS= "contactus/sendContactusEmail";
    public static final String PREFS_FILE_NAME_PARAM = "BTAPPPrefFile";

    //Retrofit Constant
    public static final int RESULT_SUCCESSFUL = 1;
    public static final int RESULT_FAIL = 0;
//    public static final long REQUEST_TIMEOUT = 500;
    public static final long CONNECTION_TIMEOUT = 200;
    public static final long READ_TIMEOUT = 200;

    //Retrofit response
    public static final String USER_NOT_EXIST = "User doesn't exist";
    public static final String USER_EXIST_PASSWORD_NOT_SET = "Not verified yet";
    public static final String DATA_NOT_FOUND= "Request data not found";

    public static final String TOKEN = "token";
    public static final String CUSTOMER_ID = "customerId";

    public static final String IS_LOGIN="isLogin";

    public static final String SAP_CUSTOMER_NAME = "SapCustomerName";
    public static final String EMAIl = "Email";
    public static final String MOBILE_NO = "MobileNo";
    public static final String PASSWORD = "pasword";

    //sharedpreference
    public static final String PREF_NAME = "BTAPP";
    public static final String CART_LIST = "cartList";

    public static final String NO_INTERNET = "Failed to connect to /35.231.248.33:80";
    public static final String TIMEOUT_ERROR = "timeout";
}