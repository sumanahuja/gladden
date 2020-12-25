package com.jil.gladdenmatresses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

public class mySharedPrefClass {
    private Context mCtx;
    String argument = "";
    private static mySharedPrefClass mInstance;

    public mySharedPrefClass(Context context) {
        mCtx = context;

    }

    public static synchronized mySharedPrefClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new mySharedPrefClass(context);
        }
        return mInstance;
    }

    void save_data(UserModel userObject) {
        SharedPreferences mSharedPreferences = mCtx.getSharedPreferences("userData", 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userObject);
        editor.putString("userObject", json);
        editor.apply();
        Log.i("Shared", "saved" + json.toString());
    }

    UserModel get_data() {
        // UserModel obj;

        SharedPreferences mSharedPreferences = mCtx.getSharedPreferences("userData", 0);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("userObject", "");
        UserModel obj = gson.fromJson(json, UserModel.class);
//        Log.i("Shared", "o" + obj.getFull_name());
        return obj;
    }
    void clear_data(){
        SharedPreferences mSharedPreferences = mCtx.getSharedPreferences("userData", 0);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
        Log.i("Editor","clear");
    }
}
