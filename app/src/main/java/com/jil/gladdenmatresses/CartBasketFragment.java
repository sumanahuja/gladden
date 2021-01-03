package com.jil.gladdenmatresses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CartBasketFragment extends Fragment {
   private Context context;
   private String cart_id="",app_token="",user_id="";
  // private  String image_url[];
   private RecyclerView recyclerView;
   private cart_items_model cart_model;
   private ArrayList<cart_items_model> cart_items_list;
   private View view;

    public CartBasketFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_cart_basket, container, false);
        context=view.getContext();
        recyclerView=view.findViewById(R.id.recycler_cart_items);
        cart_items_list=new ArrayList<cart_items_model>();

        //iv_product=view.findViewById(R.id.img_product);
        getPrefs();
        try {
            if (app_token.equals("test")) {
                user_id = mySharedPrefClass.getInstance(context).get_data().getId() + "";
            }
        }catch (Exception e){e.printStackTrace();}

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/cart/get?app_token="+app_token+"&user_id="+user_id+"&cart_id="+cart_id+"&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject r) {
                Log.i("response_size", r.length() + "");
                JSONArray response = null;
                try {
                    response = r.getJSONArray("cartItems");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.length() > 0) {

                       Log.i("cart", response.toString());
                  //  image_url = new String[response.length()];
                     Log.i("cart_items",response.length()+"");
                    Toast.makeText(context, ""+response.length(), Toast.LENGTH_SHORT).show();
                  //  image_url=new String[response.length()];
                    for (int i = 0; i < response.length(); i++) {

                        try {

                            JSONObject object = response.getJSONObject(i);
                            String item_id = object.getString("id");
                            String cart_id = object.getString("cart_id");
                            String img_URL = object.getString("image");
                            String p_name=object.getString("product_name");
                            int j=0;
                            String list_url="";
                            char []c=new char[p_name.length()];
                            while(j<p_name.length()){
                                if(" ".equals(p_name.charAt(j)+""))
                                {c[j]='-';}
                                else
                                    c[j]=p_name.charAt(j);
                                Log.i("C=",c[j]+"");
                                list_url=list_url+c[j]+"";
                                j++;
                            }
list_url=list_url.toLowerCase();
                            String p_code="SKU: "+object.getString("sku");
                            String p_price="";
                            if(!object.getString("price").equals("null")){
                             p_price= object.getDouble("price")+"";}
                            String p_id= object.getString("product_id");
                            String p_quantity=object.getString("qty");
                            String p_height="";
                            if(!object.getString("height_detail").equals("null")){
                            JSONObject height_obj=new JSONObject(object.getString("height_detail")+"");
                                 p_height= height_obj.getString("title");
                            }
                            String p_dimenssion="";
                            if(!object.getString("dimension_detail").equals("null")) {
                                JSONObject dimenssion_obj = new JSONObject(object.getString("dimension_detail"));
                                 p_dimenssion = dimenssion_obj.getString("title");
                            }
                            String total=p_price.isEmpty()?"":(Double.parseDouble(p_quantity)*Double.parseDouble(p_price)+"");
                            cart_model=new cart_items_model(item_id,cart_id,p_id, list_url, img_URL,p_name, p_code, p_price,total,  p_dimenssion+", "+p_height,  p_quantity);
                            cart_items_list.add(cart_model);
                            Log.i("added_in_list1",list_url+"   "+p_id );
                            Log.i("added_in_list",cart_items_list.size()+"");
                            // Log.i("Slider Res", object.getString("image"));
                            //image_url = new String[response.length()];
                         //   image_url[i] = "https://www.gladdenmattresses.com/uploads/product/" + object.getString("image");
                           //  Log.i("IMAGE_IF"+i,image_url[i]);
                           // Picasso.with(context).load(image_url[0]).fit().into(iv_product);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                    Toast.makeText(context, ""+cart_items_list.size(), Toast.LENGTH_SHORT).show();
                    cart_basket_items_adapter adapter=new cart_basket_items_adapter(view,getActivity(),cart_items_list);
                    LinearLayoutManager llm = new LinearLayoutManager(context);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(adapter);
//                    FragmentMatresses.mysync mys = new FragmentMatresses.mysync();
//                    mys.execute(image_url);

                } else {

                }
//                Log.i("image_slider__", image_url.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        requestQueue.add(objectRequest);

        return view;
    }
    void getPrefs()
    {
        SharedPreferences prefs = context.getSharedPreferences("data", 0);

        app_token=prefs.getString("app_token","" );
        cart_id=prefs.getString("cart_id", "");

    }
}
