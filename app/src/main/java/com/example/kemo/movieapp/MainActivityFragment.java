package com.example.kemo.movieapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
//TODO
    //a class contains data & may Inherit it from image view
public class MainActivityFragment extends Fragment {


    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovies();
    }

    public ImageListAdapter imageListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
       GridView gridView =(GridView) view.findViewById(R.id.gridView_movies);
         imageListAdapter = new ImageListAdapter(getActivity(), new String[0]);
        gridView.setAdapter(imageListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String movie =(String) imageListAdapter.getItem(i);
                Toast.makeText(getActivity(), movie, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void fetchMovies()
    {
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute("popularity.desc");
    }
    class FetchMovies extends AsyncTask<String, Void,ArrayList<Movie>>
    {
        @Override
        protected ArrayList<Movie> doInBackground(String... sortMode)   {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJson = "";
            try
            {
                URL url = fetchURL(sortMode[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJson = buffer.toString();
            }catch (Exception e)
            {
                Log.e("exeption", e.getMessage());
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try{
                return getMovieFromjson(moviesJson);
            }catch (Exception e)
            {
                Log.w("Tag", e.getMessage());
                return null;
            }

        }
        private URL fetchURL(String sortMode) throws MalformedURLException {
            /*String baseURL = "https://api.themoviedb.org/3/discover/movie?";
            String sortby = true ? "sort_by=popularity.desc" :  "sort_by=vote_average.desc";
            return baseURL + sortby + BuildConfig.THE_MOVIE_DATABASE_KEY;*/
            final String MOVIES_BASE_URL =
                    "https://api.themoviedb.org/3/discover/movie?";

            final String SORTBY = "sort_by";
            final String key = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORTBY, sortMode)
                    .appendQueryParameter(key, BuildConfig.THE_MOVIE_DATABASE_KEY)
                    .build();
            Log.w("Tag", builtUri.toString());
            return new URL(builtUri.toString());
        }
        private  ArrayList<Movie> getMovieFromjson(String jsonStr) throws JSONException {
            final String MDB_RESULTS = "results";
            final String MDB_ID = "id";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_TITLE = "title";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_VOTE_AVERAGE = "vote_average";
            final String MDB_POPULARITY = "popularity";
            final String MDB_ORIGINAL_TITLE = "original_title";
            ArrayList<Movie> movies = new ArrayList<>();
            JSONObject movieDetails = new JSONObject(jsonStr);
            JSONArray weatherData = movieDetails.getJSONArray(MDB_RESULTS);
            for (int i = 0; i < weatherData.length(); i++)
            {
                Movie movie = new Movie(getActivity());
                JSONObject jsonObject = weatherData.getJSONObject(i);
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
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
//            imageListAdapter.clear();
            ArrayList<String> uriPaths = imageListAdapter.getUriList();
            uriPaths.clear();
            for (Movie movie: movies
                 ) {
                Log.w("tag", movie.getPosterPath());
                uriPaths.add(movie.getPosterPath());
                //imageListAdapter.add(movie.getPosterPath());
            }
            imageListAdapter.notifyDataSetChanged();
        }
    }


}
class ImageListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imageUrls;

    public ImageListAdapter(Context context, String[] imageUrls) {
        this.imageUrls = new ArrayList<>();
        this.context = context;
        this.imageUrls.toArray(imageUrls);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int i) {
        return imageUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
           // convertView = inflater.inflate(R.layout.movies_posters, parent, false);

            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(context);
                //imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }


        Picasso
                .with(context)
                .load(imageUrls.get(position))
                .fit()
                .into( imageView);

        return imageView;
    }
    public ArrayList<String> getUriList()
    {
        return imageUrls;
    }
}
