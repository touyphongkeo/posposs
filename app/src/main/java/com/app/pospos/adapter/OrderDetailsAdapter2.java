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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
public class OrderDetailsAdapter2 extends RecyclerView.Adapter<OrderDetailsAdapter2.MyViewHolder> {
    Context context;
    private List<OrderDetails> orderData;
    public static double subTotalPrice=0;
    SharedPreferences sp;
    String currency,total;
    DecimalFormat f;
    //final DecimalFormat f = new DecimalFormat("#,##0");
    DecimalFormat formatter = new DecimalFormat("#,###");
    TextView telphon,usernaes,idsssss,text_v2,text_v7,text_v9,text_vkk,text_v89,text_v94,text_vlkmn,cn_kip,cnkip,text_tax,text_v6,text_v74;
    String usd;
    String bath;
    String totalls;
    double ojectto;
    public OrderDetailsAdapter2(Context context, List<OrderDetails> orderData, TextView telphon, TextView usernaes, TextView idsssss, TextView text_v2, TextView text_v7, TextView text_v9,TextView text_vkk,TextView text_v89,TextView text_v94,TextView text_vlkmn,TextView cn_kip,TextView cnkip,TextView text_tax,TextView text_v6,TextView text_v74) {
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
        this.cn_kip = cn_kip;
        this.cnkip = cnkip;
        this.text_tax = text_tax;
        this.text_v6 = text_v6;
        this.text_v74 = text_v74;

        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "");
        f = new DecimalFormat("#,###");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item_bill, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String id = orderData.get(position).getId();
        holder.txtProductName.setText(orderData.get(position).getId()+"."+orderData.get(position).getsale_name());
        holder.txt_qty.setText("("+orderData.get(position).getsale_qty()+")");
        String geTamount = orderData.get(position).geTamount();

        String tbathKip = orderData.get(position).geTbath_kip();
        String geTusdKip = orderData.get(position).geTusd_kip();
        String geTcnKip = orderData.get(position).geTcn_kip();
        String tax = orderData.get(position).geTax();
        String gettpers = orderData.get(position).gettpers();
        String gettper = orderData.get(position).gettper();
        String getttotal = orderData.get(position).getttotal();
       /*
        text_v9.setText(orderData.get(position).geTotall_price());*/
        bath = orderData.get(position).getBath();
        usd = orderData.get(position).getUsd();
        String qty = orderData.get(position).getsale_qty();
        double amountsd = Double.parseDouble(geTamount);
        double vvbbb = Double.parseDouble(getttotal);
        text_v2.setText(f.format(amountsd));

        text_v6.setText("ສ່ວນຫຼຸດ:("+gettpers+"%)");



        DecimalFormat dfsbb = new DecimalFormat(
                "#,###.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal valueshhh = new BigDecimal(gettper);
        text_v7.setText(f.format(valueshhh));

        DecimalFormat dfsbbff = new DecimalFormat(
                "#,###.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal valueshhhff = new BigDecimal(getttotal);
        text_v74.setText(f.format(valueshhhff));




        double tax_out = Double.parseDouble(tax);
        text_tax.setText(f.format(tax_out));

        double totall_price = vvbbb+tax_out;

        text_v9.setText(f.format(totall_price)+" ₭");






        double bad = Double.parseDouble(tbathKip);
        double laib = totall_price/bad;
        String obad = String.valueOf(f.format(laib));
        text_v94.setText(obad+" ฿");



        double bads = Double.parseDouble(geTusdKip);
        double laibs = totall_price/bads;
        String obads = String.valueOf(f.format(laibs));
        text_vlkmn.setText(obads+" $");

        double cny = Double.parseDouble(geTcnKip);
        double cnyp = totall_price/cny;
        String cnyd = String.valueOf(f.format(cnyp));
        cnkip.setText(cnyd+" ￥");

     /*   DecimalFormat dfs = new DecimalFormat(
                "#,###.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal values = new BigDecimal(pricev);
        text_v2.setText(f.format(values));
        text_v9.setText(f.format(values)+" ກິບ");*/


    //  String pricev = orderData.get(position).geTotall_price();

        double sqty = Double.parseDouble(qty);
        String amount = orderData.get(position).getsale_price();




       // text_vkk.setText("ເລດເງີນບາດ:"+tbathKip);
      //  text_v89.setText("ເລດເງີນໂດລາ:"+geTusdKip);
        //cn_kip.setText("ເລດເງີນຢວນ:"+f.format(geTcnKip));

        DecimalFormat tbat = new DecimalFormat(
                "#,###.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal usdd = new BigDecimal(tbathKip);
        text_vkk.setText("THB: "+f.format(usdd));







        DecimalFormat usd = new DecimalFormat(
                "#,###.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal usddf = new BigDecimal(geTusdKip);
        text_v89.setText("USD: "+f.format(usddf));


        DecimalFormat dfs = new DecimalFormat(
                "#,###.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal values = new BigDecimal(geTcnKip);
        cn_kip.setText("CNY: "+f.format(values));




      /*  try {


        }catch (Exception e){
        }
*/

     /*   double bnn = Double.parseDouble(pricev);
        double bad = Double.parseDouble(bath);
        double laib = bnn/bad;
        String obad = String.valueOf(formatter.format(laib));
        text_v94.setText(obad+" ບາດ");

        */

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