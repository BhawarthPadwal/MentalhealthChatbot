package com.bae.dialogflowbot.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.activities.ArticleDetails;
import com.bae.dialogflowbot.models.Articles;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ArticleAdapter extends FirebaseRecyclerAdapter<Articles, ArticleAdapter.MyArticleViewHolder> {
    Context context;
    public ArticleAdapter(@NonNull FirebaseRecyclerOptions<Articles> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ArticleAdapter.MyArticleViewHolder holder, int position, @NonNull Articles model) {
        holder.articleTitle.setText(model.getTitle());
        Glide.with(context).load(model.getImageUrl()).into(holder.artImg);
        holder.articleCard.setOnClickListener((v)->{
            Intent intent = new Intent(context, ArticleDetails.class);
            intent.putExtra("title",model.getTitle());
            intent.putExtra("imageUrl",model.getImageUrl());
            intent.putExtra("content",model.getContent());
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public ArticleAdapter.MyArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_recyclerview_layout, parent, false);
        return new MyArticleViewHolder(view);
    }

    public class MyArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView artImg;
        TextView articleTitle;
        CardView articleCard;
        public MyArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            artImg = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_title_item);
            articleCard = itemView.findViewById(R.id.article_card);
        }
    }
}
