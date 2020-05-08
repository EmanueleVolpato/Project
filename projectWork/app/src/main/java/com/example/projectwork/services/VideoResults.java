package com.example.projectwork.services;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VideoResults {
    /*
    {
  "id": 419704,
  "results": [
    {
      "id": "5cf7baae0e0a2619d3d2e208",
      "iso_639_1": "it",
      "iso_3166_1": "IT",
      "key": "k1dFqDhoS9A",
      "name": "Teaser Trailer",
      "site": "YouTube",
      "size": 1080,
      "type": "Trailer"
    }
  ]
}
     */

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<VideoResults.Data> results = null;

    public int getId() {
        return id;
    }

    public List<Data> getResults() {
        return results;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResults(List<Data> results) {
        this.results = results;
    }

    public static class Data {

        @SerializedName("id")
        private String id;

        @SerializedName("iso_639_1")
        private String iso_639_1;

        @SerializedName("iso_3166_1")
        private String iso_3166_1;

        @SerializedName("key")
        private String key;

        @SerializedName("name")
        private String name;

        @SerializedName("site")
        private String site;

        @SerializedName("size")
        private String size;

        @SerializedName("type")
        private String type;

        public String getId() {
            return id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public String getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
