package com.jil.gladdenmatresses;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.FragmentTransitionSupport;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    // TODO: Rename and change types of parameters

    private String full_name,email,image,phone_no;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=000,flag,id;
    private Context context;
    private GoogleSignInAccount account;
    private View view;
    private EditText edtLoginId,edtLoginPass;
    private TextView txt_forgotPass,create_an_account;
    private Button btnLogin;
    private UserModel u;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         view=inflater.inflate(R.layout.fragment_login, container, false);
        context=view.getContext();
        edtLoginId=view.findViewById(R.id.edtLoginId);
        edtLoginPass=view.findViewById(R.id.edtLoginPass);
        txt_forgotPass=view.findViewById(R.id.txt_forgotPass);
        btnLogin=view.findViewById(R.id.btnLogin);
        create_an_account=view.findViewById(R.id.create_an_account);
        create_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,SignUp.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String login_id=  edtLoginId.getText().toString();
                String login_pswd=  edtLoginPass.getText().toString();
                apiForLogin(login_id,login_pswd);
            }
        });
        txt_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment fragment2 = new ForgotPasswordFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fregmentContainer, fragment2);
                fragmentTransaction.commit();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        updateUI(account);
        SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return view;
    }

    private void apiForLogin(final String login_id, final String login_pswd) {
        String url = "https://www.gladdenmattresses.com/api/jil.0.1/auth/login";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("Response_Login", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        Toast.makeText(context, "" + "Login Successful", Toast.LENGTH_SHORT).show();
                        JSONObject userObj= jsonObject.getJSONObject("data");

                        String First_name=userObj.getString("first_name");
                        String Last_name= userObj.getString("last_name");
                        String Email=  userObj.getString("email");
                        String Image= userObj.getString("image");
                        String Phone= userObj.getString("phone");
                        int Id= userObj.getInt("id");
                        u=new UserModel(1,First_name+" "+Last_name,Email,Image,Phone,Id);

                        mySharedPrefClass.getInstance(context).save_data(u);
                       UserModel u1= mySharedPrefClass.getInstance(context).get_data();
                       if(u1!=null)
                       Log.i("U1",u1.getFull_name());

                        startActivity(new Intent(context, DrawerActivity.class));

                    } else if (jsonObject.getString("status").equals("false")) {
                        Toast.makeText(context, "" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                       edtLoginId.setText("");
                       edtLoginPass.setText("");
                        // etCode.setText("");
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
                params.put("email", login_id);
                params.put("password", login_pswd);


                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//             account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//           // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }
    private void updateUI(GoogleSignInAccount a)
    {//String  userName=a.getDisplayName();
    Log.i("USER","userName");
          //  Log.i("ACCOUNT",a.getDisplayName()+a.getEmail()+a.getGivenName()+a.getFamilyName()+a.getPhotoUrl());
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        /*
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            final String personName = account.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
            final String personEmail = account.getEmail();
            final String personId = account.getId();
            final Uri personPhoto = account.getPhotoUrl();
//            final String authToken = account.getIdToken();

            String url;
            //getData();
            url = api_url+"auth/login/google";

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.i("Response", response.toString());

                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        Log.i("Response",  jsonObject1.getString("data"));

                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("data"));
//                        Log.i("Response",  jsonObject2.getString("id"));

                        if(jsonObject2!=null) {
                            flag = 1;
                            full_name = jsonObject2.getString("first_name")+" "+jsonObject2.getString("last_name");
                            email = jsonObject2.getString("email");
                            image = jsonObject2.getString("image");
                            phone_no = jsonObject2.getString("phone");
                            id=jsonObject2.getInt("id");
                            saveData();
                            ResetUserDropDownList();
                            Intent resultIntent = new Intent();
                            setResult(2,resultIntent);
                            finish();//finishing activity
                        }else{
                            flag=0;
                        }

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
                protected Map<String, String> getParams() {
                    //getData();
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("name", personName);
                    params.put("email", personEmail);
                    params.put("provider_id", personId);
                    params.put("token", "");
                    params.put("provider", "google");
                    if (personPhoto != null){
                        params.put("avatar", personPhoto.toString());
                    } else {
                        params.put("avatar", "");
                    }
                    ////getData();
                    params.put("api_token", api_token);
                    params.put("app_token",FirebaseInstanceId.getInstance().getToken()+"");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            flag=1;

            if (account != null){

//              startActivity(new Intent(MainActivity.this, Main2Activity.class));
                Menu show=navigationView.getMenu();

                if(flag==1) {

                    name.setText(full_name);
                    full_name=personName;

                    login.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.INVISIBLE);
                    show.findItem(R.id.nav_send).setVisible(true);
                    show.findItem(R.id.nav_share).setVisible(true);
                    show.findItem(R.id.advertise).setVisible(false);
                    show.findItem(R.id.loginPage).setVisible(true);
                    logout.setVisibility(View.VISIBLE);
                }
                saveData();
                Intent intent = new Intent(context, DrawerActivity.class);
                startActivity(intent);
                Intent resultIntent = new Intent();
                setResult(2,resultIntent);
                finish();//finishing activity
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG",  "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);

        }
    }

    private void ResetUserDropDownList() {

//        String[] data = {"My Profile", "My Orders", "Logout"};
//
//        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_selectable_list_item, data);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
      //  MenuItem item_user=view.findViewById(R.id.user);
      */


    }

    public void saveData()
    {
      SharedPreferences  loginData = context.getSharedPreferences("userData",0);
        SharedPreferences.Editor editor=loginData.edit();
        editor.putInt("Flag",flag);
        editor.putString("userName",full_name);
        editor.putString("userEmail",email);
        editor.putString("userImage",image);
        editor.putString("userPhone",phone_no);
        //editor.putString("create",created);
        //editor.putString("update",updated);
        editor.putInt("userId",id);
       // editor.putString("app_token", FirebaseInstanceId.getInstance().getToken());
        editor.apply();

    }
}