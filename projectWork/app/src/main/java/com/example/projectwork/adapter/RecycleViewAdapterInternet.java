package com.example.projectwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.Film;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapterInternet extends RecyclerView.Adapter<RecycleViewAdapterInternet.MyViewHolder>{


    private Context context;
    private List<Film> mData;


    public RecycleViewAdapterInternet(Context context, List<Film> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_item_film,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.textViewTitolo.setText(mData.get(position).getTitolo());

        final ImageView vImmagine1 =  holder.imageView;

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+ mData.get(position).getThumbnail())
                .into(vImmagine1);

        //holder.imageView.setImageResource(mData.get(position).getThumbnail());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView;
        TextView textViewTitolo;
        TextView idFilm;

        public MyViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageFilm);
            textViewTitolo = itemView.findViewById(R.id.titoloFilm);
            idFilm = itemView.findViewById(R.id.idFilm);

        }
    }
}
