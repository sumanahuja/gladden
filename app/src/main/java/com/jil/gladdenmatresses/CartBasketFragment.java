package com.jil.gladdenmatresses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CartBasketFragment extends Fragment {
   private Context context;
   private String cart_id="",app_token="",user_id="";
   private  String image_url[];
   private ImageView iv_product;
    public CartBasketFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cart_basket, container, false);
        context=view.getContext();
        iv_product=view.findViewById(R.id.img_product);
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
                //Log.i("slider_length", r.length() + "");
                JSONArray response = null;
                try {
                    response = r.getJSONArray("cartItems");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.length() > 0) {

                    //   Log.i("Market Listing Detail", response.toString());
                    image_url = new String[response.length()];
                    // Log.i("image size",image_url.length+"");
                    for (int i = 0; i < response.length(); i++) {

                        try {

                            JSONObject object = response.getJSONObject(i);

                            String SliderPhotos = object.getString("image");
                            // Log.i("Slider Res", object.getString("image"));
                            //image_url = new String[response.length()];
                            image_url[i] = "https://www.gladdenmattresses.com/uploads/product/" + object.getString("image");
                             Log.i("IMAGE_IF"+i,image_url[i]);
                            Picasso.with(context).load(image_url[0]).fit().into(iv_product);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
//                    FragmentMatresses.mysync mys = new FragmentMatresses.mysync();
//                    mys.execute(image_url);

                } else {

                }
                Log.i("image_slider__", image_url.toString());

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
