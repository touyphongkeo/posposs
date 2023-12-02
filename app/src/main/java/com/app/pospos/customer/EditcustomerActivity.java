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

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditcustomerActivity extends BaseActivity {
    ProgressDialog loading;
    TextView btn_add;
    EditText customer_name,textcustomer_tel,number_card;
    ImageView img_back;
    String customer_id,customer_names,customer_tels,number_cards;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        customer_name = findViewById(R.id.customer_name);
        textcustomer_tel = findViewById(R.id.customer_tel);
        number_card = findViewById(R.id.number_card);
        img_back = findViewById(R.id.img_back);

        customer_id = getIntent().getExtras().getString(Constant.CUSTOMER_ID);
        customer_names = getIntent().getExtras().getString(Constant.CUSTOMERNAME);
        customer_tels = getIntent().getExtras().getString(Constant.CUSTOMER_TEL);
        number_cards = getIntent().getExtras().getString(Constant.NUMBER_CARD);
        customer_name.setText(customer_names);
        textcustomer_tel.setText(customer_tels);
        number_card.setText(number_cards);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditcustomerActivity.this, CustomerActivity.class);
                startActivity(i);
                finish();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customername = customer_name.getText().toString().trim();
                String customer_tel = textcustomer_tel.getText().toString().trim();
                String number_cards = number_card.getText().toString().trim();
                if (customername.isEmpty()) {
                    customer_name.setError("ກະລຸນາປ້ອນຊື່");
                    customer_name.requestFocus();
                } else {
                    update_customers(customername,customer_tel,number_cards);
                }
            }
        });
    }


    private void update_customers(String customer_name,String customer_tel,String number_card) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Customer> call = apiInterface.updatecustomerz(customer_name,customer_tel,number_card,customer_id);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        loading.dismiss();
                        Toasty.success(EditcustomerActivity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditcustomerActivity.this, CustomerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (value.equals(Constant.KEY_EXISTS)) {
                        loading.dismiss();
                        Toasty.error(EditcustomerActivity.this, getString(R.string.already_exists), Toast.LENGTH_SHORT).show();
                    } else if (value.equals(Constant.KEY_FAILURE)) {
                        loading.dismiss();
                        Toasty.error(EditcustomerActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        loading.dismiss();
                        Toasty.error(EditcustomerActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
                Toasty.error(EditcustomerActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
