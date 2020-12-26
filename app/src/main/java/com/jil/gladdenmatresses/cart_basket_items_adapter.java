package com.jil.gladdenmatresses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class product_category_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private Context context;

    private ArrayList<product_category_model> exampleItems;

    public product_category_adapter(Product_category activity , ArrayList<product_category_model> exampleList) {
        this.mActivity = activity;
        exampleItems = exampleList;
        context=activity.getApplicationContext();

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        populateItemRows((MyViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        Log.i("size",exampleItems.size()+"");
        return exampleItems == null ? 0 : exampleItems.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,description;
        Button btn_show_more;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.pc_imageView);
            title=itemView.findViewById(R.id.pc_title);
            description=itemView.findViewById(R.id.pc_description);
            btn_show_more=itemView.findViewById(R.id.btn_show_more);


        }
    }
    private void populateItemRows(final MyViewHolder holder, int position) {

        final product_category_model  current = exampleItems.get(position);
            String image_url,title,description;
        image_url= "https://www.gladdenmattresses.com/uploads/product/"+current.getImage_url();
        title=current.getList_title();
        description=current.getList_description();

        Picasso.with(context).load(image_url).fit().into(holder.imageView);
        holder.title.setText(title);
        holder.description.setText(description);
        Log.i("title"+position,holder.title.getText().toString());
        holder.btn_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mActivity, DrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("open", "detailpage");
                i.putExtra("list_id",   current.getList_id());
                i.putExtra("list_url_name", current.getList_url_name());
                Log.i("DDD",current.getList_id()+current.getList_url_name());
// Now start your activity
                context.startActivity(i);
//                Intent intent=new Intent(mActivity,CategoryDetail.class);
//                intent.putExtra("list_id",   current.getList_id());
//                intent.putExtra("list_url_name", current.getList_url_name());
//                context.startActivity(intent);
            }
        });
    }
}
