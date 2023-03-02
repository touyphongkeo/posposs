package com.app.pospos.category;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.HomeActivity;
import com.app.pospos.image.Get_imageshow;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Login;
import com.app.pospos.model.Product;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;
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

public class Edit_category extends BaseActivity {
    String category_id;
    ProgressDialog loading;
    SharedPreferences sp;

    EditText category_name;

    ImageView img_back,redfart,add_data;

    SharedPreferences.Editor editor;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcategory);
        getSupportActionBar().hide();
        category_id = getIntent().getExtras().getString(Constant.CATEGORY_ID);
        category_name = findViewById(R.id.category_name);
        redfart = findViewById(R.id.redfart);
        img_back = findViewById(R.id.img_back);
        add_data = findViewById(R.id.add_data);
        getCategory(category_id.toString());
        category_name.setEnabled(false);


        redfart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redfart.setVisibility(View.GONE);
                add_data.setVisibility(View.VISIBLE);
                category_name.setEnabled(true);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Edit_category.this,CategoryActivity.class);
                startActivity(i);
                finish();
            }
        });
    }



    public void getCategory(String category_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Catgory>> call;
        call = apiInterface.updateCategorys(category_id);
        call.enqueue(new Callback<List<Catgory>>() {
            @Override
            public void onResponse(@NonNull Call<List<Catgory>> call, @NonNull Response<List<Catgory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Catgory> productData;
                    productData = response.body();
                        String id = productData.get(0).getID();
                        String categoryId = productData.get(0).getCategory_id();
                        String categoryName = productData.get(0).getCategory_name();
                        category_name.setText(categoryName);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Catgory>> call, @NonNull Throwable t) {

                loading.dismiss();
                Toast.makeText(Edit_category.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });

    }
}
