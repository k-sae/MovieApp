package com.example.kemo.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        view = setLayout(view);
        return view;
    }
    private View setLayout(View view)
    {
        //loading Image
        ImageView imageView = (ImageView) view.findViewById(R.id.Detail_poster_imageView);
        Intent intent = getActivity().getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);
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
        Log.e("Details", Integer.toString( movie.getMovieId()));
        return view;
    }
    private void AppendText(TextView textView, String text)
    {
        textView.setText(textView.getText() +" " + text);
    }
}
