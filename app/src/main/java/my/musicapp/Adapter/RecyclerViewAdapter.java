package my.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import my.musicapp.Model.Upload;
import my.musicapp.Model.UploadOL;
import my.musicapp.R;
import my.musicapp.SongActivity;

public  class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mcontext;
    private List <UploadOL> UploadOLS ;

    public RecyclerViewAdapter(Context mcontext, List<UploadOL> uploadOLS) {
        this.mcontext = mcontext;
        this.UploadOLS = uploadOLS;
    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.card_view_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final UploadOL uploadOL = UploadOLS.get(position);
        holder.tv_book_txt.setText(uploadOL.getName());

        Glide.with(mcontext).load(uploadOL.getUrl()).into(holder.img_Book_thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, SongActivity.class);
                intent.putExtra("songCategory",uploadOL.getSongcategory());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return UploadOLS.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_txt;
        ImageView img_Book_thumbnail;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_book_txt = itemView.findViewById(R.id.book_title);
            img_Book_thumbnail = itemView.findViewById(R.id.book_img_id);
            cardView =itemView.findViewById(R.id.card_view);
        }
    }

}

