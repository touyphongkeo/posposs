package com.app.pospos.pos;
import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.pospos.Constant;
import com.app.pospos.HomeActivity;
import com.app.pospos.adapter.Sale2Adapter;
import com.app.pospos.category.CategoryActivity;
import com.app.pospos.category.EditCategoryActivity;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Sale;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.print.PrinterActivity;
import com.app.pospos.table.Mtable2Activity;
import com.app.pospos.utils.BaseActivity;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.List;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart2Activity extends BaseActivity {
    private TextView namess,print,etxt_amount;
    private ImageView img_back;
    private Context context;
    private EditText etxt_customer_search;
    private RecyclerView recyclerView;
    ImageView imgNoProduct;
    ProgressDialog loading;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartorder2);
        getSupportActionBar().setHomeButtonEnabled(true); //ຄຳສັງກັບຄືນ
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//ຄຳສັງກັບຄືນຂອງປຸ້ມກັບ
        getSupportActionBar().hide();

        namess = findViewById(R.id.namess);
        etxt_customer_search = findViewById(R.id.etxt_customer_search);
        img_back = findViewById(R.id.img_back);
        etxt_amount = findViewById(R.id.etxt_amount);
        print = findViewById(R.id.print);
       namess.setText("ລາຍການອາຫານ ");
      //dfdfdff

        etxt_customer_search.setText(Tbname);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cart2Activity.this, Mtable2Activity.class);
                startActivity(i);
                finish();
            }
        });


     /*   print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cart2Activity.this, PrinterActivity.class);
                startActivity(i);
                finish();
            }
        });*/





        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  String txt_table = txt_table1.getText().toString();
                if (txt_table.isEmpty()) {
                    errorMessage();
                }else {*/

                 //   statusAdapter = new ArrayAdapter<String>(PostableActivity.this, android.R.layout.simple_list_item_1);
                //    statusAdapter.addAll(status3);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(Cart2Activity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_touy, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
                    Button dialog_button = dialogView.findViewById(R.id.dialog_button);

                    EditText tpers = dialogView.findViewById(R.id.text_tpre);
                    EditText moneye = dialogView.findViewById(R.id.moneyed);
                    TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
                 //   ListView dialog_list = dialogView.findViewById(R.id.dialog_list);
                    TextView open = dialogView.findViewById(R.id.open);
                    TextView show_number = dialogView.findViewById(R.id.show_number);
                    TextView link = dialogView.findViewById(R.id.link);
                    CheckBox text_non = dialogView.findViewById(R.id.text_non);
                    CheckBox text_nond = dialogView.findViewById(R.id.text_nond);

                    show_number.setText(Tbname);

                    tpers.setEnabled(false);
                    moneye.setEnabled(false);
                    dialog_title.setEnabled(false);

                    text_non.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                tpers.setEnabled(true);
                                tpers.setTextColor(Color.BLACK);
                                moneye.setEnabled(false);
                                dialog_button.setEnabled(true);
                                dialog_button.setTextColor(Color.WHITE);
                        }
                    });

                    text_nond.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moneye.setEnabled(true);
                            moneye.setTextColor(Color.BLACK);
                            tpers.setEnabled(false);
                            dialog_button.setEnabled(true);
                            dialog_button.setTextColor(Color.WHITE);

                        }
                    });


                    dialog_title.setText("ຄ່າຄອມມິດຊັນ");
                  // dialog_list.setVerticalScrollBarEnabled(true);
                  // dialog_list.setAdapter(statusAdapter);
                  /*  dialog_input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                          //  statusAdapter.getFilter().filter(charSequence);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });*/
                    final AlertDialog alertDialog = dialog.create();
                    open.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }});
                       dialog_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tper = tpers.getText().toString();
                            String tmoneydg = moneye.getText().toString();

                            updateptiv(tper,tmoneydg,Tbname);

                        }
                    });
                    alertDialog.show();
                  //  dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   //     @Override
                    //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    /*    final String selectedItem = statusAdapter.getItem(position);
                        String category_id = "0";
                        link.setText(selectedItem);
                        selectedCategoryID = category_id;
                        Log.d("category_id", category_id);*/
                        }
                   // });
                //}
          //  }
        });




        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mSwipeRefreshLayout =findViewById(R.id.swipeToRefresh);
        //set color of swipe refresh
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.recycler_view);

        imgNoProduct = findViewById(R.id.image_no_product);
        print = findViewById(R.id.print);

        getorder(Tbname);

        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView


        recyclerView.setHasFixedSize(true);

        Utils utils=new Utils();


        //swipe refresh listeners



        if (utils.isNetworkAvailable(Cart2Activity.this)) {
            //Load data from server
            getorder(Tbname);
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




        etxt_customer_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("data", s.toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    //search data from server
                    getorder(s.toString());
                } else {
                    getorder("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());
            }
        });
    }//ກັບຄືນ




    private void updateptiv(String tpers,String tmoneydg, String tbname) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Sale> call = apiInterface.updatetiv(tpers,tmoneydg,tbname);
        call.enqueue(new Callback<Sale>() {
            @Override
            public void onResponse(@NonNull Call<Sale> call, @NonNull Response<Sale> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        loading.dismiss();
                        Toasty.success(Cart2Activity.this, R.string.update_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Cart2Activity.this, PrinterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (value.equals(Constant.KEY_FAILURE)) {
                        loading.dismiss();
                        Toasty.error(Cart2Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        loading.dismiss();
                        Toasty.error(Cart2Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Sale> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(Cart2Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }







    public void getorder(String searchText) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Sale>> call;
        call = apiInterface.get_order(searchText);
        call.enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sale>> call, @NonNull Response<List<Sale>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Sale> customerList;
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
                        Sale2Adapter sale2Adapter = new Sale2Adapter(Cart2Activity.this, customerList,etxt_amount);

                        recyclerView.setAdapter(sale2Adapter);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sale>> call, @NonNull Throwable t) {

                Toast.makeText(Cart2Activity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;}
        return super.onOptionsItemSelected(item);
    }
}
