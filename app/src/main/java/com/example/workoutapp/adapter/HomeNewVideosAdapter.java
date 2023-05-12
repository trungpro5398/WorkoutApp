package com.example.workoutapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.databinding.NewVideosRvLayoutBinding;
import com.example.workoutapp.entity.NewVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeNewVideosAdapter extends RecyclerView.Adapter<HomeNewVideosAdapter.NewVideoViewHolder> {
    private List<NewVideo> newVideoList;
    public HomeNewVideosAdapter(List<NewVideo> newVideoList) {
        this.newVideoList = newVideoList;
    }

    private Observer<NewVideo> itemClickObserver;
    public void setNewVideoList(List<NewVideo> newVideoList) {
        this.newVideoList = newVideoList;
        notifyDataSetChanged();
    }

    public void setItemClickObserver(Observer<NewVideo> observer){
        this.itemClickObserver = observer;
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
        holder.url.setText(newVideo.getTitle());

        // TODO: put in async thread
        if (newVideo.getImageUrl() != null) {
            Picasso.get().load(newVideo.getImageUrl()).into(holder.thumbnail);
        } else {
            holder.thumbnail.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickObserver != null) {
                    itemClickObserver.onChanged(newVideo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (newVideoList==null) return 0;
        return newVideoList.size();
    }

    public static class NewVideoViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView url;

        ImageView thumbnail;



        NewVideosRvLayoutBinding binding;
        public NewVideoViewHolder(NewVideosRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            this.title=binding.tvRvtitle;
            this.url=binding.tvRvUrl;
            this.thumbnail=binding.thumbnail;
        }
    }

}
