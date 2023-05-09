package com.example.workoutapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.databinding.NewVideosRvLayoutBinding;
import com.example.workoutapp.model.NewVideo;

import java.util.List;

public class HomeNewVideosAdapter extends RecyclerView.Adapter<HomeNewVideosAdapter.NewVideoViewHolder> {
    private List<NewVideo> newVideoList;
    public HomeNewVideosAdapter(List<NewVideo> newVideoList) {
        this.newVideoList = newVideoList;
    }

    public void setNewVideoList(List<NewVideo> newVideoList) {
        this.newVideoList = newVideoList;
    }

    @NonNull
    @Override
    public NewVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewVideosRvLayoutBinding binding= NewVideosRvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewVideoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewVideoViewHolder holder, int position) {
        //the position of the item in the adapter's dataset
        final NewVideo newVideo = newVideoList.get(position);
        holder.title.setText(newVideo.getTitle());
        holder.url.setText(newVideo.getUrl());
    }

    @Override
    public int getItemCount() {
        if (newVideoList==null) return 0;
        return newVideoList.size();
    }

    public static class NewVideoViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView url;

        NewVideosRvLayoutBinding binding;
        public NewVideoViewHolder(NewVideosRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            this.title=binding.tvRvtitle;
            this.url=binding.tvRvUrl;
        }
    }

}
