package com.app.pospos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.onlinesmartpos.R;
import com.app.pospos.login.LoginActivity;
import com.app.pospos.utils.BaseActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends BaseActivity {
    public static int splashTimeOut = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, splashTimeOut);
    }


    public void successMessage() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Done!!").setContentText("exercise 06!").show();

    }

    public void changeOnConfirm2() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("ຜິດພາດ").setContentText("ກະລຸນາເຂົ້າລະບົບອີກຄັ້ງ!").setConfirmText("Yes").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {@Override
        public void onClick(SweetAlertDialog sDialog) {
            finishAffinity();
        }
        }).show();
    }
}

