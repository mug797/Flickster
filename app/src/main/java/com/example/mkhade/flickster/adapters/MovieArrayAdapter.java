package com.example.mkhade.flickster.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkhade.flickster.R;
import com.example.mkhade.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by mkhade on 10/14/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
    }

    private static class ViewHolder_popular {
        ImageView ivpopImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return Movie.Popularity.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getPopularity().getValue();
    }

    private View getInflatedLayoutForType(int type) {
        if (type == Movie.Popularity.MORETHAN5.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.popular_movie, null);
        } else if (type == Movie.Popularity.LESSTHAN5.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }

    @Override
    public View getView(int position, View vi, ViewGroup parent) {
        Movie movie = getItem(position);
        if (movie == null) { Log.e("ERROR", "Movie cannot be null"); return null; }

        int viewType = this.getItemViewType(position);
        switch (viewType) {
            case 0: //MORETHAN5

                ViewHolder_popular viewHolder_popular;
                View convertView = vi;
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.popular_movie, parent, false);
                    viewHolder_popular = new ViewHolder_popular();
                    viewHolder_popular.ivpopImage = (ImageView) convertView.findViewById(R.id.ivpopView);
                    convertView.setTag(viewHolder_popular);
                } else {
                    viewHolder_popular = (ViewHolder_popular) convertView.getTag();
                }

                viewHolder_popular.ivpopImage.setImageBitmap(getBitmapFromUrl(movie.getPoster_path()));
                Picasso.with(getContext()).load(movie.getPoster_path()).resize(500,0).centerCrop().placeholder(R.drawable.coming_soon).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder_popular.ivpopImage);
                return convertView;

            case 1: //LESSTHAN5
                ViewHolder viewHolder;
                convertView = vi;
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_movie, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                    viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                    viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.ivImage.setImageBitmap(getBitmapFromUrl(movie.getPoster_path()));
                Picasso.with(getContext()).load(movie.getPoster_path()).resize(500,0).centerCrop().placeholder(R.drawable.coming_soon).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage);
                viewHolder.tvTitle.setText(movie.getOriginal_title());
                viewHolder.tvOverview.setText(movie.getOverview());
                return convertView;

            default:
                Log.e("ERROR", "Wrong type: " + viewType);
                break;
        }
        return null;
    }

    public Bitmap getBitmapFromUrl(String url){
        ImageLoader il = new ImageLoader(url);
        try {
            return il.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

        private String url;

        public ImageLoader(String url){
            this.url = url;
        }

        protected Bitmap doInBackground(Void... params) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("ERROR", "Error getting bitmap", e);
            }
            return bm;
        }
    }
}
