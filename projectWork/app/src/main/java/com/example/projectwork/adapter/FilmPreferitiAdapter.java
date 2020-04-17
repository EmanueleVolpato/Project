package com.example.projectwork.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;

public class FilmPreferitiAdapter extends CursorAdapter {


    public FilmPreferitiAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        final View vView = vInflater.inflate(R.layout.cell_lista_film_preferiti, null);
        final ImageView imageFilmPreferiti;

        imageFilmPreferiti =vView.findViewById(R.id.imageViewFilmPreferiti);


        imageFilmPreferiti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, DettaglioFilm.class);
                context.startActivity(i);
            }
        });

        imageFilmPreferiti.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("ATTENZIONE!");
                alert.setMessage("Togliere il film selezionato dai preferiti?");
                alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "FILM TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "FILM NON TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alert1 = alert.create();
                alert1.show();
                return true;
            }

        });

        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }
}
