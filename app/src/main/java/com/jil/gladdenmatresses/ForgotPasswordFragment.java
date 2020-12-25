package com.jil.gladdenmatresses;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordFragment extends Fragment {

   
    private Button btn_submit,btn_cancel,btn_back2;
    private EditText edt_email;
    private View view;
    private Context context;
    private String email;
    private TextView text_msg;

  
   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_forgot_password, container, false);
        context=view.getContext();
        btn_cancel=view.findViewById(R.id.btn_back);
        btn_back2=view.findViewById(R.id.btn_back2);

        btn_submit=view.findViewById(R.id.btn_submit);
        edt_email=view.findViewById(R.id.edt_email);
        text_msg=view.findViewById(R.id.text_msg);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              email=edt_email.getText().toString();
              apiForForgotPassword(email);
            }
        });
btn_cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        LoginFragment fragment2 = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fregmentContainer, fragment2);
        fragmentTransaction.commit();
    }
});
btn_back2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        btn_cancel.performClick();
    }
});
        return view;
    }

    private void apiForForgotPassword(final String email) {
        if( !email.isEmpty()) {
            edt_email.setError("");

            text_msg.setVisibility(View.VISIBLE);
            edt_email.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            btn_back2.setVisibility(View.VISIBLE);

            String url1="https://www.gladdenmattresses.com/api/jil.0.1/auth/password/email";

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("Response_facebook", response.toString());

                    try {

                        StringBuffer str=new StringBuffer();
                        str.append("We have e-mailed your password reset link to ");
                        str.append(Html.fromHtml("<b>email</b>"));
                        str.append(" If clicking the link does not work, please copy and paste the URL into your browser instead. If you do not receive an email within 5 minutes, Please check your spam / junk email folder in case the email has been received there.");
                        text_msg.setText(str.toString());


                    } catch (Exception e){

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Error",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();


                    params.put("email", email);
                   // params.put("api_token", api_token);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

        }else
            edt_email.setError("Please enter correct email address.");

    }
}