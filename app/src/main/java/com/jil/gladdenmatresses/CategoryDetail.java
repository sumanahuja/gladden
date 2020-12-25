package com.jil.gladdenmatresses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CategoryDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener {
    private Toolbar toolbar;
    private Spinner spinner_dimension, spinner_height, spinner_quantity;
    private ArrayList<String> dim_list, height_title_List, height_value_List, quantity_List;
    private RequestQueue requestQueue;
    private ArrayAdapter<String> adapter1,adapter2,adapter3;
    private myCustomPagerAdapter adapter_pager;
    private ViewPager viewPager_detail;
    private int currentPosition,dotsCount,count=0;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private LinearLayout.LayoutParams params;
    private  String[] image_url, J_dimension,J_height,J_price,final_gallery;
    private TextView tv_heading,tv_desc,tv_price,tv_sku,tv_features,tv_features_heading,tv_speci_heading,tv_speci,tv_sectionfour_heading,tv_sectionfour;
    private Button btn_AddToCart;
    private String list_id,list_url_name;
    private ImageView bottom_banner;
    private ProgressDialog progressDoalog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
       // super.onCreateDrawer();
       Bundle bundle= getIntent().getExtras();
      list_id= bundle.getString("list_id");
       list_url_name=bundle.getString("list_url_name");


        progressDoalog = new ProgressDialog(CategoryDetail.this);
        progressDoalog.setMessage("Loading in progress...");
        // progressDoalog.setTitle("ProgressDialog bar example");
//        progressDoalog.set
//        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        spinner_dimension = findViewById(R.id.spinner_dimension);
        spinner_height = findViewById(R.id.spinner_height);
        spinner_quantity = findViewById(R.id.spinner_quantity);
        viewPager_detail=findViewById(R.id.viewpager_detail);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        tv_heading=findViewById(R.id.tv_cat_heading);
        tv_desc=findViewById(R.id.tv_cat_desc);
        tv_price=findViewById(R.id.tv_cat_price_value);
        tv_sku=findViewById(R.id.tv_cat_sku);
        tv_features=findViewById(R.id.tv_features);
        tv_features_heading=findViewById(R.id.tv_features_heading);
        tv_speci_heading=findViewById(R.id.tv_speci_heading);
        tv_speci=findViewById(R.id.tv_speci);
        tv_sectionfour_heading=findViewById(R.id.tv_sectionfour_heading);
        tv_sectionfour=findViewById(R.id.tv_sectionfour);
        bottom_banner=findViewById(R.id.bottom_banner);
        toolbar=findViewById(R.id.toolbar2);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
       // toolbar.setTitle(toolbar_name);
        // use default spinner item to show options in spinner

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
            }
        });
        spinner_dimension.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int position1=position;
                String dim=dim_list.get(spinner_dimension.getSelectedItemPosition());
                String  height=height_value_List.get(spinner_height.getSelectedItemPosition()).toString();
                String quantity=quantity_List.get(spinner_quantity.getSelectedItemPosition());
                //  Log.i("onItemSelected",position+"\n"+height);
                apiToCalculatePrice(dim,height,quantity,position1);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int position1=position;
                String dim=dim_list.get(spinner_dimension.getSelectedItemPosition());
                String  height=height_value_List.get(spinner_height.getSelectedItemPosition()).toString();
                String quantity=quantity_List.get(spinner_quantity.getSelectedItemPosition());
                //  Log.i("onItemSelected",position+"\n"+height);
                apiToCalculatePrice(dim,height,quantity,position1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int position1=position;
                String dim=dim_list.get(spinner_dimension.getSelectedItemPosition());
                String  height=height_value_List.get(spinner_height.getSelectedItemPosition()).toString();
                String quantity=quantity_List.get(spinner_quantity.getSelectedItemPosition());
              //  Log.i("onItemSelected",position+"\n"+height);
                apiToCalculatePrice(dim,height,quantity,position1);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        apiCallForDimensions();

        quantity_List = new ArrayList<String>();
        quantity_List.add("1");
        quantity_List.add("2");
        quantity_List.add("3");
        quantity_List.add("4");
        quantity_List.add("5");
        quantity_List.add("6");
        quantity_List.add("7");
        quantity_List.add("8");
        quantity_List.add("9");
        quantity_List.add("10");


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // "";


    }

    private void apiToCalculatePrice(final String dim1, final String height1, final String quantity1, final int position1 ) {
        progressDoalog.show();

        requestQueue = Volley.newRequestQueue(this);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product?urlname="+list_url_name+"&pid="+list_id+"&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("apiToCalculatePrice", url+"\n"+list_id+list_url_name);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDoalog.dismiss();
                    JSONObject response_data = response.getJSONObject("data");
                    JSONArray jsonArray=response_data.getJSONArray("prices");
                    Log.i("ARRAY",jsonArray.toString()+"\n"+jsonArray.length());
                    J_dimension=new String[jsonArray.length()];
                     J_height=new String[jsonArray.length()];
                    J_price=new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++)
                    {
                       J_dimension[i]=  jsonArray.getJSONObject(i).getJSONObject("dimension").getString("title");
                         J_height[i]=  jsonArray.getJSONObject(i).getJSONObject("height").getString("height");
                         J_price[i]=  jsonArray.getJSONObject(i).getString("price");
                         Log.i("HH",J_dimension[i]+J_height[i]+J_price[i]);
                        if(dim1.equals(J_dimension[i]) && height1.equals(J_height[i]))
                        {  String PRICE= Float.parseFloat(J_price[i])*Integer.parseInt(quantity1)+"";
                        tv_price.setText(PRICE);
                            Log.i("PRICE",PRICE);
                        }

                    }
                  //  Log.i("On_selection",J_dimension.toString()+"\n"+J_height.toString()+"\n"+J_price.toString());
//                    Log.i("response", response.getString("disp_name") + "\n" + Html.fromHtml(response.getString("description")));
                    //     Log.i("dim", response_data.getJSONArray("section").toString() + response.getJSONArray("dimensions").toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            //    Toast.makeText(CategoryDetail.this, dim_list.get(position1) + " clicked", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDoalog.dismiss();
                error.printStackTrace();
            }
        });
        requestQueue.add(object);


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(CategoryDetail.this, "Nothing" + " clicked", Toast.LENGTH_SHORT).show();
    }

    void apiCallForDimensions() {

        // show it
        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(this);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product?urlname="+list_url_name+"&pid="+list_id+"&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        Log.i("apiCallForDimensions", url+"\n"+list_id+list_url_name);
        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDoalog.dismiss();
                    JSONObject response_data = response.getJSONObject("data");
                    tv_sku.setText("SKU#: "+response_data.getString("sku"));
                    tv_price.setText(response_data.getString("price"));
//                    Log.i("response", response.getString("disp_name") + "\n" + Html.fromHtml(response.getString("description")));
               //     Log.i("dim", response_data.getJSONArray("section").toString() + response.getJSONArray("dimensions").toString());

                    JSONArray section_array=response_data.getJSONArray("section");
                    String[] section_Id=new String[section_array.length()];
                    for (int i = 0; i < section_array.length(); i++) {
                        JSONObject section_ojcet=section_array.getJSONObject(i);
                        section_Id[i]=(section_ojcet.getString("sectionno"));
                        if(section_Id[i].equals("2"))
                        {
                            tv_features_heading.setText(section_ojcet.getString("title"));
                            tv_features.setText(Html.fromHtml(section_ojcet.getString("description").trim(),new ImageGetter(tv_features),null));
                            Log.i("section 2",Html.fromHtml(section_ojcet.getString("description"))+"");
                        }
                        if(section_Id[i].equals("3"))
                        {
                            tv_speci_heading.setText(section_ojcet.getString("title"));
                            tv_speci.setText(Html.fromHtml(section_ojcet.getString("description").trim(),new ImageGetter(tv_speci),null));
                            Log.i("section 3",Html.fromHtml(section_ojcet.getString("description"))+"");
                        }
                        if(section_Id[i].equals("4"))
                        {if(!section_ojcet.get("title").toString().equals("null")) {
                            tv_sectionfour_heading.setText(section_ojcet.getString("title"));
                            tv_sectionfour.setText(Html.fromHtml(section_ojcet.getString("description").trim(), new ImageGetter(tv_sectionfour), null));
                            Log.i("section 4", Html.fromHtml(section_ojcet.getString("description")) + "");
                        }
                        else {
                            tv_sectionfour_heading.setVisibility(View.GONE);
                            tv_sectionfour.setVisibility(View.GONE);
                        }

                        }
                        if(section_Id[i].equals("5"))
                        {
                            String image_URL="https://www.gladdenmattresses.com/uploads/product//imgbg/"+section_ojcet.getString("mobbanner");
                            Picasso.with(getApplicationContext()).load(image_URL).fit().into(bottom_banner);
                            Log.i("section 5",image_URL+"");
                        }
                       image_url=new String[section_ojcet.getJSONArray("gallery").length()];
                        if(section_Id[i].equals("12"))
                        {
                            tv_heading.setText(Html.fromHtml(section_ojcet.getString("title")));
                            toolbar.setTitle(Html.fromHtml(section_ojcet.getString("title")));
                            tv_desc.setText(Html.fromHtml(section_ojcet.getString("description")));



                            Log.i("section 12:",section_ojcet.getString("product_id")+section_ojcet.getString("title")+section_ojcet.getString("description")+section_ojcet.getJSONArray("gallery")+"");
                            for (int j = 0; j < section_ojcet.getJSONArray("gallery").length(); j++) {
                                Log.i("gallery",section_ojcet.getJSONArray("gallery").getJSONObject(j).getString("image"));
                            image_url[j]="https://www.gladdenmattresses.com/uploads/product/"+section_ojcet.getJSONArray("gallery").getJSONObject(j).getString("image");
                            }
                            try {

                                 final_gallery= getGallery(image_url);
                                adapter_pager = new myCustomPagerAdapter(CategoryDetail.this, image_url);
                                //  Log.i("myBitmap", myBitmap.toString());
                                viewPager_detail.setAdapter(adapter_pager);
                                viewPager_detail.setCurrentItem(0);
                                viewPager_detail.setOnPageChangeListener(CategoryDetail.this);
                              //  createAutoSliderShow();
                                if (image_url.length <= 0) {

                                } else {
                                    setPageViewIndicator();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Object OBJ_dim=response.get("dimensions");
                    Object OBJ_height=response.get("heights");

                    if(OBJ_dim instanceof JSONObject){
    Log.i("dim_list","null"+response.getJSONObject("dimensions").length());
    JSONObject jsonArray_dim = response.getJSONObject("dimensions");
    dim_list = new ArrayList<>(response.getJSONObject("dimensions").length());
    for (int i = 0; i <response.getJSONObject("dimensions").length(); i++) {
        int j=i;
        if(jsonArray_dim.has(j+"")){
        dim_list.add(jsonArray_dim.getJSONObject(j+"").getString("title"));
        //Log.i("dimllist", dim_list.get(j));
        }
        else
        {j=++i;
            dim_list.add(jsonArray_dim.getJSONObject(j+"").getString("title"));
           // Log.i("dimllist", dim_list.get(j));
        }
    }
}
else {
    JSONArray jsonArray_dim = response.getJSONArray("dimensions");
    dim_list = new ArrayList<>(jsonArray_dim.length());
    for (int i = 0; i < jsonArray_dim.length(); i++) {
        dim_list.add(jsonArray_dim.getJSONObject(i).getString("title"));
        Log.i("dimllist", dim_list.get(i));
    }
}
                    if(OBJ_height instanceof JSONObject){
                        Log.i("dim_list","null"+response.getJSONObject("heights").length());
                        JSONObject jsonArray_dim = response.getJSONObject("heights");
                        height_title_List = new ArrayList<>(response.getJSONObject("heights").length());
                        height_value_List= new ArrayList<>(response.getJSONObject("heights").length());
                        for (int i = 0; i <response.getJSONObject("heights").length(); i++) {
                            int j=i;
                            if(jsonArray_dim.has(j+"")){
                                height_title_List.add(jsonArray_dim.getJSONObject(j+"").getString("title"));
                                height_value_List.add(jsonArray_dim.getJSONObject(j+"").getString("height"));
                                //Log.i("dimllist", dim_list.get(j));
                            }
                            else
                            {j=++i;
                                height_title_List.add(jsonArray_dim.getJSONObject(j+"").getString("title"));
                                height_value_List.add(jsonArray_dim.getJSONObject(j+"").getString("height"));

                                // Log.i("dimllist", dim_list.get(j));
                            }
                        }
                    }
                    else {
//                        JSONArray jsonArray_dim = response.getJSONArray("heights");
//                        dim_list = new ArrayList<>(jsonArray_dim.length());
//                        for (int i = 0; i < jsonArray_dim.length(); i++) {
//                            dim_list.add(jsonArray_dim.getJSONObject(i).getString("title"));
//                            Log.i("dimllist", dim_list.get(i));
//                        }
                        JSONArray jsonArray_height = response.getJSONArray("heights");
                        height_title_List = new ArrayList<>(jsonArray_height.length());
                        height_value_List = new ArrayList<>(jsonArray_height.length());

                        for (int i = 0; i < jsonArray_height.length(); i++) {
                            height_title_List.add(jsonArray_height.getJSONObject(i).getString("title"));
                            height_value_List.add(jsonArray_height.getJSONObject(i).getString("height"));
                        }
                        Log.i("height_value_List", height_value_List.toString());


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter1 = new ArrayAdapter<String>(CategoryDetail.this, android.R.layout.simple_spinner_dropdown_item, dim_list);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_dimension.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                //spinner_dimension.setOnItemSelectedListener(CategoryDetail.this);

                adapter2 = new ArrayAdapter<String>(CategoryDetail.this, android.R.layout.simple_spinner_dropdown_item, height_title_List);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_height.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                //spinner_height.setOnItemSelectedListener(CategoryDetail.this);

                adapter3 = new ArrayAdapter<String>(CategoryDetail.this, android.R.layout.simple_spinner_dropdown_item, quantity_List);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_quantity.setAdapter(adapter3);
                adapter3.notifyDataSetChanged();
                //spinner_quantity.setOnItemSelectedListener(CategoryDetail.this);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDoalog.dismiss();
                error.printStackTrace();
            }
        });
        requestQueue.add(object);

    }
    private void setPageViewIndicator() {

        //Log.d("###setPageViewIndicator", " : called");
        dotsCount = adapter_pager.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            //  Log.i("IMAGEURL",image_url[i]);
            Picasso.with(getApplicationContext()).load(image_url[i]).resize(120, 100).into(dots[i]);
            // dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );

            params.setMargins(4, 0, 4, 0);
            dots[i].setPadding(5, 5, 5, 5);

//            dots[i].getLayoutParams().width=100;
         //   dots[i].setBackground(getResources().getDrawable(R.drawable.edit_text_border));


            final int presentPosition = currentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    viewPager_detail.setCurrentItem(presentPosition);
                    return true;
                }

            });


            pager_indicator.addView(dots[i], params);
        }

      //  Picasso.with(getApplicationContext()).load(image_url[0]).resize(60,60).into(dots[0]);
//        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }
    void createAutoSliderShow() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Log.i("run","run");
                if (currentPosition == adapter_pager.getCount()) {
                    currentPosition = 0;
                }
                if (currentPosition < adapter_pager.getCount()) {
                    // currentPosition=0;
                    // Log.i("hi",adapter_pager.getCount()+"");
                    viewPager_detail.setCurrentItem(currentPosition++, true);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            image_url = final_gallery;
            for (int i = 0; i < dotsCount; i++) {
                // Log.i("IMAGEURL@@",adapter_pager.getPageTitle(position).toString());
                Picasso.with(getApplicationContext()).load(image_url[i]).resize(120, 100).into(dots[i]);
                // dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }
            Picasso.with(getApplicationContext()).load(image_url[position]).resize(120, 85).into(dots[position]);
            // dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    String[] getGallery(String [] image_url){
        return image_url;
    }


    class ImageGetter implements Html.ImageGetter
    {
        TextView tv;
        public ImageGetter(TextView tv_speci) {
            this.tv=tv_speci;
        }

        @Override
        public Drawable getDrawable(String source) {
            LevelListDrawable d = new LevelListDrawable();
          //  d.setBounds(25,25,25,25);
          //  d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            Log.i("D",d.getIntrinsicWidth()+""+d.getIntrinsicHeight()+"");
            new ImageGetterAsyncTask(CategoryDetail.this, source, d).execute(tv);

            return d;
        }
    }
    static class ImageGetterAsyncTask extends AsyncTask<TextView, Void, Bitmap> {


        private LevelListDrawable levelListDrawable;
        private Context context;
        private String source;
        private TextView t;

        public ImageGetterAsyncTask(Context context, String source, LevelListDrawable levelListDrawable) {
            this.context = context;
            this.source = source;
            this.levelListDrawable = levelListDrawable;
        }

        @Override
        protected Bitmap doInBackground(TextView... params) {
            t = params[0];
            try {
                Log.i("LOG_CAT", "Downloading the image from: " + source);
                return Picasso.with(context).load(source).get();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            try {
                Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                Point size = new Point();
                ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
                // Lets calculate the ratio according to the screen width in px
                int multiplier = size.x / bitmap.getWidth();
                Log.d("LOG_CAT", "multiplier: " + multiplier);
                levelListDrawable.addLevel(1, 1, d);
                // Set bounds width  and height according to the bitmap resized size
                if(t.getId()==R.id.tv_speci){
                    levelListDrawable.setBounds(0,0,350,350);
                }
                    //levelListDrawable.setBounds(0, 0, bitmap.getWidth() * multiplier, bitmap.getHeight() * multiplier);}
                else if(t.getId()==R.id.tv_features) levelListDrawable.setBounds(0,0,250,250);
                else levelListDrawable.setBounds(0,0,650,850);
                    //levelListDrawable.setBounds(0, 0, bitmap.getWidth() * multiplier, bitmap.getHeight() * multiplier);
                //
                levelListDrawable.setLevel(1);
                t.setTextSize(16);
                t.setText(t.getText()); // invalidate() doesn't work correctly...
            } catch (Exception e) { /* Like a null bitmap, etc. */ }
        }
    }

}