package com.app.pospos.staff;

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
import com.app.pospos.model.Admin;
import com.app.pospos.model.Staff;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;
import com.bumptech.glide.Glide;

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

public class EditstaffActivity extends BaseActivity {
    ProgressDialog loading;
    ArrayAdapter<String> statusAdapter,cookAdapter,categoryAdapter;
    List<String> Names,name_cook,categoryname;
    List<Admin> list_Status;
    TextView btn_add;
    EditText userid,textusername,textuserpass,etxtProductCategory,textemail;
    String mediaPath="", encodedImage = "N/A",selectedID,COOKId,selectCategoryID,user_coded,img_urls,useridd,usernamed,userpassd,emaild,userstatused;
    ImageView img_back,txt_choose_image,image_product;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        userid = findViewById(R.id.userid);
        textusername = findViewById(R.id.username);
        textuserpass = findViewById(R.id.textuserpass);
        textemail = findViewById(R.id.email);
        img_back = findViewById(R.id.img_back);
        txt_choose_image = findViewById(R.id.txt_choose_image);
        image_product = findViewById(R.id.image_product);
        etxtProductCategory = findViewById(R.id.etxtProductCategory);

        user_coded = getIntent().getExtras().getString(Constant.USE_CODE);
        img_urls = getIntent().getExtras().getString(Constant.IMG_URLS);
        useridd = getIntent().getExtras().getString(Constant.SP_EMAIL);
        usernamed = getIntent().getExtras().getString(Constant.USERNAME);
        userpassd = getIntent().getExtras().getString(Constant.USERPASS);
        emaild = getIntent().getExtras().getString(Constant.EMAIL);
        userstatused = getIntent().getExtras().getString(Constant.USERSTATUS);
        userid.setText(useridd);
        textusername.setText(usernamed);
        textuserpass.setText(userpassd);
        textemail.setText(emaild);
        etxtProductCategory.setText(userstatused);

        getStaff(user_coded.toString());

        getItem();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditstaffActivity.this, StaffActivity.class);
                startActivity(i);
                finish();
            }
        });

        txt_choose_image.setOnClickListener(v -> {
            Intent intent = new Intent(EditstaffActivity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });


        image_product.setOnClickListener(v -> {
            Intent intent = new Intent(EditstaffActivity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });



        etxtProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAdapter = new ArrayAdapter<>(EditstaffActivity.this, android.R.layout.simple_list_item_1);
                statusAdapter.addAll(Names);

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditstaffActivity.this);
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



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userids = userid.getText().toString().trim();
                String usernames = textusername.getText().toString().trim();
                String userpasss = textuserpass.getText().toString().trim();
                String emaill = textemail.getText().toString().trim();
                String userstatus = etxtProductCategory.getText().toString().trim();
                if (userids.isEmpty()) {
                    userid.setError("ກະລຸນາປ້ອນຊື່");
                    userid.requestFocus();
                } else {
                    add_update(userids,usernames,userpasss,emaill,userstatus);
                }
            }
        });
    }


    private void  add_update(String userids,String usernamess,String userpasss,String emaills,String userstatuss) {
        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        File file;
        RequestBody requestBody;
        MultipartBody.Part fileToUpload = null;
        RequestBody filename = null;
        // Map is used to multipart the file using okhttp3.RequestBody
        if (mediaPath.equals("")) {
            //code
        } else {
            file = new File(mediaPath);
            requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        }

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userids);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), usernamess);
        RequestBody userpass = RequestBody.create(MediaType.parse("text/plain"), userpasss);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emaills);
        RequestBody userstatus = RequestBody.create(MediaType.parse("text/plain"), userstatuss);
        RequestBody user_code = RequestBody.create(MediaType.parse("text/plain"),user_coded);

        ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Staff> call;
        if (mediaPath.equals("")){
            call = getResponse.update_staff_image(userid,username,userpass,email,userstatus,user_code);
        }else {
            call = getResponse.updateStaff(fileToUpload, filename, userid,username,userpass,email,userstatus,user_code);
        }
        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(@NonNull Call<Staff> call, @NonNull Response<Staff> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loading.dismiss();
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.success(getApplicationContext(), R.string.update_successfully, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditstaffActivity.this, StaffActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if (value.equals(Constant.KEY_FAILURE)) {
                        loading.dismiss();
                        Toasty.error(EditstaffActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        loading.dismiss();
                        Toasty.error(EditstaffActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.dismiss();
                    Log.d("Error",response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Staff> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(EditstaffActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
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



    public void getItem() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Admin>> call;
        call = apiInterface.getAdmin();
        call.enqueue(new Callback<List<Admin>>() {
            @Override
            public void onResponse(@NonNull Call<List<Admin>> call, @NonNull Response<List<Admin>> response) {
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
            public void onFailure(@NonNull Call<List<Admin>> call, @NonNull Throwable t) {
                //write own action
            }
        });
    }



    public void getStaff(String use_code) {
        Log.d("use_code",use_code);
        loading=new ProgressDialog(EditstaffActivity.this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Staff>> call;
        call = apiInterface.getSelect_staff(use_code);
        call.enqueue(new Callback<List<Staff>>() {
            @Override
            public void onResponse(@NonNull Call<List<Staff>> call, @NonNull Response<List<Staff>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Staff> productData;
                    productData = response.body();
                    loading.dismiss();
                    if (productData.isEmpty()) {
                        Toasty.warning(EditstaffActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {







                     //   String image= productData.get(0).getimg_urls();
                        String imageUrl= Constant.PRODUCT_IMAGE_URL+img_urls;
                        if (img_urls != null) {
                            if (img_urls.length() < 3) {
                                image_product.setImageResource(R.drawable.image_placeholder);
                            } else {
                                Glide.with(EditstaffActivity.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.image_placeholder)
                                        .into(image_product);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Staff>> call, @NonNull Throwable t) {
                loading.dismiss();
                Toast.makeText(EditstaffActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }



}
