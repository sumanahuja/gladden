package com.jil.gladdenmatresses;

public class MenuModel {

        public String menuName, url,id,image,icon;
        public boolean hasChildren, isGroup;

    public String getMenuName() {
        return menuName;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public MenuModel(String menuName, String url, String id, String image, String icon, boolean hasChildren, boolean isGroup) {
        this.menuName = menuName;
        this.url = url;
        this.id = id;
        this.image = image;
        this.icon = icon;
        this.hasChildren = hasChildren;
        this.isGroup = isGroup;
    }
}
