package com.example.projectwork.adapter;

import com.example.projectwork.services.FilmResults;

import java.util.List;

public class SalvataggioRecycleViewFilmSimili {

    private FilmSimiliAdapter mAdapter;
    private static SalvataggioRecycleViewFilmSimili adapterSave;
    private List<FilmResults.Data> listaFilms;

    private SalvataggioRecycleViewFilmSimili() {

    }

    public static SalvataggioRecycleViewFilmSimili getInstance() {
        if (adapterSave == null)
            adapterSave = new SalvataggioRecycleViewFilmSimili();
        return adapterSave;
    }

    public FilmSimiliAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(FilmSimiliAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setListFilms(List<FilmResults.Data> listaFilms) {
        this.listaFilms = listaFilms;
    }

    public List<FilmResults.Data> getListFilms() {
        return listaFilms;
    }
}
