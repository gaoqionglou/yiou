package com.design.camera;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.blankj.utilcode.util.FileUtils;
import com.design.R;
import com.design.utils.JFileUtil;

import java.io.File;

/**
 * Created by Anding
 * 照片处理Core类
 */

public class CameraHandler implements View.OnClickListener {

    private final AlertDialog DIALOG;

    private Context context;

    private Fragment fragment;

    public CameraHandler(Context context) {
        this.context = context;
        DIALOG = new AlertDialog.Builder(context).create();
    }

    public CameraHandler withFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public void beginCameraDialog() {
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera_panel);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.photo_dialog_btn_cancel).setOnClickListener(this);
            window.findViewById(R.id.photo_dialog_btn_take).setOnClickListener(this);
            window.findViewById(R.id.photo_dialog_btn_native).setOnClickListener(this);

        }
    }

    private String getPhotoName() {
        return JFileUtil.getFileNameByTime("IMG", "jpg");
    }

    //头像-拍照
    private void takePhone() {
        final String currentPhotoName = getPhotoName();
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File tempFile = new File(JFileUtil.CAMERA_PHOTO_DIR, currentPhotoName);

        //兼容7.0及以上的写法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getPath());
            final Uri uri = context.getContentResolver().
                    insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            //需要讲Uri路径转化为实际路径
            final File realFile =
                    FileUtils.getFileByPath(JFileUtil.getRealFilePath(context, uri));
            final Uri realUri = Uri.fromFile(realFile);
            CameraImageBean.getInstance().setPath(realUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            final Uri fileUri = Uri.fromFile(tempFile);
            CameraImageBean.getInstance().setPath(fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }

        if (fragment != null) {
            fragment.startActivityForResult(intent, RequestCodes.TAKE_PHOTO);
        } else {
            ((Activity) context).startActivityForResult(intent, RequestCodes.TAKE_PHOTO);
        }
    }

    //头像-相册选择
    private void pickPhoto() {
        final Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (fragment != null) {
            fragment.startActivityForResult
                    (Intent.createChooser(intent, "选择获取图片的方式"), RequestCodes.PICK_PHOTO);
        } else {
            ((Activity) context).startActivityForResult
                    (Intent.createChooser(intent, "选择获取图片的方式"), RequestCodes.PICK_PHOTO);
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.photo_dialog_btn_cancel) {
            DIALOG.cancel();
        } else if (id == R.id.photo_dialog_btn_take) {
            takePhone();
            DIALOG.cancel();
        } else if (id == R.id.photo_dialog_btn_native) {
            pickPhoto();
            DIALOG.cancel();
        }
    }

}
