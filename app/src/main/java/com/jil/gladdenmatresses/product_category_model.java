package com.jil.gladdenmatresses;

import android.os.Parcel;
import android.os.Parcelable;

public class product_category_model implements Parcelable {
    private String image_url;
    private String list_title;
    private String list_description;

    public String getList_id() {
        return list_id;
    }

    public String getList_url_name() {
        return list_url_name;
    }

    private String list_id;
    private String list_url_name;

    public product_category_model(String image_url, String list_title, String list_description, String list_id, String list_url_name) {
        this.image_url = image_url;
        this.list_title = list_title;
        this.list_description = list_description;
        this.list_id = list_id;
        this.list_url_name = list_url_name;
    }



//    public product_category_model(String image_url, String list_title, String list_description) {
//        this.image_url = image_url;
//        this.list_title = list_title;
//        this.list_description = list_description;
//    }

    protected product_category_model(Parcel in) {
        image_url = in.readString();
        list_title = in.readString();
        list_description = in.readString();
        list_id = in.readString();
        list_url_name = in.readString();

    }

    public static final Creator<product_category_model> CREATOR = new Creator<product_category_model>() {
        @Override
        public product_category_model createFromParcel(Parcel in) {
            return new product_category_model(in);
        }

        @Override
        public product_category_model[] newArray(int size) {
            return new product_category_model[size];
        }
    };

    public String getImage_url() {
        return image_url;
    }

    public String getList_title() {
        return list_title;
    }

    public String getList_description() {
        return list_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_url);
        dest.writeString(list_title);
        dest.writeString(list_description);
        dest.writeString(list_id);
        dest.writeString(list_url_name);
    }
}
