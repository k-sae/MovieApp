package com.example.kemo.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MovieAppActivity {
    public boolean isDual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       View view  = LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);
        setContentView(view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       if (view.findViewById(R.id.DetailsContainer) != null)
        {
            isDual = true;
            if(savedInstanceState == null)
            {
              /*  getSupportFragmentManager().beginTransaction().add(R.id.DetailsContainer,
                        new DetailsActivityFragment())
                        .commit();*/
            }
        }
        else isDual = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigate(Movie movie) {
            if (isDual)
            {
                DetailsActivityFragment.movie = movie;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.DetailsContainer,
                        new DetailsActivityFragment())
                        .commit();
            }
            else
            {
                Intent intent = new Intent(this, DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            }
    }
}
