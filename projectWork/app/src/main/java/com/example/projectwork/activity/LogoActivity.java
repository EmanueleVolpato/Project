package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.projectwork.R;
import com.example.projectwork.SharedPref;
import com.example.projectwork.localDatabase.FilmPreferredProvider;
import com.example.projectwork.localDatabase.FilmPreferredTableHelper;
import com.example.projectwork.services.GuestSessionResults;
import com.example.projectwork.services.IWebServiceGuestSession;
import com.example.projectwork.services.WebService;

public class LogoActivity extends AppCompatActivity {

    SharedPref sharedPref;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    private WebService webService;
    String idSessionGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState() == true)
            setTheme(R.style.DarkLogoTheme);
        else setTheme(R.style.LogoTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        final ImageView imageView = (ImageView) findViewById(R.id.imageLogo);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        imageView.startAnimation(animation_2);
        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animation_2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animation_3);
                finish();
                webService = WebService.getInstance();
                setIdKeySession();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setIdKeySession() {
        webService.getGuestIdSession(API_KEY, new IWebServiceGuestSession() {
            @Override
            public void onGuestFetched(boolean success, GuestSessionResults guest, int errorCode, String errorMessage) {
                if (success) {
                    idSessionGuest = guest.getGuest_session_id();

                    LogoActivity.this.getContentResolver().delete(FilmPreferredProvider.FILMS_URI,
                            FilmPreferredTableHelper.ID_MOVIE + " = ?", new String[]{("key_session")});

                    ContentValues cv = new ContentValues();
                    cv.put(FilmPreferredTableHelper.ID_MOVIE, "key_session");
                    cv.put(FilmPreferredTableHelper.KEY_GUEST_VOTO, idSessionGuest);
                    LogoActivity.this.getContentResolver().insert(FilmPreferredProvider.FILMS_URI, cv);

                } else {
                    Toast.makeText(LogoActivity.this, "CONNESSIONE INTERNET ASSENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
