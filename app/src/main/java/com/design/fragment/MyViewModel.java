package com.design.fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.design.model.Moment;
import com.design.model.OldPic;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.design.utils.ToastUtil.toast;

public class MyViewModel extends ViewModel {

    private MutableLiveData<List<OldPic>> oldpics = new MutableLiveData<>();
    private MutableLiveData<List<Moment>> moments = new MutableLiveData<>();

    public MutableLiveData<List<OldPic>> getOldpics() {
        return oldpics;
    }

    public MutableLiveData<List<Moment>> getMoments() {
        return moments;
    }

    public void oldPics() {
        BmobQuery<OldPic> query = new BmobQuery<>();
        query.findObjects(new FindListener<OldPic>() {
            @Override
            public void done(List<OldPic> list, BmobException e) {

                if (e == null) {
                    toast("查询成功");
                    oldpics.setValue(list);
                } else {
                    toast("查询失败");
                    e.printStackTrace();
                }
            }
        });
    }

    public void moments() {
        BmobQuery<Moment> query = new BmobQuery<>();
        query.findObjects(new FindListener<Moment>() {
            @Override
            public void done(List<Moment> list, BmobException e) {
                if (e == null) {
                    toast("查询成功");
                    moments.setValue(list);
                } else {
                    toast("查询失败");
                    e.printStackTrace();
                }
            }
        });
    }

}
