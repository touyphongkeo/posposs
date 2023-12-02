package com.app.pospos.adapter;

import android.content.Context;
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
import com.app.pospos.model.Catgory;
import com.app.pospos.model.financial_report;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.Utils;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinanreportAdapter extends RecyclerView.Adapter<FinanreportAdapter.MyViewHolder> {
    private List<financial_report> customerData;
    private Context context;
    Utils utils;

    public FinanreportAdapter(Context context, List<financial_report> customerData) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportdata_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String id = customerData.get(position).getId();
        String sale_save_bill = customerData.get(position).getSale_save_bill();
        String money = customerData.get(position).getMoney();
        String pay_day = customerData.get(position).getPay_day();
        String save_currency = customerData.get(position).getSave_currency();
        String online_pay = customerData.get(position).getOnline_pay();
        String rates = customerData.get(position).getRates();
        String receive = customerData.get(position).getReceive();
        String sale_save_status = customerData.get(position).getSale_save_status();


        holder.txtCustomerName.setText("ເລກບີນ: "+sale_save_bill);
        holder.txtCell.setText("ຈຳນວນເງີນ: "+money);
        holder.txtEmail.setText("ວັນທີເດືນນປີ: "+pay_day);





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
          /*  progressBar2.setVisibility(View.VISIBLE);
            Intent i = new Intent(context, EditCategoryActivity.class);
            i.putExtra("Id", customerData.get(getAdapterPosition()).getID());
            i.putExtra("category_id", customerData.get(getAdapterPosition()).getCategory_id());
            i.putExtra("category_name", customerData.get(getAdapterPosition()).getCategory_name());

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
