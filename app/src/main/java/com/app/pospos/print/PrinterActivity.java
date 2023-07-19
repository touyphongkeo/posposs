package com.app.pospos.print;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.HomeActivity;
import com.app.pospos.adapter.OrderDetailsAdapter2;
import com.app.pospos.model.OrderDetails;
import com.app.pospos.model.office;
import com.app.pospos.model.rate;
import com.app.pospos.model.totall;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;
public class PrinterActivity extends AppCompatActivity {
    private RecyclerView cart_recyclerview;
    private OrderDetailsAdapter2 orderDetailsAdapter;
    String tt;
    TextView text_v2;
    String currency;
    TextView  txt_BillTotal;
    List<String> where = new ArrayList<String>();

    //private TemplatePDF templatePDF;
    Bitmap bm = null;
    String customer;
    String users;
    String totalls;
    String baths;
    RecyclerView recyclerView;

    //List<user> DataUser;
    List<OrderDetails> orderDetails;
    ListView listView;
    View mView,mView1;
    CardView button1,button2;
    ProgressDialog loading;
    String CreateDate = "";
    String HH_YEAR = "";
    DecimalFormat f;
    public DecimalFormat f1;

    EditText txt_stocks;

    DecimalFormat formatter = new DecimalFormat("#,###,###");
    TextView textBill,text_v9,text_v7,textBillDate,usernaes,idsssss,barcode,telphon,onpay,text6,text_vkk,text_v89,text_v94,text_vlkmn;
  //  private OrderDetails_orderAdapter orderDetails_orderAdapter;

    String[] mobileArray = {"ທົດລອງ 01 2005 ","ທົດລອງ2","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};
    String[] listItem;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    ImageView imagebarcode,images;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("ລະບົບພິມບິນຂາຍ");

        button1= findViewById(R.id.button1);
        button2= findViewById(R.id.button2);


        txt_stocks= findViewById(R.id.txt_stocks);
        f = new DecimalFormat("#,###,##0");


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView1 = inflater.inflate(R.layout.activity_print, null);
        @SuppressLint("WrongViewCast") RelativeLayout view1 = (RelativeLayout) contentView1.findViewById(R.id.relatived);

        textBill = view1.findViewById(R.id.textBill);
        text_v2 = view1.findViewById(R.id.text_v2);
        text_v9 = view1.findViewById(R.id.text_v9);
        text_v7 = view1.findViewById(R.id.text_v7);
        textBillDate = view1.findViewById(R.id.textBillDate);
        usernaes = view1.findViewById(R.id.usernaes);
        idsssss = view1.findViewById(R.id.idsssss);
        barcode = view1.findViewById(R.id.barcode);
        telphon = view1.findViewById(R.id.telphon);
        imagebarcode = view1.findViewById(R.id.imagebarcode);
        images = view1.findViewById(R.id.images);
        onpay = view1.findViewById(R.id.onpay);
        text6 = view1.findViewById(R.id.text6);
        text_vkk = view1.findViewById(R.id.text_vkk);
        text_v89 = view1.findViewById(R.id.text_v89);
        text_v94 = view1.findViewById(R.id.text_v94);
        text_vlkmn = view1.findViewById(R.id.text_vlkmn);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());

        String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date()); //HH:mm:ss a

        textBillDate.setText("ວັນທີ: "+currentDate+" ເວລາ: "+currentTime);
        onpay.setText("OnePay "+currentDate+"/"+currentTime);

        cart_recyclerview = view1.findViewById(R.id.cart_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PrinterActivity.this, LinearLayoutManager.VERTICAL, false);
        cart_recyclerview.setLayoutManager(linearLayoutManager);
        cart_recyclerview.setHasFixedSize(true);
         getProductsData(Tbname.toString());
      //   get_totall(Tbname.toString());
       //  get_office();
       //  get_rate();

        textBill.setText("ເລກທີບີນ:"+SALE_BILL+"/"+Tbname.toString());




        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getProductsData(Tbname.toString());
                //  get_totall(Tbname.toString());
           //     get_office();
           //        textBill.setText("ເລກບິນ:"+SALE_BILL+"/"+Tbname.toString());
           //     barcode.setText(finvoiceId);



                view1.setDrawingCacheEnabled(true);
                view1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view1.layout(0, 0, view1.getMeasuredWidth(), view1.getMeasuredHeight());
                view1.buildDrawingCache(true);
                mView1 = view1;
                doPhotoPrint1();

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrinterActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
    }



    private void doPhotoPrint1() {
        PrintHelper photoPrinter1 = new PrintHelper(this);
        photoPrinter1.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap1 = mView1.getDrawingCache();
        photoPrinter1.printBitmap("image.png_test_print", bitmap1, new PrintHelper.OnPrintFinishCallback() {
            @Override
            public void onFinish() {

                changeOnCon();
            }
        });
    }



   public void getProductsData(String Tbname) {
       Log.d("tbname",Tbname);
       loading=new ProgressDialog(PrinterActivity.this);
       loading.setCancelable(false);
       loading.setMessage(getString(R.string.please_wait));
       loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<OrderDetails>> call;
        call = apiInterface.OrderDetailsByInvoice(Tbname);
        call.enqueue(new Callback<List<OrderDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderDetails>> call, @NonNull Response<List<OrderDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                   orderDetails = response.body();
                   loading.dismiss();
                   // get_office();
                 //   get_totall(Tbname);
                //    get_rate();
                   orderDetailsAdapter = new OrderDetailsAdapter2(PrinterActivity.this, orderDetails,telphon,usernaes,idsssss,text_v2,text_v7,text_v9,text_vkk,text_v89,text_v94,text_vlkmn);
                   cart_recyclerview.setAdapter(orderDetailsAdapter);
                   //  getProductsDatas(finvoiceId);
                   Toasty.success(PrinterActivity.this, "Successfuly", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<OrderDetails>> call, @NonNull Throwable t) {
                Toast.makeText(PrinterActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }



    public void changeOnCon() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Successfuly").setContentText("ພີມສຳເລັດ!").setConfirmText("ຕົກລົງ").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {@Override
        public void onClick(SweetAlertDialog sDialog) {
            try {
                Intent intent = new Intent(PrinterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e){
            }
        }
        }).show();
    }


  /*  public void changeOnCon() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Successfuly").setContentText("ພີມສຳເລັດ!").setConfirmText("ຕົກລົງ").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {@Override
        public void onClick(SweetAlertDialog sDialog) {
            try {
                Intent intent = new Intent(PrinterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e){
            }
        }
        }).show();
    }*/



   /* public void get_office() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<office>> call;
        call = apiInterface.get_office();
        call.enqueue(new Callback<List<office>>() {
            @Override
            public void onResponse(@NonNull Call<List<office>> call, @NonNull Response<List<office>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<office> productData;
                    productData = response.body();

                    if (productData.isEmpty()) {
                      //  Toasty.warning(PrinterActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {
                        String getcom_name_la = productData.get(0).getcom_name_la();
                        String image = productData.get(0).getimg_url();
                        text6.setText(getcom_name_la);
                        String imageUrls= Constant.PRODUCT_IMAGE_URL+image;

                        if (image != null) {
                            if (image.length() < 3) {
                                images.setImageResource(R.drawable.image_placeholder);
                            } else {
                                Glide.with(PrinterActivity.this)
                                        .load(imageUrls)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.image_placeholder)
                                        .into(images);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<office>> call, @NonNull Throwable t) {
                loading.dismiss();
                Toast.makeText(PrinterActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }
*/


/*
    public void get_totall(String Tbname) {
        Log.d("tbname",Tbname);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<totall>> call;
        call = apiInterface.gettotall(Tbname);
        call.enqueue(new Callback<List<totall>>() {
            @Override
            public void onResponse(@NonNull Call<List<totall>> call, @NonNull Response<List<totall>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<totall> productDatas;
                    productDatas = response.body();

                    if (productDatas.isEmpty()) {
                        //   Toasty.warning(PrinterActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {
                        totalls = productDatas.get(0).gettotall_price();
                        baths= productDatas.get(0).gettbath();
                        String usd = productDatas.get(0).getusd();


                        double totall_sume = Double.parseDouble(totalls);
                        double formtbath = Double.parseDouble(baths);
                        text_v2.setText(formatter.format(totall_sume));
                        text_v9.setText(formatter.format(totall_sume)+" ກິບ");


                        text_v94.setText(formatter.format(formtbath)+" ກິບ");
                        text_vlkmn.setText(formatter.format(formtbath)+" ກິບ");
                        //double totalCost = OrderDetailsAdapter2.bath;


                    }
                }

            }
            @Override
            public void onFailure(@NonNull Call<List<totall>> call, @NonNull Throwable t) {
                loading.dismiss();
                Toast.makeText(PrinterActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }
*/




/*
    public void get_rate() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<rate>> call;
        call = apiInterface.get_rate();
        call.enqueue(new Callback<List<rate>>() {
            @Override
            public void onResponse(@NonNull Call<List<rate>> call, @NonNull Response<List<rate>> response) {
                if (response.isSuccessful() && response.body() != null) {
                try {
                    List<rate> rateDatas;
                    rateDatas = response.body();

                    if (rateDatas.isEmpty()) {
                        Toasty.warning(PrinterActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();
                    } else {
                        String id = rateDatas.get(0).getID();
                        String ex_date = rateDatas.get(0).getcomex_date();
                        String ex_time = rateDatas.get(0).getcomex_time();
                        String ex_kip_bath = rateDatas.get(0).getcomex_kip_bath();
                        String ex_kip_us = rateDatas.get(0).getcomex_kip_us();
                        String ex_status = rateDatas.get(0).getcomex_status();



                        double bath = Double.parseDouble(ex_kip_bath);
                        double bbb = Double.parseDouble(ex_kip_us);
                        text_vkk.setText("ເລດເງີນ:"+formatter.format(bath));
                        double sbath = Double.parseDouble(totalls);
                        double sumbath = sbath/bath;
                        double usd = sbath/bbb;

                        text_v94.setText(formatter.format(sumbath)+"B");
                        text_vlkmn.setText(formatter.format(usd)+"$");
                        double us = Double.parseDouble(ex_kip_us);
                      //  double sumbaont = sbath/us;
                        text_v89.setText("ເລດເງີນໂດລາ:"+formatter.format(us));



                     //


                    }
                }catch (Exception e){

                }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<rate>> call, @NonNull Throwable t) {
                loading.dismiss();
                Toast.makeText(PrinterActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }
*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
