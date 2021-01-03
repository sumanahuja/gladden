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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cart_basket_items_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private Context context;
    private String USER_ID,app_token,cart_id;
    private ArrayList<cart_items_model> exampleItems;

    public cart_basket_items_adapter(View view ,Activity activity, ArrayList<cart_items_model> exampleList) {
     this.mActivity = activity;
        exampleItems = exampleList;
        context=view.getContext();

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cart_basket_items_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        populateItemRows((MyViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
//        Log.i("size",exampleItems.size()+"");
        return exampleItems == null ? 0 : exampleItems.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,img_delete;
        TextView tv_heading,tv_code,tv_price,tv_total_price,tv_product_specifications;
        EditText et_quantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_product);
            tv_heading=itemView.findViewById(R.id.tv_product_name);
            tv_code=itemView.findViewById(R.id.tv_product_code);
            tv_product_specifications=itemView.findViewById(R.id.tv_product_specifications);
            tv_price=itemView.findViewById(R.id.tv_product_price);
            tv_total_price=itemView.findViewById(R.id.tv_product_total_price);
            et_quantity=itemView.findViewById(R.id.tv_product_quantity);
            img_delete=itemView.findViewById(R.id.img_product_delete);


        }
    }
    private void populateItemRows(final MyViewHolder holder, int position) {

        final cart_items_model  current = exampleItems.get(position);
            String image_url,list_title,list_code,list_price,list_total_price,list_specifications,list_quantity;

        image_url= "https://www.gladdenmattresses.com/uploads/product/"+current.getImage_url();
        list_title=current.getList_title();
        list_code=current.getList_code();
        list_price=current.getList_price();
        list_total_price=current.getList_total_price();
        list_specifications=current.getList_specifications();
        list_quantity=current.getList_quantity();

        Picasso.with(context).load(image_url).fit().into(holder.imageView);
        holder.tv_heading.setText(list_title);
        holder.tv_code.setText(list_code);
        holder.tv_price.setText(list_price);
        holder.tv_total_price.setText(list_total_price);
        holder.tv_product_specifications.setText(list_specifications);
        holder.et_quantity.setText(list_quantity);
        //Log.i("title"+position,holder.title.getText().toString());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DDD",current.getList_id()+current.getList_url());
                Intent i = new Intent(mActivity, DrawerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("open", "detailpage");
                i.putExtra("list_id",   current.getList_id());
                i.putExtra("list_url_name", current.getList_url());
                Log.i("DDD",current.getList_id()+current.getList_url());
                // Now start your activity
                context.startActivity(i);

            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/cart/delete?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
Log.i("delete_url",url);
                RequestQueue requestQueue = Volley.newRequestQueue(context);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("Response_Delete_Item", response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("status").equals("true")) {
//                                Toast.makeText(context, "add to card , total items" + jsonObject.getString("totalitems"), Toast.LENGTH_SHORT).show();
//                                SharedPreferences prefs = context.getSharedPreferences("data", 0);
//                                SharedPreferences.Editor editor = prefs.edit();
//                                editor.putString("app_token", app_token);
//                                editor.putString("cart_id", jsonObject.getString("cart_id"));
//                                editor.apply();
//
//                            } else if (jsonObject.getString("status").equals("false")) {
//                                //   Toast.makeText(context, "" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(context, "" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
//
//                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences prefs = context.getSharedPreferences("data", 0);
                        cart_id=prefs.getString("cart_id","");
                        //  getData();
                        UserModel u;
                        try {

                            mySharedPrefClass user = new mySharedPrefClass(context);
                            u= user.get_data();
                            USER_ID=u.getId()+"";
                            app_token="test";
                        }catch(Exception e){
                            e.printStackTrace();
                            USER_ID="";
                            //SharedPreferences prefs = context.getSharedPreferences("data", 0);
                            app_token=prefs.getString("app_token","");
                             //cart_id=prefs.getString("cart_id","");
                        }
                        String URL="https://www.gladdenmattresses.com/api/jil.0.1/v2/cart/delete?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs"+",app_token=" +app_token + ",user_id=" +USER_ID+ ",cart_id=" +cart_id+",cart_item_id=" +current.getItem_id()+ ",product_id="+ current.getList_id();
                      Log.i("URL1",URL);
                       Map<String, String> params = new HashMap<String, String>();
                        params.put("app_token",app_token);
                        params.put("user_id",USER_ID);
                        params.put("cart_id", cart_id);
                        params.put("cart_item_id",current.getItem_id() );
                        params.put("product_id", current.getList_id());
                        //params.put("api_token", "awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs");
Log.i("map", "app_token" +app_token + "user_id" +USER_ID+ "cart_id" +cart_id+"cart_item_id" +current.getItem_id()+ "product_id"+ current.getList_id());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);


            }
        });
    }
}
