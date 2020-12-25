package com.jil.gladdenmatresses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FragmentMatresses extends Fragment implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    int currentPosition;
    private myCustomPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    int dotsCount;
    ImageView[] dots;
    String image_url[];
    Bitmap bitmap;
    Bitmap myBitmap[];
    View view;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matresses, container, false);
        context=view.getContext();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        //  mViewPager = (ViewPager) findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/cms/1/banner?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("slider_url", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
                            // Log.i("IMAGE_IF"+i,image_url[i]);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                    mysync mys = new mysync();
                    mys.execute(image_url);

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
        requestQueue.add(jsonArrayRequest);

        return view;
    }

        class mysync extends AsyncTask<String, Void, Bitmap[]> {
            Bitmap myBitmap[];
            Bitmap bitmap;

            // int[] myIntArray;
            @Override
            protected Bitmap[] doInBackground(String... strings) {
                try {
                    // Log.i("doing",strings.length+"");
                    myBitmap = new Bitmap[strings.length];
                    // Log.i("doing",myBitmap+"");
                    for (int i = 0; i < strings.length; i++) {

                        URL url1 = new URL(strings[i]);

                        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

                        connection.setDoInput(true);
                        connection.connect();

                        InputStream input = connection.getInputStream();

                        myBitmap[i] = BitmapFactory.decodeStream(input);
//

                        // Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                        myBitmap[i] = Bitmap.createScaledBitmap(myBitmap[i], (int) (myBitmap[i].getWidth() * 2), (int) (myBitmap[i].getHeight() * 2), true);
                        //  myBitmap[i]=   getScalledBitmap( myBitmap[i]);
                        // myBitmap[i]=   scaleDown( myBitmap[i],250,true);


                        // Log.i("MYBITMAP 2", myBitmap[i] + "");


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                //return myBitmap;
                return myBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap[] myBitmap) {
                try {

                    for (int i = 0; i < myBitmap.length; i++) {

                    }
                    //  Log.i("myBitmap 1", myBitmap.toString());

                    mAdapter = new myCustomPagerAdapter(context, image_url);
                    //  Log.i("myBitmap", myBitmap.toString());
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setCurrentItem(0);
                 //   mViewPager.setOnPageChangeListener((ViewPager.OnPageChangeListener) context);
                  //  mViewPager.addOnAdapterChangeListener(context);
                    createAutoSliderShow();
                    if (myBitmap.length <= 0) {

                    } else {
                        setPageViewIndicator();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private void setPageViewIndicator() {

            //Log.d("###setPageViewIndicator", " : called");
            dotsCount = mAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(context);
                dots[i].setImageDrawable(context.getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                final int presentPosition =currentPosition= i;
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
        void createAutoSliderShow()
        {
            final Handler handler=new Handler();
            final Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    // Log.i("run","run");
                    if(currentPosition==mAdapter.getCount())
                    {currentPosition=0;}
                    if(currentPosition<mAdapter.getCount())
                    {
                        // currentPosition=0;
                        // Log.i("hi",mAdapter.getCount()+"");
                        mViewPager.setCurrentItem(currentPosition++,true);
                    }

                }
            };
            Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            },250,2500);
        }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i("onPageSelected",dotsCount+"");
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}