package com.example.tyminiproject.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.tyminiproject.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "MessAppDB.db";

    private static final int DB_VERSION = 1;
    private static final String TAG = "Database";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductID", "ProductName", "Quantity", "Price", "Discount", "MessName"};
        String TABLE_NAME = "OrderDetail";

        qb.setTables(TABLE_NAME);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {

                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductID")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount")),
                        cursor.getString(cursor.getColumnIndex("MessName"))));


                Log.e(TAG, "inside getCarts : ProductName: " + cursor.getColumnIndex("ProductName") + cursor.getString(1));
                Log.e(TAG, "inside getCarts : Quantity: " + cursor.getColumnIndex("Quantity") + cursor.getString(2));
                Log.e(TAG, "inside getCarts : Price :" + cursor.getColumnIndex("Price") + cursor.getString(3));
                Log.e(TAG, "inside getCarts : Discount:" + cursor.getColumnIndex("Discount") + cursor.getString(4));
                Log.e(TAG, "inside getCarts : MessName:" + cursor.getColumnIndex("MessName") + cursor.getString(5));
            } while (cursor.moveToNext());
        }
        return result;
    }


    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductID,ProductName,Quantity,Price,Discount,MessName) VALUES ('%s','%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getMessName()
        );

        Log.e(TAG, "inside addToCart : INSERTING");
        Log.e(TAG, "inside addToCart : INSERTING");
        db.execSQL(query);

        Log.e(TAG, "inside addToCart : INSERTED SUCCESSFULLY ");
        Log.e(TAG, "inside addToCart : INSERTED SUCCESSFULLY ");
        Log.e(TAG, "inside addToCart : INSERTED SUCCESSFULLY ");
        Log.e(TAG, "inside addToCart : INSERTED SUCCESSFULLY ");
        Log.e(TAG, "inside addToCart : INSERTED SUCCESSFULLY ");

    }


    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }
}
