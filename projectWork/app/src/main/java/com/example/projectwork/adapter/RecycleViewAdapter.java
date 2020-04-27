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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.FilmResults;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    private Context context;
    private List<FilmResults.Data> mData;

    Dialog myDialogLike;

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
        final CardView card = holder.cellView.findViewById(R.id.cardViewId);
        myDialogLike = new Dialog(context);


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
                bundle.putString(FilmTableHelper.DATA, mData.get(position).getReleaseDate());
                bundle.putString(FilmTableHelper.DESCRIZIONE, mData.get(position).getOverview());
                bundle.putString(FilmTableHelper.IMG_PRINCIPALE, mData.get(position).getPosterPath());
                bundle.putString(FilmTableHelper.IMG_DETTAGLIO, mData.get(position).getBackdropPath());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });



        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myDialogLike.setContentView(R.layout.like_dialog);
                ImageView imageViewCopertina;
                imageViewCopertina = myDialogLike.findViewById(R.id.imageViewfilmLike);
                String immagineDettaglio = mData.get(position).getBackdropPath();
                RatingBar ratingBar;
                ratingBar = myDialogLike.findViewById(R.id.ratingBar);
                TextView titolo;
                //titolo = myDialogLike.findViewById(R.id.textViewtitoloLike);
                Button esc,vota;
                esc = myDialogLike.findViewById(R.id.buttoncancelLike);
                vota = myDialogLike.findViewById(R.id.buttonVota);
                final TextView votoPersonale;
                votoPersonale = myDialogLike.findViewById(R.id.textViewVotoPersonale);

                int [] color={ Color.rgb(3,107,218), Color.rgb(10,10,10)};


                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        votoPersonale.setText("IL TUO VOTO: "+ratingBar.getRating()+" /10");
                    }
                });


                esc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogLike.dismiss();
                    }
                });


                vota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Toast.makeText(context,"votato",Toast.LENGTH_SHORT).show();
                    }
                });


                Double valutazione =mData.get(position).getVoteAverage();
                Double conteggio = 10-valutazione;

                Float number[] = {Float.valueOf(String.valueOf(valutazione)), Float.valueOf(String.valueOf(conteggio))};

                List<PieEntry> pieEntries = new ArrayList<>();
                for(int i =0;i<number.length;i++)
                {
                    pieEntries.add(new PieEntry(number[i]));
                }

                PieDataSet dataSet = new PieDataSet(pieEntries,"");
                PieData data = new PieData(dataSet);
                dataSet.setColors(color);

                PieChart pieChart = myDialogLike.findViewById(R.id.pieChart);
                pieChart.setData(data);
                pieChart.invalidate();

                String titoloLike = mData.get(position).getTitle();
                //titolo.setText(titoloLike);


                Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w500/" + immagineDettaglio)
                        .into(imageViewCopertina);

                myDialogLike.show();


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

    private void  pieChart() {

    }
}
