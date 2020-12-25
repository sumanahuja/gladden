package com.jil.gladdenmatresses;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentReturnPolicy extends Fragment {
private Context context;
private TextView txt_policy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_return_policy, container, false);
        context=view.getContext();
        txt_policy=view.findViewById(R.id.txt_policy);
        txt_policy.setText("If you are not entirely satisfied with your purchase or if you have received the purchased product in damaged condition, we are here to help you. We provide 7 calendar days to return from date of purchased product delivered to you.");

   return view;
    }
}