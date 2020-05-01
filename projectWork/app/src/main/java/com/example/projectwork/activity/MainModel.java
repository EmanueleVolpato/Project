package com.example.projectwork.activity;

class MainModel {

    Integer logo;
    String nome;

    public  MainModel(Integer logo,String nome){
        this.logo=logo;
        this.nome=nome;
    }

    public Integer getLogo()
    {
        return  logo;
    }

    public String getNome()
    {
        return  nome;
    }
}
