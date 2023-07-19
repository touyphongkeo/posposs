package com.app.pospos.image;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.app.pospos.pos.ProductCart;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.app.pospos.ClassLibs.Tbname;
public class Get_imageshow extends BaseActivity {
    ImageView imgProduct;
    String productID;
    ProgressDialog loading;
    TextView pro_name,price,qty,namess,product_id,barcode,bprice,category_name;
    DecimalFormat f;
    DatabaseAccess databaseAccess;
    String productName,prices;

    EditText txt_qty;
    ImageView img_back,img_cart;
    private Context context;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String username ="";
    Integer cust =0;

    String productImage,custqty;
    public static TextView txtCount;
    TextView qtys,txt_saved;
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
        txt_qty = findViewById(R.id.txt_qty);
        txtCount = findViewById(R.id.txt_count);
        product_id = findViewById(R.id.product_id);
        barcode = findViewById(R.id.barcode);
        bprice = findViewById(R.id.bprice);
        category_name = findViewById(R.id.category_name);
        img_cart = findViewById(R.id.img_cart);
        getSupportActionBar().hide();

/*
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        username = sp.getString(Constant.SP_EMAIL, "");
*/




        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Get_imageshow.this, ProductCart.class);
                startActivity(i);
                finish();
            }
        });

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
                insert_data();
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
                        productName = productData.get(0).getProduct_name();
                        prices = productData.get(0).getPrice();
                        custqty = productData.get(0).getCut_qty();
                        String qtys = productData.get(0).getQty();
                        String product_ids = productData.get(0).getProduct_id();
                        String barcodes = productData.get(0).getBarcode();
                        String bprices = productData.get(0).getBprice();
                        String category_names = productData.get(0).getCategory_name();


                        productImage = productData.get(0).getImg_url();
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
                            price.setText("ລາຄາຂາຍ: "+f.format(values) +" ກິບ");
                            pro_name.setText(productName+" / "+f.format(values) +" ກິບ");
                            qty.setText("ຈຳນວນໃນສ້າງ: "+qtys+" ລາຍການ");
                            product_id.setText("ລະຫັດສີນຄ້າ: "+product_ids);
                            barcode.setText("ບາໂຄ້ດ: "+barcodes);
                            bprice.setText("ລາຄາຊື້: "+bprices);
                            category_name.setText("ໜວດໝູ: "+category_names);
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




    private void insert_data() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy");
        String dateStrings = sdfs.format(date);
        String sale_bill = dateStrings+"000001";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sale_date = sdf.format(new Date());
        String txt_qtys = txt_qty.getText().toString();
        databaseAccess = DatabaseAccess.getInstance(Get_imageshow.this);
        databaseAccess.open();
        int check = databaseAccess.addToCart2(sale_bill,sale_date,productID.toString(),Tbname,productName,prices,txt_qtys,productImage,custqty);
        databaseAccess.open();
        int count=databaseAccess.getCartItemCount();
        if (count==0) {
            Get_imageshow.txtCount.setVisibility(View.INVISIBLE);
        } else{
            Get_imageshow. txtCount.setVisibility(View.VISIBLE);
            Get_imageshow.txtCount.setText(String.valueOf(count));
        }
        if (check == 1) {
            Toast.makeText(Get_imageshow.this, "Order Successfully Done", Toast.LENGTH_SHORT).show();
        } else if (check == 2) {
            Toast.makeText(Get_imageshow.this, "In Taka already", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(Get_imageshow.this, "Error", Toast.LENGTH_SHORT).show();
        }
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
