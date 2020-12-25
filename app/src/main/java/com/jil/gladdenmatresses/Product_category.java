package com.jil.gladdenmatresses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Product_category extends AppCompatActivity {
Toolbar toolbar;
RecyclerView pc_listview;
ArrayList<product_category_model> arrayList;
product_category_adapter adapter;
String toolbar_name,banner_url;
ImageView IV_banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        toolbar=findViewById(R.id.toolbar2);
       toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        IV_banner=findViewById(R.id.banner);
        pc_listview=findViewById(R.id.pc_listview);
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            toolbar_name=bundle.getString("toolbar_name","Matresses Category");
            banner_url=bundle.getString("banner_url","https://www.gladdenmattresses.com/uploads/category/compress-mobile-banner_48397.jpg");
            Log.i("banner_url",banner_url);
            Picasso.with(getApplicationContext()).load(banner_url).fit().into(IV_banner);
            arrayList = new ArrayList<>();
            arrayList = bundle.getParcelableArrayList("ARRAYLIST");
            toolbar.setTitle(toolbar_name);
            //apiCall();
            adapter = new product_category_adapter(Product_category.this, arrayList);
            Log.i("exampleItems", arrayList.size() + "");
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            pc_listview.setLayoutManager(llm);
            pc_listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
                    onBackPressed();
                }
            });
        }catch(Exception e){e.printStackTrace();}

    }

   /* void apiCall_spinal(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product?page=1&per_page=15&caturl=spinal-care-series&catid=3&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("data");
                    Log.i("slider_length", jsonArray.length() + "" + jsonArray.toString());
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        object.getString("model_image");
                        object.getString("list_title");
                        object.getString("list_desc");
                        Log.i("object", object.getString("list_title"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if (response.length() > 0) {
//
//                    //   Log.i("Market Listing Detail", response.toString());
//                    image_url = new String[response.length()];
//                    // Log.i("image size",image_url.length+"");
//                    for (int i = 0; i < response.length(); i++) {
//
//                        try {
//
//                            JSONObject object = response.getJSONObject(i);
//
//                            String SliderPhotos = object.getString("image");
//                            // Log.i("Slider Res", object.getString("image"));
//                            //image_url = new String[response.length()];
//                            image_url[i] = "https://www.gladdenmattresses.com/uploads/banner/" + object.getString("image");
//
//                        } catch (Exception e) {
//
//                            e.printStackTrace();
//                        }
//
//                    }
//                    DrawerActivity.mysync mys = new DrawerActivity.mysync();
//                    mys.execute(image_url);
//
//                } else {
//
//
//                }
//                Log.i("image_slider__", image_url.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }*/
}