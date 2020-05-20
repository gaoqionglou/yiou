package com.design.camera;

import android.net.Uri;

import com.design.utils.JFileUtil;

public class CameraUtil {
    public static Uri createCropFile() {
        return Uri.parse(
                JFileUtil.createFile(
                        "crop_image",
                        JFileUtil.getFileNameByTime("IMG", "jpg")
                ).getPath()
        );
    }
}