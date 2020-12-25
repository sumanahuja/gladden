package com.jil.gladdenmatresses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText etName, etLastName, etEmail, etPassword, etCnfPassword, etCode;
    private Button btnSignUp, btnSubmitCode;
    private String Name, LastName, Email, Paswd, Paswd2;
    private LinearLayout signUpLayout;
    private TextView title, txtThanksMsg, txtOTP,login;
    private UserModel u;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.toolbar2);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etCnfPassword = findViewById(R.id.etConfirmPassword);
        etCode = findViewById(R.id.etCode);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSubmitCode = findViewById(R.id.btnSubmitCode);
        signUpLayout = findViewById(R.id.signUpLayout);
        title = findViewById(R.id.title);
        txtThanksMsg = findViewById(R.id.txtThanksMsg);
        txtOTP = findViewById(R.id.txtOTP);
        login=findViewById(R.id.login);

        toolbar.setTitle("REGISTRATION");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DrawerActivity.class);
                intent.putExtra("open","login");
                startActivity(intent);
            }
        });
        btnSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitCodeApi(Email, etCode.getText());
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = etName.getText().toString();
                LastName = etLastName.getText().toString();
                Email = etEmail.getText().toString();
                Paswd = etPassword.getText().toString();
                Paswd2 = etCnfPassword.getText().toString();
                if (Paswd.equals(Paswd2) && !Name.isEmpty() && !LastName.isEmpty() && !Email.isEmpty() && !Paswd.isEmpty()) {
                    Name = Name + " " + LastName;
                    SubmitApi(Name, Email, Paswd, Paswd2);
                }

            }
        });

    }

    private void submitCodeApi(final String email, final Editable text) {
        String url = "https://www.gladdenmattresses.com/api/jil.0.1/account/confirm";

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.i("Response_CodeSubmission", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        Toast.makeText(SignUp.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        mySharedPrefClass.getInstance(getApplicationContext()).save_data(u);
//                       UserModel u1= mySharedPrefClass.getInstance(getApplicationContext()).get_data();
//                       Log.i("U1",u1.getFull_name());

                        startActivity(new Intent(getApplicationContext(), DrawerActivity.class));

                    } else if (jsonObject.getString("status").equals("false")) {
                        Toast.makeText(SignUp.this, "" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        etCode.setText("");
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                //  getData();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("token", String.valueOf(text));


                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void SubmitApi(final String name, final String email, final String paswd, String paswd2) {
        String url = "https://www.gladdenmattresses.com/api/jil.0.1/auth/register?api_token=awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs";

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.i("Response_SignUp", response);

                try {

                    title.setText("VERIFICATION");
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("false")) {
                        Toast.makeText(SignUp.this, jsonObject.getString("error") + "", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(getApplicationContext(),SignUp.class));
                    } else {
                        signUpLayout.setVisibility(View.GONE);
                        txtThanksMsg.setText(Html.fromHtml(jsonObject.getString("message")));
                        txtThanksMsg.setVisibility(View.VISIBLE);
                        etCode.setVisibility(View.VISIBLE);
                        txtOTP.setVisibility(View.VISIBLE);
                        btnSubmitCode.setVisibility(View.VISIBLE);
                        JSONObject userObj = jsonObject.getJSONObject("data");

                        String First_name = userObj.getString("first_name");
                        String Last_name = userObj.getString("last_name");
                        String Email = userObj.getString("email");
                        String Image = userObj.getString("image");
                        String Phone = userObj.getString("phone");
                        int Id = userObj.getInt("id");
                        u = new UserModel(1, First_name + " " + Last_name, Email, Image, Phone, Id);
                        Log.i("USER", u.getFull_name() + u.getEmail() + u.getImage());
                    }
//                    SharedPreferences get_sign_up_data = SignUp.this.getSharedPreferences("data", 0);
//                    SharedPreferences.Editor editor = get_sign_up_data.edit();
//                    editor.putString("data", data);
//                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //  getData();
                Map<String, String> params = new HashMap<String, String>();
                JSONObject onj1 = new JSONObject();

                try {
                    //String app_token= FirebaseInstanceId.getInstance().getToken()+"";
                    onj1.put("name", name);
                    onj1.put("email", email);
                    onj1.put("password", paswd);
                    onj1.put("image", "avatar.png");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.put("data", onj1.toString());


                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}