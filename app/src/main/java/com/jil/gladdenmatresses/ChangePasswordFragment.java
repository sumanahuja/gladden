package com.jil.gladdenmatresses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    private EditText new_password,confirm_password,current_password;
    private Button change_password_confirm,btn_back;
    private  View view;
    private Context context;
    private int ID;

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_change_password, container, false);
         context=view.getContext();
       ID=  mySharedPrefClass.getInstance(context).get_data().getId();
        new_password=view.findViewById(R.id.new_password);
        confirm_password=view.findViewById(R.id.confirm_password);
        change_password_confirm=view.findViewById(R.id.change_password_confirm);
        btn_back=view.findViewById(R.id.btn_back);
        current_password=view.findViewById(R.id.current_password);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment2=new ProfileFragment();
                FragmentManager fm=getFragmentManager();
                fm.beginTransaction().replace(R.id.fregmentContainer,fragment2).commit();
            }
        });
        change_password_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_password.getText().toString().equals("") && confirm_password.getText().toString().equals("")) {
                    Toast.makeText(context, "You can't submit empty field", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (new_password.getText().toString().equals(confirm_password.getText().toString())) {
                        String url = "https://www.gladdenmattresses.com/api/jil.0.1/account/profile/" + ID;
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Response", response);
if(response.equals("true"))
{
    Toast.makeText(context, "Password has changed successfully", Toast.LENGTH_SHORT).show();
}

                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("Error", error.toString());
                                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                               // getData();
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", "" + ID);
                                params.put("old_password", current_password.getText().toString());
                                params.put("password", new_password.getText().toString());

                                //  params.put("api_token", api_token);
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
//                        Intent intent = new Intent(ChangePassword.this, Profile.class);
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        return view;
    }
}