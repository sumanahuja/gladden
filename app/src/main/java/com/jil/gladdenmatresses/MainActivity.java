package com.jil.gladdenmatresses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // mySharedPrefClass.getInstance(getApplicationContext()).clear_data();
        startActivity(new Intent(getApplicationContext(),DrawerActivity.class));
    }
}