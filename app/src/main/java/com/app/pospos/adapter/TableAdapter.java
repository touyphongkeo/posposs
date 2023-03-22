package com.app.pospos.adapter;
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

import com.app.pospos.Constant;
import com.app.pospos.model.Table;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.pos.Pos2Activity;
import com.app.pospos.pos.PosActivity;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;
import static com.app.pospos.ClassLibs.Status;
import static com.app.pospos.ClassLibs.Tbname;
import static com.app.pospos.ClassLibs.SALE_BILL;
import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder> {

    private List<Table> customerData;
    private Context context;
    Utils utils;
    public TableAdapter(Context context, List<Table> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();



    }


    @NonNull
    @Override
    public TableAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final TableAdapter.MyViewHolder holder, int position) {
        final String id = customerData.get(position).getId();
        String tbname = customerData.get(position).getTbname();
        String Statustable = customerData.get(position).getStatustable();
        String statustable = customerData.get(position).getStatustable();
        String stt = customerData.get(position).getStt();
        String linkid = customerData.get(position).getLinkid();
        String tbl = customerData.get(position).getTbl();
        String sale_bill = customerData.get(position).get_sale_bill();


        Statustable = Statustable.trim();

        if(Statustable.equals("1")){
            holder.layout.setBackgroundColor(Color.parseColor("#ABB1B8"));
            holder.txt_category_name.setText(tbname +" ວ່າງ");
        }else if(Statustable.equals("2")){
            holder.layout.setBackgroundColor(Color.parseColor("#FB4106"));
            holder.txt_category_name.setText(tbname +" ບໍວ່າງ");
        }else if(Statustable.equals("3")){
            holder.layout.setBackgroundColor(Color.parseColor("#CA9907"));
            holder.txt_category_name.setText(tbname +" ເຊັກບີນແລ້ວ");
        }
        else{
            holder.layout.setBackgroundColor(Color.parseColor("#ABB1B8"));
            holder.txt_category_name.setText(tbname +" ວ່າງ");
        }


        holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status = statustable;
                Tbname = tbname;
                SALE_BILL = sale_bill;
                if (Status.equals("1")){
                    Intent i = new Intent(context, PosActivity.class);
                    context.startActivity(i);
                }else if (Status.equals("2")){
                    Intent i = new Intent(context, Pos2Activity.class);
                    context.startActivity(i);
                }else if (Status.equals("4")){
//                    Intent i = new Intent(context, PosActivity.class);
//                    context.startActivity(i);
                }else if (Status.equals("3")) {
                    NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                    dialogBuilder
                            .withTitle("ຄຳເຕືອນ")
                            .withMessage("ຂໍອະໄພລໍຖ້າເກັບເງີນສຳເລັດກອນ......")
                            .withEffect(Slidetop)
                            .withDialogColor("#2979ff") //use color code for dialog
                            .withButton1Text("ຕົກລົງ")
                            .withButton2Text("ຍົກເລີກ")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (utils.isNetworkAvailable(context)) {
                                       // update_table1(Tbname);
                                        customerData.remove(holder.getAdapterPosition());
                                        dialogBuilder.dismiss();
                                    }
                                    else
                                    {
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


            }
        });
    }



    @Override
    public int getItemCount() {
        return customerData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_category_name;
        CardView card_category;
        ImageView imgStatus;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_category_name = itemView.findViewById(R.id.txt_category_name);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            card_category = itemView.findViewById(R.id.card_category);
            layout = itemView.findViewById(R.id.layout);


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
