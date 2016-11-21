package com.example.kemo.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kemo on 21/11/2016.
 */
public class DetailsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> items;
    private View movieView;
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
            if (view == null || !(view instanceof RelativeLayout))
            view = movieView;
        }

        return view;
    }
    public ArrayList<Object> getItems()
    {
        return items;
    }
}
