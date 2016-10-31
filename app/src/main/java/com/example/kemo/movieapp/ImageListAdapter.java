package com.example.kemo.movieapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kemo on 30/10/2016.
 */
public class ImageListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movies;

    public ImageListAdapter(Context context, Movie[] movies) {
        this.movies = new ArrayList<>();
        this.context = context;
        this.movies.toArray(movies);
            }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        RelativeLayout relativeLayout;
        final ProgressBar progressBar;
        //i think creating it using code is better

//         convertView = inflater.inflate(R.layout.movies_posters, parent, false);
        if (convertView == null) {
           relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.movies_posters, parent, false);
            Log.e("TAAAAAG", Integer.toString(relativeLayout.getChildCount()));
//           imageView = (ImageView) relativeLayout.findViewById(R.id.Detail_poster_imageView);
//           progressBar = (ProgressBar) relativeLayout.findViewById(R.id.movie_poster_progressBar);
                imageView = (ImageView) relativeLayout.getChildAt(1);
                progressBar = (ProgressBar) relativeLayout.getChildAt(0);
//            imageView = new ImageView(context);
//            //static height -_-
//            relativeLayout = new RelativeLayout(context);
//            relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            relativeLayout.addView(imageView);
//            progressBar = new ProgressBar(context,null, android.R.attr.progressBarStyleLarge);
//            progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            progressBar.setIndeterminate(true);
//            progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
//            progressBar.setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(100,100);
//            layoutParam.addRule(RelativeLayout.CENTER_IN_PARENT);
//            relativeLayout.addView(progressBar, layoutParam);
        } else {
            relativeLayout = (RelativeLayout) convertView;
                imageView = (ImageView) relativeLayout.getChildAt(1);
                progressBar = (ProgressBar) relativeLayout.getChildAt(0);
                progressBar.setVisibility(View.VISIBLE);
        }
            Picasso
                    .with(context)
                    .load(movies.get(position).getPosterPath())
                    .fit()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //TODO
                           progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
            return relativeLayout;
        }


    public ArrayList<Movie> getUriList() {
        return movies;
    }
}

