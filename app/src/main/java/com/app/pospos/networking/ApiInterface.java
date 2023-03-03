package com.app.pospos.networking;
import com.app.pospos.Constant;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Customer;
import com.app.pospos.model.Login;
import com.app.pospos.model.Product;
import com.app.pospos.model.Sale;
import com.app.pospos.model.Table;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    public static final String SEARCH_TEXT = "search_text";
    //for login
    @FormUrlEncoded
    @POST("login.php")
    Call<Login> login(
            @Field(Constant.USERID) String userid,
            @Field(Constant.USERPASS) String userpass);
    //get table data

    @POST("get_table.php")
    Call<List<Table>> get_table(
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    @POST("orders_submit.php")
    Call<String> submitOrders(
            @Body RequestBody ordersData
    );

    @POST("orders_submits.php")
    Call<String> submitOrderss(
            @Body RequestBody ordersData
    );

    @GET("get_table2.php")
    Call<List<Table>> get_table2(
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get ORDER
    @POST("get_order.php")
    Call<List<Sale>> get_order(
            @Query(Constant.SALE_TABLE) String searchText
    );

    //get catgory
    @GET("get_catgory.php")
    Call<List<Catgory>> get_catgory();

    @GET("get_listcategory.php")
    Call<List<Catgory>> getlist_catgory(
            @Query(Constant.SEARCH_TEXT) String searchText
    );

    //get product
    @GET("get_products.php")
    Call<List<Product>> get_products(
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get user
    @GET("getUser.php")
    Call<List<Login>> get_user(
            @Query(Constant.SP_EMAIL) String userid
    );


    //get product data
    @GET("get_product_by_id.php")
    Call<List<Product>> getProductById(
            @Query(Constant.PRODUCT_ID) String productId

    );



    @FormUrlEncoded
    @POST("update_category.php")
    Call<Catgory> update_category(
            @Query(Constant.CATEGORY_ID) String category_id,
            @Query(Constant.CATEGORY_NAME) String category_name
    );

    @FormUrlEncoded
    @POST("add_category.php")
    Call<Catgory> addCategory(
            @Field(Constant.CATEGORY_NAME) String category_name
    );


    @FormUrlEncoded
    @POST("update_table1.php")
    Call<Table> update_table1(
            @Field(Constant.TBNAME) String tbname
    );

    //for product data
    @GET("get_catgoryId.php")
    Call<List<Product>> get_catgoryId(
            @Query(Constant.CATEGORY_ID) String categoryId
    );


    //update table  status =2
    @FormUrlEncoded
    @POST("update_table2.php")
    Call<Table> update_table2(
            @Field(Constant.TBNAME) String tbname
    );

   //delete category
    @FormUrlEncoded
    @POST("delete_category.php")
    Call<Catgory> deleteCategory(
            @Field(Constant.CATEGORY_ID) String category_id
    );




    @GET("select_category.php")
    Call<List<Catgory>> updateCategorys(
            @Query(Constant.CATEGORY_ID) String category_id
    );



    //add sale data to server
    @FormUrlEncoded
    @POST("add_sale.php")
    Call<Sale> addsale(
            @Field(Constant.SALE_BILL) String sale_bill,
            @Field(Constant.SALE_DTE) String sale_date,
            @Field(Constant.SALE_TABLE) String sale_table,
            @Field(Constant.SALE_PROID) String sale_proid,
            @Field(Constant.SALE_NAME) String sale_name,
            @Field(Constant.SALE_PRICE) String sale_price,
            @Field(Constant.SALE_QTY) String sale_qty,
            @Field(Constant.SALE_STATUS) String sale_status,
            @Field(Constant.EDIT_SALE) String edit_sale,
//            @Field(Constant.SENDID) String sendid,
//            @Field(Constant.SENDTIME) String sendtime,
//            @Field(Constant.PRINT) String print,
            @Field(Constant.USERNAME) String username
//            @Field(Constant.STATUSORDER) String statusorder,
//            @Field(Constant.REMARK) String remark
            );




    //update cut_qty
    @FormUrlEncoded
    @POST("sale_cut_qty.php")
    Call<Product> update_cut_qty(
            @Field(Constant.PRODUCT_ID) String product_id,
            @Field(Constant.QTY) String qty

    );



    //for get count id order
    @GET("get_countorder.php")
    Call<List<Sale>> get_countorder(
            @Query(Constant.TBNAME) String tbname
    );

    //get customers data
    @GET("get_customer.php")
    Call<List<Customer>> getCustomers(
            @Query(Constant.SEARCH_TEXT) String searchText

    );

}