package com.app.pospos.customer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.model.Customer;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddcustomerActivity extends BaseActivity {
    ProgressDialog loading;
    TextView btn_add;
    EditText customer_name,customer_tel,number_card;
    ImageView img_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        customer_name = findViewById(R.id.customer_name);
        customer_tel = findViewById(R.id.customer_tel);
        number_card = findViewById(R.id.number_card);
        img_back = findViewById(R.id.img_back);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddcustomerActivity.this, CustomerActivity.class);
                startActivity(i);
                finish();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customername = customer_name.getText().toString().trim();
                String customer_tels = customer_tel.getText().toString().trim();
                String number_cards = number_card.getText().toString().trim();
                String customer_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                String customer_time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date()); //HH:mm:ss a
                if (customername.isEmpty()) {
                    customer_name.setError("ກະລຸນາປ້ອນຊື່");
                    customer_name.requestFocus();
                } else {
                    add_customers(customername,customer_tels,customer_date,customer_time,number_cards);
                }
            }
        });
    }


    private void add_customers(String customer_name,String customer_tel,String customer_date,String customer_time,String number_card) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Customer> call = apiInterface.add_customers(customer_name,customer_tel,customer_date,customer_time,number_card);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        loading.dismiss();
                        Toasty.success(AddcustomerActivity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddcustomerActivity.this, CustomerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (value.equals(Constant.KEY_EXISTS)) {
                        loading.dismiss();
                        Toasty.error(AddcustomerActivity.this, getString(R.string.already_exists), Toast.LENGTH_SHORT).show();
                    } else if (value.equals(Constant.KEY_FAILURE)) {
                        loading.dismiss();
                        Toasty.error(AddcustomerActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        loading.dismiss();
                        Toasty.error(AddcustomerActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                      }
                } else {
                    loading.dismiss();
                    Log.d("Error", "Error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Customer> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(AddcustomerActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
