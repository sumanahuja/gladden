package com.jil.gladdenmatresses;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import androidx.viewpager.widget.PagerAdapter;

public class myCustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    LayoutInflater mLayoutInflater;
   private String[] mResources;
   //private int[] mResources;

    public myCustomPagerAdapter(Context context, String []resources) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = resources;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item,container,false);
        ImageView imageView =  itemView.findViewById(R.id.imageView);
       // imageView.setImageResource(mResources[position]);
       // Log.i("abc",mResources[position]);
        Picasso.with(mContext).load(mResources[position]).fit().into(imageView);
        //imageView.setImageBitmap(mResources[position]);


        // Picasso.with(mContext).load(mResources).resize(300,300);
       // imageView.setImageBitmap(Bitmap.createScaledBitmap(mResources, 120, 120, false));
//        imageView.setMaxHeight(200);
//        imageView.setMaxWidth(200);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}