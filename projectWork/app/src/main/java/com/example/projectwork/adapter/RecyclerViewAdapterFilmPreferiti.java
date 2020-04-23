package com.example.projectwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferitiProvider;
import com.example.projectwork.localDatabase.FilmPreferitiTableHelper;
import com.example.projectwork.services.MovieResults;

import java.util.List;

public class RecyclerViewAdapterFilmPreferiti extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>{

    private Context context;
    private List<MovieResults.ResultsBean> mData;

    public RecyclerViewAdapterFilmPreferiti(Context context, List<MovieResults.ResultsBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_item_film,parent,false);

        return new RecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewAdapter.MyViewHolder holder, final int position) {
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
                Intent intent = new Intent(context, DettaglioFilmPreferiti.class);
                Bundle bundle = new Bundle();
                int id = mData.get(position).getId();
                bundle.putString(FilmPreferitiTableHelper.ID_MOVIE, Integer.toString(id));
                Toast.makeText(context, id +"", Toast.LENGTH_SHORT).show();
                bundle.putString(FilmPreferitiTableHelper.TITOLO, mData.get(position).getTitle());
                bundle.putString(FilmPreferitiTableHelper.DESCRIZIONE, mData.get(position).getOverview());
                bundle.putString(FilmPreferitiTableHelper.IMG_PRINCIPALE, mData.get(position).getPosterPath());
                bundle.putString(FilmPreferitiTableHelper.IMG_DETTAGLIO, mData.get(position).getBackdropPath());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });




        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int id2 = mData.get(position).getId();
                Toast.makeText(context, id2 +"", Toast.LENGTH_SHORT).show();

                //context.getContentResolver().delete(Uri.parse(String.valueOf(FilmPreferitiProvider.FILMS_URI)), FilmPreferitiTableHelper.ID_MOVIE + "=" + id, null);

                return false;
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
