package com.jil.gladdenmatresses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import javax.xml.parsers.FactoryConfigurationError;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class createPopUpMenu {
    FrameLayout fregmentContainer;
    Activity activity;
    Context context;
    MenuItem item;
    int flag;
    FragmentManager fragmentManager;

    public createPopUpMenu(Context context, MenuItem item, Activity activity, int flag, FrameLayout fregmentContainer, FragmentManager
            fragmentManager) {
        this.context = context;
        this.item = item;
        this.flag = flag;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.fregmentContainer = fregmentContainer;

    }

    void create() {
        try {

//                fregmentContainer.removeAllViews();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fregmentContainer, new LoginFragment()).commit();


            final View menuItemView = activity.findViewById(item.getItemId());
            final PopupMenu popup = new PopupMenu(context, menuItemView);
            //Inflating the Popup using xml file
            if (flag == 1) {
                popup.getMenuInflater().inflate(R.menu.popup2, popup.getMenu());
            } else {
                popup.getMenuInflater().inflate(R.menu.popup1, popup.getMenu());
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    switch (item.getItemId()) {
                        case R.id.itemLoginRegister:
                            fregmentContainer.removeAllViews();
                            replaceFragment(new LoginFragment());
                           // fragmentManager.beginTransaction().replace(R.id.fregmentContainer, new LoginFragment()).commit();
                            break;
                        case R.id.itemLogout:
                            mySharedPrefClass.getInstance(context).clear_data();
                            Toast.makeText(context, "Logout Successfully ", Toast.LENGTH_SHORT).show();
                           Intent intent=new Intent(context,DrawerActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case R.id.itemMyProfile:
                            fregmentContainer.removeAllViews();
                            replaceFragment(new ProfileFragment());
                          //  fragmentManager.beginTransaction().replace(R.id.fregmentContainer, new ProfileFragment()).commit();
                            break;
                    }
                    return true;
                }
            });

            popup.show();//showing popup menu

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();


        boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fregmentContainer, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
