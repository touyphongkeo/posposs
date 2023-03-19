package com.app.pospos.pos;

import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.pospos.Constant;
import com.app.pospos.HomeActivity;
import com.app.pospos.adapter.Catgory2Adapter;
import com.app.pospos.adapter.Product2Adapter;
import com.app.pospos.database.DatabaseAccess;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Product;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pos2Activity extends BaseActivity {
    private TextView namess,table,txtNoProducts;
    private ImageView img_back,img_cart;
    private Context context;

    SharedPreferences sp;
    private RecyclerView recyclerView,recycler_views;
    ImageView imgNoProduct;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout swipeToRefreshs;

    public static TextView txtCount;
    DatabaseAccess databaseAccess;

    EditText etxtSearch;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos2);
        getSupportActionBar().setHomeButtonEnabled(true); //ຄຳສັງກັບຄືນ
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//ຄຳສັງກັບຄືນຂອງປຸ້ມກັບ
        getSupportActionBar().hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        namess = findViewById(R.id.namess);
        img_back = findViewById(R.id.img_back);
       // namess.setText("ເລກໂຕະ: "  +Tbname);
        namess.setText("ຂາຍສີນຄ້າ: "  +Tbname);
       // namess.setText("ເລກໂຕະ: ");
        table = findViewById(R.id.table);
        table.setText(SALE_BILL);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        swipeToRefreshs =findViewById(R.id.swipeToRefreshs);
        txtCount = findViewById(R.id.txt_count);
        img_cart = findViewById(R.id.img_cart);
        recyclerView = findViewById(R.id.recycler_view);
        recycler_views = findViewById(R.id.recycler_views);
        imgNoProduct = findViewById(R.id.image_no_product);
        txtNoProducts = findViewById(R.id.txt_no_products);
        etxtSearch = findViewById(R.id.etxtSearch);
        getCatgory();
        databaseAccess = DatabaseAccess.getInstance(Pos2Activity.this);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pos2Activity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });


        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pos2Activity.this, Product2Cart.class);
                startActivity(i);
                finish();
            }
        });



          GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
          recycler_views.setLayoutManager(gridLayoutManager);
          recycler_views.setHasFixedSize(true);


          LinearLayoutManager linerLayoutManager = new LinearLayoutManager(Pos2Activity.this,LinearLayoutManager.HORIZONTAL,false);
          recyclerView.setLayoutManager(linerLayoutManager);
          recyclerView.setHasFixedSize(true);
          Utils utils=new Utils();



        databaseAccess.open();
        int count = databaseAccess.getCartItemCount();
        if (count == 0) {
            txtCount.setVisibility(View.INVISIBLE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(count));
        }

//====================================select product============================================================
        swipeToRefreshs.setOnRefreshListener(() -> {
            if (utils.isNetworkAvailable(Pos2Activity.this)) {
                getProduct("");
            } else{
                Toasty.error(Pos2Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
            swipeToRefreshs.setRefreshing(false);
        });
        if (utils.isNetworkAvailable(Pos2Activity.this)) {
            getProduct("");
        }else {
            recycler_views.setVisibility(View.GONE);
            imgNoProduct.setVisibility(View.VISIBLE);
            imgNoProduct.setImageResource(R.drawable.not_found);
            swipeToRefreshs.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            Toasty.error(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
        }



        etxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("data", s.toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    //search data from server
                    getProduct(s.toString());
                } else {
                    getProduct("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());
            }
        });
    }


    public void getCatgory() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Catgory>> call;
        call = apiInterface.get_catgory();
        call.enqueue(new Callback<List<Catgory>>() {
            @Override
            public void onResponse(@NonNull Call<List<Catgory>> call, @NonNull Response<List<Catgory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Catgory> Catgorylist;
                    Catgorylist = response.body();
                    if (Catgorylist.isEmpty()) {
                        imgNoProduct.setImageResource(R.drawable.not_found);
                    } else {
                        Catgory2Adapter catgory2Adapter = new Catgory2Adapter(Pos2Activity.this, Catgorylist,recycler_views, imgNoProduct, txtNoProducts, mShimmerViewContainer);
                        recyclerView.setAdapter(catgory2Adapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Catgory>> call, @NonNull Throwable t) {
                Toast.makeText(Pos2Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }




    public void getProduct(String searchText) {
        loading=new ProgressDialog(Pos2Activity.this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Product>> call;
        call = apiInterface.get_products(searchText);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> customerList;
                    customerList = response.body();
                    loading.dismiss();
                    if (customerList.isEmpty()) {
                        loading.dismiss();
                        recycler_views.setVisibility(View.GONE);
                        imgNoProduct.setVisibility(View.VISIBLE);
                        imgNoProduct.setImageResource(R.drawable.not_found);
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    } else {
                        loading.dismiss();
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        recycler_views.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        Product2Adapter product2Adapter = new Product2Adapter(Pos2Activity.this, customerList);
                        recycler_views.setAdapter(product2Adapter);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(Pos2Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });






        //get count order id



    }//ກັບຄືນ


    //login method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;}
        return super.onOptionsItemSelected(item);
    }
}
