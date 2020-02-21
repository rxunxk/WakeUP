package com.raunak.alarmdemo4.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raunak.alarmdemo4.Interfaces.SongSelectorInterface;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsView> implements Filterable {
    private ArrayList<String> songsArrayList;
    private ArrayList<String> allSongs;
    SongSelectorInterface anInterface;

    public SongsAdapter(ArrayList<String> song,SongSelectorInterface songSelectorInterfacerface){
        this.songsArrayList = song;
        this.anInterface = songSelectorInterfacerface;
        this.allSongs = new ArrayList<>(song);
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
        if (songsArrayList.get(position).length() > 10){
            holder.songName.setText(songsArrayList.get(position));
            holder.songName.setSelected(true);
        }else {
            holder.songName.setText(songsArrayList.get(position));
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<String> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty())
                filteredList.addAll(allSongs);
            else{
                for (String song: allSongs){
                    if (song.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(song);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            songsArrayList.clear();
            songsArrayList.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
        }
    };

    class SongsView extends RecyclerView.ViewHolder{
        TextView songName;
        SongsView(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.txtSong);
            songName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
