package com.app.pospos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.pospos.Constant;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {


        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }




    //get order type data
    public ArrayList<HashMap<String, String>> getPaymentMethod() {
        ArrayList<HashMap<String, String>> paymentMethod = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM payment_method ORDER BY payment_method_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("payment_method_id", cursor.getString(0));
                map.put("payment_method_name", cursor.getString(1));
                paymentMethod.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return paymentMethod;
    }





    public ArrayList<HashMap<String, String>> getOrderType() {
        ArrayList<HashMap<String, String>> orderType = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM order_type ORDER BY order_type_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("order_type_id", cursor.getString(0));
                map.put("order_type_name", cursor.getString(1));
                orderType.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return orderType;
    }






    //get cart item count
    public int getCartItemCount() {
        Cursor cursor = database.rawQuery("SELECT * FROM tbsale_save_data", null);
        int itemCount = cursor.getCount();
        cursor.close();
        database.close();
        return itemCount;
    }


    //delete product from cart
    public void updateProductQty(String id, String qty) {
        ContentValues values = new ContentValues();
        values.put("sale_qty", qty);
        database.update("tbsale_save_data", values, "Id=?", new String[]{id});

    }




    //get product name
    public String getCurrency() {
        String currency = "n/a";
        Cursor cursor = database.rawQuery("SELECT * FROM shop", null);
        if (cursor.moveToFirst()) {
            do {
                currency = cursor.getString(5);
            } while (cursor.moveToNext());
    }
        cursor.close();
        database.close();
        return currency;
    }


    //calculate total price of product
    public double getTotalPrice() {
        double totalPrice = 0;
        Cursor cursor = database.rawQuery("SELECT * FROM tbsale_save_data", null);
        if (cursor.moveToFirst()) {
            do {
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("sale_price")));
                int qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex("sale_qty")));
                double subTotal = price * qty;
                totalPrice = totalPrice + subTotal;
            } while (cursor.moveToNext());
        } else {
            totalPrice = 0;
        }
        cursor.close();
        database.close();
        return totalPrice;
    }


    public String getTable(){
        String table = "n/a";
        Cursor cursor = database.rawQuery("SELECT * FROM tbsale_save_data", null);
        if (cursor.moveToFirst()) {
            do {
                table = String.valueOf(cursor.getString(cursor.getColumnIndex("tbname")));
            } while (cursor.moveToNext());
        } else {

        }
        cursor.close();
        database.close();
        return table;
    }



    //get shop information
    public ArrayList<HashMap<String, String>> getShopInformation() {
        ArrayList<HashMap<String, String>> shopInfo = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM shop", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();


                map.put("shop_name", cursor.getString(1));
                map.put("shop_contact", cursor.getString(2));
                map.put("shop_email", cursor.getString(3));
                map.put("shop_address", cursor.getString(4));
                map.put("shop_currency", cursor.getString(5));
                map.put("tax", cursor.getString(6));


                shopInfo.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return shopInfo;
    }



    //Add product into cart
    public int addToCart(String sale_bill,String sale_date,String sale_table,String product_id,String sale_name,String sale_price,String sale_qty,String sale_status,String edit_sale, String username, String Image, String cut_qty) {
        Cursor result = database.rawQuery("SELECT * FROM tbsale_save_data WHERE sale_proid='" + product_id + "'", null);
        if (result.getCount() >= 1) {
            return 2;
        } else {
            ContentValues values = new ContentValues();
            values.put(Constant.SALE_BILL,sale_bill);
            values.put(Constant.SALE_DTE,sale_date);
            values.put(Constant.TBNAME,sale_table);
            values.put(Constant.SALE_PROID,product_id);
            values.put(Constant.SALE_NAME,sale_name);
            values.put(Constant.SALE_PRICE,sale_price);
            values.put(Constant.SALE_QTY,sale_qty);
            values.put(Constant.SALE_STATUS,sale_status);
            values.put(Constant.EDIT_SALE,edit_sale);
            values.put(Constant.USERNAME,username);
            values.put(Constant.IMG_URL,Image);
            values.put(Constant.CUST_QTY,cut_qty);
            long check = database.insert(Constant.tbsale_save_data, null, values);
            result.close();
            database.close();
            //if data insert success, its return 1, if failed return -1
            if (check == -1) {
                return -1;
            } else {
                return 1;
            }
        }
    }


    public int addToCart2(String sale_bill,String sale_date,String product_id,String Tbname,String pro_name,String sale_price,String sale_qty,String Image,String cut_qty,String optionss) {
        Cursor result = database.rawQuery("SELECT * FROM tbsale_save_data WHERE sale_proid='" + product_id + "'", null);
        if (result.getCount() >= 1) {
            return 2;
        } else {
            ContentValues values = new ContentValues();
            values.put(Constant.SALE_BILL,sale_bill);
            values.put(Constant.SALE_DTE,sale_date);
            values.put(Constant.TBNAME,Tbname);
            values.put(Constant.SALE_PROID,product_id);
            values.put(Constant.SALE_NAME,pro_name);
            values.put(Constant.SALE_PRICE,sale_price);
            values.put(Constant.SALE_QTY,sale_qty);
            values.put(Constant.SALE_STATUS,1);
            values.put(Constant.EDIT_SALE,1);
        //    values.put(Constant.USERNAME,username);
            values.put(Constant.IMG_URL,Image);
            values.put(Constant.CUST_QTY,cut_qty);
            values.put(Constant.Options,optionss);
            long check = database.insert(Constant.tbsale_save_data, null, values);
            result.close();
            database.close();
            //if data insert success, its return 1, if failed return -1
            if (check == -1) {
                return -1;
            } else {
                return 1;
            }
        }
    }







    //insert expense
    public boolean addUser(String UserID,String Name, String images, String email) {
        ContentValues values = new ContentValues();
        values.put("UserID", UserID);
        values.put("Name", Name);
        values.put("images", images);
        values.put("email", email);
        long check = database.insert("User", null, values);
        database.close();
        if (check == -1) {
            return false;
        } else {
            return true;
        }
    }

    //get product data
    public ArrayList<HashMap<String, String>> getUser() {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM User", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("UserID", cursor.getString(1));
                map.put("Name", cursor.getString(2));
                map.put("images", cursor.getString(3));
                map.put("email", cursor.getString(4));
                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return product;

    }



    public ArrayList<HashMap<String, String>> getCartProduct() {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM tbsale_save_data", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.ID, cursor.getString(cursor.getColumnIndex("Id")));
                map.put(Constant.SALE_BILL, cursor.getString(cursor.getColumnIndex("sale_bill")));
                map.put(Constant.SALE_DTE, cursor.getString(cursor.getColumnIndex("sale_date")));
                map.put(Constant.TBNAME, cursor.getString(cursor.getColumnIndex("tbname")));
                map.put(Constant.SALE_PROID, cursor.getString(cursor.getColumnIndex("sale_proid")));
                map.put(Constant.SALE_NAME, cursor.getString(cursor.getColumnIndex("sale_name")));
                map.put(Constant.SALE_PRICE, cursor.getString(cursor.getColumnIndex("sale_price")));
                map.put(Constant.SALE_QTY, cursor.getString(cursor.getColumnIndex("sale_qty")));
                map.put(Constant.SALE_STATUS, cursor.getString(cursor.getColumnIndex("sale_status")));
                map.put(Constant.EDIT_SALE, cursor.getString(cursor.getColumnIndex("edit_sale")));
                map.put(Constant.IMG_URL, cursor.getString(cursor.getColumnIndex("img_url")));
                map.put(Constant.CUST_QTY, cursor.getString(cursor.getColumnIndex("cut_qty")));
              //  map.put(Constant.Options, cursor.getString(cursor.getColumnIndex("options")));
                product.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return product;
    }


    //empty cart
    public void emptyCart() {

        database.delete(Constant.tbsale_save_data, null, null);
        database.close();
    }






    public boolean deleteProductFromCart(String id) {
        long check = database.delete(Constant.tbsale_save_data, "Id=?", new String[]{id});
        database.close();
        if (check == 1) {
            return true;
        } else {
            return false;
        }

    }





    //get product category data
    public ArrayList<HashMap<String, String>> getProductCategory() {
        ArrayList<HashMap<String, String>> productCategory = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM tbsale_save_data", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("Id", cursor.getString(0));
                map.put("sale_bill", cursor.getString(1));
                map.put("sale_date", cursor.getString(2));
                map.put("sale_table", cursor.getString(3));
                map.put("product_id", cursor.getString(4));
                map.put("sale_name", cursor.getString(5));
                map.put("sale_price", cursor.getString(6));
                map.put("sale_qty", cursor.getString(7));
                map.put("sale_status", cursor.getString(8));
                map.put("edit_sale", cursor.getString(9));
                map.put("username", cursor.getString(10));
                map.put("img_url", cursor.getString(11));
                map.put("options", cursor.getString(13));

                productCategory.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return productCategory;
    }



    //get product payment method
    public ArrayList<HashMap<String, String>> searchPaymentMethod(String s) {
        ArrayList<HashMap<String, String>> paymentMethod = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM payment_method WHERE payment_method_name LIKE '%" + s + "%' ORDER BY payment_method_id DESC ", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();


             //   map.put(Constant.PAYMENT_METHOD_ID, cursor.getString(0));
           //     map.put(Constant.PAYMENT_METHOD_NAME, cursor.getString(1));

                paymentMethod.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return paymentMethod;
    }





    //update employee
   /* public boolean UpdateOfsiion(String category_id,String category_name) {
        long check = 1;
        try {
            Connection con = DB_CON.CONN();
            if (con == null) {
            } else {
                String query = "update AP_Groups set grp_nm_l=N'"+category_name+"' where grp_id="+category_id+"";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                check = 1;
            }
            con.close();
        } catch (Exception ex) {
        }
        return check != -1;
    }*/



    public void UpdateOfsiion(String category_id, String options) {
        ContentValues values = new ContentValues();
        values.put("options", options);
        database.update("tbsale_save_data", values, "Id=?", new String[]{category_id});

    }


    //get product supplier data
    public ArrayList<HashMap<String, String>> getProductSupplier() {
        ArrayList<HashMap<String, String>> productSuppliers = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM catgory", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                map.put("cat_id", cursor.getString(0));
                map.put("cat_name", cursor.getString(1));
                productSuppliers.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return productSuppliers;
    }



    //get product supplier data
    public ArrayList<HashMap<String, String>> getPay() {
        ArrayList<HashMap<String, String>> productSuppliers = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM appay", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                map.put("p_id", cursor.getString(0));
                map.put("pay", cursor.getString(1));
                productSuppliers.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return productSuppliers;
    }





    //get product supplier data
    public ArrayList<HashMap<String, String>> getWeightUnit() {
        ArrayList<HashMap<String, String>> productWeightUnit = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM product_weight", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();


                map.put("weight_id", cursor.getString(0));
                map.put("weight_unit", cursor.getString(1));

                productWeightUnit.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return productWeightUnit;
    }

    //get product data
    public ArrayList<HashMap<String, String>> searchExpense(String s) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM expense WHERE expense_name LIKE '%" + s + "%' ORDER BY expense_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                map.put("expense_id", cursor.getString(cursor.getColumnIndex("expense_id")));
                map.put("expense_name", cursor.getString(cursor.getColumnIndex("expense_name")));
                map.put("expense_note", cursor.getString(cursor.getColumnIndex("expense_note")));
                map.put("expense_amount", cursor.getString(cursor.getColumnIndex("expense_amount")));
                map.put("expense_date", cursor.getString(cursor.getColumnIndex("expense_date")));
                map.put("expense_time", cursor.getString(cursor.getColumnIndex("expense_time")));


                product.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return product;
    }


    //get product data
    public ArrayList<HashMap<String, String>> getSearchProducts(String s) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM products WHERE product_name LIKE '%" + s + "%' OR product_code LIKE '%" + s + "%' ORDER BY product_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", cursor.getString(0));
                map.put("product_name", cursor.getString(1));
                map.put("product_code", cursor.getString(2));
                map.put("product_category", cursor.getString(3));
                map.put("product_description", cursor.getString(4));

                map.put("product_sell_price", cursor.getString(5));
                map.put("product_supplier", cursor.getString(6));
                map.put("product_image", cursor.getString(7));

                map.put("product_weight_unit_id", cursor.getString(8));
                map.put("product_weight", cursor.getString(9));


                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return product;
    }


    //get suppliers data
    public ArrayList<HashMap<String, String>> getSuppliers() {
        ArrayList<HashMap<String, String>> supplier = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM suppliers ORDER BY suppliers_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();




                supplier.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return supplier;
    }


    //delete customer
    public boolean deleteCustomer(String customerId) {


        long check = database.delete("customers", "customer_id=?", new String[]{customerId});

        database.close();

        if (check == 1) {
            return true;
        } else {
            return false;
        }

    }


    //delete category
    public boolean deleteUser(String id) {
        long check = database.delete("users", "id=?", new String[]{id});

        database.close();

        if (check == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAllUser() {
        long check = database.delete("User", null,null);
        database.close();
        return check == 1;
    }


    //delete category
    public boolean deleteCategory(String categoryId) {


        long check = database.delete("product_category", "category_id=?", new String[]{categoryId});

        database.close();

        if (check == 1) {
            return true;
        } else {
            return false;
        }


    }



}