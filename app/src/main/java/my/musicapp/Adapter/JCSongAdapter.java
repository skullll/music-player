package my.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.musicapp.Model.GetSongs;
import my.musicapp.Model.Utility;
import my.musicapp.R;

public class JCSongAdapter extends RecyclerView.Adapter<JCSongAdapter.SongsAdapterViewHolder> {

    private int selectedPosition;
    Context context;
    List<GetSongs> arrayListSongs;
    private RecyclerItemClickListener listener;

    public JCSongAdapter(Context context, List<GetSongs> arrayListSongs, RecyclerItemClickListener listener) {
        this.context = context;
        this.arrayListSongs = arrayListSongs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.songs_rows,parent,false);
        return new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapterViewHolder holder, int position) {

        GetSongs getSongs = arrayListSongs.get(position);

        if(getSongs != null){
            if(selectedPosition == position){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.darkgrey));
                holder.play_active.setVisibility(View.VISIBLE);
            }else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.black));
                holder.play_active.setVisibility(View.INVISIBLE);
            }
        }

        holder.tv_title.setText(getSongs.getSongTitle());
        holder.tv_artist.setText(getSongs.getArtist());
        //String duration = Utility.convertDuration(Long.parseLong(getSongs.getsongDuration()));
        //holder

        holder.bind(getSongs,listener);

    }

    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title , tv_artist ;
        ImageView play_active;
        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_titleol);
            tv_artist = itemView.findViewById(R.id.artistol);
            play_active  = itemView.findViewById(R.id.playctivity);
        }

        public void bind(GetSongs getSongs, RecyclerItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(getSongs , getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerItemClickListener {

        void  onClickListener(GetSongs songs , int position);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
