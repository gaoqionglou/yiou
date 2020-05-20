package com.design.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.design.R;
import com.design.databinding.FragmentActivityBinding;
import com.design.databinding.FragmentOldPicBinding;
import com.design.model.OldPic;

import java.util.List;

public class FragmentOldPic extends Fragment {


    private FragmentOldPicBinding binding;
    private MyViewModel myViewModel;
    private PicListAdapter mAdapter;
    public FragmentOldPic() {

    }

    public static FragmentOldPic newInstance() {
        FragmentOldPic fragment = new FragmentOldPic();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOldPicBinding.inflate(inflater);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        mAdapter = new PicListAdapter(null,getContext());
//        DividerItemDecoration dec = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        dec.setDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.divider_line_color)));
//        binding.rvOldPic.addItemDecoration(dec);
        binding.rvOldPic.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOldPic.setAdapter(mAdapter);
        myViewModel.getOldpics().observe(getViewLifecycleOwner(), new Observer<List<OldPic>>() {
            @Override
            public void onChanged(List<OldPic> oldPics) {
                mAdapter.setData(oldPics);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myViewModel.oldPics();
    }




}
