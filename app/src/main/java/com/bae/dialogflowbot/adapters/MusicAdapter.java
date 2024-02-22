package com.bae.dialogflowbot.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bae.dialogflowbot.R;
import com.bae.dialogflowbot.models.RelaxingSounds;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.IOException;
import java.util.Locale;

public class MusicAdapter extends FirebaseRecyclerAdapter<RelaxingSounds, MusicAdapter.MyMusicViewHolder> {
    Context context;
    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable updateProgressRunnable;
    RecyclerView recyclerView;
    boolean isPlaying = false;
    int currentlyPlayingPosition = -1; // Initially no item is playing

    public MusicAdapter(@NonNull FirebaseRecyclerOptions<RelaxingSounds> options, Context context, RecyclerView recyclerView) {
        super(options);
        this.context = context;
        this.recyclerView = recyclerView;
        mediaPlayer = new MediaPlayer();
        handler = new Handler();
        updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (isPlaying) {
                    updateMusicProgress(currentlyPlayingPosition);
                }
                handler.postDelayed(this, 1000); // Update every second
            }
        };
    }

    private void updateMusicProgress(int position) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        if (holder instanceof MyMusicViewHolder) {
            MyMusicViewHolder musicViewHolder = (MyMusicViewHolder) holder;
            if (mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int totalDuration = mediaPlayer.getDuration();
                int seconds = (currentPosition / 1000) % 60;
                int minutes = (currentPosition / (1000 * 60)) % 60;
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                musicViewHolder.musicDuration.setText(formattedTime + " / " + getTotalDuration(totalDuration));
            }
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull MyMusicViewHolder holder, int position, @NonNull RelaxingSounds model) {
        holder.musicName.setText(model.getName());
        holder.musicDuration.setText(model.getDuration());
        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAbsoluteAdapterPosition();
                if (isPlaying && currentlyPlayingPosition == clickedPosition) {
                    pauseMusic();
                    isPlaying = false;
                    handler.removeCallbacks(updateProgressRunnable);
                } else {
                    playMusic(model.getUrl());
                    isPlaying = true;
                    currentlyPlayingPosition = clickedPosition; // Update currently playing position
                    handler.postDelayed(updateProgressRunnable, 1000);
                }
                notifyDataSetChanged(); // Notify adapter of data set changed to update UI
                holder.playBtn.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play); // Update play button icon
            }
        });

        // Set listener for completion of playback
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                currentlyPlayingPosition = -1; // Reset currently playing position
                handler.removeCallbacks(updateProgressRunnable); // Stop updating progress when playback completes
                notifyDataSetChanged(); // Notify adapter of data set changed to update UI
                holder.playBtn.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        });
    }

    private String getTotalDuration(int durationInMilliseconds) {
        int seconds = (durationInMilliseconds / 1000) % 60;
        int minutes = (durationInMilliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @NonNull
    @Override
    public MyMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_track_layout, parent, false);
        return new MyMusicViewHolder(view);
    }

    public class MyMusicViewHolder extends RecyclerView.ViewHolder {
        ImageView playBtn;
        TextView musicName, musicDuration;

        public MyMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            musicName = itemView.findViewById(R.id.music_name);
            musicDuration = itemView.findViewById(R.id.music_duration);
            playBtn = itemView.findViewById(R.id.music_play_button);
        }
    }

    private void playMusic(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}

