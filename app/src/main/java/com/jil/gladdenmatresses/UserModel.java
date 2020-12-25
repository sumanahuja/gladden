package com.jil.gladdenmatresses;

public class UserModel {
    private int flag;
    private String full_name;
    private String email;
    private String image;
    private String phone_no;
    private int id;

    public int getFlag() {
        return flag;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public int getId() {
        return id;
    }



    public UserModel(int flag, String full_name, String email, String image, String phone_no, int id) {
        this.flag = flag;
        this.full_name = full_name;
        this.email = email;
        this.image = image;
        this.phone_no = phone_no;
        this.id = id;
    }

}
