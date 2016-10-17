package com.example.mkhade.flickster.models;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.mkhade.flickster.R;

import java.util.ArrayList;

/**
 * Created by mkhade on 10/14/2016.
 */
public class Movie {
    private String poster_path;
    private String original_title;
    private String overview;
    private float vote_average;
    private Popularity popularity;

    public enum Popularity {
        MORETHAN5(0),
        LESSTHAN5(1);

        private int value;
        private Popularity(int value){
            this.value = value;
        }
        public int getValue(){
            return this.value;
        }
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.poster_path = jsonObject.getString("poster_path");
        this.original_title = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.vote_average = (float) jsonObject.getDouble("vote_average");

        if(this.vote_average>5){
            popularity = Popularity.MORETHAN5;
        } else {
            popularity = Popularity.LESSTHAN5;
        }
    }

    public String getPoster_path() {

        if (poster_path.equals("null"))
             return "https://upload.wikimedia.org/wikipedia/en/7/75/No_image_available.png";

        return  "https://image.tmdb.org/t/p/w342"+ poster_path;
    }

    public String getOriginal_title() {
        return original_title != "" ? original_title : "No title available";
    }

    public String getOverview() {
        return overview != "" ? overview : "No overveiw available";
    }

    public float getVote_average() { return vote_average; }
    public Popularity getPopularity() { return popularity; }


    public static ArrayList<Movie> fromJSONArray(JSONArray arr) {
        ArrayList<Movie> res = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                res.add(new Movie(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}