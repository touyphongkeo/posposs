package com.app.pospos.image;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.pospos.Constant;
import com.app.pospos.database.DatabaseAccess;
import com.app.pospos.model.Product;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.pos.Pos2Activity;
import com.app.pospos.pos.PosActivity;
import com.app.pospos.utils.BaseActivity;
import com.app.onlinesmartpos.R;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Get_imageshow extends BaseActivity {
    ImageView imgProduct;
    String productID;
    ProgressDialog loading;
    TextView pro_name,price,qty,namess;

    DecimalFormat f;
    DatabaseAccess databaseAccess;
    ImageView img_back;
    private Context context;
    TextView qtys,txt_saved,txtCount;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("ຂໍ້ມູນສີນຄ້າ");
        f = new DecimalFormat("#,###");
        imgProduct = findViewById(R.id.image_product);
        pro_name = findViewById(R.id.pro_name);
        price = findViewById(R.id.price);
        qty = findViewById(R.id.qty);
        namess = findViewById(R.id.namess);
        img_back = findViewById(R.id.img_back);
        txt_saved = findViewById(R.id.txt_saved);
        txtCount = findViewById(R.id.txt_count);
        getSupportActionBar().hide();



        productID = getIntent().getExtras().getString(Constant.PRODUCT_ID);
        getProductsData(productID);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Get_imageshow.this, Pos2Activity.class);
                startActivity(i);
            }
        });


        txt_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                long date = System.currentTimeMillis();
                SimpleDateFormat sdfs = new SimpleDateFormat("yyyy");
                String dateStrings = sdfs.format(date);
                String sale_bill = dateStrings+"000001";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sale_date = sdf.format(new Date());

                databaseAccess = DatabaseAccess.getInstance(Get_imageshow.this);
                databaseAccess.open();
                int check = databaseAccess.addToCart2(sale_bill,sale_date,productID.toString());
                if (check == 1) {
                    Toasty.success(context, "ສັງສຳເລັດ", Toast.LENGTH_SHORT).show();
                } else if (check == 2) {
                    Toasty.info(context, "ສີນຄ້ານີ້ມີແລ້ວ", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(context, "ຜິດຜາດ", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toasty.error(context, "ຜິດຜາດ"+e, Toast.LENGTH_SHORT).show();
              }
            }
        });
    }




    public void getProductsData(String productId) {
        Log.d("ProductID",productId);
        loading=new ProgressDialog(Get_imageshow.this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Product>> call;
        call = apiInterface.getProductById(productId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productData;
                    productData = response.body();
                    loading.dismiss();
                    if (productData.isEmpty()) {
                        Toasty.warning(Get_imageshow.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {
                        String productName = productData.get(0).getProduct_name();
                        String prices = productData.get(0).getPrice();
                        String qtys = productData.get(0).getQty();
                        String productImage = productData.get(0).getImg_url();
                        String imageUrl= Constant.PRODUCT_IMAGE_URL+productImage;

                        if (productImage != null) {
                            if (productImage.length() < 3) {

                                imgProduct.setImageResource(R.drawable.image_placeholder);
                            } else {


                                Glide.with(Get_imageshow.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.image_placeholder)
                                        .into(imgProduct);
                            }
                        }


                        try {


                            namess.setText(productName);
                            DecimalFormat dfs = new DecimalFormat(
                                    "#,###.00",
                                    new DecimalFormatSymbols(new Locale("pt", "BR")));
                            BigDecimal values = new BigDecimal(prices);
                            price.setText("ລາຄາ: "+f.format(values) +" ກິບ");
                            pro_name.setText(productName+" / "+f.format(values) +" ກິບ");
                            qty.setText("ຈຳນວນໃນສ້າງ: "+qtys);



                        }catch (Exception e){

                        }


                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {

                loading.dismiss();
                Toast.makeText(Get_imageshow.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }







    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
