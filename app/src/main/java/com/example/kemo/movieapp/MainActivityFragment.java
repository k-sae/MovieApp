package com.example.kemo.movieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView_movies);
        imageListAdapter = new ImageListAdapter(getActivity(), new Movie[0]);
        gridView.setAdapter(imageListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) imageListAdapter.getItem(i);
                ((MovieAppActivity) getActivity()).navigate(movie);
            }
        });
        return view;
    }

    private void fetchMovies() {
        MovieFetcher fetchMovies = new MovieFetcher();
        fetchMovies.imageListAdapter = imageListAdapter;
        fetchMovies.movieAppActivity =(MovieAppActivity) getActivity();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = preferences.getString(getString(R.string.pref_sort_key)
                , getString(R.string.pref_sort_default));
        if(sortBy.equals(getString(R.string.pref_sortBy_favourites_value)))
        {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Movie> realmResults = realm.where(Movie.class).findAll();
            ArrayList<Object> movies = new ArrayList<>(realmResults.size());
            movies.addAll(realmResults);
            fetchMovies.updateMovies(movies);
        }
        else
        fetchMovies.execute(sortBy);
    }

}



