package com.example.medimate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class TheAdapter extends RecyclerView.Adapter<com.example.medimate.TheAdapter.MyViewHolder> implements Filterable {

    Context context;
    List<Model> newsList;
    List<Model> newsListFiltered;

    public TheAdapter(Context context, List<Model> newsList) {
        this.context = context;
        this.newsList = newsList;
        this.newsListFiltered = newsList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charcater = constraint.toString();
                if (charcater.isEmpty()){
                    newsListFiltered = newsList ;
                }else {
                    List<Model> filterList = new ArrayList<>();
                    for (Model row: newsList){
                        if (row.getTitle().toLowerCase().contains(charcater.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    newsListFiltered = filterList ;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = newsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                newsListFiltered = (ArrayList<Model>) results.values ;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newslist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        String time = newsList.get(position).getTime();
        String date = newsList.get(position).getTime();
        String title = newsListFiltered.get(position).getTitle();
        String image = newsListFiltered.get(position).getUrlToImage();
        String description = newsListFiltered.get(position).getDescription();
        String source = newsListFiltered.get(position).getSource();

        RequestOptions requestOptions = new RequestOptions();

        holder.title.setText(title);
        holder.source.setText(source);
        holder.description.setText(description);
        holder.time.setText("\u2022" + Utils.DateToTimeFormat(time));
        holder.publishedAt.setText(Utils.DateFormat(date));

        Glide.with(context).load(image).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;            }
        }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return newsListFiltered.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView title, publishedAt, time, source, description;
        ImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder( View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            time = itemView.findViewById(R.id.time);
            source = itemView.findViewById(R.id.source);
            description = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.prograss_load_photo);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            int position = getAdapterPosition();
            Model models = newsListFiltered.get(position);

            Uri uri = Uri.parse(models.getUrl());

            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}
