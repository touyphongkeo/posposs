package com.app.pospos.adapter;

import static com.app.pospos.ClassLibs.Id_table;
import static com.app.pospos.ClassLibs.Idd;
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
import com.app.pospos.model.Table;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.table.EdittableActivity;
import com.app.pospos.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listable_tAdapter extends RecyclerView.Adapter<Listable_tAdapter.MyViewHolder> {
    private List<Table> customerData;
    private Context context;
    Utils utils;

    public Listable_tAdapter(Context context, List<Table> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();
    }


    @NonNull
    @Override
    public Listable_tAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listabledata_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Listable_tAdapter.MyViewHolder holder, int position) {

        final String id = customerData.get(position).getId();
        String tbname = customerData.get(position).getTbname();
        String Statustable = customerData.get(position).getStatustable();
        String statustable = customerData.get(position).getStatustable();
        String stt = customerData.get(position).getStt();
        String linkid = customerData.get(position).getLinkid();
        String tbl = customerData.get(position).getTbl();
        String sale_bill = customerData.get(position).get_sale_bill();

        holder.txtCustomerName.setText("ລຳດັບ: "+id);
        holder.txtCell.setText("ເລກໂຕະ: "+tbname);
        holder.txtEmail.setText("ສະຖານະ: "+Statustable);



//
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id_table = tbname;
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
                                    deletecategory(tbname);
                                    customerData.remove(holder.getAdapterPosition());
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
                Idd = id;
                Intent intent=new Intent(context, EdittableActivity.class);
                intent.putExtra("Id",id);
                intent.putExtra("tbname",tbname);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCustomerName, txtCell, txtEmail;
        ImageView imgDelete, imgCall;
        ProgressBar progressBar2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            txtCell = itemView.findViewById(R.id.txt_cell);
            txtEmail = itemView.findViewById(R.id.txt_email);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgCall = itemView.findViewById(R.id.img_call);
            progressBar2 = itemView.findViewById(R.id.progressBar_household2);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           progressBar2.setVisibility(View.VISIBLE);
            Intent i = new Intent(context, EdittableActivity.class);
            i.putExtra("Id", customerData.get(getAdapterPosition()).getId());
            i.putExtra("tbname", customerData.get(getAdapterPosition()).getTbname());
            context.startActivity(i);
        }
    }


    //delete from server
    private void deletecategory(String tbname) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Table> call = apiInterface.deletetable(tbname);
        call.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(@NonNull Call<Table> call, @NonNull Response<Table> response) {
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
            public void onFailure(@NonNull Call<Table> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
