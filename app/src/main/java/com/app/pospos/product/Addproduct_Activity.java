package com.app.pospos.product;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.category.CategoryActivity;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Cook;
import com.app.pospos.model.Product;
import com.app.pospos.model.Status;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addproduct_Activity extends BaseActivity {
    ProgressDialog loading;
    TextView btn_add;
    List<Status> list_Status;
    List<Cook> list_COOK;
    List<Catgory> list_category;
    public static EditText etxtProductCode;
    ArrayAdapter<String> statusAdapter,cookAdapter,categoryAdapter;
    String mediaPath="", encodedImage = "N/A",selectedID,COOKId,selectCategoryID;
    EditText category_name,etxtProductCategory,etxt_cook,text_product_name,text_bprice,text_price,text_qty,text_size;
    ImageView img_back,txt_choose_image,image_product,imgScanCode;
    List<String> Names,name_cook,categoryname;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        category_name = findViewById(R.id.category_name);
        img_back = findViewById(R.id.img_back);

        text_product_name = findViewById(R.id.text_product_name);
        text_price = findViewById(R.id.text_price);
        text_size = findViewById(R.id.text_size);

        txt_choose_image = findViewById(R.id.txt_choose_image);
        image_product = findViewById(R.id.image_product);
        etxtProductCode = findViewById(R.id.etxt_product_code);
        text_qty = findViewById(R.id.text_qty);
        text_bprice = findViewById(R.id.text_bprice);
        imgScanCode = findViewById(R.id.img_scan_code);
        etxtProductCategory = findViewById(R.id.etxtProductCategory);
        etxt_cook = findViewById(R.id.etxt_cook);
        getItem();
        getItem2();
        getCategory();




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
                    String tbcode = etxtProductCode.getText().toString().trim();
                    String product_name = text_product_name.getText().toString().trim();
                    String bprice = text_bprice.getText().toString().trim();
                    String price = text_price.getText().toString().trim();
                    String qty = text_qty.getText().toString().trim();
                    String size = text_size.getText().toString().trim();
                 //   addCategory(tbcode,product_name,selectCategoryID,bprice,price,qty,size,selectedID,COOKId);
                addProduct(tbcode,product_name,selectCategoryID,bprice,price,qty,size,selectedID,COOKId);


            }
        });


        etxtProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAdapter = new ArrayAdapter<>(Addproduct_Activity.this, android.R.layout.simple_list_item_1);
                statusAdapter.addAll(Names);

                AlertDialog.Builder dialog = new AlertDialog.Builder(Addproduct_Activity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = dialogView.findViewById(R.id.dialog_list);

                dialogTitle.setText("ເລືອກສະຖານະ");
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(statusAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        statusAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("data", s.toString());
                    }
                });


                final AlertDialog alertDialog = dialog.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();


                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        alertDialog.dismiss();
                        final String selectedItem = statusAdapter.getItem(position);

                        String categoryId = "0";
                        etxtProductCategory.setText(selectedItem);


                     /*   if (selectedItem.equals("close")){
                            etxtCustomerEmail.setEnabled(false);
                            same_as_before_check.setEnabled(false);
                            coffeestrong_check.setEnabled(false);
                            youngcoffee_check.setEnabled(false);
                        }else {
                            etxtCustomerEmail.setEnabled(true);
                            same_as_before_check.setEnabled(true);
                            coffeestrong_check.setEnabled(true);
                            youngcoffee_check.setEnabled(true);
                        }*/




                        for (int i = 0; i < Names.size(); i++) {
                            if (Names.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                categoryId = list_Status.get(i).getID();

                            }
                        }

                        selectedID = categoryId;

                    }
                });
            }
        });
        etxt_cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookAdapter = new ArrayAdapter<>(Addproduct_Activity.this, android.R.layout.simple_list_item_1);
                cookAdapter.addAll(name_cook);

                AlertDialog.Builder dialog = new AlertDialog.Builder(Addproduct_Activity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = dialogView.findViewById(R.id.dialog_list);


                dialogTitle.setText("ເລືອກສົ່ງໄປຄົວ");
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(cookAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        cookAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("data", s.toString());
                    }
                });


                final AlertDialog alertDialog = dialog.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();


                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        alertDialog.dismiss();
                        final String cookItem = cookAdapter.getItem(position);

                        String cook_Id = "0";
                        etxt_cook.setText(cookItem);


                     /*   if (selectedItem.equals("close")){
                            etxtCustomerEmail.setEnabled(false);
                            same_as_before_check.setEnabled(false);
                            coffeestrong_check.setEnabled(false);
                            youngcoffee_check.setEnabled(false);
                        }else {
                            etxtCustomerEmail.setEnabled(true);
                            same_as_before_check.setEnabled(true);
                            coffeestrong_check.setEnabled(true);
                            youngcoffee_check.setEnabled(true);
                        }*/




                        for (int i = 0; i < name_cook.size(); i++) {
                            if (name_cook.get(i).equalsIgnoreCase(cookItem)) {
                                // Get the ID of selected Country
                                cook_Id = list_COOK.get(i).getCID();
                            }
                        }

                        COOKId = cook_Id;

                    }
                });
            }
        });
        category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAdapter = new ArrayAdapter<>(Addproduct_Activity.this, android.R.layout.simple_list_item_1);
                categoryAdapter.addAll(categoryname);

                AlertDialog.Builder dialog = new AlertDialog.Builder(Addproduct_Activity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = dialogView.findViewById(R.id.dialog_list);


                dialogTitle.setText("ເລືອກສົ່ງໄປຄົວ");
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(categoryAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        categoryAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("data", s.toString());
                    }
                });


                final AlertDialog alertDialog = dialog.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();


                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        alertDialog.dismiss();
                        final String categoryItem = categoryAdapter.getItem(position);

                        String cat_Id = "0";
                        category_name.setText(categoryItem);

                        for (int i = 0; i < categoryname.size(); i++) {
                            if (categoryname.get(i).equalsIgnoreCase(categoryItem)) {
                                // Get the ID of selected Country
                                cat_Id = list_category.get(i).getCategory_id();


                            }
                        }

                        selectCategoryID = cat_Id;


                    }
                });
            }
        });

    }


/*
    private void addCategory(String barcode,String product_name,String category_id,String bprice,String price,String qty,String size,String cut_qty,String cook) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Product> call = apiInterface.addProduct(barcode,product_name,category_id,bprice,price,qty,size,cut_qty,cook);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
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
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(Addproduct_Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

    private void  addProduct(String barcodes,String product_names,String category_ids,String bprices,String prices,String qtys,String sizes,String cut_qtys,String cooks) {
        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        if (mediaPath.isEmpty()) {
            Toasty.warning(this, R.string.choose_product_image, Toast.LENGTH_SHORT).show();
            loading.dismiss();
        } else {
            File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            RequestBody barcode = RequestBody.create(MediaType.parse("text/plain"), barcodes);
            RequestBody product_name = RequestBody.create(MediaType.parse("text/plain"), product_names);
            RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), category_ids);
            RequestBody bprice = RequestBody.create(MediaType.parse("text/plain"), bprices);
            RequestBody price = RequestBody.create(MediaType.parse("text/plain"), prices);
            RequestBody qty = RequestBody.create(MediaType.parse("text/plain"), qtys);
            RequestBody size = RequestBody.create(MediaType.parse("text/plain"), sizes);
            RequestBody cut_qty = RequestBody.create(MediaType.parse("text/plain"), cut_qtys);
            RequestBody cook = RequestBody.create(MediaType.parse("text/plain"), cooks);


            ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Product> call = getResponse.addProducts(fileToUpload, filename, barcode, product_name, category_id, bprice, price,qty, size, cut_qty, cook);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        loading.dismiss();
                        String value = response.body().getValue();
                        if (value.equals(Constant.KEY_SUCCESS)) {
                            Toasty.success(getApplicationContext(), R.string.product_successfully_added, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Addproduct_Activity.this, ProductActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
                        Log.d("Error", response.errorBody().toString());
                    }

                }


                @Override
                public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                    loading.dismiss();
                    Log.d("Error! ", t.toString());
                    Toasty.error(Addproduct_Activity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                }
            });

        }
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




    public void getItem() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Status>> call;
        call = apiInterface.getStatus();
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(@NonNull Call<List<Status>> call, @NonNull Response<List<Status>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list_Status = response.body();
                    String ID = list_Status.get(0).getID();
                        Names = new ArrayList<>();
                    for (int i = 0; i < list_Status.size(); i++) {
                        Names.add(list_Status.get(i).getName());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Status>> call, @NonNull Throwable t) {
                //write own action
            }
        });
    }
    public void getItem2() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Cook>> call;
        call = apiInterface.getCook();
        call.enqueue(new Callback<List<Cook>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cook>> call, @NonNull Response<List<Cook>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list_COOK = response.body();
                    String ID = list_COOK.get(0).getCID();
                    name_cook = new ArrayList<>();
                    for (int i = 0; i < list_COOK.size(); i++) {
                        name_cook.add(list_COOK.get(i).getCName());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Cook>> call, @NonNull Throwable t) {
                //write own action
            }
        });
    }

    public void getCategory() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Catgory>> call;
        call = apiInterface.get_catgory();
        call.enqueue(new Callback<List<Catgory>>() {
            @Override
            public void onResponse(@NonNull Call<List<Catgory>> call, @NonNull Response<List<Catgory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list_category = response.body();
                    String ID = list_category.get(0).getCategory_id();
                    categoryname = new ArrayList<>();
                    for (int i = 0; i < list_category.size(); i++) {
                        categoryname.add(list_category.get(i).getCategory_name());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Catgory>> call, @NonNull Throwable t) {
                //write own action
            }
        });
    }

}
