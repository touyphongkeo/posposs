package com.app.pospos.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pospos.category.EditCategoryActivity;
import com.app.pospos.database.DatabaseAccess;
import com.app.pospos.model.Catgory;
import com.app.pospos.utils.Utils;
import com.app.onlinesmartpos.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;


public class CatgoryAdapter extends RecyclerView.Adapter<CatgoryAdapter.MyViewHolder> {
    private List<Catgory> customerData;
    private Context context;
    Utils utils;

    RecyclerView recycler_views;
    ImageView imgNoProduct;
    TextView txtNoProducts;
    private ShimmerFrameLayout mShimmerViewContainer;


    public CatgoryAdapter(Context context, List<Catgory> customerData, RecyclerView recycler_views, ImageView imgNoProduct, TextView txtNoProducts, ShimmerFrameLayout mShimmerViewContainer) {
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
    public CatgoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CatgoryAdapter.MyViewHolder holder, int position) {
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        final String id = customerData.get(position).getID();
        String categoryId = customerData.get(position).getCategory_id();
        String category_name = customerData.get(position).getCategory_name();
        holder.txt_category_name.setText(category_name);


     /*   holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //player.start();
              getProductsData(categoryId);

              mShimmerViewContainer.startShimmer();


            }
        });*/
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
            Intent i = new Intent(context, EditCategoryActivity.class);
            i.putExtra("customer_id", customerData.get(getAdapterPosition()).getID());
            i.putExtra("customer_name", customerData.get(getAdapterPosition()).getCategory_id());
            i.putExtra("customer_name", customerData.get(getAdapterPosition()).getCategory_name());

            context.startActivity(i);
        }
    }







}
