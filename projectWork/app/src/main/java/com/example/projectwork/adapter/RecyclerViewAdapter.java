package com.example.projectwork.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectwork.R;
import com.example.projectwork.activity.DettaglioFilm;
import com.example.projectwork.activity.Film;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<Film> mData;

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
                intent.putExtra("filmID", Integer.parseInt(String.valueOf(mData.get(position).getId())));

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
        TextView idFilm;

        public MyViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.imageFilm);
            textViewTitolo = itemView.findViewById(R.id.titoloFilm);
            idFilm = itemView.findViewById(R.id.idFilm);

        }
    }
}
