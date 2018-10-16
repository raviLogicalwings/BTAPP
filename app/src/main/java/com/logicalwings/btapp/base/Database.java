package com.logicalwings.btapp.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.logicalwings.btapp.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String KEY_ROWID = "id";
    private static final String CART_PHONE_EMAIl = "phone_email";
    private static final String CART_ITEM_ID = "ItemId";
    private static final String CART_ITEM_NAME = "ItemName";
    private static final String CART_SAP_ITEM_CODE = "SapItemCode";
    private static final String CART_FK_ITEM_GROUP_ID = "fkItemGroupId";
    private static final String CART_SIZE = "Size";
    private static final String CART_PATTERN = "Pattern";
    private static final String CART_SEGMENT_TYPE = "SegmentType";
    private static final String CART_FK_TUBE_ITEM_ID = "fkTubeItemId";
    private static final String CART_FK_FLAP_ITEM_ID = "fkFlapItemId";
    private static final String CART_QTY = "Qty";

    private static final String DATABASE_NAME = "btapp";
    private static final String CART_TABLE = "cartTable";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase ourDb;
    private final Context ourCon;
    private SQL ourHelper;

    private static class SQL extends SQLiteOpenHelper {


        private SQL(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + CART_TABLE + " ( " +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CART_PHONE_EMAIl + " TEXT NOT NULL," +
                    CART_ITEM_ID + " TEXT NOT NULL," +
                    CART_ITEM_NAME + " TEXT NOT NULL," +
                    CART_SAP_ITEM_CODE + " TEXT NOT NULL," +
                    CART_FK_ITEM_GROUP_ID + " TEXT NOT NULL," +
                    CART_SIZE + " TEXT NOT NULL, " +
                    CART_PATTERN + " TEXT NOT NULL, " +
                    CART_SEGMENT_TYPE + " TEXT NOT NULL, " +
                    CART_FK_TUBE_ITEM_ID + " TEXT NOT NULL, " +
                    CART_FK_FLAP_ITEM_ID + " TEXT NOT NULL, " +
                    CART_QTY + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
            onCreate(db);
        }
    }

    public Database(Context c) {
        ourCon = c;
    }

    public Database open() throws SQLException {
        ourHelper = new SQL(ourCon);
        ourDb = ourHelper.getWritableDatabase();
        return this;
    }

    public Database close() {
        ourDb.close();
        return this;
    }

    public void createCartEntry(String cartPhoneEmail, String cartItemId, String cartItemName, String cartSapItemCode, String cartFKItemGroupId, String cartSize, String cartPattern, String cartSegmentType, String cartFkTubeItemId, String cartFkFlapItemId, String cartQuantity) {
        ContentValues cv = new ContentValues();
        cv.put(CART_PHONE_EMAIl, cartPhoneEmail);
        cv.put(CART_ITEM_ID, cartItemId);
        cv.put(CART_ITEM_NAME, cartItemName);
        cv.put(CART_SAP_ITEM_CODE, cartSapItemCode);
        cv.put(CART_FK_ITEM_GROUP_ID, cartFKItemGroupId);
        cv.put(CART_SIZE, cartSize);
        cv.put(CART_PATTERN, cartPattern);
        cv.put(CART_SEGMENT_TYPE, cartSegmentType);
        cv.put(CART_FK_TUBE_ITEM_ID, cartFkTubeItemId);
        cv.put(CART_FK_FLAP_ITEM_ID, cartFkFlapItemId);
        cv.put(CART_QTY, cartQuantity);
        ourDb.insert(CART_TABLE, null, cv);
    }

    public List<Cart> getCartItem() {
        String[] data = new String[]{KEY_ROWID, CART_PHONE_EMAIl, CART_ITEM_ID, CART_ITEM_NAME, CART_SAP_ITEM_CODE, CART_FK_ITEM_GROUP_ID, CART_SIZE, CART_PATTERN, CART_SEGMENT_TYPE, CART_FK_TUBE_ITEM_ID, CART_FK_FLAP_ITEM_ID, CART_QTY};
        Cursor c = ourDb.query(CART_TABLE, data, null, null, null, null, null);

        int rowId = c.getColumnIndex(KEY_ROWID);
        int cartPhoneEmail = c.getColumnIndex(CART_PHONE_EMAIl);
        int cartItemId = c.getColumnIndex(CART_ITEM_ID);
        int cartItemName = c.getColumnIndex(CART_ITEM_NAME);
        int cartSapItemCode = c.getColumnIndex(CART_SAP_ITEM_CODE);
        int cartFKItemGroupId = c.getColumnIndex(CART_FK_ITEM_GROUP_ID);
        int cartSize = c.getColumnIndex(CART_SIZE);
        int cartPattern = c.getColumnIndex(CART_PATTERN);
        int cartSegmentType = c.getColumnIndex(CART_SEGMENT_TYPE);
        int cartFkTubeItemId = c.getColumnIndex(CART_FK_TUBE_ITEM_ID);
        int cartFkFlapItemId = c.getColumnIndex(CART_FK_FLAP_ITEM_ID);
        int cartQuantity = c.getColumnIndex(CART_QTY);

        List<Cart> list = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Cart cart = new Cart(c.getString(rowId), c.getString(cartPhoneEmail), c.getString(cartItemId), c.getString(cartItemName), c.getString(cartSapItemCode), c.getString(cartFKItemGroupId), c.getString(cartSize), c.getString(cartPattern), c.getString(cartSegmentType), c.getString(cartFkTubeItemId), c.getString(cartFkFlapItemId), c.getString(cartQuantity));
            list.add(cart);
        }

        return list;
    }

    public void updateCartQuantity(String cartItemId, String quantity) {
        ContentValues cv = new ContentValues();
        cv.put(CART_QTY, quantity);
        ourDb.update(CART_TABLE, cv, "ItemId=" + cartItemId, null);
    }

    public void deleteCartData(String cartItemId) {
        ourDb.delete(CART_TABLE, "ItemId=" + cartItemId, null);
    }

    public void deleteCart(String cartPhoneEmail) {
        ourDb.delete(CART_TABLE, "phone_email=" + cartPhoneEmail, null);
    }

}
