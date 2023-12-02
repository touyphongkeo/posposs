package com.app.pospos.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
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
import com.app.pospos.database.DatabaseAccess;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Product;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Catgory2Adapter extends RecyclerView.Adapter<Catgory2Adapter.MyViewHolder> {
    private List<Catgory> customerData;
    private Context context;
    Utils utils;

    RecyclerView recycler_views;
    ImageView imgNoProduct;
    TextView txtNoProducts;
    int row_index = -1;
    String category_name;
    private ShimmerFrameLayout mShimmerViewContainer;


    public Catgory2Adapter(Context context, List<Catgory> customerData, RecyclerView recycler_views, ImageView imgNoProduct, TextView txtNoProducts, ShimmerFrameLayout mShimmerViewContainer) {
        this.context = context;
        this.customerData = customerData;
        utils=new Utils();
        this.recycler_views=recycler_views;
        this.imgNoProduct=imgNoProduct;
        this.txtNoProducts=txtNoProducts;
        this.mShimmerViewContainer=mShimmerViewContainer;
    }


    @NonNull
    @Override
    public Catgory2Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Catgory2Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        final String id = customerData.get(position).getID();
        String categoryId = customerData.get(position).getCategory_id();
        category_name = customerData.get(position).getCategory_name();
        holder.txt_category_name.setText(category_name);
        holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //player.start();
                getProductsData(categoryId);
                mShimmerViewContainer.startShimmer();
                row_index=position;
                notifyDataSetChanged();

            }
        });



        if (row_index==position) {
            holder.layout.setBackgroundColor(Color.parseColor("#3C8E40"));
            holder.txt_category_name.setTextColor(Color.parseColor("#dddddd"));
           // Toasty.success(context, category_name.toString(), Toast.LENGTH_SHORT).show();
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#dddddd"));
            holder.txt_category_name.setTextColor(Color.parseColor("#88000000"));
        }
    }



    @Override
    public int getItemCount() {
        return customerData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_category_name;
        CardView card_category;
        LinearLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_category_name = itemView.findViewById(R.id.txt_category_name);
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




    public void getProductsData(String categoryId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Product>> call;
        call = apiInterface.get_catgoryId(categoryId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productsList;
                    productsList = response.body();
                    if (productsList.isEmpty()) {
                        recycler_views.setVisibility(View.GONE);
                        imgNoProduct.setVisibility(View.VISIBLE);
                        imgNoProduct.setImageResource(R.drawable.not_found);
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        changeOnConfirm3();
                    } else {
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        recycler_views.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        Product2Adapter product2Adapter = new Product2Adapter(context, productsList);
                        recycler_views.setAdapter(product2Adapter);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.something_went_wrong +"OK", Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });
    }
    public void changeOnConfirm3() {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText("ປະເພດນີ້").setContentText("ບໍມີຂໍ້ມູນ!").show();
    }
}
