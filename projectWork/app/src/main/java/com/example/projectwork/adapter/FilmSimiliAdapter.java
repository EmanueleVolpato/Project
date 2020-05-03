package com.example.projectwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.activity.DettaglioFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.FilmResults;

import java.util.ArrayList;
import java.util.List;

public class FilmSimiliAdapter extends RecyclerView.Adapter<FilmSimiliAdapter.ViewHolder> {

    private List<FilmResults.Data> mData;
    Context context;

    public FilmSimiliAdapter(Context context, List<FilmResults.Data> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setFilms(List<FilmResults.Data> mData) {
        this.mData = mData;
    }

    public void resetFilms() {
        this.mData.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_film_simili, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        CardView cardView = holder.cellView.findViewById(R.id.cardViewIdFilmSimili);
        ImageView imageView = holder.cellView.findViewById(R.id.imageFilmSimile);

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/" + mData.get(position).getPosterPath())
                .into(imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
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
                bundle.putString(FilmTableHelper.DATA, mData.get(position).getReleaseDate());
                bundle.putString(FilmTableHelper.VOTO, String.valueOf(mData.get(position).getVoteAverage()));

                List<Integer> genresFilmInput = mData.get(position).getGenreIds();
                if (genresFilmInput != null) {
                    int[] genresFilmOutput = new int[genresFilmInput.size()];
                    for (int i = 0; i < genresFilmInput.size(); i++) {
                        genresFilmOutput[i] = genresFilmInput.get(i);
                    }
                    bundle.putIntArray("generiID", genresFilmOutput);
                }

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View cellView;

        ViewHolder(@NonNull View cellView) {
            super(cellView);
            this.cellView = cellView;
        }
    }

}
