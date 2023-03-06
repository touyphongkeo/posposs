package com.app.pospos.product;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addproduct_Activity extends BaseActivity {
    ProgressDialog loading;
    TextView btn_add;

    public static EditText etxtProductCode;

    String mediaPath="", encodedImage = "N/A";
    EditText category_name;
    ImageView img_back,txt_choose_image,image_product,imgScanCode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        category_name = findViewById(R.id.category_name);
        img_back = findViewById(R.id.img_back);

        txt_choose_image = findViewById(R.id.txt_choose_image);
        image_product = findViewById(R.id.image_product);
        etxtProductCode = findViewById(R.id.etxt_product_code);
        imgScanCode = findViewById(R.id.img_scan_code);


        imgScanCode.setOnClickListener(v -> {
            Intent intent = new Intent(Addproduct_Activity.this, ScannerViewActivity.class);
            startActivity(intent);
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Addproduct_Activity.this, ProductActivity.class);
                startActivity(i);
                finish();
            }
        });


        txt_choose_image.setOnClickListener(v -> {
            Intent intent = new Intent(Addproduct_Activity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });


        image_product.setOnClickListener(v -> {

            Intent intent = new Intent(Addproduct_Activity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = category_name.getText().toString().trim();
                if (categoryName.isEmpty()) {
                    category_name.setError("ກະລຸນາປ້ອນໜວດໝູ");
                    category_name.requestFocus();

                } else {
                    addCategory(categoryName);

                }
            }
        });


    }


    private void addCategory(String category_name) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Catgory> call = apiInterface.addCategory(category_name);
        call.enqueue(new Callback<Catgory>() {
            @Override
            public void onResponse(@NonNull Call<Catgory> call, @NonNull Response<Catgory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();


                    if (value.equals(Constant.KEY_SUCCESS)) {
                        loading.dismiss();
                        Toasty.success(Addproduct_Activity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Addproduct_Activity.this, CategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (value.equals(Constant.KEY_EXISTS)) {

                        loading.dismiss();

                        Toasty.error(Addproduct_Activity.this, getString(R.string.already_exists), Toast.LENGTH_SHORT).show();


                    } else if (value.equals(Constant.KEY_FAILURE)) {

                        loading.dismiss();

                        Toasty.error(Addproduct_Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        loading.dismiss();
                        Toasty.error(Addproduct_Activity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.dismiss();
                    Log.d("Error", "Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Catgory> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(Addproduct_Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 1213 && resultCode == RESULT_OK && null != data) {
                mediaPath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                Bitmap selectedImage = BitmapFactory.decodeFile(mediaPath);
                image_product.setImageBitmap(selectedImage);
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
        }
    }
}
