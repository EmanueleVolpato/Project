package com.example.projectwork.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.activity.DettaglioFilmPreferiti;
import com.example.projectwork.localDatabase.FilmPreferitiProvider;
import com.example.projectwork.localDatabase.FilmPreferitiTableHelper;
import com.example.projectwork.services.MovieResults;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterFilmPreferiti extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<MovieResults.ResultsBean> mData;
    private List<MovieResults.ResultsBean> mDataSearch;
    
    public RecyclerViewAdapterFilmPreferiti(Context context, List<MovieResults.ResultsBean> mData) {
        this.context = context;
        this.mData = mData;
        mDataSearch = new ArrayList<>(mData);
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
                final int idMovie = mData.get(position).getId();
                new AlertDialog.Builder(context)
                        .setTitle("ATTENZIONE!!")
                        .setMessage("Togliere il film dai preferiti?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                context.getContentResolver().delete(Uri.parse(String.valueOf(FilmPreferitiProvider.FILMS_URI)), FilmPreferitiTableHelper.ID_MOVIE + "=" + idMovie, null);
                                mData.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

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
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MovieResults.ResultsBean> filtroList = new ArrayList<>();
            if(constraint == null || constraint.length()==0)
            {
                filtroList.addAll(mDataSearch);
            }else
            {
                String filtroPattern = constraint.toString().toLowerCase().trim();

                for(MovieResults.ResultsBean item :mDataSearch)
                {
                    if(item.getTitle().toLowerCase().contains(filtroPattern)) {
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
            mData.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
