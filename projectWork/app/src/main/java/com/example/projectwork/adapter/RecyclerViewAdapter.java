package com.example.projectwork.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.activity.Film;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<Film> mData;
    int stato =0;

    public RecyclerViewAdapter(Context context, List<Film> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.prova,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.titoloFilm.setText(mData.get(position).getTitolo());
        holder.film_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.titoloFilm2.setText(mData.get(position).getTitolo());
        holder.film_thumbnail2.setImageResource(mData.get(position).getThumbnail());


        holder.film_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DettaglioFilm.class);
                intent.putExtra("titoloFilm",mData.get(position).getTitolo());
                intent.putExtra("descrizione",mData.get(position).getDescrizione());
                intent.putExtra("categoria",mData.get(position).getCategoria());
                intent.putExtra("thumbnail",mData.get(position).getThumbnail());
                context.startActivity(intent);
            }
        });

        holder.film_thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (stato == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("ATTENZIONE");
                    alert.setMessage("Aggiungere il film selezionato ai preferiti?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                            //image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_piena));
                            stato= 1;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM NON AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                            stato =0;
                        }
                    });
                    AlertDialog alert1 = alert.create();
                    alert1.show();
                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("FILM GIA' AGGIUNTO AI PREFERITI!");
                    alert.setMessage("Togliere il film selezionato dai preferiti?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                            //image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star));
                            stato=0;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM NON TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                            stato=1;
                        }
                    });
                    AlertDialog alert1 = alert.create();
                    alert1.show();
                }


                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titoloFilm,titoloFilm2;
        ImageView film_thumbnail,film_thumbnail2;
        ImageView stella,stella2;

        public MyViewHolder(View itemView) {
            super(itemView);

            titoloFilm = itemView.findViewById(R.id.titoloFilm);
            titoloFilm2 = itemView.findViewById(R.id.titoloFilm2);
            film_thumbnail = itemView.findViewById(R.id.imageFilm);
            film_thumbnail2 = itemView.findViewById(R.id.imageFilm2);
            stella = itemView.findViewById(R.id.stella);
            stella2 = itemView.findViewById(R.id.stella2);

        }
    }
}
