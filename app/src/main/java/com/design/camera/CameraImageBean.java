package com.design.camera;

import android.net.Uri;

/**
 * Created by Andings
 * 存储一些中间值
 */

public final class CameraImageBean {

    private static final CameraImageBean INSTANCE = new CameraImageBean();
    private Uri mPath = null;

    public static CameraImageBean getInstance() {
        return INSTANCE;
    }

    public Uri getPath() {
        return mPath;
    }

    public void setPath(Uri mPath) {
        this.mPath = mPath;
    }
}
