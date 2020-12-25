package com.jil.gladdenmatresses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainFragment extends Fragment  {
    private Context context;
    private ViewPager mViewPager;
    private int currentPosition;
    private myCustomPagerAdapter adapter;
    private LinearLayout pager_indicator, category_layout_top;
    private TextView cmsHead, cmsDetail, tv_subCat_heading;
    private ImageView cms_image;
    private RelativeLayout category_layout, relativeLayout;
    private List<String> listDataGroup, list2_url_names, list2_ids;
    private HashMap<String, List<String>> listDataChild;
    private ProgressDialog progressDoalog;
    private UserModel userModel;
    private MenuItem itemUser;
    private ArrayList<String> list_title_cat;
    private ArrayList<String> list_image_cat, url_toCategoryListing, list_id_cat;
    private int dotsCount, flag;
    private ImageView[] dots;
    private String image_url[], list_image_cat1[], PID = "1", product_name = "mattresses", toolbar_name = "Gladden Mattresses", openFragment = "";
    private RequestQueue requestQueue;
    private ArrayList<product_category_model> data;
    private DrawerLayout drawer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        context = view.getContext();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        //  mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        cmsHead = view.findViewById(R.id.cms_heading);
        cmsDetail = view.findViewById(R.id.cms_detail);
        cms_image = view.findViewById(R.id.cms_image);

        // category_layout = view.findViewById(R.id.category_layout);
        category_layout_top = view.findViewById(R.id.category_layout_top);
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Loading in progress...");
        tv_subCat_heading = view.findViewById(R.id.tv_subCat_heading);


        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(context);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/cms/1/banner?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDoalog.dismiss();
                Log.i("slider_length", response.length() + "");
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
                            image_url[i] = "https://www.gladdenmattresses.com/uploads/banner/" + object.getString("image");

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                    try {

                        adapter = new myCustomPagerAdapter(context, image_url);
                        //  Log.i("myBitmap", myBitmap.toString());
                        mViewPager.setAdapter(adapter);
                        mViewPager.setCurrentItem(0);
                     //   onPageSelected(mViewPager.getCurrentItem());
                        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                              //  onPageSelected(mViewPager.getCurrentItem());
                            }

                            @Override
                            public void onPageSelected(int position) {
                                for (int i = 0; i < dotsCount; i++) {
                                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                                //    Toast.makeText(context, "onPageSelected1"+dotsCount, Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(context, "onPageSelected2"+dotsCount, Toast.LENGTH_SHORT).show();

                                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        createAutoSliderShow();
                        if (image_url.length <= 0) {

                        } else {

                            setPageViewIndicator();
                        //    Toast.makeText(context, "else"+dotsCount, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }
                Log.i("image_slider__", image_url.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDoalog.dismiss();
            }

        });
        requestQueue.add(jsonArrayRequest);
        tv_subCat_heading.setText("CATEGORIES");
        apiCallForCMS();
        apiToCallCategoriesImages();


        return view;
    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        // Log.i("onPageSelected", dotsCount + "");
//
//        for (int i = 0; i < dotsCount; i++) {
//            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
//            Toast.makeText(context, "onPageSelected1"+dotsCount, Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(context, "onPageSelected2"+dotsCount, Toast.LENGTH_SHORT).show();
//
//        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
//
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }

    private void setPageViewIndicator() {

        //Log.d("###setPageViewIndicator", " : called");
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];
    //    Toast.makeText(context, "setPageViewIndicator"+dotsCount, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = currentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    void createAutoSliderShow() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Log.i("run","run");
                if (currentPosition == adapter.getCount()) {
                    currentPosition = 0;
                }
                if (currentPosition < adapter.getCount()) {
                    // currentPosition=0;
                    // Log.i("hi",mAdapter.getCount()+"");
                    mViewPager.setCurrentItem(currentPosition++, true);
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 250, 2500);
    }

    void apiCallForCMS() {
        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(context);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/information?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    progressDoalog.dismiss();
//                    response = response.getJSONObject("data");
//                    Log.i("response", response.getString("disp_name") + "\n" + Html.fromHtml(response.getString("description")));

                    cmsHead.setText(response.getString("title"));
                    cmsDetail.setText(Html.fromHtml(response.getString("description")));
                    if (!response.getString("image").isEmpty())
                        Picasso.with(context)
                                .load("https://www.gladdenmattresses.com/uploads/information/" + response.getString("image"))
                                .into(cms_image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDoalog.dismiss();
            }
        });
        requestQueue.add(object);
    }

    void apiToCallCategoriesImages() {
        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(context);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/categories?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDoalog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("length", jsonArray.length() + "");
                    list_title_cat = new ArrayList<>();
                    list_image_cat = new ArrayList<>();
                    list_id_cat = new ArrayList<>();
                    url_toCategoryListing = new ArrayList<>();
                    int total = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray jsonSubArray = jsonArray.getJSONObject(i).getJSONArray("subcat");
                        // Log.i("object",jsonArray.getJSONObject(i).toString());
                        //Log.i("Sub-object",jsonArray.getJSONObject(i).getJSONArray("subcat").length()+"");
                        total = jsonArray.getJSONObject(i).getJSONArray("subcat").length();
                        // url_toCategoryListing=new String[total];
                        for (int j = 0; j < total; j++) {
                            //list_image_cat1[j]="https://www.gladdenmattresses.com/uploads/category/"+jsonSubArray.getJSONObject(j).getString("image");
                            list_title_cat.add(jsonSubArray.getJSONObject(j).getString("title").trim());
                            list_id_cat.add(jsonSubArray.getJSONObject(j).getString("id").trim());

                            list_image_cat.add("https://www.gladdenmattresses.com/uploads/category/" + jsonSubArray.getJSONObject(j).getString("image").trim());
                            url_toCategoryListing.add(jsonSubArray.getJSONObject(j).getString("url_name"));

                            // Log.i("title"+j, list_image_cat1[j]+"");
                        }
                        list_image_cat1 = new String[list_image_cat.size()];
                        for (int s = 0; s < list_image_cat.size(); s++) {
                            list_image_cat1[s] = list_image_cat.get(s);
                        }
                    }


                    //  for (int k = 0; k < list_image_cat.size(); k++) {
                    try {
                        //  new mysync2().execute(list_image_cat1);
                        for (int k = 0; k < list_image_cat1.length; k++) {
                            final int m = k;
                            relativeLayout = new RelativeLayout(context);
                            relativeLayout.setId(k);
                            Log.i("K", k + "\n" + list_image_cat1[k]);
                            ImageView Iv = new ImageView(context);
                            //  myBitmap[k]=Bitmap.createBitmap(myBitmap[k],500,500, Bitmap.Config.ARGB_8888);
                            //Iv.setImageBitmap(myBitmap[k]);
                            Picasso.with(context).load(list_image_cat1[k])
                                    .resize(800, 600)
                                    .into(Iv);
                            // Iv.setTooltipText(list_title_cat.get(k)+"");
                            //tv.setId(i);
                            Iv.setPadding(0, 5, 0, 0);
                            Iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("cat", url_toCategoryListing.get(m) + "\n" + list_title_cat.get(m) + "\n" + list_image_cat1[m]);
                                    String url2 = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product?&caturl=" + url_toCategoryListing.get(m) + "&catid=" + list_id_cat.get(m) + "&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
                                    apiCall_spinal(url2, list_title_cat.get(m), list_image_cat.get(m));
                                }
                            });
                            // Iv.set(0, 0, 0, 0);
                            RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                            textViewParams.setMargins(0, 10, 0, 0);

                            // Log.i("IV",k+""+myBitmap.length);
                            relativeLayout.addView(Iv, textViewParams);
                            TextView Tv = new TextView(context);
                            // Tv.setImageBitmap(myBitmap[k]);
                            Tv.setText(list_title_cat.get(k) + "");
                            Tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url2 = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product?&caturl=" + url_toCategoryListing.get(m) + "&catid=" + list_id_cat.get(m) + "&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
                                    apiCall_spinal(url2, list_title_cat.get(m), list_image_cat.get(m));
                                }
                            });
                            Tv.setTextSize(25);
                            Tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            //tv.setId(i);
                            Tv.setPadding(0, 10, 0, 0);
                            RelativeLayout.LayoutParams textViewParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            textViewParams.setMargins(0, 0, 0, 0);
                            //textViewParams2.
                            textViewParams2.setMargins(15, 15, 0, 0);
                            // Log.i("IV",k+""+myBitmap.length);
                            relativeLayout.addView(Tv, textViewParams2);
                            LinearLayout.LayoutParams textViewParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            // category_layout_top.removeAllViews();

                            category_layout_top.addView(relativeLayout, textViewParams3);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDoalog.dismiss();
            }
        });
        requestQueue.add(object);
    }

    void apiCall_spinal(String url, final String toolBar_name, final String banner) {
        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDoalog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("slider_length", jsonArray.length() + "" + jsonArray.toString());
                    data = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.i("object", object.toString());
                        String image = String.valueOf(Html.fromHtml(object.getString("model_image")));
                        String title = String.valueOf(Html.fromHtml(object.getString("list_title")));
                        String desc = String.valueOf(Html.fromHtml(object.getString("list_desc")));
                        String id = object.getString("id");
                        String url_name = object.getString("url_name");
                        Log.i("object", object.getString("list_title"));
                        product_category_model model = new product_category_model(image, title, desc, id, url_name);
                        data.add(model);
                        Log.i("arraylist-", data.get(i).toString() + data.size());
                    }
                    Intent intent = new Intent(context, Product_category.class);
                    intent.putExtra("toolbar_name", toolBar_name);
                    intent.putExtra("banner_url", banner);
                    intent.putParcelableArrayListExtra("ARRAYLIST", data);
                    startActivity(intent);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDoalog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fregmentContainer, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

}