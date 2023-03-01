package com.app.pospos.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.app.pospos.Constant;
import com.app.pospos.HomeActivity;
import com.app.pospos.database.DatabaseAccess;
import com.app.onlinesmartpos.R;
import com.app.pospos.model.Login;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.BaseActivity;
import com.app.pospos.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    EditText etxtEmail, etxtPassword,etxt_serach;
    TextView txtLogin,username,txt_item_name;
    SharedPreferences sp;
    ProgressDialog loading;
    Utils utils;
    private Context context;
    CardView view;
    LinearLayout layout1,view2;

    ImageView img_delete,image_view;
    CheckBox check;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        etxtEmail = findViewById(R.id.etxt_email);
        etxtPassword = findViewById(R.id.etxt_password);
        txtLogin = findViewById(R.id.txt_login);
        view = findViewById(R.id.view);
        layout1 = findViewById(R.id.layout1);
        img_delete = findViewById(R.id.img_delete);
        check = findViewById(R.id.check);
        username = findViewById(R.id.username);
        txt_item_name = findViewById(R.id.txt_item_name);
        view2 = findViewById(R.id.view2);
        etxt_serach = findViewById(R.id.etxt_serach);

        image_view = findViewById(R.id.image_view);
        utils = new Utils();
        view.setVisibility(View.GONE);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sp.getString(Constant.SP_EMAIL, "");
        String password = sp.getString(Constant.SP_PASSWORD, "");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.register));
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
        databaseAccess.open();
        List<HashMap<String, String>> userData;
        userData = databaseAccess.getUser();

        if (userData.size() > 0) {
            String UserID=userData.get(0).get("UserID");
            String usern=userData.get(0).get("Name");
            String emails=userData.get(0).get("email");
            String images=userData.get(0).get("images");
            etxtEmail.setText(UserID);
            etxt_serach.setText(UserID);
            username.setText(usern);
            txt_item_name.setText(emails);
            getorder(UserID);


            if (UserID.equals("")){
                check.setChecked(false);
                view.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);

            }else {
                check.setChecked(true);
                layout1.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }

        }




        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setVisibility(View.VISIBLE);
                username.setVisibility(View.GONE);
                txt_item_name.setVisibility(View.GONE);
                image_view.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                check.setChecked(false);
                etxtEmail.clearFocus();
                etxtEmail.requestFocus();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
                databaseAccess.open();
                boolean del = databaseAccess.deleteAllUser();

            }
        });


        if (email.length() >= 3 && password.length() >= 3) {
            if (utils.isNetworkAvailable(LoginActivity.this)) {
                login(email, password);
            } else {
                Toasty.error(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        }


        txtLogin.setOnClickListener(v -> {
            String email1 = etxtEmail.getText().toString().trim();
            String password1 = etxtPassword.getText().toString().trim();
            if (email1.isEmpty()) {
                etxtEmail.setError("ກະລຸນາປ້ອນຊື່ຜູ້ໃຊ້");
                etxtEmail.requestFocus();
            } else if (password1.isEmpty()) {
                etxtPassword.setError("ກະລຸນາປ້ອນລະຫັດຜ່ານ");
                etxtPassword.requestFocus();
            } else {

                if (utils.isNetworkAvailable(LoginActivity.this)) {
                    login(email1, password1);

                } else {
                    Toasty.error(LoginActivity.this, "ກະລຸນາເຊື່ອມຕໍອີນເຕີເນັດ", Toast.LENGTH_SHORT).show();

                }
            }
        });


        etxt_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("data", s.toString());
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    //search data from server
                    getorder(s.toString());
                } else {
                    getorder("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());
            }
        });

    }


    public void getorder(String searchText) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Login>> call;
        call = apiInterface.get_user(searchText);
        call.enqueue(new Callback<List<Login>>() {
            @Override
            public void onResponse(@NonNull Call<List<Login>> call, @NonNull Response<List<Login>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Login> userList;
                    userList = response.body();

                    String productImage = userList.get(0).getimg_urls();
                    String imageUrl= Constant.PRODUCT_IMAGE_URL+productImage;

                    if (productImage != null) {
                        if (productImage.length() < 3) {

                            image_view.setImageResource(R.drawable.image_placeholder);
                        } else {


                            Glide.with(LoginActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.image_placeholder)
                                    .into(image_view);
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Login>> call, @NonNull Throwable t) {

                Toast.makeText(LoginActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }


    //login method
    private void login(String userid, String userpass) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Login> call = apiInterface.login(userid, userpass);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {

                if (response.body() != null && response.isSuccessful()) {
                    String value = response.body().getValue();
                    String message = response.body().getMassage();
                    String shopEmail = response.body().getUserid();
                    String username = response.body().getusername();
                    String images = response.body().getimg_urls();
                    String email = response.body().getimgemail();


                    if (shopEmail != null) {
                        if (check.isChecked()){
                            loading.dismiss();

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sp.edit();
                            //Adding values to editor
                            editor.putString(Constant.SP_EMAIL, userid);
                            editor.putString(Constant.SP_PASSWORD, userpass);
                            //Saving values to Share preference
                            editor.apply();

                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(LoginActivity.this);
                            databaseAccess.open();
                            boolean del = databaseAccess.deleteAllUser();
                            databaseAccess.open();
                            boolean inser = databaseAccess.addUser(userid,username,images,email);

                            Toasty.success(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }else {
                            loading.dismiss();
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sp.edit();
                            //Adding values to editor
                            editor.putString(Constant.SP_EMAIL, userid);
                            editor.putString(Constant.SP_PASSWORD, userpass);
                            //Saving values to Share preference
                            editor.apply();

                            Toasty.success(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        loading.dismiss();
                        Toasty.error(LoginActivity.this, "ຊື່ຜູ້ໃຊ້ ແລະ ລະຫັດຜ່ານບໍຖືກຕ້ອງ!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.dismiss();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {

                loading.dismiss();

            }
        });
    }
}
