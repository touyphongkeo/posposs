package com.app.pospos.adapter;

import static com.app.pospos.ClassLibs.Image;
import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import com.app.pospos.image.Get_imageshow;
import com.app.pospos.model.Product;
import com.app.pospos.pos.Pos2Activity;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class Product2Adapter extends RecyclerView.Adapter<Product2Adapter.MyViewHolder> {
    private List<Product> customerData;
    private Context context;
    Utils utils;
    MediaPlayer player;
    DecimalFormat f;
    DatabaseAccess databaseAccess;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String username ="";
    Integer cust =0;
    TextView table;

    public Product2Adapter(Context context, List<Product> customerData) {
    this.context = context;
    this.customerData = customerData;
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
    public Product2Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pos2_product_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Product2Adapter.MyViewHolder holder, int position) {
        final String code1 = customerData.get(position).getCode1();
        String product_id = customerData.get(position).getProduct_id();
        String barcode = customerData.get(position).getBarcode();
        String product_name = customerData.get(position).getProduct_name();
        String category_id = customerData.get(position).getCategory_id();
        String bprice = customerData.get(position).getBprice();
        String price = customerData.get(position).getPrice();
        String qty = customerData.get(position).getQty();
        String size = customerData.get(position).getSize();
        String img_url = customerData.get(position).getImg_url();
        String cook = customerData.get(position).getCook();
        String cut_qty = customerData.get(position).getCut_qty();

      //  holder.check_qty.setBackgroundColor(Color.parseColor("#1566e0"));
     //   holder.check_qty.setText("ຈຳນວນໃນສາງ: "+qty);


        String imageUrl= Constant.PRODUCT_IMAGE_URL+img_url;
        try {
            holder.txt_product_name.setText(product_name);
            DecimalFormat df = new DecimalFormat(
            "#,###",
            new DecimalFormatSymbols(new Locale("pt", "BR")));
            BigDecimal value = new BigDecimal(price);
            holder.txt_price.setText("ລາຄາ: "+f.format(value) +" ກິບ");
        }catch (Exception e){

        }


        if (img_url != null) {
            if (img_url.length() < 3) {
                holder.images.setImageResource(R.drawable.image_placeholder);
            } else {
                Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_placeholder)
                .into(holder.images);
            }
        }


        int ab = Integer.parseInt(qty);
        int cus = 1;
        int cuty = Integer.parseInt(cut_qty);

        if(qty.equals("0")){
            holder.check_qty.setBackgroundColor(Color.parseColor("#E32626"));
            holder.check_qty.setText("ສີນຄ້າບໍມີຈຳນວນ: "+qty+" ລາຍການ");
        }else {
            holder.check_qty.setBackgroundColor(Color.parseColor("#0879D3"));
            holder.check_qty.setText("ສີນຄ້າມີຈຳນວນ: "+qty+" ລາຍການ");
        }

        holder.txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String qty1 = holder.txt_number.getText().toString();
                    int get_qty = Integer.parseInt(qty1);
                    if (get_qty>=ab && cuty==cus) {
                  //  Toasty.error(context, "ຈຳນວນສີນຄ້າໃນສ້າງບໍພຽງພໍ!", Toast.LENGTH_SHORT).show();
                        changeOnConfirm2();
                    } else {
                    get_qty++;
                    holder.txt_number.setText("" + get_qty);
                    cust = get_qty;
                    }
                }catch (Exception e){
                }
            }
        });

        holder.card_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.start();
                Intent intent=new Intent(context, Get_imageshow.class);
                intent.putExtra("product_id",product_id);
                context.startActivity(intent);
            }
        });



        holder.txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty1 = holder.txt_number.getText().toString();
                int get_qty = Integer.parseInt(qty1);
                if (get_qty==0) {
                Toasty.error(context, "ຜິດຜາດ!", Toast.LENGTH_SHORT).show();
                } else {
                get_qty--;
                holder.txt_number.setText("" + get_qty);
                cust = get_qty;
                }
            }
        });




        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               player.start();
                Image =img_url;
                databaseAccess.open();
                long date = System.currentTimeMillis();
                SimpleDateFormat sdfs = new SimpleDateFormat("yyyy");
                String dateStrings = sdfs.format(date);
                String sale_bill = dateStrings+"000001";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sale_date = sdf.format(new Date());
                String sale_status = "1";
                String edit_sale = "1";

                if (cust.equals(0)){
                 //   Toasty.error(context, "ກະລຸນາກຳນົດຈຳນວນກອນ", Toast.LENGTH_SHORT).show();
                    changeOnConfirm3();
                }else {

                int check = databaseAccess.addToCart(SALE_BILL,sale_date,Tbname,product_id,product_name,price,cust.toString(),sale_status,edit_sale,username,Image,cut_qty);
                databaseAccess.open();
                int count=databaseAccess.getCartItemCount();
                if (count==0) {
                    Pos2Activity.txtCount.setVisibility(View.INVISIBLE);
                } else{
                    Pos2Activity. txtCount.setVisibility(View.VISIBLE);
                    Pos2Activity.txtCount.setText(String.valueOf(count));
                }
                if (check == 1) {
                 //   Toasty.success(context, "ສັງສຳເລັດ", Toast.LENGTH_SHORT).show();
                    player.start();
                    successMessage();
                } else if (check == 2) {
                    Toasty.info(context, "ສີນຄ້ານີ້ມີແລ້ວ", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(context, "ຜິດຜາດ", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_product_name,txt_price,txt_number,check_qty;
        ImageView images;
        Button btnAddToCart,txt_plus,txt_minus;
        CardView card_product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            images = itemView.findViewById(R.id.images);
            txt_number = itemView.findViewById(R.id.txt_number);
            btnAddToCart = itemView.findViewById(R.id.btn_add_cart);
            txt_plus = itemView.findViewById(R.id.txt_plus);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            card_product = itemView.findViewById(R.id.card_product);
            check_qty = itemView.findViewById(R.id.check_qty);
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


    public void successMessage() {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText("ເລືອກ!!").setContentText("ເລືອກສີນຄ້າສຳເລັດ!").show();

    }

    public void changeOnConfirm2() {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("ຜິດພາດ!!").setContentText("ກະລຸນາກວດເບີງຈຳນວນສີນຄ້າ!").show();

    }

    public void changeOnConfirm3() {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("ຜິດພາດ!!").setContentText("ກະລຸນາກຳນົດຈຳນວນກອນ!").show();

    }
}
