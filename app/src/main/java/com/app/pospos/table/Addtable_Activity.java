package com.app.pospos.table;

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
import com.app.pospos.category.CategoryActivity;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Table;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.table_list.Tablelist_Activity;
import com.app.pospos.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addtable_Activity extends BaseActivity {
    ProgressDialog loading;
    TextView btn_add;
    EditText table_name;
    ImageView img_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        table_name = findViewById(R.id.table_name);
        img_back = findViewById(R.id.img_back);



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Addtable_Activity.this, Tablelist_Activity.class);
                startActivity(i);
                finish();
            }
        });



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table_names = table_name.getText().toString().trim();
                if (table_names.isEmpty()) {
                    table_name.setError("ກະລຸນາປ້ອນໜວດໝູ");
                    table_name.requestFocus();

                } else {
                    add_table(table_names);

                }
            }
        });


    }


    private void add_table(String tbname) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Table> call = apiInterface.addtable(tbname);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(@NonNull Call<Table> call, @NonNull Response<Table> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        loading.dismiss();
                        Toasty.success(Addtable_Activity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Addtable_Activity.this, Tablelist_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (value.equals(Constant.KEY_EXISTS)) {
                        loading.dismiss();
                        Toasty.error(Addtable_Activity.this, getString(R.string.already_exists), Toast.LENGTH_SHORT).show();
                    } else if (value.equals(Constant.KEY_FAILURE)) {
                        loading.dismiss();
                        Toasty.error(Addtable_Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        loading.dismiss();
                        Toasty.error(Addtable_Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.dismiss();
                    Log.d("Error", "Error");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Table> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(Addtable_Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
