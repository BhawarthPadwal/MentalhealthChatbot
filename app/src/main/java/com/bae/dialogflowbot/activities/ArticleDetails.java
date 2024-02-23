package com.bae.dialogflowbot.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bae.dialogflowbot.R;
import com.bumptech.glide.Glide;

public class ArticleDetails extends AppCompatActivity {
    ImageView articleImage;
    TextView articleTitle, articleContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        call_all_ids();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String imageUrl = intent.getStringExtra("imageUrl");

        articleTitle.setText(title);
        articleContent.setText(content);
        Glide.with(this).load(imageUrl).into(articleImage);

    }
    private void call_all_ids() {
        articleTitle = findViewById(R.id.article_details_title);
        articleContent = findViewById(R.id.article_content);
        articleImage = findViewById(R.id.article_detail_image);
    }
}