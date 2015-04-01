package com.acadgild.imdb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;


public class ListAdapter extends ArrayAdapter<MovieInfo> {

    private Context context;
    private int resource;
    private List<MovieInfo> movieList;

    public ListAdapter(Context context, int resource, List<MovieInfo> movieList) {
        super(context, resource, movieList);

        this.context = context;
        this.resource = resource;
        this.movieList = movieList;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {

        Holder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.titleHolder = (TextView) row.findViewById(R.id.titleTextView);
            holder.dateHolder = (TextView) row.findViewById(R.id.releaseDateTextView);
            holder.ratingHolder = (RatingBar) row.findViewById(R.id.ratingBar);
            holder.imageHolder = (ImageView) row.findViewById(R.id.movieImageView);
            holder.voteHolder = (TextView) row.findViewById(R.id.voteCountTextView);

            row.setTag(holder);
        } else {

            holder = (Holder) row.getTag();
        }

        MovieInfo info = movieList.get(position);

        holder.titleHolder.setText(info.getTitle());
        holder.dateHolder.setText(info.getDate());
        holder.ratingHolder.setRating(Float.parseFloat(info.getVote_average()) / 2);
        holder.imageHolder.setImageResource(R.drawable.loading_image);
        if (info.getPoster().equals("null")) {
            holder.imageHolder.setImageResource(R.drawable.loading_image);
        } else {
            new DownloadImageTask(holder.imageHolder).execute("http://image.tmdb.org/t/p/w500" + info.getPoster());
        }
        holder.voteHolder.setText("(" + info.getVote_average() + "/10) voted by " + info.getVote_count() + " users");

        return row;
    }

    private static class Holder {
        TextView titleHolder;
        TextView dateHolder;
        RatingBar ratingHolder;
        ImageView imageHolder;
        TextView voteHolder;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}