package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmProvider;
import com.example.projectwork.localDatabase.FilmTableHelper;

public class DettaglioFilm extends AppCompatActivity {

    TextView txtTitolo, txtDecrizione;
    ImageView imageViewDettaglio, imgStella;

    String idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_film);
        getSupportActionBar().setTitle("MOVIE DETAILS");

        txtTitolo = findViewById(R.id.titoloFilmDettaglio);
        txtDecrizione = findViewById(R.id.descrizioneFilmDettaglio);
        imageViewDettaglio = findViewById(R.id.imageViewDettaglio);
        imgStella = findViewById(R.id.stella);

        if (getIntent().getExtras() != null) {
            String titolo = getIntent().getExtras().getString(FilmTableHelper.TITOLO);
            String descrizione = getIntent().getExtras().getString(FilmTableHelper.DESCRIZIONE);
            String img = getIntent().getExtras().getString(FilmTableHelper.IMG_DETTAGLIO);

            idMovie = getIntent().getExtras().getString(FilmTableHelper.ID_MOVIE);

            if (img.equals(null) || img.equals("") || (TextUtils.isEmpty(img)))
                img = getIntent().getExtras().getString(FilmTableHelper.IMG_PRINCIPALE);

            txtTitolo.setText(titolo);
            txtDecrizione.setText(descrizione);

            Glide.with(DettaglioFilm.this)
                    .load("https://image.tmdb.org/t/p/w500/" + img)
                    .into(imageViewDettaglio);

            imgStella.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    Toast.makeText(DettaglioFilm.this, "id :" + idMovie, Toast.LENGTH_SHORT).show();

                    // Defines an object to contain the updated values
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(FilmTableHelper.FLAG_PREFERITO, 1);

                    // Defines selection criteria for the rows you want to update
                    String selectionClause = FilmTableHelper.FLAG_PREFERITO + " LIKE ?";
                    String[] selectionArgs = {idMovie};

                    // Defines a variable to contain the number of updated rows
                    int rowsUpdated = 0;

                    /*
                     * Sets the updated value and updates the selected words.
                     */
                    updateValues.putNull(FilmTableHelper.FLAG_PREFERITO);

                    rowsUpdated = getContentResolver().update(
                            FilmProvider.FILMS_URI,   // the user dictionary content URI
                            updateValues,                      // the columns to update
                            selectionClause,                   // the column to select on
                            selectionArgs                      // the value to compare to
                    );
                }
            });
        }
    }
}
