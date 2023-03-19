package com.app.pospos.adapter;

import android.content.Context;
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

import com.app.onlinesmartpos.R;
import com.app.pospos.database.DatabaseAccess;
import com.app.pospos.model.Catgory;
import com.app.pospos.model.Product;
import com.app.pospos.networking.ApiClient;
import com.app.pospos.networking.ApiInterface;
import com.app.pospos.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Catgory1Adapter extends RecyclerView.Adapter<Catgory1Adapter.MyViewHolder> {
    private List<Catgory> customerData;
    private Context context;
    Utils utils;

    RecyclerView recycler_views;
    ImageView imgNoProduct;
    TextView txtNoProducts;
    private ShimmerFrameLayout mShimmerViewContainer;


    public Catgory1Adapter(Context context, List<Catgory> customerData, RecyclerView recycler_views, ImageView imgNoProduct, TextView txtNoProducts, ShimmerFrameLayout mShimmerViewContainer) {
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
    public Catgory1Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Catgory1Adapter.MyViewHolder holder, int position) {
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        final String id = customerData.get(position).getID();
        String categoryId = customerData.get(position).getCategory_id();
        String category_name = customerData.get(position).getCategory_name();
        holder.txt_category_name.setText(category_name);


        holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //player.start();
              getProductsData(categoryId);

                mShimmerViewContainer.startShimmer();


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


                    } else {


                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        recycler_views.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        ProductAdapter productAdapter = new ProductAdapter(context, productsList);
                        recycler_views.setAdapter(productAdapter);

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



}
