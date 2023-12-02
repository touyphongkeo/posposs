package com.app.pospos.adapter;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pospos.Constant;
import com.app.pospos.database.DatabaseAccess;
import com.app.pospos.model.Customer;
import com.app.pospos.model.Sale;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sale2Adapter extends RecyclerView.Adapter<Sale2Adapter.MyViewHolder> {
    private List<Sale> customerData;
    private Context context;
    Utils utils;
    MediaPlayer player;
    DecimalFormat f;
    DatabaseAccess databaseAccess;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String username ="";
    Integer cust =0;
    String sale_price="";
    double aa = 0;

    TextView etxt_amount;


    public Sale2Adapter(Context context, List<Sale> customerData,TextView etxt_amount) {
    this.context = context;
    this.customerData = customerData;
    this.etxt_amount = etxt_amount;
    utils=new Utils();
    player = MediaPlayer.create(context, R.raw.delete_sound);
    databaseAccess = DatabaseAccess.getInstance(context);
    f = new DecimalFormat("#,###");

    sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    editor = sp.edit();
    username = sp.getString(Constant.SP_EMAIL, "");


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart2_product_items, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String id = customerData.get(position).getId();
        String sale_bill = customerData.get(position).getSale_bill();
        String sale_date = customerData.get(position).getSale_date();
        String sale_table = customerData.get(position).getSale_table();
        String sale_proid = customerData.get(position).getSale_proid();
        String sale_name = customerData.get(position).getSale_name();
        String sale_price = customerData.get(position).getSale_price();
        String sale_qty = customerData.get(position).getSale_qty();
        String sale_status = customerData.get(position).getSale_status();
        String edit_sale = customerData.get(position).getEdit_sale();
        String sendid = customerData.get(position).getSendid();
        String print = customerData.get(position).getSendtime();
        String sendtime = customerData.get(position).getPrint();
        String username = customerData.get(position).getUsername();
        String statusorder = customerData.get(position).getStatusorder();
        String remark = customerData.get(position).getRemark();
        String img_url = customerData.get(position).get_Img_url();
        String sumamount = customerData.get(position).getSumamount();






        DecimalFormat ss = new DecimalFormat(
                "#,###",
        new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal amount = new BigDecimal(sumamount);
        etxt_amount.setText("(ມຸນຄ່າລວມ:"+f.format(amount)+" ກິບ)");

        String imageUrl= Constant.PRODUCT_IMAGE_URL+img_url;

        if (img_url != null) {
            if (img_url.length() < 3) {
                holder.cart_product_image.setImageResource(R.drawable.image_placeholder);
            } else {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.image_placeholder)
                        .into(holder.cart_product_image);
            }
        }



        try {
            holder.txt_item_name.setText("ສີນຄ້າ: "+sale_name);
            holder.txt_number.setText(sale_qty);
            holder.tabel.setText("ລະຫັດ: "+id);
            DecimalFormat dfs = new DecimalFormat(
                    "#,###",
                    new DecimalFormatSymbols(new Locale("pt", "BR")));
            BigDecimal value1 = new BigDecimal(sale_price);
            holder.txt_weight.setText("ລາຄາ:"+f.format(value1)+" ກິບ");
            int a = Integer.parseInt(sale_qty);
            double b = Double.parseDouble(sale_price);
            double ab = b*a;

            DecimalFormat df = new DecimalFormat(
                    "#,###",
                    new DecimalFormatSymbols(new Locale("pt", "BR")));
            BigDecimal value = new BigDecimal(ab);

            holder.txt_price.setText("ລວມ:"+f.format(value)+" ກິບ");
        }catch (Exception e){

        }






        //  Statusorder = Statusorder.trim();
        if (statusorder==null){

            holder.status.setBackgroundColor(Color.parseColor("#43a047"));
            holder.status.setText("ລໍຖ້າຮັບອໍເດີ..");
        }
        else if (statusorder.equals("1")){

            holder.status.setBackgroundColor(Color.parseColor("#E8B10B"));
            holder.status.setText("ກຳລັງປຸງແຕ່ງ");
        }
        else if (statusorder.equals("0")){

            holder.status.setBackgroundColor(Color.parseColor("#D13909"));
            holder.status.setText(remark);
        }else if (statusorder.equals("2")){

            holder.status.setBackgroundColor(Color.parseColor("#048DFA"));
            holder.status.setText("ປຸງແຕ່ງສຳເລັດ");
        }else if (statusorder.equals("3")){

            holder.status.setBackgroundColor(Color.parseColor("#6C6E6C"));
            holder.status.setText("ເສິບແລ້ວ..");
        }

        if (sale_price.equals("0")){
            holder.img_delete.setImageResource(R.drawable.baseline_check_circle_24);
        }else {
            holder.img_delete.setImageResource(R.drawable.baseline_add_circle_24);
        }

        if (sale_price.equals("0")){

        }else {



        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle("ຄຳເຕືອນ!")
                        .withMessage("ທ່ານແນ່ໃຈແລ້ວບໍລາຍການນີ້ແຖມ!")
                        .withEffect(Slidetop)
                        .withDialogColor("#FA9909") //use color code for dialog
                        .withButton1Text("ຕົກລົງ")
                        .withButton2Text("ຍົກເລີກ")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (utils.isNetworkAvailable(context)) {
                                    update_free(id);
                                    customerData.remove(holder.getAdapterPosition());
                                    dialogBuilder.dismiss();
                                }
                                else
                                {
                                    dialogBuilder.dismiss();
                                    Toasty.error(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                                }
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
    }




    @Override
    public int getItemCount() {
        return customerData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_item_name,txt_weight,txt_price,status,txt_number,tabel;
        ImageView img_delete;

        ImageView cart_product_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_weight = itemView.findViewById(R.id.txt_weight);
            txt_price = itemView.findViewById(R.id.txt_price);
            status = itemView.findViewById(R.id.status);
            cart_product_image = itemView.findViewById(R.id.cart_product_image);
            img_delete = itemView.findViewById(R.id.img_delete);
            txt_number = itemView.findViewById(R.id.txt_number);
            tabel = itemView.findViewById(R.id.tabel);

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





    private void update_free(String Id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Sale> call = apiInterface.update_free(Id);
        call.enqueue(new Callback<Sale>() {
            @Override
            public void onResponse(@NonNull Call<Sale> call, @NonNull Response<Sale> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.success(context, "ການແຖມຂອງທ່ານສຳເລັດ", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                    else if (value.equals(Constant.KEY_FAILURE)){
                        Toasty.error(context, "ການແຖມຂອງທ່ານຜິດພາດ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "ບໍ່ສາມາດເຊື່ອມຕໍ່ອິນເຕີເນັດ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Sale> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
