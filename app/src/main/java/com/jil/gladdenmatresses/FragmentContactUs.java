package com.jil.gladdenmatresses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class FragmentContactUs extends Fragment {

//Toolbar toolbar;
EditText ed_name,ed_email,ed_message,ed_phone,ed_subject;
Button btn_send;
Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contactus, container, false);
        context=view.getContext();
//        toolbar=view.findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        ed_email=view.findViewById(R.id.contact_us_email);
        ed_name=view.findViewById(R.id.contact_us_name);
        ed_phone=view.findViewById(R.id.contact_us_phone);
        ed_subject=view.findViewById(R.id.contact_us_subject);
        ed_message=view.findViewById(R.id.contact_us_message);
        btn_send=view.findViewById(R.id.contact_us_send);
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButtonAPI();
            }
        });




//        toolbar.setNavigationOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //getActivity().onBackPressed();
//                startActivity(new Intent(getContext(),DrawerActivity.class));
//            }
//        });
//        toolbar.setTitle("Contact Us");
      //  toolbar.setLogo(R.drawable.ic_arrow_back);
        return view;
    }
void sendButtonAPI()
{
    Log.i("name",ed_name.getText().toString()+"");
    String url="https://www.gladdenmattresses.com/api/jil.0.1/v2/contact-us";
    Log.i("URL",url);
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("Response", response);
            try {
                JSONObject jsonObject=new JSONObject(response);
               String status= jsonObject.getString("status");
               if(status.equals("true"))
               {
                   Toast.makeText(context, "Message sent Successfully", Toast.LENGTH_SHORT).show();
               }
                else
               {
                   Toast.makeText(context, jsonObject.getString("error").toString(), Toast.LENGTH_SHORT).show();
               }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
            }


        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Error", error.toString());
                    Toast.makeText(context, error+"", Toast.LENGTH_SHORT).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() {
            //getData();
            Map<String, String> params = new HashMap<String, String>();
            params.put("name" ,ed_name.getText().toString());
            params.put("email" ,ed_email.getText().toString());
            params.put("phone" ,ed_phone.getText().toString());
            params.put("subject" ,ed_subject.getText().toString());
            params.put("message" ,ed_message.getText().toString());
            params.put("api_token", "awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs");
            return params;
        }
    };
    requestQueue.add(stringRequest);

}
}