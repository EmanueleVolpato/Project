package com.example.projectwork.adapter;

import com.example.projectwork.services.FilmResults;

import java.util.List;

public class SalvataggioRecycleViewAdapter {

    private RecycleViewAdapter mAdapter;
    private static SalvataggioRecycleViewAdapter adapterSave;
    private List<FilmResults.Data> listaFilms;

    private SalvataggioRecycleViewAdapter() {

    }

    public static SalvataggioRecycleViewAdapter getInstance() {
        if (adapterSave == null)
            adapterSave = new SalvataggioRecycleViewAdapter();
        return adapterSave;
    }

    public RecycleViewAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(RecycleViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setListFilms(List<FilmResults.Data> listaFilms) {
        this.listaFilms = listaFilms;
    }

    public List<FilmResults.Data> getListFilms() {
        return listaFilms;
    }
}
