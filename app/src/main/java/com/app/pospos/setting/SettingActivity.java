package com.app.pospos.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.HomeActivity;
import com.app.pospos.customer.CustomerActivity;
import com.app.pospos.finan_report.FinanreportActivity;
import com.app.pospos.sale_list.SalelistActivity;
import com.app.pospos.staff.StaffActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences sp;
    ImageView img_back;
    CardView card_customer,card_staff,finan_report,card_listsaled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sp.getString(Constant.SP_EMAIL, "");
        getSupportActionBar().hide();
        String password = sp.getString(Constant.SP_PASSWORD, "");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("ການຕັ້ງຄ່າລະບົບນຳໃຊ້");
        card_customer = findViewById(R.id.card_customer);
        card_staff = findViewById(R.id.card_staff);
        finan_report = findViewById(R.id.finan_report);
        img_back = findViewById(R.id.img_back);
        card_listsaled = findViewById(R.id.card_listsaled);

        card_listsaled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, SalelistActivity.class);
                startActivity(i);
            }
        });

        card_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, CustomerActivity.class);
                startActivity(i);
            }
        });


        card_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, StaffActivity.class);
                startActivity(i);
            }
        });


        finan_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, FinanreportActivity.class);
                startActivity(i);
            }
        });



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
