package com.design.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.design.R;
import com.design.common.MConstants;
import com.design.databinding.PicListViewItemBinding;
import com.design.model.OldPic;

import java.util.List;

public class PicListAdapter extends RecyclerView.Adapter<PicListAdapter.ViewHolder> {


    private List<OldPic> mInfoList;
    private Context mContext;

    public PicListAdapter(List<OldPic> mInfoList, Context mContext) {
        this.mInfoList = mInfoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_list_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OldPic pic = mInfoList.get(position);
        holder.viewItemBinding.desc.setText(pic.getDesc());
        Glide.with(mContext).load(pic.getImg()).into(holder.viewItemBinding.pic);


    }

    @Override
    public int getItemCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    public void setData(List<OldPic> infoList) {
        this.mInfoList = infoList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        PicListViewItemBinding viewItemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewItemBinding = PicListViewItemBinding.bind(itemView);
        }
    }
}
