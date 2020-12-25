package com.jil.gladdenmatresses;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRegistrationFragment extends Fragment {

    private Spinner spinner_states, spinner_model;
    private EditText et_name, et_email, et_phone, et_city, et_zipCode, et_serialNo, et_invoice, et_dOP, et_dealerName;
    private Button btn_register, btn_back;
    private String name, email, phone, city, state, model, invoiceNo, zipCode, serialNo, dop, dealerName;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_product_registration, container, false);
        context = view.getContext();
        btn_back = view.findViewById(R.id.btn_back);
        btn_register = view.findViewById(R.id.btn_register);
        et_name = view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_email);
        et_phone = view.findViewById(R.id.et_phone);
        et_city = view.findViewById(R.id.et_city);
        et_zipCode = view.findViewById(R.id.et_zipCode);
        et_serialNo = view.findViewById(R.id.et_serialNo);
        et_invoice = view.findViewById(R.id.et_invoice);
        et_dOP = view.findViewById(R.id.et_dOP);
        et_dealerName = view.findViewById(R.id.et_dealerName);
        ;
        spinner_states = view.findViewById(R.id.spinner_state);
        spinner_model = view.findViewById(R.id.spinner_model);


        String[] states_array = getResources().getStringArray(R.array.states_array);
//        Toast.makeText(context, ""+states_array.length+"", Toast.LENGTH_SHORT).show();
        final List myList = new ArrayList();
        Collections.addAll(myList, states_array);
        //      List<String> stateList = new ArrayList<String>(Arrays.asList(states_array));
//        Toast.makeText(context, ""+stateList.get(1), Toast.LENGTH_SHORT).show();

        ArrayAdapter adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, myList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_states.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
        final String[] model_array = {"Select Model", "GRAND", "ORTHO PRO", "DURO PLUS", "TITANIUM", "GLADDEN FIRMER", "GLADDEN LUXURY", "DUVET", "DUVET LUXURY", "REFLEXO", "AURA SOFT", "ULTIMA", "LOTUS", "HYBRID PRO", "HYBRID LUXURY"
                , "PRIME SOFT", "SIESTA", "REGIS", "GLADDEN ESCAPE", "ICE COOL", "GLADDEN DELIGHT", "GLADDEN STERLING", "GLADDEN EDGE"};

        final List myList_model = new ArrayList<String>(Arrays.asList(model_array));
        ArrayAdapter adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, myList_model);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_model.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m = myList_model.get(position).toString().toLowerCase();
                if (m.equals("Select Model".toLowerCase())) {
                    //Toast.makeText(context, "Select Valid Model:", Toast.LENGTH_SHORT).show();
                } else model = myList_model.get(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                model = "";
            }
        });
        spinner_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m = myList.get(position).toString().toLowerCase();
                if (m.equals("Select State".toLowerCase())) {
                   // Toast.makeText(context, "Select Valid State:", Toast.LENGTH_SHORT).show();
                } else state = myList.get(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                state = "";
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, ""+getFragmentManager().getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
//              getFragmentManager().popBackStack();
            }
        });
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month + 1);
                int month1 = month + 1;
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                et_dOP.setText(dayOfMonth + "/" + month1 + "/" + year);
            }


        };

        et_dOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                email = et_email.getText().toString();
                phone = et_phone.getText().toString();
                city = et_city.getText().toString();
                zipCode = et_zipCode.getText().toString();
                serialNo = et_serialNo.getText().toString();
                dop = et_dOP.getText().toString();
                dealerName = et_dealerName.getText().toString();
                invoiceNo = et_invoice.getText().toString();
                if (name.length() == 0)
                    et_name.setError("Please enter valid Entry");
                else et_name.setError(null);
                if (email.length() == 0)
                    et_email.setError("Please enter valid Entry");
                else et_email.setError(null);
                if (phone.length() == 0)
                    et_phone.setError("Please enter valid Entry");
                else et_phone.setError(null);
                if (zipCode.length() == 0)
                    et_zipCode.setError("Please enter valid Entry");
                else et_zipCode.setError(null);
                if (dealerName.length() == 0)
                    et_dealerName.setError("Please enter valid Entry");
                else et_dealerName.setError(null);
                if (dop.length() == 0)
                    et_dOP.setError("Please enter valid Entry");
                else et_dOP.setError(null);
                if (serialNo.length() == 0)
                    et_serialNo.setError("Please enter valid Entry");
                else et_serialNo.setError(null);
                if (city.length() == 0)
                    et_city.setError("Please enter valid Entry");
                else et_city.setError(null);
                if (invoiceNo.length() == 0)
                    et_invoice.setError("Please enter valid Entry");
                else et_invoice.setError(null);
                if (state.length() == 0 || model.length() == 0) {
                    Toast.makeText(context, "Select valid entry", Toast.LENGTH_SHORT).show();
                }
                if (name.length() > 2 && email.length() > 0 && phone.length() > 0 && city.length() > 0 && state.length() > 0 && model.length() > 0 && invoiceNo.length() > 0 && zipCode.length() > 0 && serialNo.length() > 0 && dop.length() > 0 && dealerName.length() > 0) {
                    submitCodeApi();
                } else
                    Toast.makeText(context, "Please enter valid entries to register", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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
    private void submitCodeApi() {
        String url = "https://www.gladdenmattresses.com/api/jil.0.1/v2/product/registeration";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.i("Response_CodeSubmission", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("true"))
                    {
                        Toast.makeText(context, "Product is registered successfully" , Toast.LENGTH_SHORT).show();

                    Fragment fragment2=new ProductRegistrationFragment();
                        replaceFragment(fragment2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("status").equals("true")) {
//                        Toast.makeText(SignUp.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                        mySharedPrefClass.getInstance(getApplicationContext()).save_data(u);
////                       UserModel u1= mySharedPrefClass.getInstance(getApplicationContext()).get_data();
////                       Log.i("U1",u1.getFull_name());
//
//                        startActivity(new Intent(getApplicationContext(), DrawerActivity.class));
//
//                    } else if (jsonObject.getString("status").equals("false")) {
//                        Toast.makeText(SignUp.this, "" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
//                        etCode.setText("");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("city", city);
                params.put("state", state);
                params.put("zipcode", zipCode);
                params.put("model_name", model);
                params.put("serial_number", serialNo);
                params.put("invoice_number", invoiceNo);
                params.put("purchase_date", dop);
                params.put("dealer_name", dealerName);
                params.put("api_token", "awbjNS6ocmUw0lweblc1FuvMqgUp3ayD8d3n0almUCYs");

                Log.i("params", name);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}