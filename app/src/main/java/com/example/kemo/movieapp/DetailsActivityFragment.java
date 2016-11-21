package com.example.kemo.movieapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    private static Movie movie;
    public static void setMovie(Movie movie) {
        DetailsActivityFragment.movie = movie;
    }
    DetailsAdapter detailsAdapter;
    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        //get the movie View
        //initialize arrayList
        ArrayList<Object> objects = new ArrayList<>();
        //add movie to this arrayList
        objects.add(movie);
        //pass this array list to the adapter
        detailsAdapter = new DetailsAdapter(getActivity(),objects);
        ListView listView = (ListView) view.findViewById(R.id.details_listView);
        //bind the list to the adapter
        listView.setAdapter(detailsAdapter);
        fetchReviews(movie.getMovieId());
        Log.e("ID","" + movie.getMovieId());
        return view;
    }

    private void fetchReviews(int id)
    {
        URLConnector urlConnector = new URLConnector() {
            @Override
            protected ArrayList<Object> getObjFromjson(String Jsonstr) {
                final String MDB_RESULTS = "results";
                final String AUTHOR = "author";
                final String CONTENT = "content";
                ArrayList<Object> reviews = new ArrayList<>();
                try {
                    JSONObject movieReviewsResults = new JSONObject(Jsonstr);
                    JSONArray Jsonreviews = movieReviewsResults.getJSONArray(MDB_RESULTS);
                    for (int i = 0; i < Jsonreviews.length(); i++)
                    {
                        JSONObject jsonObject = Jsonreviews.getJSONObject(i);
                        Review review = new Review();
                        review.setAuthor(jsonObject.getString(AUTHOR));
                        review.setContent(jsonObject.getString(CONTENT));
                        reviews.add(review);
                    }
                    return reviews;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Object> objects) {
                super.onPostExecute(objects);
                if (objects == null) return;
                detailsAdapter.getItems().addAll(objects);
                detailsAdapter.notifyDataSetChanged();

            }

            @Override
            protected URL fetchURL(String s) {
                final String BASE_URL =  "https://api.themoviedb.org/3/movie/" + s + "/reviews?";
                final String KEY = "api_key";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon().
                        appendQueryParameter(KEY,BuildConfig.THE_MOVIE_DATABASE_KEY).build();
                try {
                    return new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    Log.e("FetchTrailers", e.getMessage());
                }
                return null;
            }
        };
        urlConnector.execute("" + id);
    }
}
