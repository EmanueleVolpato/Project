package com.example.projectwork;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;


public class ListaFilmAdapter extends CursorAdapter {

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
                Intent i=new Intent(context,ListaFilmPreferiti.class);
                context.startActivity(i);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,ListaFilmPreferiti.class);
                context.startActivity(i);
            }
        });

        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



    }
}
