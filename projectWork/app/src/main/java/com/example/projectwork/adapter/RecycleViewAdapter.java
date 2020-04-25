package com.example.projectwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.FilmResults;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private Context context;
    private List<FilmResults.Data> mData;

    public RecycleViewAdapter(Context context,  List<FilmResults.Data> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setFilms(List<FilmResults.Data> mData){
        this.mData = mData;
    }

    public void resetFilms(){
        this.mData.clear();
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

        final ImageView img = holder.cellView.findViewById(R.id.imageFilm);
        final TextView txt = holder.cellView.findViewById(R.id.titoloFilm);
        final CardView card = holder.cellView.findViewById(R.id.cardViewId);

        txt.setText(mData.get(position).getTitle());

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/"+ mData.get(position).getPosterPath())
                .into(img);


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DettaglioFilm.class);
                Bundle bundle = new Bundle();
                int id = mData.get(position).getId();
                bundle.putString(FilmTableHelper.ID_MOVIE, Integer.toString(id));
                bundle.putString(FilmTableHelper.TITOLO, mData.get(position).getTitle());
                bundle.putString(FilmTableHelper.DESCRIZIONE, mData.get(position).getOverview());
                bundle.putString(FilmTableHelper.IMG_PRINCIPALE, mData.get(position).getPosterPath());
                bundle.putString(FilmTableHelper.IMG_DETTAGLIO, mData.get(position).getBackdropPath());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View cellView;
        MyViewHolder(@NonNull View cellView) {
            super(cellView);
            this.cellView = cellView;
        }
    }
}
