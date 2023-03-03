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
import com.app.pospos.category.Editcategory_Activity;
import com.app.pospos.model.Catgory;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import java.util.List;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.app.pospos.ClassLibs.Chart_id;
public class Categorys_tAdapter extends RecyclerView.Adapter<Categorys_tAdapter.MyViewHolder> {
    private List<Catgory> customerData;
    private Context context;
    Utils utils;

    public Categorys_tAdapter(Context context, List<Catgory> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();
    }


    @NonNull
    @Override
    public Categorys_tAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catgorydata_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Categorys_tAdapter.MyViewHolder holder, int position) {
        final String Id = customerData.get(position).getID();
        String 	category_id = customerData.get(position).getCategory_id();
        String chart_name = customerData.get(position).getCategory_name();
        holder.txtCustomerName.setText("ລະຫັດ: "+Id);
        holder.txtCell.setText("ລະຫັດໜວດໜູ່: "+category_id);
        holder.txtEmail.setText("ປະເພດ: "+chart_name);

//
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chart_id = category_id;
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
                                    deletecategory(category_id);
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
                Chart_id = category_id;
                Intent intent=new Intent(context, Editcategory_Activity.class);
                intent.putExtra("category_id",category_id);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCustomerName = itemView.findViewById(R.id.txt_customer_name);
            txtCell = itemView.findViewById(R.id.txt_cell);
            txtEmail = itemView.findViewById(R.id.txt_email);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgCall = itemView.findViewById(R.id.img_call);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
       /*     Intent i = new Intent(context, EditChartaccountActivity.class);
            i.putExtra("Id", customerData.get(getAdapterPosition()).getId());
            i.putExtra("chart_id", customerData.get(getAdapterPosition()).getchart_id());
            i.putExtra("chart_name", customerData.get(getAdapterPosition()).getchart_name());
            i.putExtra("type", customerData.get(getAdapterPosition()).getype());

            context.startActivity(i);*/
        }
    }


    //delete from server
    private void deletecategory(String category_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Catgory> call = apiInterface.deleteCategory(category_id);
        call.enqueue(new Callback<Catgory>() {
            @Override
            public void onResponse(@NonNull Call<Catgory> call, @NonNull Response<Catgory> response) {


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
            public void onFailure(@NonNull Call<Catgory> call, Throwable t) {
                Toast.makeText(context, "Error! " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
