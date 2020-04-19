package com.example.projectwork.activity;

public class Film {


    private String Titolo;
    private int ID;
    private String Categoria;
    private String Descrizione;
    private int Thumbnail;


    public Film(){

    }

    public Film(String titolo,String categoria,String descrizione,int thumbnail,int id){

        Titolo = titolo;
        Categoria = categoria;
        Descrizione = descrizione;
        ID = id;
        Thumbnail = thumbnail;
    }


    public String getTitolo() {
        return Titolo;
    }

    public int getId() {
        return ID;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public void setID(int id) {
        ID = id;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}
