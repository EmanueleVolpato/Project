package com.example.projectwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.projectwork.R;
import com.example.projectwork.localDatabase.FilmTableHelper;
import com.example.projectwork.services.IWebServiceVideoFilm;
import com.example.projectwork.services.VideoResults;
import com.example.projectwork.services.WebService;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class VideoActivity extends YouTubeBaseActivity {
    String keyVideo = null;
    private WebService webService;
    private String API_KEY = "e6de0d8da508a9809d74351ed62affef";
    String video;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    private YouTubePlayer youTubePlayer;
    private int seekTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        youTubePlayerView = findViewById(R.id.youtubePlayrerview);

        if (getIntent().getExtras() != null) {
             video = getIntent().getExtras().getString("video");
        }
        if (controlloConnessione()) {
            webService = WebService.getInstance();
            getVideo(video, "it");
        }



        onInitializedListener= new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
               // youTubePlayer.loadVideo( "Zs-AVod-tsM");
                if(keyVideo != null){
                youTubePlayer.loadVideo( keyVideo);
            }else
                {
                    Toast.makeText(VideoActivity.this, "video non disponibile", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
    }

    private boolean controlloConnessione() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void getVideo(String idFilm, String language) {
        webService.getVideoFilm(idFilm, API_KEY, language, new IWebServiceVideoFilm() {
            @Override
            public void onVideoFetched(boolean success, List<VideoResults.Data> videos, int errorCode, String errorMessage) {
                if (success) {
                    try {
                        if (videos != null)
                            keyVideo = videos.get(0).getKey();
                    } catch (Exception ex) {
                        Toast.makeText(VideoActivity.this, "errore link video youtube", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VideoActivity.this, "errore link video youtube", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
