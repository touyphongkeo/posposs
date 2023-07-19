package com.app.pospos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.pospos.about.AboutActivity;
import com.app.pospos.adapter.RateAdapter;
import com.app.pospos.category.CategoryActivity;
import com.app.pospos.model.Login;
import com.app.pospos.model.rate;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.print.PrinterActivity;
import com.app.pospos.product.ProductActivity;
import com.app.pospos.setting.SettingActivity;
import com.app.pospos.table.Mtable2Activity;
import com.app.pospos.table.MtableActivity;
import com.app.onlinesmartpos.R;

import com.app.pospos.login.LoginActivity;

import com.app.pospos.table_list.Tablelist_Activity;
import com.app.pospos.utils.BaseActivity;
import com.app.pospos.utils.LocaleManager;
import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.pospos.ClassLibs.Tbname;
import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

public class HomeActivity extends BaseActivity {

    private int[] mImages = new int[] {
            R.drawable.tyhf, R.drawable.cxsd,R.drawable.pos44,R.drawable.zxcz,

    };


    CardView  cardLogout,card_table,card_pos,view1,card_category,card_product,list_table,view2;
    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long backPressed;
    private Context context;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userType;
    TextView txtShopName,txtSubText,views,view2s,view3;
    private RateAdapter rateAdapter;
    List<rate> rateData;
    ProgressDialog loading;
    ImageView image_view;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().hide();

        cardLogout = findViewById(R.id.card_logout);
        card_table = findViewById(R.id.card_table);
        card_category = findViewById(R.id.card_category);
        views = findViewById(R.id.views);
        card_pos = findViewById(R.id.card_pos);
        view1 = findViewById(R.id.view1);
        view2s = findViewById(R.id.view2s);
        view3 = findViewById(R.id.view3);
        image_view = findViewById(R.id.image_view);
        card_product = findViewById(R.id.card_product);
        list_table = findViewById(R.id.list_table);
        view2 = findViewById(R.id.view2);






        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        String email = sp.getString(Constant.SP_EMAIL, "");
        String password = sp.getString(Constant.SP_PASSWORD, "");
       // views.setText("ລະຫັດຜູ້ໃຊ້: "+email);

        CarouselView carouselView = findViewById(R.id.carousel);
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
            }
        });



        getUser(email);

        if (email==""){
            cardLogout.setEnabled(false);
            card_table.setEnabled(false);
            views.setEnabled(false);
            card_pos.setEnabled(false);
            view1.setEnabled(false);
            view2s.setEnabled(false);
            view3.setEnabled(false);
            image_view.setEnabled(false);
            card_category.setEnabled(false);
            list_table.setEnabled(false);
            view2.setEnabled(false);
        }

      //  userType = sp.getString(Constant.SP_USER_TYPE, "");
     //   String shopName = sp.getString(Constant.SP_SHOP_NAME, "");
     //   String staffName = sp.getString(Constant.SP_STAFF_NAME, "");
     //   txtShopName.setText(shopName);
     //   txtSubText.setText("Hi "+staffName);

        card_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Mtable2Activity.class);
                startActivity(intent);
            }
        });


        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });



        card_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });


        card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
               startActivity(intent);
            }
        });

        list_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Tablelist_Activity.class);
                startActivity(intent);
            }
        });

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });



        card_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MtableActivity.class);
                startActivity(intent);
            }
        });



        if (Build.VERSION.SDK_INT >= 23) //Android MarshMellow Version or above
        {
            requestPermission();

        }





        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(HomeActivity.this);
                dialogBuilder
                        .withTitle("ອອກຈາກລະບົບ")
                        .withMessage("ທ່ານຕ້ອງການອອກຈາກລະບົບບໍ?")
                        .withEffect(Slidetop)
                        .withDialogColor("#2979ff") //use color code for dialog
                        .withButton1Text("ຕົກລົງ")
                        .withButton2Text("ຍົກເລີກ")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putString(Constant.SP_PASSWORD, "");
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        })
               .show();
            }
        });
    }

    //get User
    public void getUser(String userid) {
        Log.d("email",userid);
        loading=new ProgressDialog(HomeActivity.this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<Login>> call;
        call = apiInterface.get_user(userid);
        call.enqueue(new Callback<List<Login>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<Login>> call, @NonNull retrofit2.Response<List<Login>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Login> userData;
                    userData = response.body();
                    loading.dismiss();
                    if (userData.isEmpty()) {
                        Toasty.warning(HomeActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {
                        loading.dismiss();
                        String userids = userData.get(0).getUserid();
                        String username = userData.get(0).getusername();
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH).format(new Date());
                        views.setText("ລະຫັດຜູ້ໃຊ້: "+userids);
                        view2s.setText("ຊື່ຜູ້ໃຊ້: "+username);
                        view3.setText("ວັນທີ: "+currentDate);
                        String productImage = userData.get(0).getimg_urls();
                        String imageUrl= Constant.PRODUCT_IMAGE_URL+productImage;
                        if (productImage != null) {
                            if (productImage.length() < 3) {
                                image_view.setImageResource(R.drawable.vbd);
                            } else {
                                Glide.with(HomeActivity.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.vbd)
                                        .into(image_view);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Login>> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.local_french:
                setNewLocale(this, LocaleManager.FRENCH);
                return true;
            case R.id.local_english:
                setNewLocale(this, LocaleManager.ENGLISH);
                return true;
            default:
                Log.d("Default", "default");
        }
        return super.onOptionsItemSelected(item);
    }


    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    //double back press to exit
    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toasty.info(this, R.string.press_once_again_to_exit,
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }


    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //write your action if needed
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
}
