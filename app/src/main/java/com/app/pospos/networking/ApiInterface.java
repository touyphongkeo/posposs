package com.app.pospos.networking;
import com.app.pospos.Constant;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Cook;
import com.app.pospos.model.Customer;
import com.app.pospos.model.Login;
import com.app.pospos.model.OrderDetails;
import com.app.pospos.model.Product;
import com.app.pospos.model.Sale;
import com.app.pospos.model.Status;
import com.app.pospos.model.Table;
import com.app.pospos.model.office;
import com.app.pospos.model.rate;
import com.app.pospos.model.totall;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    @POST("get_status.php")
    Call<List<Status>> getStatus();


    @POST("get_cook.php")
    Call<List<Cook>> getCook();



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


    @GET("select_product.php")
    Call<List<Product>> getSelectID(
            @Query(Constant.PRODUCT_ID) String productId

    );





    @FormUrlEncoded
    @POST("update_category.php")
    Call<Catgory> updateCategory(
            @Field(Constant.CATEGORY_ID) String category_id,
            @Field(Constant.CATEGORY_NAME) String category_name);

    @FormUrlEncoded
    @POST("add_category.php")
    Call<Catgory> addCategory(
            @Field(Constant.CATEGORY_NAME) String category_name
    );


    @POST("get_office.php")
    Call<List<office>> get_office();

    @GET("order_details_by_invoice.php")
    Call<List<OrderDetails>> OrderDetailsByInvoice(
            @Query(Constant.TBNAME) String tbname
    );



    @GET("get_sumtotall.php")
    Call<List<totall>> gettotall(
            @Query(Constant.TBNAME) String tbname
    );


    @GET("get_rate.php")
    Call<List<rate>> get_rate();

    @FormUrlEncoded
    @POST("add_product.php")
    Call<Product> addProduct(
            @Field(Constant.BARCODE) String barcode,
            @Field(Constant.PRODUCT_NAME) String product_name,
            @Field(Constant.CATEGORY_ID) String category_id,
            @Field(Constant.BPRICE) String bprice,
            @Field(Constant.PRICE) String price,
            @Field(Constant.QTY) String qty,
            @Field(Constant.SIZE) String size,
            @Field(Constant.CUST_QTY) String cut_qty,
            @Field(Constant.COOK) String cook

    );


    //for upload image and info
    @Multipart
    @POST("addproduct.php")
    Call<Product> addProducts(@Part MultipartBody.Part file,
                             @Part(Constant.KEY_FILE) RequestBody name,
                             @Part(Constant.BARCODE) RequestBody barcode,
                             @Part(Constant.PRODUCT_NAME) RequestBody product_name,
                             @Part(Constant.CATEGORY_ID) RequestBody category_id,
                             @Part(Constant.BPRICE) RequestBody bprice,
                             @Part(Constant.PRICE) RequestBody price,
                             @Part(Constant.QTY) RequestBody qty,
                             @Part(Constant.SIZE) RequestBody size,
                             @Part(Constant.CUST_QTY) RequestBody cut_qty,
                             @Part(Constant.COOK) RequestBody cook

    );



    @Multipart
    @POST("update_product_without_image.php")
    Call<Product> updateProductWithoutImage(
            @Part(Constant.BARCODE) RequestBody barcode,
            @Part(Constant.PRODUCT_NAME) RequestBody product_name,
            @Part(Constant.CATEGORY_ID) RequestBody categoryId,
            @Part(Constant.BPRICE) RequestBody bprice,
            @Part(Constant.PRICE) RequestBody price,
            @Part(Constant.QTY) RequestBody qty,
            @Part(Constant.SIZE) RequestBody size,
            @Part(Constant.CUST_QTY) RequestBody cut_qty,
            @Part(Constant.COOK) RequestBody cook,
            @Part(Constant.PRODUCT_ID) RequestBody productId);


    @Multipart
    @POST("update_product.php")
    Call<Product> updateProduct(@Part MultipartBody.Part file,
          @Part(Constant.KEY_FILE) RequestBody name,
          @Part(Constant.BARCODE) RequestBody barcode,
          @Part(Constant.PRODUCT_NAME) RequestBody product_name,
          @Part(Constant.CATEGORY_ID) RequestBody category_id,
          @Part(Constant.BPRICE) RequestBody bprice,
          @Part(Constant.PRICE) RequestBody price,
          @Part(Constant.QTY) RequestBody qty,
          @Part(Constant.SIZE) RequestBody size,
          @Part(Constant.CUST_QTY) RequestBody cut_qty,
          @Part(Constant.COOK) RequestBody cook,
          @Part(Constant.PRODUCT_ID) RequestBody product_id);



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


    @FormUrlEncoded
    @POST("delete_product.php")
    Call<Product> deleteProduct(
            @Field(Constant.PRODUCT_ID) String product_id
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