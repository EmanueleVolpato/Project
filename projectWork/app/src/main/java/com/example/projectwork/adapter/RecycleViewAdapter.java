package com.example.projectwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.localDatabase.FilmPreferitiProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.MovieResults;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<MovieResults.ResultsBean> mData;
    private List<MovieResults.ResultsBean> mDataSearch;

    public RecycleViewAdapter(Context context,  List<MovieResults.ResultsBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setMovies(List<MovieResults.ResultsBean> mData){
        this.mData = mData;
        mDataSearch = new ArrayList<>(mData);
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
