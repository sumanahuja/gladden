package com.jil.gladdenmatresses;

import android.os.Parcel;
import android.os.Parcelable;

public class cart_items_model implements Parcelable {
    private String list_id,list_url,image_url, list_title,list_code,list_price,list_total_price,list_specifications,list_quantity;
    protected cart_items_model(Parcel in) {
        list_id = in.readString();
        list_url = in.readString();
        image_url = in.readString();
        list_title = in.readString();
        list_code = in.readString();
        list_price = in.readString();
        list_total_price = in.readString();
        list_specifications = in.readString();
        list_quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(list_id);
        dest.writeString(list_url);
        dest.writeString(image_url);
        dest.writeString(list_title);
        dest.writeString(list_code);
        dest.writeString(list_price);
        dest.writeString(list_total_price);
        dest.writeString(list_specifications);
        dest.writeString(list_quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<cart_items_model> CREATOR = new Creator<cart_items_model>() {
        @Override
        public cart_items_model createFromParcel(Parcel in) {
            return new cart_items_model(in);
        }

        @Override
        public cart_items_model[] newArray(int size) {
            return new cart_items_model[size];
        }
    };

    public String getList_id() {
        return list_id;
    }

    public String getList_url() {
        return list_url;
    }



    public String getImage_url() {
        return image_url;
    }

    public String getList_title() {
        return list_title;
    }

    public String getList_code() {
        return list_code;
    }

    public String getList_price() {
        return list_price;
    }

    public String getList_total_price() {
        return list_total_price;
    }

    public String getList_specifications() {
        return list_specifications;
    }

    public String getList_quantity() {
        return list_quantity;
    }

    public cart_items_model(String list_id, String list_url, String image_url, String list_title, String list_code, String list_price, String list_total_price, String list_specifications, String list_quantity) {
        this.list_id = list_id;
        this.list_url = list_url;
        this.image_url = image_url;
        this.list_title = list_title;
        this.list_code = list_code;
        this.list_price = list_price;
        this.list_total_price = list_total_price;
        this.list_specifications = list_specifications;
        this.list_quantity = list_quantity;
    }


}
