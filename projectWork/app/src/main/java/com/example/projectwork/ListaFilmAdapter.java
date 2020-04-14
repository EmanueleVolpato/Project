package com.example.projectwork;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectwork.R;

public class ListaFilmAdapter extends CursorAdapter {

    public ListaFilmAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        View vView = vInflater.inflate(R.layout.cell_lista_film, null);
        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titoloAttivita = view.findViewById(R.id.textView5),
                nomeUtente = view.findViewById(R.id.textView6);
        titoloAttivita.setText("ciao");
        nomeUtente.setText("ciao");

    }
}
