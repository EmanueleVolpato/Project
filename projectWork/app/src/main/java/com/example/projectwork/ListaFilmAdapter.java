package com.example.projectwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;


public class ListaFilmAdapter extends CursorAdapter{

    public ListaFilmAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        final View vView = vInflater.inflate(R.layout.cell_lista_film, null);
        RelativeLayout image1,image2,button1,button2;
        image1 =vView.findViewById(R.id.relativeLayoutImage1);
        image2 =vView.findViewById(R.id.relativeLayoutImage2);
        button1 =vView.findViewById(R.id.btn1);
        button2 =vView.findViewById(R.id.btn2);



        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,DettaglioFilm.class);
                context.startActivity(i);
            }
        });


        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,DettaglioFilm.class);
                context.startActivity(i);
            }
        });



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("ATTENZIONE");
                alert.setMessage("Aggiungere il film selezionato ai preferiti?");
                alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "FILM AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "FILM NON AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("ATTENZIONE");
                alert.setMessage("Aggiungere il film selezionato ai preferiti?");
                alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "FILM AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "FILM NON AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });

        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



    }
}
