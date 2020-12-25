package com.jil.gladdenmatresses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class FragmentAbout extends Fragment {
private TextView cmsHead,cmsDetail,cmsDetail2;
private ImageView imageView,imageView2;
private Context context;
//private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about, container, false);
        context=view.getContext();
        cmsHead=view.findViewById(R.id.cms_heading);
        cmsDetail=view.findViewById(R.id.cms_detail);
        cmsDetail2=view.findViewById(R.id.cms_detail2);
        imageView=view.findViewById(R.id.cms_image);
        imageView2=view.findViewById(R.id.cms_image2);
//        toolbar=view.findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //getActivity().onBackPressed();
//                startActivity(new Intent(getContext(),DrawerActivity.class));
//            }
//        });
//        toolbar.setTitle("About Us");

        apiCallForCMS();
        // Inflate the layout for this fragment
        return view;
    }
    void apiCallForCMS() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/cms?urlname=about-us&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response = response.getJSONObject("data");
                    Log.i("response", response.getString("disp_name") + "\n" + Html.fromHtml(response.getString("description")));

                    cmsHead.setText(response.getString("disp_name"));
                    cmsDetail.setText(Html.fromHtml(response.getString("description")));
                    Picasso.with(context).load("https://www.gladdenmattresses.com/uploads/cms/"+response.getString("image"))
                            .into(imageView);
                    Log.i("desc",response.getString("description2")+"");
                  //  cmsDetail2.setText(Html.fromHtml(response.getString("description2")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
              /*  if (response.length()>0) {

                    //   Log.i("Market Listing Detail", response.toString());
                    image_url = new String[response.length()];
                    // Log.i("image size",image_url.length+"");
                    for (int i = 0; i<response.length(); i++) {

                        try {

                            JSONObject object = response.getJSONObject(i);

                            String SliderPhotos = object.getString("disp_name");
                             Log.i("Slider Res", SliderPhotos+object.getString("description"));
                            //image_url = new String[response.length()];
                            //image_url[i]= "https://www.gladdenmattresses.com/uploads/banner/"+object.getString("image");
                             //Log.i("IMAGE_IF"+i,disp_name+"\n"+description);

//                            sliderListData = new SliderListData(SliderPhotos);
//                            sliderList.add(sliderListData);
                            // Log.i("slider_if Data", sliderListData.toString());
//                            sliderAdapter = new MarketSliderListAdapter(context, sliderList);
//                            recyclerSliderPhotos.setAdapter(sliderAdapter);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                   // mysync mys = new mysync();
                    //mys.execute(image_url);

                } else  {

//                    image_url = new String[1];
//                    // Log.i("image size",response.length()+"");
//                    // image_url[i]= object.getString("image");
//
//                    // Log.i("Slider Res+Slider_else ","slider");
//  zsawr5ec44436FNXCTyyyyyyyyyyyyyyyfc'zzgyt67890gVDHHRURDG FFR4GFTDVML
//                    SliderPhotos="defult-banner-730x410.jpeg";
//                    image_url[0]=cdn_url+"uploads/product/defult-banner-730x410.jpeg";
//
//                    //Log.i("IMAGE_ELSE",image_url[0]);
//                    mysync mys = new mysync();
//                    mys.execute(image_url);
//
//
//                    sliderListData = new SliderListData(SliderPhotos);
//                    sliderList.add(sliderListData);
//                    // Log.i("slider_else Data", sliderListData.toString());
//                    sliderAdapter = new MarketSliderListAdapter(context, sliderList);
//                    recyclerSliderPhotos.setAdapter(sliderAdapter);


                }*/
            //Log.i("image_slider__",image_url.toString());
//                PreviousItemImageViewSlider.setVisibility(View.GONE);
//                NextItemImageViewSlider.setVisibility(View.GONE);

            // sliderAdapter.notifyDataSetChanged();

            //}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(object);
    }

}