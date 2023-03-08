package com.app.pospos.adapter;

import static com.app.pospos.ClassLibs.Chart_id;
import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.category.EditCategoryActivity;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Product;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.product.Editproduct_Activity;
import com.app.pospos.utils.Utils;
import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_tAdapter extends RecyclerView.Adapter<Product_tAdapter.MyViewHolder> {
    private List<Product> productData;
    private Context context;
    Utils utils;

    public Product_tAdapter(Context context, List<Product> productData) {
        this.context = context;
        this.productData = productData;
        utils=new Utils();
    }


    @NonNull
    @Override
    public Product_tAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productt_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Product_tAdapter.MyViewHolder holder, int position) {
        final String code1 = productData.get(position).getCode1();
        String 	product_id = productData.get(position).getProduct_id();
        String barcode = productData.get(position).getBarcode();
        String category_id = productData.get(position).getCategory_id();
        String product_name = productData.get(position).getProduct_name();
        String qty = productData.get(position).getQty();
        String img_url = productData.get(position).getImg_url();



        holder.txtCustomerName.setText("ລະຫັດສີນຄ້າ: "+product_id);
        holder.txtCell.setText("ລະຫັດໜວດໜູ່: "+category_id);
        holder.txtEmail.setText("ສີນຄ້າ: "+product_name);
        holder.txt_address.setText("ຈຳນວນ: "+qty);

        String imageUrl= Constant.PRODUCT_IMAGE_URL+img_url;
//
       holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Chart_id = category_id;
                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle("ຄຳເຕືອນ")
                        .withMessage("ທ່ານຕ້ອງການລົບຂໍ້ມູນນີ້ ແທ້ ຫຼື ບໍ ?")
                        .withEffect(Slidetop)
                        .withDialogColor("#2979ff") //use color code for dialog
                        .withButton1Text("ຕົກລົງ")
                        .withButton2Text("ຍົກເລີກ")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (utils.isNetworkAvailable(context)) {
                                    delete_product(product_id);
                                    productData.remove(holder.getAdapterPosition());
                                    dialogBuilder.dismiss();
                                } else {
                                    dialogBuilder.dismiss();
                                    Toasty.error(context, "ບໍ່ສາມາດເຊື່ອມຕໍ່ອິນເຕີເນັດ", Toast.LENGTH_SHORT).show();
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





        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(context, Editproduct_Activity.class);
                s.putExtra(Constant.PRODUCT_ID, product_id);
                context.startActivity(s);

            }
        });



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

    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCustomerName, txtCell, txtEmail,txt_address;
        ImageView imgDelete, imgCall,cart_product_image;
        ProgressBar progressBar2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            txtCell = itemView.findViewById(R.id.txt_cell);
            txtEmail = itemView.findViewById(R.id.txt_email);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgCall = itemView.findViewById(R.id.img_call);
            cart_product_image = itemView.findViewById(R.id.cart_product_image);
            txt_address = itemView.findViewById(R.id.txt_address);
            progressBar2 = itemView.findViewById(R.id.progressBar_household2);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            progressBar2.setVisibility(View.VISIBLE);
            Intent i = new Intent(context, Editproduct_Activity.class);
            i.putExtra(Constant.PRODUCT_ID, productData.get(getAdapterPosition()).getProduct_id());
            context.startActivity(i);
        }
    }


    //delete from server
    private void delete_product(String product_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Product> call = apiInterface.deleteProduct(product_id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String value = response.body().getValue();

                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.success(context, "ການລົບຂໍ້ມູນຂອງທານສຳເລັດ", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    }

                    else if (value.equals(Constant.KEY_FAILURE)){
                        Toasty.error(context, "ການລົບຂໍ້ມູນຂອງທານຜິດພາດ", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(context, "ບໍ່ສາມາດເຊື່ອມຕໍ່ອິນເຕີເນັດ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
