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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.localDatabase.FilmTableHelper;

import androidx.core.content.ContextCompat;


public class ListaFilmAdapter extends CursorAdapter{

    int stato1 =0;
    int stato2 =0;

    public ListaFilmAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        final View vView = vInflater.inflate(R.layout.cell_lista_film, null);
        RelativeLayout RelativeImage1,RelativeImage2;
        final ImageView image,image2;


        RelativeImage1 =vView.findViewById(R.id.relativeLayoutImage1);
        RelativeImage2 =vView.findViewById(R.id.relativeLayoutImage2);
        image = vView.findViewById(R.id.imageViewStar1);
        image2 = vView.findViewById(R.id.imageViewStar2);





        RelativeImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, DettaglioFilm.class);
                context.startActivity(i);
            }
        });


        RelativeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,DettaglioFilm.class);
                context.startActivity(i);
            }
        });


        RelativeImage1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (stato1 == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("ATTENZIONE");
                    alert.setMessage("Aggiungere il film selezionato ai preferiti?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                            image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_piena));
                            stato1= 1;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM NON AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                            stato1 =0;
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
                            image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star));
                            stato1=0;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM NON TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                            stato1=1;
                        }
                    });
                    AlertDialog alert1 = alert.create();
                    alert1.show();
                }


                return true;
            }
        });



        RelativeImage2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (stato2 == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("ATTENZIONE");
                    alert.setMessage("Aggiungere il film selezionato ai preferiti?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                            image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_piena));
                            stato2 = 1;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM NON AGGIUNTO AI PREFERITI!", Toast.LENGTH_LONG).show();
                            stato2 = 0;
                        }
                    });
                    AlertDialog alert1 = alert.create();
                    alert1.show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("FILM GIA' AGGIUNTO AI PREFERITI!");
                    alert.setMessage("Togliere il film selezionato dai preferiti?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                            image2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star));
                            stato2 = 0;
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "FILM NON TOLTO DAI PREFERITI!", Toast.LENGTH_LONG).show();
                            stato2 = 1;
                        }
                    });
                    AlertDialog alert1 = alert.create();
                    alert1.show();
                }


                return true;
            }
        });


        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



    }
}
