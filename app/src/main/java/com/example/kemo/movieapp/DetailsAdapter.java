package com.example.kemo.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * Created by kemo on 21/11/2016.
 */
public class DetailsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> items;
    private View movieView;
    private boolean isInFavourites;
    FlowLayout trailersViewer;
    public DetailsAdapter(Context context, ArrayList<Object> items, View movieView)
    {
        this.items = items;
        this.context = context;
        this.movieView = movieView;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Object item = items.get(i);
        if(item instanceof Review) {
            if (view == null || !(view instanceof LinearLayout))
               view = LayoutInflater.from(context).inflate(R.layout.movie_review, viewGroup, false);
            ((TextView)view.findViewById(R.id.review_author_textView)).setText(((Review)item).getAuthor()+":");
            ((TextView)view.findViewById(R.id.review_content_textView)).setText("\""+((Review)item).getContent()+ "\"");
        }
        else if (item instanceof Movie) {
            if (view == null || !(view instanceof RelativeLayout)) {
                view = LayoutInflater.from(context).inflate(R.layout.movie_details, viewGroup, false);
                view = setLayout(view, viewGroup, (Movie) item);
            }
        }

        return view;
    }
    public ArrayList<Object> getItems()
    {
        return items;
    }





    private View setLayout(View view, ViewGroup container, Movie movie)
    {
        //loading Image
        ImageView imageView = (ImageView) view.findViewById(R.id.Detail_poster_imageView);
        Button favouritesButton =(Button) view.findViewById(R.id.favourites_button);
        setfavouriteButton(favouritesButton);
        Picasso.with(context).load(movie.getPosterPath()).fit().into(imageView);
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
                        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context)
                                .inflate(R.layout.movie_trailer, null, false);
                        TextView textView = (TextView) linearLayout.findViewById(R.id.trailer_textView);
                        textView.setText(movieTrailer.getName());
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               context.startActivity
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

        RealmResults<Movie> realmQuery = realm.where(Movie.class).equalTo("movieId", ((Movie)items.get(0)).getMovieId()).findAll();
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
                    Utility.removeFromRealm(((Movie)items.get(0)).getMovieId());
                }
                else Utility.addToRealm(((Movie)items.get(0)));
                isInFavourites = !isInFavourites;
                updateLayout(isInFavourites, favouritesButton);
            }
        });
    }

    private void updateLayout(boolean b, Button favButton)
    {
        if(b)
        {

            favButton.setText(context.getString(R.string.remove_from_favourites_button));
        }
        else
        {
            favButton.setText(context.getString(R.string.add_to_favourites_button));
        }
    }
}
