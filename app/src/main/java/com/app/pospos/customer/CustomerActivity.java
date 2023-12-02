package com.app.pospos.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.onlinesmartpos.R;
import com.app.pospos.adapter.CustomerAdapter;
import com.app.pospos.model.Customer;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.setting.SettingActivity;
import com.app.pospos.utils.BaseActivity;
import com.app.pospos.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends BaseActivity {
    private RecyclerView recyclerView;
    ImageView imgNoProduct,img_back,img_cart;
    EditText etxtSearch;
    FloatingActionButton fabAdd;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcustomer);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("ລາຍການລູກຄ້າ");
        getSupportActionBar().hide();
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mSwipeRefreshLayout =findViewById(R.id.swipeToRefresh);
        //set color of swipe refresh
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.recycler_view);

        imgNoProduct = findViewById(R.id.image_no_product);
        etxtSearch = findViewById(R.id.etxt_customer_search);
        fabAdd = findViewById(R.id.fab_add);
        img_back = findViewById(R.id.img_back);
        img_cart = findViewById(R.id.img_cart);

        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView


        recyclerView.setHasFixedSize(true);

        Utils utils=new Utils();



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerActivity.this, SettingActivity.class);
                startActivity(i);
                finish();
            }
        });


        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerActivity.this, AddcustomerActivity.class);
                startActivity(i);
                finish();
            }
        });


        //swipe refresh listeners
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (utils.isNetworkAvailable(CustomerActivity.this)) {
                getCustomerData("");
            } else {
                Toasty.error(CustomerActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
            //after shuffle id done then swife refresh is off
            mSwipeRefreshLayout.setRefreshing(false);
        });


        if (utils.isNetworkAvailable(CustomerActivity.this)) {
            //Load data from server
            getCustomerData("");
        } else {
            recyclerView.setVisibility(View.GONE);
            imgNoProduct.setVisibility(View.VISIBLE);
            imgNoProduct.setImageResource(R.drawable.not_found);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            //Stopping Shimmer Effects
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
                    getCustomerData(s.toString());
                } else {
                    getCustomerData("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());
            }
        });

    }


    public void getCustomerData(String searchText) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Customer>> call;
        call = apiInterface.getCustomers(searchText);

        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Customer>> call, @NonNull Response<List<Customer>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<Customer> customerList;
                    customerList = response.body();


                    if (customerList.isEmpty()) {

                        recyclerView.setVisibility(View.GONE);
                        imgNoProduct.setVisibility(View.VISIBLE);
                        imgNoProduct.setImageResource(R.drawable.not_found);
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);


                    } else {


                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        CustomerAdapter customerAdapter = new CustomerAdapter(CustomerActivity.this, customerList);

                        recyclerView.setAdapter(customerAdapter);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Customer>> call, @NonNull Throwable t) {

                Toast.makeText(CustomerActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
