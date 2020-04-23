package com.example.projectwork.services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResults {

/*
{
  "page": 1,
  "total_results": 10000,
  "total_pages": 500,
  "results": [
    {
      "popularity": 492.069,
      "vote_count": 3023,
      "video": false,
      "poster_path": "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
      "id": 419704,
      "adult": false,
      "backdrop_path": "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
      "original_language": "en",
      "original_title": "Ad Astra",
      "genre_ids": [
        18,
        878
      ],
      "title": "Ad Astra",
      "vote_average": 6,
      "overview": "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
      "release_date": "2019-09-17"
    }
  ]
}
 */

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<ResultsBean> results = null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }



    public static class ResultsBean {

        private String filmPreferiti;


        @SerializedName("popularity")
        private double popularity;

        @SerializedName("vote_count")
        private int voteCount;

        @SerializedName("video")
        private boolean video;

        @SerializedName("poster_path")
        private String posterPath;

        @SerializedName("id")
        private int id;

        @SerializedName("adult")
        private boolean adult;

        @SerializedName("backdrop_path")
        private String backdropPath;

        @SerializedName("original_language")
        private String originalLanguage;

        @SerializedName("original_title")
        private String originalTitle;

        @SerializedName("genre_ids")
        private List<Integer> genreIds = null;

        @SerializedName("title")
        private String title;

        @SerializedName("vote_average")
        private double voteAverage;

        @SerializedName("overview")
        private String overview;

        @SerializedName("release_date")
        private String releaseDate;

        public double getPopularity() {
            return popularity;
        }

        public String getFilmPreferiti(){return this.filmPreferiti;}
        public void setFilmPreferiti(String filmPreferiti){
            this.filmPreferiti = filmPreferiti;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public boolean getVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean getAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }
    }
}
