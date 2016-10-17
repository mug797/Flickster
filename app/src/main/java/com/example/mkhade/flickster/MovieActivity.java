package com.example.mkhade.flickster;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mkhade.flickster.adapters.MovieArrayAdapter;
import com.example.mkhade.flickster.models.Movie;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    ListView lvItems;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieArrayAdapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        getMovieList();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMovieList();
            }
        });
    }

    void getMovieList(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                movies.clear();
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieArrayAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", "Num movies: " + movies.size());
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}