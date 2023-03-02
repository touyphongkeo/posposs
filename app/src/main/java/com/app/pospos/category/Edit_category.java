package com.app.pospos.category;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcategory);
        getSupportActionBar().hide();
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        String email = sp.getString(Constant.SP_EMAIL, "");
        String password = sp.getString(Constant.SP_PASSWORD, "");

        category_id = getIntent().getExtras().getString(Constant.CATEGORY_ID);

        Toast.makeText(this, category_id.toString(), Toast.LENGTH_SHORT).show();
        getCategory(category_id);
    }



    public void getCategory(String category_id) {
        Log.d("category_id",category_id);
        loading=new ProgressDialog(Edit_category.this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<Catgory>> call;
        call = apiInterface.updateCategorys(category_id);
        call.enqueue(new Callback<List<Catgory>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<Catgory>> call, @NonNull retrofit2.Response<List<Catgory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Catgory> userData;
                    userData = response.body();
                    loading.dismiss();
                    if (userData.isEmpty()) {
                        Toasty.warning(Edit_category.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {
                        loading.dismiss();
                        String id = userData.get(0).getID();
                        String categoryId = userData.get(0).getCategory_id();
                        String category_name = userData.get(0).getCategory_name();


                    }
                }
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Catgory>> call, @NonNull Throwable t) {
                Toast.makeText(Edit_category.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }

}
