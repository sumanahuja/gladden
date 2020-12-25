package com.jil.gladdenmatresses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private EditText edt_Phone, edt_Email, edt_LName, edt_FName;
    private Button btn_Submit, btn_Cancel;
    private View view;
    private Context context;
    private UserModel u;
    private ImageView iv_profilePic;
    private TextView tv_changePassword;
    private String ImageString, imageName;
    private int PICK_IMAGE_MULTIPLE = 1,profilePicClicked=0;
    private Bitmap bitmapImage;

    public ProfileFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();
        u = mySharedPrefClass.getInstance(context).get_data();
        Log.i("Profile", u.getFull_name());
        edt_Phone = view.findViewById(R.id.edt_Phone);
        edt_Email = view.findViewById(R.id.edt_Email);
        edt_LName = view.findViewById(R.id.edt_LName);
        edt_FName = view.findViewById(R.id.edt_FName);
        btn_Submit = view.findViewById(R.id.btn_Submit);
        btn_Cancel = view.findViewById(R.id.btn_Cancel);
        iv_profilePic = view.findViewById(R.id.iv_profilePic);

        tv_changePassword = view.findViewById(R.id.tv_changePassword);
        tv_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordFragment fragment2 = new ChangePasswordFragment();
                replaceFragment(fragment2);
               // FragmentManager fm = getFragmentManager();
                //fm.beginTransaction().replace(R.id.fregmentContainer, fragment2).commit();

            }
        });
        iv_profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
                profilePicClicked=1;
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DrawerActivity.class));
            }
        });
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_phone = edt_Phone.getText().toString();
                String edit_email = edt_Email.getText().toString();
                String edit_lastName = edt_LName.getText().toString();
                String edit_firstName = edt_FName.getText().toString();

                apiForSubmit(edit_firstName, edit_lastName, edit_phone, edit_email);
            }
        });
        if (u != null) {
            if (!u.getPhone_no().toString().equals("null"))
                edt_Phone.setText(u.getPhone_no());
            else edt_Phone.setText("");
            edt_Email.setText(u.getEmail());
            String fullName = u.getFull_name();
            int idx = fullName.lastIndexOf(" ");
            if (idx == -1) {
                String firstName = fullName;
                String lastName = "";
                edt_FName.setText(firstName);
                edt_LName.setText(lastName);
            } else {
                String firstName = fullName.substring(0, idx);
                String lastName = fullName.substring(idx + 1);
                edt_FName.setText(firstName);
                edt_LName.setText(lastName);
            }
            if (!u.getImage().equals("null"))
                Picasso.with(context).load("https://www.gladdenmattresses.com/uploads/profile/" + u.getImage()).fit().into(iv_profilePic);
            else
                Picasso.with(context).load("https://www.gladdenmattresses.com/uploads/profile/avatar.png").fit().into(iv_profilePic);
Log.i("u.getImage","https://www.gladdenmattresses.com/uploads/profile/" + u.getImage());
        }
        return view;
    }

    private void apiForSubmit(final String edit_firstName, final String edit_lastName, final String edit_phone, String edit_email) {
        try {


            final int ID = mySharedPrefClass.getInstance(context).get_data().getId();
            final String user_NAME = mySharedPrefClass.getInstance(context).get_data().getFull_name();


            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            String myurl = "https://www.gladdenmattresses.com/api/jil.0.1/account/profile/" + ID;
            Log.i("URl_photo_upload", myurl);
            //  progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, myurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Response11 ", response);

                    //  progressDialog.dismiss();
                    try {
                        JSONObject obj=new JSONObject(response);
                        String status=obj.getString("status");
                        if(status.equals("true")) {
                            String message = obj.getString("message");
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            JSONObject userObj=obj.getJSONObject("data");
                            String First_name=userObj.getString("first_name");
                            String Last_name= userObj.getString("last_name");
                            String Email=  userObj.getString("email");
                            String Image= userObj.getString("image");
                            String Phone= userObj.getString("phone");
                            int Id= userObj.getInt("id");
                            u=new UserModel(1,First_name+" "+Last_name,Email,Image,Phone,Id);

                            mySharedPrefClass.getInstance(context).save_data(u);
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.fregmentContainer, fragment2).commit();
                        }
                        else  if(status.equals("false")) {
                            String error = obj.getString("error");
                            Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Error", error.toString());
                    // progressDialog.dismiss();
                    Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {  // getData();
                    Map<String, String> params = new HashMap<String, String>();
                    Log.i("imageName", "https://www.gladdenmattresses.com/uploads/profile/" + edit_firstName.trim().toLowerCase() + "_" + imageName);
                    params.put("id", ID + "");
                    params.put("first_name", edit_firstName);
                    params.put("last_name", edit_lastName.trim());
                    params.put("mobile", edit_phone);
                    params.put("phone", edit_phone);
                    params.put("address", "");
                    if(profilePicClicked==1) {
                        params.put("filename", "https://www.gladdenmattresses.com/uploads/profile/" + edit_firstName.trim().toLowerCase() + "_" + imageName);
                        params.put("image", ImageString);
                    }
                    params.put("token", "awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs");

                    return params;
                }
            };
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong." + e, Toast.LENGTH_SHORT).show();
        }

    }

    public String getStringImage(Bitmap bitmap) {
        // Log.i("function bit",""+bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @SuppressLint("IntentReset")
    private void showFileChooser() {
//        Intent cameraIntent = new Intent();
//        cameraIntent.setType("image/*");
//        cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
//       // startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
////
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        cameraIntent.setType("image/*");
        //  cameraIntent.setAction();
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(cameraIntent, "Select Picture"), 1000);
        } else
            Toast.makeText(context, "Profile Image has not been picked,Try again.", Toast.LENGTH_SHORT).show();


//
//        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        cameraIntent.setType("image/*");
//
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                //File f = new File(String.valueOf(returnUri));
                File f = new File((returnUri + ""));

                imageName = f.getName() + ".jpeg";
                String path = f.getPath();
// it contains your image path...I'm using a temp string...
                String filename = path.substring(path.lastIndexOf("/") + 1);
                Log.i("file", f.getPath() + "\n" + filename + f.getName().lastIndexOf("."));
                bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    ImageString = getStringImage(bitmapImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Picasso.with(context).load(String.valueOf(bitmapImage)).fit().into(iv_profilePic);
                iv_profilePic.setImageBitmap(bitmapImage);

            }
        }

    }
    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fregmentContainer, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}