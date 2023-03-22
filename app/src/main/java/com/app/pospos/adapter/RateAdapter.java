package com.app.pospos.adapter;

import static com.app.pospos.ClassLibs.Kip_bath;
import static com.app.pospos.ClassLibs.Status;
import static com.app.pospos.ClassLibs.Tbname;
import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.model.Table;
import com.app.pospos.model.rate;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.pos.Pos2Activity;
import com.app.pospos.pos.PosActivity;
import com.app.pospos.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.MyViewHolder> {

    private List<rate> customerData;
    private Context context;
    TextView text_vkk;
    Utils utils;
    public RateAdapter(Context context, List<rate> customerData,TextView text_vkk) {
        this.context = context;
        this.customerData = customerData;
        this.text_vkk = text_vkk;
        utils=new Utils();



    }


    @NonNull
    @Override
    public RateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RateAdapter.MyViewHolder holder, int position) {
        final String id = customerData.get(position).getID();
        String date = customerData.get(position).getcomex_date();
        String time = customerData.get(position).getcomex_time();
        Kip_bath = customerData.get(position).getcomex_kip_bath();
        String kip_us = customerData.get(position).getcomex_kip_us();
        String status = customerData.get(position).getcomex_status();


        text_vkk.setText(Kip_bath);



    }



    @Override
    public int getItemCount() {
        return customerData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);




            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            Intent i = new Intent(context, EditCustomersActivity.class);
//            i.putExtra("customer_id", customerData.get(getAdapterPosition()).getCustomerId());
//            i.putExtra("customer_name", customerData.get(getAdapterPosition()).getCustomerName());
//            i.putExtra("customer_cell", customerData.get(getAdapterPosition()).getCustomerCell());
//            i.putExtra("customer_email", customerData.get(getAdapterPosition()).getCustomerEmail());
//            i.putExtra("customer_address", customerData.get(getAdapterPosition()).getCustomerAddress());
//            context.startActivity(i);
        }
    }



    private void update_table1(String tbname) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Table> call = apiInterface.update_table1(tbname);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(@NonNull Call<Table> call, @NonNull Response<Table> response) {

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String value = response.body().getValue();
                        if (value.equals(Constant.KEY_SUCCESS)) {
                            Toasty.success(context, "ສັງສຳເລັດ", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                        else if (value.equals(Constant.KEY_FAILURE)) {
                            Toasty.error(context, "ຜິດພາດ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toasty.error(context, "ຜິດພາດ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                }
            }

            @Override
            public void onFailure(@NonNull Call<Table> call, @NonNull Throwable t) {
                Log.d("Error! ", t.toString());
            }
        });
    }



}
