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
import android.widget.Toast;

public class DettaglioFilmAdapter extends CursorAdapter {

    public DettaglioFilmAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        final View vView = vInflater.inflate(R.layout.cell_lista_dettaglio_film, null);


        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



    }
}

