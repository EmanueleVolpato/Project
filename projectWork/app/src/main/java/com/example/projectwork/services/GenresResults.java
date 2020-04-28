package com.example.projectwork.services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresResults {

    /*
    {
  "genres": [
    {
      "id": 28,
      "name": "Azione"
    },
    {
      "id": 12,
      "name": "Avventura"
    }
  ]
}
     */

    @SerializedName("genres")
    private List<GenresResults.Data> genres = null;

    public List<GenresResults.Data> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresResults.Data> genres) {
        this.genres = genres;
    }

    public static class Data {

        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
