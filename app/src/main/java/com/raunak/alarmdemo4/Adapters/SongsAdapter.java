package com.raunak.alarmdemo4.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsView>{
    private ArrayList<String> songsArrayList;

    public SongsAdapter(ArrayList<String> song){
        this.songsArrayList = song;
    }


    @NonNull
    @Override
    public SongsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.songs_recyclerview,parent,false);
        return new SongsView(view);
    }

    @Override
    public int getItemCount() {
        return songsArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SongsView holder, int position) {
        holder.songName.setText(songsArrayList.get(position));
    }

    class SongsView extends RecyclerView.ViewHolder{
        TextView songName;
        SongsView(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.txtSong);
        }
    }
}
