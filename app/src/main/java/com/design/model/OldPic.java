package com.design.model;

import cn.bmob.v3.BmobObject;

public class OldPic extends BmobObject {
    private String img;
    private String desc;
    private String avatar;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
