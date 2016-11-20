package com.example.kemo.movieapp;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kemo on 12/11/2016.
 */
public class MovieFetcher extends URLConnector {
    public MovieAppActivity movieAppActivity;
    ImageListAdapter imageListAdapter;
    @Override
    protected void onPostExecute(ArrayList<Object> movies) {
        super.onPostExecute(movies);
        if(movies == null) return;
        updateMovies(movies);
    }
    public void updateMovies(ArrayList<Object> movies)
    {
        ArrayList<Movie> uriPaths = imageListAdapter.getUriList();
        uriPaths.clear();
        if (((MainActivity) movieAppActivity).isDual && movies.size() > 0)
            movieAppActivity.navigate((Movie) movies.get(0));
        for (Object movie: movies
                ) {
            uriPaths.add((Movie) movie);
        }
        imageListAdapter.notifyDataSetChanged();
    }
    @Override
    protected   ArrayList<Object> getObjFromjson(String jsonStr){
        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_TITLE = "title";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_POPULARITY = "popularity";
        final String MDB_ORIGINAL_TITLE = "original_title";
        ArrayList<Object> movies = new ArrayList<>();
        try {
            JSONObject movieDetails = new JSONObject(jsonStr);
            JSONArray movieData = movieDetails.getJSONArray(MDB_RESULTS);
            for (int i = 0; i < movieData.length(); i++) {
                Movie movie = new Movie();
                JSONObject jsonObject = movieData.getJSONObject(i);
                movie.setMovieId(jsonObject.getInt(MDB_ID));
                movie.setPosterPath(jsonObject.getString(MDB_POSTER_PATH));
                movie.setOverView(jsonObject.getString(MDB_OVERVIEW));
                movie.setTitle(jsonObject.getString(MDB_TITLE));
                movie.setReleaseData(jsonObject.getString(MDB_RELEASE_DATE));
                movie.setVoteAverage(jsonObject.getDouble(MDB_VOTE_AVERAGE));
                movie.setPopularity(jsonObject.getDouble(MDB_POPULARITY));
                movie.setOriginalTitle(jsonObject.getString(MDB_ORIGINAL_TITLE));
                movies.add(movie);
            }
            return movies;
        }catch (Exception e)
        {
            Log.e("getMovieFromJson", e.getMessage());
        }
        return null;
    }
    protected URL fetchURL(String sortMode) {
        final String MOVIES_BASE_URL =
                "https://api.themoviedb.org/3/discover/movie?";

        final String SORTBY = "sort_by";
        final String key = "api_key";

        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendQueryParameter(SORTBY, sortMode)
                .appendQueryParameter(key, BuildConfig.THE_MOVIE_DATABASE_KEY)
                .build();

        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
