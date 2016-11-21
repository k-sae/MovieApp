package com.example.kemo.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import io.realm.Realm;
import io.realm.RealmResults;
import org.apmem.tools.layouts.FlowLayout;
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
    private boolean isInFavourites;
    public DetailsActivityFragment() {
    }
    FlowLayout trailersViewer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        //get the movie View
        View movieDetailsView = inflater.inflate(R.layout.movie_details, container, false);
        movieDetailsView = setLayout(movieDetailsView,container);
        //initialize arrayList
        ArrayList<Object> objects = new ArrayList<>();
        //add movie to this arrayList
        objects.add(movie);
        //pass this array list to the adapter
        detailsAdapter = new DetailsAdapter(getActivity(),objects,movieDetailsView);
        ListView listView = (ListView) view.findViewById(R.id.details_listView);
        //bind the list to the adapter
        listView.setAdapter(detailsAdapter);
        fetchReviews(movie.getMovieId());
        Log.e("ID","" + movie.getMovieId());
        return view;
    }
    private View setLayout(View view, ViewGroup container)
    {
        //loading Image
        ImageView imageView = (ImageView) view.findViewById(R.id.Detail_poster_imageView);
        Button favouritesButton =(Button) view.findViewById(R.id.favourites_button);
        setfavouriteButton(favouritesButton);
         Picasso.with(getActivity()).load(movie.getPosterPath()).fit().into(imageView);
        //title
        TextView textView = (TextView) view.findViewById(R.id.movieTitle_textView);
        textView.setText(textView.getText() +" " + movie.getTitle());
        //date
        textView = (TextView) view.findViewById(R.id.movieDate_textView);
        AppendText(textView, movie.getReleaseData());
        //Description
        textView = (TextView) view.findViewById(R.id.movieDescription_textView);
        AppendText(textView, "\n" +movie.getOverView());
        //rating
        textView = (TextView) view.findViewById(R.id.movieVoteAverage_textView);
        AppendText(textView,Double.toString(movie.getVoteAverage()));
        //popularity
        textView = (TextView) view.findViewById(R.id.moviePopularity);
        AppendText(textView,Double.toString(movie.getPopularity()));
        trailersViewer = (FlowLayout) view.findViewById(R.id.trailers_container);
        fetchTrailers(movie.getMovieId());
        return view;
    }
    private void AppendText(TextView textView, String text)
    {
        textView.setText(textView.getText() +" " + text);
    }
    private void fetchTrailers(int id)
    {
        URLConnector urlConnector = new URLConnector() {
            @Override
            protected ArrayList<Object> getObjFromjson(String jsonStr) {
                ArrayList<Object> movieTrailers = new ArrayList<>();
                final String NAME = "name";
                final String KEY = "key";
                final String MDB_RESULTS = "results";
                try
                {
                    JSONObject movieTrailersDetails = new JSONObject(jsonStr);

                    JSONArray movieTrailerData = movieTrailersDetails.getJSONArray(MDB_RESULTS);
                    for (int i = 0; i< movieTrailerData.length(); i++)
                    {
                        JSONObject jsonObject = movieTrailerData.getJSONObject(i);
                        MovieTrailer movieTrailer = new MovieTrailer();
                        movieTrailer.setKey(jsonObject.getString(KEY));
                        movieTrailer.setName(jsonObject.getString(NAME));
                        movieTrailers.add(movieTrailer);
                    }
                    return movieTrailers;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected URL fetchURL(String s) {
                final String BASE_URL =  "https://api.themoviedb.org/3/movie/" + s + "/videos?";
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

            @Override
            protected void onPostExecute(ArrayList<Object> objects) {
                super.onPostExecute(objects);
                if (objects == null) return;
                trailersViewer.findViewById(R.id.movie_trailers_loading).setVisibility(View.GONE);
                for (Object o :
                        objects) {
                    try {
                        final MovieTrailer movieTrailer = (MovieTrailer) o;
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity())
                                .inflate(R.layout.movie_trailer, null, false);
                        TextView textView = (TextView) linearLayout.findViewById(R.id.trailer_textView);
                        textView.setText(movieTrailer.getName());
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity
                                        (new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://www.youtube.com/watch?v="
                                                        + movieTrailer.getKey())));
                            }
                        });
                        trailersViewer.addView(linearLayout);
                    }catch (Exception e)
                    {
                        try {
                            Log.e("DetailFragment", e.getMessage());
                        }catch (Exception e1)
                        {
                            //ignore
                        }
                    }
                }
            }
        };

        urlConnector.execute(Integer.toString(id));
    }
   private boolean isInFavourites()
   {
       //SetRealm
       Realm realm = Realm.getDefaultInstance();

        RealmResults<Movie> realmQuery = realm.where(Movie.class).equalTo("movieId", movie.getMovieId()).findAll();
       return (realmQuery.size() != 0);

   }

   private void setfavouriteButton(final Button favouritesButton)
   {
         isInFavourites = isInFavourites();
       updateLayout(isInFavourites, favouritesButton);
       favouritesButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                if (isInFavourites)
                {
                    Utility.removeFromRealm(movie.getMovieId());
                }
                else Utility.addToRealm(movie);
               isInFavourites = !isInFavourites;
               updateLayout(isInFavourites, favouritesButton);
           }
       });
   }

   private void updateLayout(boolean b, Button favButton)
   {
       if(b)
       {

           favButton.setText(getString(R.string.remove_from_favourites_button));
       }
       else
       {
           favButton.setText(getString(R.string.add_to_favourites_button));
       }
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
