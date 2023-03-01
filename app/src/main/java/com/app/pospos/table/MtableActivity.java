package com.app.pospos.table;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.app.pospos.adapter.TableAdapter;
import com.app.pospos.model.Table;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MtableActivity extends BaseActivity {
    private RecyclerView recyclerView;
    ImageView imgNoProduct;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText etxtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());

      //  getSupportActionBar().setTitle("ລາຍການໂຕະ"+currentDate);
        getSupportActionBar().setTitle("ລາຍການໂຕະ");

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mSwipeRefreshLayout =findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        recyclerView = findViewById(R.id.recycler_view);
        imgNoProduct = findViewById(R.id.image_no_product);
        etxtSearch = findViewById(R.id.etxtSearch);



//      set a GridLayoutManager with default vertical orientation and 3 number of columns
//      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//      recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
//      recyclerView.setHasFixedSize(true);



        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);


        Utils utils=new Utils();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (utils.isNetworkAvailable(MtableActivity.this)){
                getTable("");
            } else{
                Toasty.error(MtableActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });
        if (utils.isNetworkAvailable(MtableActivity.this)) {
            getTable("");
        }else{
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
                    getTable(s.toString());
                } else {
                    getTable("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());
            }
        });
    }





    public void getTable(String searchText) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Table>> call;
        call = apiInterface.get_table(searchText);
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(@NonNull Call<List<Table>> call, @NonNull Response<List<Table>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Table> customerList;
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
                        TableAdapter customerAdapter = new TableAdapter(MtableActivity.this, customerList);
                        recyclerView.setAdapter(customerAdapter);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Table>> call, @NonNull Throwable t) {
                Toast.makeText(MtableActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
