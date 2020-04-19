package com.example.projectwork.adapter;

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

import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.activity.Film;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<Film> mData;


    public RecyclerViewAdapter(Context context, List<Film> mData) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.titoloFilm.setText(mData.get(position).getTitolo());
        holder.film_thumbnail.setImageResource(mData.get(position).getThumbnail());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DettaglioFilm.class);
                intent.putExtra("titoloFilm",mData.get(position).getTitolo());
                intent.putExtra("descrizione",mData.get(position).getDescrizione());
                intent.putExtra("thumbnail",mData.get(position).getThumbnail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titoloFilm;
        ImageView film_thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            titoloFilm = itemView.findViewById(R.id.titoloFilm);
            film_thumbnail = itemView.findViewById(R.id.imageFilm);
            cardView = itemView.findViewById(R.id.cardViewId);

        }
    }
}
