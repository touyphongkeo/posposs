package com.app.pospos.adapter;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.R;
import com.app.pospos.Constant;
import com.app.pospos.model.Staff;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.staff.EditstaffActivity;
import com.app.pospos.utils.Utils;
import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.MyViewHolder> {
    private List<Staff> customerData;
    private Context context;
    Utils utils;


    public StaffAdapter(Context context, List<Staff> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String id = customerData.get(position).getId();
        String use_code = customerData.get(position).getuser_code();
        String userid = customerData.get(position).getUserid();
        String username = customerData.get(position).getUsername();
        String userstatus = customerData.get(position).getUserstatus();
        String userpass = customerData.get(position).getuserpass();
        String sale = customerData.get(position).getSale();
        String tbl = customerData.get(position).geTbl();
        String report = customerData.get(position).getReport();
        String stock = customerData.get(position).getStock();
        String setup = customerData.get(position).getsetup();
        String users = customerData.get(position).getusers();
        String edit = customerData.get(position).getedit();
        String img_urls = customerData.get(position).getimg_urls();
        String email = customerData.get(position).getemail();

        holder.txtCustomerName.setText(email);
        holder.txtCell.setText("ລະຫັດ: "+userid);
        holder.txtEmail.setText("ຊື່: "+username);
        holder.txtAddress.setText("ສະຖານະ: "+userstatus);

        String imageUrl= Constant.PRODUCT_IMAGE_URL+img_urls;

        if (img_urls != null) {
            if (img_urls.length() < 3) {
                holder.cart_product_image.setImageResource(R.drawable.image_placeholder);
            } else {
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.image_placeholder)
                        .into(holder.cart_product_image);
            }
        }



       // holder.imgCall.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
             //   Intent s = new Intent(context, Editproduct_Activity.class);
               // s.putExtra(Constant.USE_CODE, use_code);
               // s.putExtra(Constant.IMG_URLS, img_urls);
             //   context.startActivity(s);

          //  }
     //   });



        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle(context.getString(R.string.delete))
                        .withMessage("ທ່ານຕ້ອງການລົບຂໍ້ມູນນີ້ແທ້ຫຼື ບໍ?")
                        .withEffect(Slidetop)
                        .withDialogColor("#2979ff") //use color code for dialog
                        .withButton1Text(context.getString(R.string.yes))
                        .withButton2Text(context.getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (utils.isNetworkAvailable(context)) {
                                   deletStaff(use_code);
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

    @Override
    public int getItemCount() {
        return customerData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtCustomerName, txtCell, txtEmail, txtAddress;
        ImageView imgDelete, imgCall,cart_product_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            txtCell = itemView.findViewById(R.id.txt_cell);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtAddress = itemView.findViewById(R.id.txt_address);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgCall = itemView.findViewById(R.id.img_call);
            cart_product_image = itemView.findViewById(R.id.cart_product_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditstaffActivity.class);
            i.putExtra(Constant.USE_CODE, customerData.get(getAdapterPosition()).getuser_code());
            i.putExtra(Constant.IMG_URLS, customerData.get(getAdapterPosition()).getimg_urls());
            i.putExtra(Constant.SP_EMAIL, customerData.get(getAdapterPosition()).getUserid());
            i.putExtra(Constant.USERNAME, customerData.get(getAdapterPosition()).getUsername());
            i.putExtra(Constant.USERPASS, customerData.get(getAdapterPosition()).getuserpass());
            i.putExtra(Constant.EMAIL, customerData.get(getAdapterPosition()).getemail());
            i.putExtra(Constant.USERSTATUS, customerData.get(getAdapterPosition()).getUserstatus());
            context.startActivity(i);
        }
    }


    //delete from server
    private void deletStaff(String use_code) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Staff> call = apiInterface.deletestaff(use_code);
        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(@NonNull Call<Staff> call, @NonNull Response<Staff> response) {
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
            public void onFailure(@NonNull Call<Staff> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
