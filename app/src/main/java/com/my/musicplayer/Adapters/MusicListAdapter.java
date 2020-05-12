package com.my.musicplayer.Adapters;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.musicplayer.AudioModel;
import com.my.musicplayer.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.my.musicplayer.MyMainActivity.controller;
import static com.my.musicplayer.MyMainActivity.getController;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private ArrayList<AudioModel> listData;
    private Activity activity;

    public MusicListAdapter(ArrayList<AudioModel> listData, Activity activity) {
        this.listData = listData;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.music_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AudioModel audioModel = listData.get(position);
        holder.textView.setText(audioModel.getaName());
        holder.albumName.setText(audioModel.getaAlbum());
        getAudioAlbumImageContentUri(audioModel.getaPath(), holder.imageView);
        holder.relativeLayout.setOnClickListener(view -> {
            getController().setLastTrackName(audioModel.getaName());
            getController().pref.setTrackPosition(position);
            getController().playAudio(audioModel.getaPath());
            Log.d("Position", "" + position + " " + audioModel.getaName());
        });
//                Toast.makeText(view.getContext(), "click on item: " + audioModel.getaPath(), Toast.LENGTH_LONG).show());
    }

    private void getAudioAlbumImageContentUri(String filePath, ImageView imageView) {
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA + "=? ";
        String[] projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID};

        Cursor cursor = activity.getContentResolver().query(audioUri, projection, selection,
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri imgUri = ContentUris.withAppendedId(sArtworkUri, albumId);
            Log.d(TAG, "AudioCoverImgUri = " + imgUri.toString());
            try {
                Glide.with(activity)
                        .load(imgUri)
                        .placeholder(R.mipmap.music)
                        .error(R.mipmap.music)
                        .into(imageView);
            } catch (Exception e) {
                //TODO:FileNotFoundException
            }
            cursor.close();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView, albumName;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.songImage);
            this.textView = itemView.findViewById(R.id.songName);
            this.albumName = itemView.findViewById(R.id.albumName);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}