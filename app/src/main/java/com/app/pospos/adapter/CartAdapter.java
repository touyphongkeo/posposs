package com.app.pospos.adapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pospos.Constant;
import com.app.pospos.database.DatabaseAccess;
import com.app.onlinesmartpos.R;
import com.app.pospos.ofsion.Addofsion_Activity;
import com.app.pospos.pos.Cart2Activity;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.app.pospos.ClassLibs.IDS;
import static com.app.pospos.ClassLibs.Options;
import es.dmoral.toasty.Toasty;
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private List<HashMap<String, String>> cartProduct;
    private Context context;
    MediaPlayer player;
    TextView txtNoProduct;
    TextView txtTotalPrice;
    public static Double totalPrice;
    ImageView imgNoProduct;
    Button btnSubmitOrder;
    SharedPreferences sp;
    String currency;
    Integer cust =0;

    public CartAdapter(Context context, List<HashMap<String, String>> cartProduct, TextView txtTotalPrice, Button btnSubmitOrder, ImageView imgNoProduct, TextView txtNoProduct) {
        this.context = context;
        this.cartProduct = cartProduct;
        player = MediaPlayer.create(context, R.raw.delete_sound);
        this.txtTotalPrice = txtTotalPrice;
        this.btnSubmitOrder = btnSubmitOrder;
        this.imgNoProduct = imgNoProduct;
        this.txtNoProduct = txtNoProduct;
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "");
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        final String Id = cartProduct.get(position).get("Id");
        String sale_bill = cartProduct.get(position).get("sale_bill");
        final String sale_date = cartProduct.get(position).get("sale_date");
        final String sale_table = cartProduct.get(position).get("sale_table");
        final String product_id = cartProduct.get(position).get("product_id");
        final String sale_name = cartProduct.get(position).get("sale_name");
        final String sale_price = cartProduct.get(position).get("sale_price");
        final String sale_qty = cartProduct.get(position).get("sale_qty");
        final String sale_status = cartProduct.get(position).get("sale_status");
        final String edit_sale = cartProduct.get(position).get("edit_sale");
        final String username = cartProduct.get(position).get("username");
        final String img_url = cartProduct.get(position).get("img_url");
        final String options = cartProduct.get(position).get("options");
        String imageUrl= Constant.PRODUCT_IMAGE_URL+img_url;
        final DecimalFormat f = new DecimalFormat("#,###");



        if (options==null){
            holder.status.setText("ລົດຊາດ: ບໍມີ");
        }else {
            holder.status.setText("ລົດຊາດ: ("+options+")");
        }

        databaseAccess.open();
        totalPrice = databaseAccess.getTotalPrice();
       /* DecimalFormat df = new DecimalFormat(
        "#,###.00",
        new DecimalFormatSymbols(new Locale("pt", "BR")));
        BigDecimal value = new BigDecimal(totalPrice);*/
        txtTotalPrice.setText("("+"ຈຳນວນເງີນລວມ: "+f.format(totalPrice)+" ກິບ"+")");


        if (img_url != null) {
            if (img_url.isEmpty()) {
                holder.cart_product_image.setImageResource(R.drawable.image_placeholder);
                holder.cart_product_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {


                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.image_placeholder)
                        .into(holder.cart_product_image);

            }
        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                databaseAccess.open();
                boolean deleteProduct = databaseAccess.deleteProductFromCart(Id);
                if (deleteProduct) {
                    Toasty.success(context, "ລົບສຳເລັດ", Toast.LENGTH_SHORT).show();
                    player.start();
                    cartProduct.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());

                    databaseAccess.open();
                    totalPrice = databaseAccess.getTotalPrice();
                    txtTotalPrice.setText("("+"ຈຳນວນເງີນລວມ: "+f.format(totalPrice)+" ກິບ"+")");
                } else {
                    Toasty.error(context,"ຜິດພາດ", Toast.LENGTH_SHORT).show();
                }

                databaseAccess.open();
                int itemCount = databaseAccess.getCartItemCount();
                Log.d("itemCount", "" + itemCount);
                if (itemCount <= 0) {
                    txtTotalPrice.setVisibility(View.GONE);
                    btnSubmitOrder.setVisibility(View.GONE);
                    imgNoProduct.setVisibility(View.VISIBLE);
                    txtNoProduct.setVisibility(View.VISIBLE);
                }


            }

        });


        holder.CardviewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDS = Id;
                Options = options;
                Intent i = new Intent(context, Addofsion_Activity.class);
                context.startActivity(i);

            }
        });





        try {
        holder.txt_item_name.setText("ສີນຄ້າ: "+sale_name);
        DecimalFormat dfs = new DecimalFormat(
        "#,###.00",
        new DecimalFormatSymbols(new Locale("pt", "BR")));
            BigDecimal values = new BigDecimal(sale_price);
        holder.txt_price.setText("ມຸນຄ່າ: "+f.format(values)+" ກີບ");
        holder.txt_weight.setText("ຈຳນວນສັ່ງ:"+sale_qty+" ລາຍການ");
        }catch (Exception e){
       }



        //ການເພີ້ມຈຳນວນໃນກະຕາ
        holder.txtPlus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                try {
                        player.start();
                        String qty1 = holder.txtQtyNumber.getText().toString();
                        int get_qty = Integer.parseInt(qty1);
                        get_qty++;
                        holder.txtQtyNumber.setText("" + get_qty);
                        cust = get_qty;
                        int b = Integer.valueOf(sale_qty);
                        double ab = get_qty+b;
                        String bv = String.valueOf(f.format(ab));
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                        databaseAccess.open();
                        databaseAccess.updateProductQty(Id, "" + bv);
                        holder.txt_weight.setText("ຈຳນວນສັ່ງ:"+bv+" ລາຍການ");
                        totalPrice = totalPrice + Double.valueOf(sale_price);
                        txtTotalPrice.setText("("+"ຈຳນວນເງີນລວມ: "+f.format(totalPrice)+" ກິບ"+")");

                }catch (Exception e){
                }
             }
        });


        //ການລົດຈຳນວນລົງໃນກະຕາ
        holder.txt_minus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                        player.start();
                        String qty1 = holder.txtQtyNumber.getText().toString();
                        int get_qty = Integer.parseInt(qty1);
                        if (get_qty==0){
                        Toasty.error(context,"ຜິດພາດ", Toast.LENGTH_SHORT).show();
                        }else {
                        get_qty--;
                        holder.txtQtyNumber.setText("" + get_qty);
                        cust = get_qty;
                        int b = Integer.valueOf(sale_qty);
                        double ab = get_qty + b;
                        String bv = String.valueOf(f.format(ab));
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                        databaseAccess.open();
                        databaseAccess.updateProductQty(Id, "" + bv);
                        holder.txt_weight.setText("ຈຳນວນສັ່ງ:" + bv+" ລາຍການ");
                        totalPrice = totalPrice + Double.valueOf(sale_price);
                        txtTotalPrice.setText("("+"ຈຳນວນເງີນລວມ: "+f.format(totalPrice)+" ກິບ"+")");
                    }

            }
        });



    }
    @Override
    public int getItemCount() {
    return cartProduct.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
    TextView txt_item_name,txt_weight,txtQtyNumber,txtPlus,txt_minus,txt_price,status;
    ImageView img_delete;

    CardView CardviewId;
    ImageView cart_product_image;
    public MyViewHolder(View itemView) {
    super(itemView);
            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_weight = itemView.findViewById(R.id.txt_weight);
            txt_price = itemView.findViewById(R.id.txt_price);
            cart_product_image = itemView.findViewById(R.id.cart_product_image);
            img_delete = itemView.findViewById(R.id.img_delete);
            txtQtyNumber = itemView.findViewById(R.id.txt_number);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            txtPlus = itemView.findViewById(R.id.txt_plus);
            CardviewId = itemView.findViewById(R.id.CardviewId);
            status = itemView.findViewById(R.id.status);
        }
    }
}
