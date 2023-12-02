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

public class AddstaffActivity extends BaseActivity {
    ProgressDialog loading;
    ArrayAdapter<String> statusAdapter,cookAdapter,categoryAdapter;
    List<String> Names,name_cook,categoryname;
    List<Admin> list_Status;
    TextView btn_add;
    EditText userid,username,userpass,etxtProductCategory,email;
    String mediaPath="", encodedImage = "N/A",selectedID,COOKId,selectCategoryID;
    ImageView img_back,txt_choose_image,image_product;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        getSupportActionBar().hide();

        btn_add = findViewById(R.id.btn_add);
        userid = findViewById(R.id.userid);
        username = findViewById(R.id.username);
        userpass = findViewById(R.id.userpass);
        email = findViewById(R.id.email);
        img_back = findViewById(R.id.img_back);
        txt_choose_image = findViewById(R.id.txt_choose_image);
        image_product = findViewById(R.id.image_product);
        etxtProductCategory = findViewById(R.id.etxtProductCategory);

        getItem();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddstaffActivity.this, StaffActivity.class);
                startActivity(i);
                finish();
            }
        });

        txt_choose_image.setOnClickListener(v -> {
            Intent intent = new Intent(AddstaffActivity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });


        image_product.setOnClickListener(v -> {
            Intent intent = new Intent(AddstaffActivity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });



        etxtProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusAdapter = new ArrayAdapter<>(AddstaffActivity.this, android.R.layout.simple_list_item_1);
                statusAdapter.addAll(Names);

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddstaffActivity.this);
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



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userids = userid.getText().toString().trim();
                String usernames = username.getText().toString().trim();
                String userpasss = userpass.getText().toString().trim();
                String emaill = email.getText().toString().trim();
                String userstatus = etxtProductCategory.getText().toString().trim();

                if (userids.isEmpty()) {
                    userid.setError("ກະລຸນາປ້ອນຊື່");
                    userid.requestFocus();
                } else {
                    add_staff(userids,usernames,userpasss,emaill,userstatus);
                }
            }
        });
    }


    private void  add_staff(String userids,String usernames,String userpasss,String emaills,String userstatuss) {
        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        // Map is used to multipart the file using okhttp3.RequestBody
        if (mediaPath.isEmpty()) {
            Toasty.warning(this, R.string.choose_product_image, Toast.LENGTH_SHORT).show();
            loading.dismiss();
        }
        else {
            File file = new File(mediaPath);
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userids);
            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), usernames);
            RequestBody userpass = RequestBody.create(MediaType.parse("text/plain"), userpasss);
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emaills);
            RequestBody userstatus = RequestBody.create(MediaType.parse("text/plain"), userstatuss);



            ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Staff> call = getResponse.addStaff(fileToUpload, filename, userid,username,userpass,email,userstatus);
            call.enqueue(new Callback<Staff>() {
                @Override
                public void onResponse(@NonNull Call<Staff> call, @NonNull Response<Staff> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        loading.dismiss();
                        String value = response.body().getValue();
                        if (value.equals(Constant.KEY_SUCCESS)) {
                            Toasty.success(getApplicationContext(), R.string.product_successfully_added, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddstaffActivity.this, StaffActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (value.equals(Constant.KEY_FAILURE)) {

                            loading.dismiss();

                            Toasty.error(AddstaffActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            loading.dismiss();
                            Toasty.error(AddstaffActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        loading.dismiss();
                        Log.d("Error", response.errorBody().toString());
                    }

                }


                @Override
                public void onFailure(@NonNull Call<Staff> call, @NonNull Throwable t) {
                    loading.dismiss();
                    Log.d("Error! ", t.toString());
                    Toasty.error(AddstaffActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
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


}
