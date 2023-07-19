package com.app.pospos.table;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class EdittableActivity extends BaseActivity {
    ProgressDialog loading;
    TextView txtEdit, txtUpdate;
    EditText etCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("ແກ້ໄຂເລກໂຕະ");

        txtEdit = findViewById(R.id.txt_edit);
        txtUpdate = findViewById(R.id.txt_update);
        etCategoryName = findViewById(R.id.et_category_name);

        txtUpdate.setVisibility(View.INVISIBLE);

        String id = getIntent().getExtras().getString("Id");
        String tbname = getIntent().getExtras().getString("tbname");

        etCategoryName.setText(tbname);

        etCategoryName.setEnabled(false);

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCategoryName.setEnabled(true);
                etCategoryName.setTextColor(Color.RED);
                txtUpdate.setVisibility(View.VISIBLE);
                txtEdit.setVisibility(View.GONE);
            }
        });


        txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   String categoryName = etCategoryName.getText().toString().trim();
                   if (categoryName.isEmpty()) {
                       etCategoryName.setError(getString(R.string.category_name));
                       etCategoryName.requestFocus();
                   } else {
                       updateCategory(id.toString(), categoryName);
                   }
               }catch (Exception e){

               }

            }
        });


    }

    private void updateCategory(String Id, String tbname) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Table> call = apiInterface.updatetable(Id, tbname);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(@NonNull Call<Table> call, @NonNull Response<Table> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        loading.dismiss();
                        Toasty.success(EdittableActivity.this, R.string.update_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EdittableActivity.this, Tablelist_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (value.equals(Constant.KEY_FAILURE)) {

                        loading.dismiss();

                        Toasty.error(EdittableActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        loading.dismiss();
                        Toasty.error(EdittableActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Table> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(EdittableActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
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