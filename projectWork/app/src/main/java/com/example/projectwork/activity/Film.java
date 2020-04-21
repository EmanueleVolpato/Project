package com.example.projectwork.activity;

public class Film {

    private int ID;
    private String Titolo;
    private String Descrizione;
    private String Thumbnail;

    public Film() {
    }

    public String getTitolo() {
        return Titolo;
    }

    public int getId() {
        return ID;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public void setID(int id) {
        ID = id;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void setImg(String thumbnail) {
        Thumbnail = thumbnail;
    }
}
