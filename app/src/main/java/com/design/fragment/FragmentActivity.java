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
import com.design.model.Moment;
import com.design.model.OldPic;

import java.util.List;

public class FragmentActivity extends Fragment {
    private FragmentActivityBinding binding;
    private MyViewModel myViewModel;
    private MomentListAdapter mAdapter;

    public FragmentActivity() {

    }

    public static FragmentActivity newInstance() {
        FragmentActivity fragment = new FragmentActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentActivityBinding.inflate(inflater);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        mAdapter = new MomentListAdapter(null, getContext());
//        DividerItemDecoration dec = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        dec.setDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.divider_line_color)));
//        binding.rvAct.addItemDecoration(dec);
        binding.rvAct.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAct.setAdapter(mAdapter);
        myViewModel.getMoments().observe(getViewLifecycleOwner(), new Observer<List<Moment>>() {
            @Override
            public void onChanged(List<Moment> moments) {
                mAdapter.setData(moments);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myViewModel.moments();
    }



}
