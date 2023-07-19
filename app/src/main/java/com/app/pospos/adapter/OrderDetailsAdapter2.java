package com.app.pospos.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.model.OrderDetails;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailsAdapter2 extends RecyclerView.Adapter<OrderDetailsAdapter2.MyViewHolder> {


    Context context;
    private List<OrderDetails> orderData;
    public static double subTotalPrice=0;
    SharedPreferences sp;
    String currency,total;
    DecimalFormat f;
    //final DecimalFormat f = new DecimalFormat("#,##0");
    DecimalFormat formatter = new DecimalFormat("#,###");

    TextView telphon,usernaes,idsssss,text_v2,text_v7,text_v9,text_vkk,text_v89,text_v94,text_vlkmn;
    String usd;
    String bath;

    String totalls;
    double ojectto;
    public OrderDetailsAdapter2(Context context, List<OrderDetails> orderData, TextView telphon, TextView usernaes, TextView idsssss, TextView text_v2, TextView text_v7, TextView text_v9,TextView text_vkk,TextView text_v89,TextView text_v94,TextView text_vlkmn) {
        this.context = context;
        this.orderData = orderData;
        this.telphon = telphon;
        this.usernaes = usernaes;
        this.idsssss = idsssss;
        this.text_v2 = text_v2;
        this.text_v7 = text_v7;
        this.text_v9 = text_v9;
        this.text_vkk = text_vkk;
        this.text_v89 = text_v89;
        this.text_v94 = text_v94;
        this.text_vlkmn = text_vlkmn;

        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "");
        f = new DecimalFormat("#,###,##0");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item_bill, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtProductName.setText(orderData.get(position).getsale_name());
        holder.txt_qty.setText(orderData.get(position).getsale_qty());

       /*
        text_v9.setText(orderData.get(position).geTotall_price());*/
        bath = orderData.get(position).getBath();
        usd = orderData.get(position).getUsd();
        String qty = orderData.get(position).getsale_qty();
        String pricev = orderData.get(position).geTotall_price();

        double sqty = Double.parseDouble(qty);
        String amount = orderData.get(position).getsale_price();

        text_vkk.setText("ເລດເງີນບາດ:"+bath);
        text_v89.setText("ເລດເງີນໂດລາ:"+usd);
        text_v2.setText(pricev);
        text_v9.setText(pricev);


        double bnn = Double.parseDouble(pricev);
        double bad = Double.parseDouble(bath);
        double laib = bnn/bad;
        String obad = String.valueOf(formatter.format(laib));
        text_v94.setText(obad+" ບາດ");

        double bads = Double.parseDouble(usd);
        double laibs = bnn/bads;
        String obads = String.valueOf(formatter.format(laibs));
        text_vlkmn.setText(obads+" ໂດລາ");

       // double ratebath = Double.parseDouble(bath);
      //  double usds = Double.parseDouble(usd);

       // totalls = text_v2.getText().toString();



      //  String formtbath = String.valueOf(totalls);
      //  double converbath = Double.parseDouble(formtbath);


      //  text_v94.setText(formatter.format(converbath));

        



        double price = Double.parseDouble(amount);

        if (price==0){
            holder.txtProductPrice.setText("ແຖມ");
        }else {
            holder.txtProductPrice.setText(formatter.format(price));
        }
        double aa = price*sqty;
      
        holder.txtTotalCost.setText(formatter.format(aa));


    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtProductName, txtProductPrice, txtTotalCost,txt_qty;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtProductPrice = itemView.findViewById(R.id.txt_price);

            txtTotalCost = itemView.findViewById(R.id.txt_total_cost);
            txt_qty = itemView.findViewById(R.id.txt_qty);

        }


    }


}