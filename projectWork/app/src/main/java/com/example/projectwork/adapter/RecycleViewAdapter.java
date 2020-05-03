package com.example.projectwork.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.activity.MainActivity;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.FilmResults;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<FilmResults.Data> mData;
    private List<FilmResults.Data> mDataSearch;


    Dialog myDialogLike;

    public RecycleViewAdapter(Context context, List<FilmResults.Data> mData) {
        this.context = context;
        this.mData = mData;
        mDataSearch = new ArrayList<>(mData);
    }

    public void setFilms(List<FilmResults.Data> mData) {
        this.mData = mData;
    }

    public void resetFilms() {
        this.mData.clear();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_item_film, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final ImageView img = holder.cellView.findViewById(R.id.imageFilm);
        final CardView card = holder.cellView.findViewById(R.id.cardViewId);

        myDialogLike = new Dialog(context);


        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/" + mData.get(position).getPosterPath())
                .into(img);


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DettaglioFilm.class);
                Bundle bundle = new Bundle();
                int id = mData.get(position).getId();
                bundle.putString(FilmTableHelper.ID_MOVIE, Integer.toString(id));
                bundle.putString(FilmTableHelper.TITOLO, mData.get(position).getTitle());
                bundle.putString(FilmTableHelper.DATA, mData.get(position).getReleaseDate());
                bundle.putString(FilmTableHelper.DESCRIZIONE, mData.get(position).getOverview());
                bundle.putString(FilmTableHelper.IMG_PRINCIPALE, mData.get(position).getPosterPath());
                bundle.putString(FilmTableHelper.IMG_DETTAGLIO, mData.get(position).getBackdropPath());
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FilmResults.Data> filtroList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filtroList.addAll(mDataSearch);
            } else {
                String filtroPattern = constraint.toString().toLowerCase().trim();

                for (FilmResults.Data item : mDataSearch) {
                    if (item.getTitle().toLowerCase().contains(filtroPattern)) {
                        filtroList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtroList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData.clear();
            mData.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View cellView;

        MyViewHolder(@NonNull View cellView) {
            super(cellView);
            this.cellView = cellView;
        }
    }


}
