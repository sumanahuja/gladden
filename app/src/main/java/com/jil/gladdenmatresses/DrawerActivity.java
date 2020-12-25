package com.jil.gladdenmatresses;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DrawerActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private RequestQueue requestQueue;
   //  private Bundle savedInstanceState2;
    private ExpandableListView expendable_listview;
    private DrawerLayout drawer;
    private FrameLayout fregmentContainer;
    private expendable_adapter expandableListViewAdapter;
    private ProgressDialog progressDoalog;
    private TextView textCartItemCount;
    private String list_id,list_url_name;

    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    ArrayList<product_category_model> data;
    int  flag;
    private int mCartItemCount = 1;

    String  PID = "1", product_name = "mattresses", toolbar_name = "Gladden Mattresses", openFragment = "";

    private String url, banner_url;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quick_links_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.header_CartBtn);

            View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge(getBadgeCount()+mCartItemCount);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                Log.i("onOptionsItemSelected", flag + "");
                new createPopUpMenu(getApplicationContext(), item, DrawerActivity.this, flag, fregmentContainer, getSupportFragmentManager()).create();
                break;

            case R.id.contact_us:
                fregmentContainer.removeAllViews();
                replaceFragment(new FragmentContactUs());
//                startActivity(new Intent(getApplicationContext(), SignUp.class));
                Log.i("contact_us", "clicked");
                break;

            case R.id.warranty:
                Log.i("warranty", "clicked");

                fregmentContainer.removeAllViews();
                replaceFragment(new ProductRegistrationFragment());
                break;

            case R.id.return_policy:
                Log.i("return_policy", "clicked");
                fregmentContainer.removeAllViews();
                replaceFragment(new FragmentReturnPolicy());
                break;

            case R.id.retailer:
                Log.i("retailer", "clicked");
                break;

            case R.id.header_CartBtn:
                Log.i("header_CartBtn", "clicked");
                break;
        }
        return true;
    }
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)


   // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // savedInstanceState2=savedInstanceState;
        setContentView(R.layout.activity_drawer);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            openFragment = bundle.getString("open", "");
            list_id= bundle.getString("list_id","");
            list_url_name=bundle.getString("list_url_name","");
            Log.i("DDD@",list_id+list_url_name);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.White)));
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //menuItem=new MenuItem[10];
        Menu menu = findViewById(R.id.mymenu);
        //   popup_menu=findViewById(R.id.popupMenu1);
        navigationView.setNavigationItemSelectedListener(this);
        expendable_listview = findViewById(R.id.expendable_listview);
        fregmentContainer = findViewById(R.id.fregmentContainer);
        ImageView toolbar_logo = findViewById(R.id.toolbar_logo);
        progressDoalog = new ProgressDialog(DrawerActivity.this);
        progressDoalog.setMessage("Loading in progress...");

        toolbar_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
            }
        });
        if (openFragment.length() != 0) {
            Fragment fragment = null;

            //initializing the fragment object which is selected
            switch (openFragment) {
                case "login":
                    fragment = new LoginFragment();
                    break;
                case "about":
                    fragment = new FragmentAbout();
                    break;
                case "contact":
                    fragment = new FragmentContactUs();
                    break;
                case "header_CartBtn":
                    break;
                case "detailpage":
                    fragment = new DetailPageFragment();
                    Bundle bundle = new Bundle();
                   // bundle.putString("String", "String text");
                    bundle.putString("list_id",list_id);
                    bundle.putString("list_url_name",list_url_name);
                    fragment.setArguments(bundle);
                    break;

                default:
                    fragment =new MainFragment();
            }
            if (fragment != null) {
//                fregmentContainer.removeAllViews();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.fregmentContainer, fragment);
//                ft.commit();
                replaceFragment(fragment);
            }
        }
        else
            replaceFragment(new MainFragment());

        try {
            UserModel u = mySharedPrefClass.getInstance(getApplicationContext()).get_data();
            if(u!=null) {
                if (u.getFlag() == 1) {
                    flag = 1;
                    Log.i("TRUE", flag + "");
                } else {
                    flag = 0;
                    Log.i("False", flag + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        initViews();//expandable menulist

        // initializing the listeners
        initListeners();


        // preparing list data
        initListData();


    }


    private void apiForUrlAndId(int tlevel) {

        MenuModel menuModel = new MenuModel("About", "about-us", null, null, null, false, true); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/categories?tlevel=" + tlevel + "&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("myjasonarray", jsonArray.toString());
                    String[] ids = new String[jsonArray.length()];
                    String[] url_names = new String[jsonArray.length()];
                    String[] icons = new String[jsonArray.length()];
                    String[] title = new String[jsonArray.length()];
                    String[] images = new String[jsonArray.length()];


                    for (int i = 0; i < jsonArray.length(); i++) {
                        title[i] = (jsonArray.getJSONObject(i).getString("title"));
                        ids[i] = (jsonArray.getJSONObject(i).getString("id"));
                        url_names[i] = (jsonArray.getJSONObject(i).getString("url_name"));
                        icons[i] = (jsonArray.getJSONObject(i).getString("icon"));
                        images[i] = (jsonArray.getJSONObject(i).getString("image"));
                        Log.i("ABC", title[i]);//+url_names[i],ids[i],images[i],icons[i])
                        final MenuModel menuModel = new MenuModel(title[i], url_names[i], ids[i], images[i], icons[i], true, true); //Menu of Android Tutorial. No sub menus
                        headerList.add(menuModel);
                        Log.i("ABC", title[i] + "\n" + menuModel.getMenuName());

                        Log.i("header", headerList.toString());
                        if (!menuModel.hasChildren) {
                            childList.put(menuModel, null);
                        } else {
                            requestQueue = Volley.newRequestQueue(getApplicationContext());
                            String url2 = "https://www.gladdenmattresses.com/api/jil.0.1/v2/categories?tlevel=" + ids[i] + "&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
                            Log.i("header_url", url2);
                            JsonObjectRequest objectRequest1 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jsonArray = response.getJSONArray("data");
                                        String[] ids = new String[jsonArray.length()];
                                        String[] url_names = new String[jsonArray.length()];
                                        String[] icons = new String[jsonArray.length()];
                                        String[] title = new String[jsonArray.length()];
                                        String[] images = new String[jsonArray.length()];

                                        List<MenuModel> childModelsList = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            title[i] = (jsonArray.getJSONObject(i).getString("title"));
                                            ids[i] = (jsonArray.getJSONObject(i).getString("id"));
                                            url_names[i] = (jsonArray.getJSONObject(i).getString("url_name"));
                                            icons[i] = (jsonArray.getJSONObject(i).getString("icon"));
                                            images[i] = (jsonArray.getJSONObject(i).getString("image"));


                                            MenuModel childModel = new MenuModel(title[i], url_names[i], ids[i], images[i], icons[i], false, false); //Menu of Android Tutorial. No sub menus   childModelsList.add(childModel);
                                            childModelsList.add(childModel);


                                        }
                                        if (menuModel.hasChildren) {
                                            childList.put(menuModel, childModelsList);
                                            Log.i("CHILD", menuModel.getMenuName() + childModelsList.toString());
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            });
                            requestQueue.add(objectRequest1);


                        }

                    }

                    MenuModel menuModel1 = new MenuModel("Product Registration", "product-registration", null, null, null, false, true); //Menu of Android Tutorial. No sub menus
                    headerList.add(menuModel1);
                    Log.i("menu added", menuModel1.getMenuName());
                    MenuModel menuModel2 = new MenuModel("Contact Us", "contact-us", null, null, null, false, true); //Menu of Android Tutorial. No sub menus
                    headerList.add(menuModel2);
                    Log.i("menu added", menuModel1.getMenuName());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(objectRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                expandableListViewAdapter = new expendable_adapter(getApplicationContext(), headerList, childList);

                // setting list adapter
                expendable_listview.setAdapter(expandableListViewAdapter);
                expandableListViewAdapter.notifyDataSetChanged();
            }
        });
        // url="https://www.gladdenmattresses.com/api/jil.0.1/v2/categories?tlevel=1&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs"
    }

    @Override
    public void onBackPressed() {
        //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1){
                finish();

            } else {
                getSupportFragmentManager().popBackStack();
            }

        }


    }







    void apiCall_spinal(String url, final String toolBar_name, final String banner) {
        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(this);

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
                    Intent intent = new Intent(getApplicationContext(), Product_category.class);
                    intent.putExtra("toolbar_name", toolBar_name);
                    intent.putExtra("banner_url", banner);
                    intent.putParcelableArrayListExtra("ARRAYLIST", data);
                    startActivity(intent);
                    // Toast.makeText(DrawerActivity.this, "ABOUT", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    //  Log.i("arraylist-",data.get(i).toString());


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



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initViews() {

        expendable_listview = findViewById(R.id.expendable_listview);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        //expandableListView.setIndicatorBounds(width-GetPixelFromDips(35), width-GetPixelFromDips(20));

        expendable_listview.setIndicatorBoundsRelative(width - GetPixelFromDips(35), width - GetPixelFromDips(10));

    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {

//        // ExpandableListView on child click listener
        expendable_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                fregmentContainer.removeAllViews();
                // parent.getAdapter().getItemId(groupPosition)
//                Toast.makeText(
//                        getApplicationContext(),
//                        headerList.get(groupPosition)
//                                + " :  "
//                                + childList.get(
//                                headerList.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product?&caturl=" + childList.get(headerList.get(groupPosition)).get(childPosition).getUrl() + "&catid=" + childList.get(headerList.get(groupPosition)).get(childPosition).getId() + "&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
                Log.i("slider_url", url);
                toolbar_name = childList.get(headerList.get(groupPosition)).get(childPosition).getMenuName();
                apiForBanner(childList.get(headerList.get(groupPosition)).get(childPosition).getUrl(), Integer.parseInt(childList.get(headerList.get(groupPosition)).get(childPosition).getId()));
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        apiCall_spinal(url, toolbar_name, banner_url);
                    }
                });

                return false;
            }
        });


        // ExpandableListView Group expanded listener
        expendable_listview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

//                Toast.makeText(getApplicationContext(),
//                        headerList.get(groupPosition).getMenuName() + "Expand ", Toast.LENGTH_SHORT).show();
                switch (headerList.get(groupPosition).getMenuName()) {
                    case ("About"):
                        fregmentContainer.removeAllViews();
                        replaceFragment(new FragmentAbout());
                      //  getSupportFragmentManager().beginTransaction().add(R.id.fregmentContainer, new FragmentAbout()).addToBackStack(null).commit();
                        //Toast.makeText(DrawerActivity.this, "ABOUT", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawers();
                        break;
                    case ("Contact Us"):
                        fregmentContainer.removeAllViews();
                        replaceFragment(new FragmentContactUs());
                        //().beginTransaction().add(R.id.fregmentContainer, new FragmentContactUs()).addToBackStack(null).commit();
                        //  Toast.makeText(DrawerActivity.this, "ABOUT", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawers();
                        break;
                    case ("Product Registration"):
                        fregmentContainer.removeAllViews();
                        replaceFragment(new ProductRegistrationFragment());
                    //    getSupportFragmentManager().beginTransaction().add(R.id.fregmentContainer, new ProductRegistrationFragment()).addToBackStack(null).commit();
                        //Toast.makeText(DrawerActivity.this, "ABOUT", Toast.LENGTH_SHORT).show();
                        drawer.closeDrawers();
                        break;

                }


                //    return true;
            }

        });

        // ExpandableListView Group collapsed listener
        expendable_listview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

//                Toast.makeText(getApplicationContext(),
//                        headerList.get(groupPosition) + "Collapse",
//                        Toast.LENGTH_SHORT).show();

            }
        });

    }
    void apiForBanner(String url_name, int id) {
        progressDoalog.show();
        requestQueue = Volley.newRequestQueue(DrawerActivity.this);
        final String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product/category?urlname=" + url_name + "&id=" + id + "&api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";

        // "https://www.gladdenmattresses.com/api/jil.0.1/v2/categories?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";
        //Log.i("slider_url", url);
        final JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDoalog.dismiss();
                    banner_url = "https://www.gladdenmattresses.com/uploads/category/" + response.getString("mobbanner");
                    Log.i("response1", banner_url);

                    // Picasso.with(getApplicationContext()).load(banner).fit().into();
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
    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fregmentContainer, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
    private void initListData() {

        apiForUrlAndId(0);

    }
    public int getBadgeCount()
    {
       return Integer.parseInt(textCartItemCount.getText().toString());
    }
    public void setupBadge(int mCartItemCount1) {

        if (textCartItemCount != null) {
            if (mCartItemCount1 == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount1, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    mCartItemCount=mCartItemCount1;

                }
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}