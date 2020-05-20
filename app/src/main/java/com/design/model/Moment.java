package com.design.model;

import cn.bmob.v3.BmobObject;

public class Moment extends BmobObject {
    private String img;
    private String desc;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
