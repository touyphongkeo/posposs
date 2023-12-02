package com.app.pospos.adapter;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.app.pospos.customer.EditcustomerActivity;
import com.app.pospos.model.Customer;
import com.app.pospos.model.Salelist;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalelistAdapter extends RecyclerView.Adapter<SalelistAdapter.MyViewHolder> {
    private List<Salelist> customerData;
    private Context context;
    Utils utils;

    DecimalFormat f;
    public SalelistAdapter(Context context, List<Salelist> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();
        f = new DecimalFormat("#,###");

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salelist_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final String customer_id = customerData.get(position).getsale_save_bill();
        String name = customerData.get(position).getsale_date();
        String tel = customerData.get(position).getsale_save_table();
        String time = customerData.get(position).getsale_qty();
        String date = customerData.get(position).getsale_amount();




        try {
            holder.txtCustomerName.setText("ເລກທີບີນ: "+customer_id);
            holder.txtCell.setText("ເບີໂຕະ: "+tel);
            holder.txtEmail.setText("ຈຳນວນຂາຍ: "+time+" ລາຍການ");
            holder.dates.setText("ວັນທີເດືອນ: "+name);

            DecimalFormat df = new DecimalFormat(
                    "#,###",
                    new DecimalFormatSymbols(new Locale("pt", "BR")));
            BigDecimal value = new BigDecimal(date);
            holder.txtAddress.setText("ຈຳນວນເງີນ: "+f.format(value) +" ກິບ");


        }catch (Exception e){

        }


        holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                String phone = "tel:" + tel;
                callIntent.setData(Uri.parse(phone));
                context.startActivity(callIntent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle(context.getString(R.string.delete))
                        .withMessage(context.getString(R.string.want_to_delete_customer))
                        .withEffect(Slidetop)
                        .withDialogColor("#2979ff") //use color code for dialog
                        .withButton1Text(context.getString(R.string.yes))
                        .withButton2Text(context.getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (utils.isNetworkAvailable(context)) {
                                   deleteCustomer(customer_id);
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

        TextView txtCustomerName, txtCell, txtEmail, txtAddress,dates;
        ImageView imgDelete, imgCall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            txtCell = itemView.findViewById(R.id.txt_cell);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtAddress = itemView.findViewById(R.id.txt_address);

            imgDelete = itemView.findViewById(R.id.img_delete);
            imgCall = itemView.findViewById(R.id.img_call);
            dates = itemView.findViewById(R.id.dates);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           /* Intent i = new Intent(context, EditcustomerActivity.class);
            i.putExtra(Constant.CUSTOMER_ID, customerData.get(getAdapterPosition()).getsale_save_bill());
            i.putExtra(Constant.CUSTOMERNAME, customerData.get(getAdapterPosition()).getsale_date());
            i.putExtra(Constant.CUSTOMER_TEL, customerData.get(getAdapterPosition()).getsale_save_table());
            i.putExtra(Constant.NUMBER_CARD, customerData.get(getAdapterPosition()).getsale_qty());
            i.putExtra(Constant.NUMBER_CARD, customerData.get(getAdapterPosition()).getsale_amount());
            context.startActivity(i);*/
        }
    }


    //delete from server
    private void deleteCustomer(String customer_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Customer> call = apiInterface.delete_customer(customer_id);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(@NonNull Call<Customer> call, @NonNull Response<Customer> response) {
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
            public void onFailure(@NonNull Call<Customer> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
