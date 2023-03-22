package com.app.pospos.adapter;
import static com.app.pospos.ClassLibs.Kip_bath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.app.pospos.ClassLibs.Status;
import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pospos.model.Table;
import com.app.pospos.pos.Cart2Activity;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class Table2Adapter extends RecyclerView.Adapter<Table2Adapter.MyViewHolder> {


    private List<Table> customerData;
    private Context context;
    Utils utils;
    public Table2Adapter(Context context, List<Table> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();



    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table2_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String id = customerData.get(position).getId();
        String tbname = customerData.get(position).getTbname();
        String Statustable = customerData.get(position).getStatustable();
        String statustable = customerData.get(position).getStatustable();
        String stt = customerData.get(position).getStt();
        String linkid = customerData.get(position).getLinkid();
        String tbl = customerData.get(position).getTbl();
        String sale_bill = customerData.get(position).get_sale_bill();
        String sale_time = customerData.get(position).get_time();

        String currentTime = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(new Date()); //HH:mm:ss a

        Statustable = Statustable.trim();

        if(Statustable.equals("1")){
            holder.layout.setBackgroundColor(Color.parseColor("#ABB1B8"));
            holder.txt_category_name.setText("ເລກໂຕະ: "+tbname);
        }else if(Statustable.equals("2")){
            holder.layout.setBackgroundColor(Color.parseColor("#FB4106"));
            holder.txt_category_name.setText("ເລກໂຕະ: "+tbname);
            holder.time_date.setText("ໂມງ: "+sale_time+"-"+currentTime);
        }else{
            holder.layout.setBackgroundColor(Color.parseColor("#ABB1B8"));
            holder.txt_category_name.setText("ເລກໂຕະ: "+tbname);
        }


       holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status = statustable;
                Tbname = tbname;
                SALE_BILL = sale_bill;

                if (Status.equals("2")){
                    Toast.makeText(context, Kip_bath, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Cart2Activity.class);
                    context.startActivity(i);
                }else {
                    Toasty.error(context, "ກະລຸນາເປິດໂຕະກອນ!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    @Override
    public int getItemCount() {
        return customerData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_category_name,time_date;
        CardView card_category;
        ImageView imgStatus;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_category_name = itemView.findViewById(R.id.txt_category_name);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            card_category = itemView.findViewById(R.id.card_category);
            layout = itemView.findViewById(R.id.layout);
            time_date = itemView.findViewById(R.id.time_date);


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






}
