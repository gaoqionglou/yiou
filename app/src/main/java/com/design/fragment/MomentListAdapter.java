package com.design.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.design.R;
import com.design.databinding.MomentListViewItemBinding;
import com.design.databinding.PicListViewItemBinding;
import com.design.model.Moment;
import com.design.model.OldPic;

import java.util.List;

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.ViewHolder> {


    private List<Moment> mInfoList;
    private Context mContext;

    public MomentListAdapter(List<Moment> mInfoList, Context mContext) {
        this.mInfoList = mInfoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_list_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Moment moment = mInfoList.get(position);
        holder.viewItemBinding.desc.setText(moment.getDesc());
        Glide.with(mContext).load(moment.getImg()).into(holder.viewItemBinding.pic);


    }

    @Override
    public int getItemCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    public void setData(List<Moment> infoList) {
        this.mInfoList = infoList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MomentListViewItemBinding viewItemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewItemBinding = MomentListViewItemBinding.bind(itemView);
        }
    }
}
