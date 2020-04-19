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

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<Film> mData;
    int stato =0;
    int stato2 =0;

    public RecyclerViewAdapter(Context context, List<Film> mData) {
        this.context = context;
        this.mData = mData;
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
        holder.textViewTitolo.setText(mData.get(position).getTitolo());
        holder.imageView.setImageResource(mData.get(position).getThumbnail());



        holder.imageView.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView;
        TextView textViewTitolo;

        public MyViewHolder(View itemView) {
            super(itemView);

          /*  titoloFilm = itemView.findViewById(R.id.titoloFilm);
            titoloFilm2 = itemView.findViewById(R.id.titoloFilm2);
            film_thumbnail = itemView.findViewById(R.id.imageFilm);
            film_thumbnail2 = itemView.findViewById(R.id.imageFilm2);
            stella = itemView.findViewById(R.id.stella);
            stella2 = itemView.findViewById(R.id.stella2);
*/

            imageView = itemView.findViewById(R.id.imageFilm);
            textViewTitolo = itemView.findViewById(R.id.titoloFilm);

        }
    }
}
