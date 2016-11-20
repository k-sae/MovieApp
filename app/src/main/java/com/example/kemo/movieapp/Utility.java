package com.example.kemo.movieapp;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by kemo on 19/11/2016.
 */
public class Utility {
    private final static String TEMP_REALM = "Temp_realm";
    public static void addToRealm(Movie movie)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(movie);
        realm.commitTransaction();

    }
    public static void removeFromRealm(int id)
    {
        Realm realm = Realm.getDefaultInstance();
        findFirstItem(id,realm);
    }
    private static Movie findFirstItem(int id, Realm realm)
    {
        RealmResults<Movie> realmResults = realm.where(Movie.class).equalTo("movieId",id).findAll();
        realm.beginTransaction();
        Movie movie =  realmResults.get(0);
        movie.removeFromRealm();
        realm.commitTransaction();
        return movie;
    }

}
