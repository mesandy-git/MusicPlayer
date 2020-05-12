package com.my.musicplayer.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.musicplayer.Adapters.MusicListAdapter;
import com.my.musicplayer.AudioModel;
import com.my.musicplayer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.my.musicplayer.MyMainActivity.controller;
import static com.my.musicplayer.MyMainActivity.getAct;
import static com.my.musicplayer.MyMainActivity.getController;

public class MusicFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<AudioModel> songsLists;
    private CardView playAllView, shuffleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setData(ArrayList<AudioModel> songsList) {
        MusicListAdapter adapter = new MusicListAdapter(songsList, getAct());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<AudioModel> getMusicList() {
        ArrayList<AudioModel> audioList = new ArrayList<>();
        SharedPreferences shared = controller.getSharedPreferences("MUSICLIST", MODE_PRIVATE);
        String strText = shared.getString("MUSICLISTS", "null");
        try {
            JSONArray jsonArray = new JSONArray(strText);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AudioModel audioModel = new AudioModel();
                audioModel.setaName(jsonObject.getString("aName"));
                audioModel.setaAlbum(jsonObject.getString("aAlbum"));
                audioModel.setaPath(jsonObject.getString("aPath"));
                audioList.add(audioModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (audioList.size() != 0)
            Collections.sort(audioList, (AudioModel s1, AudioModel s2) -> s1.getaName().compareToIgnoreCase(s2.getaName()));
        return audioList;
    }


    private void playAll() {
        if (controller.tempAudioList.size() > 0) {
            int pos = 0;
            String path = controller.tempAudioList.get(pos).getaPath();
            controller.setLastTrackName(controller.tempAudioList.get(pos).getaName());
            controller.pref.setTrackPosition(pos);
            controller.playAudio(path);
        }
    }

    private void shufflePlay() {
        if (controller.tempAudioList.size() > 0) {
            Random random = new Random();
            int pos = random.nextInt(controller.tempAudioList.size() - 1);
            String path = controller.tempAudioList.get(pos).getaPath();
            controller.setLastTrackName(controller.tempAudioList.get(pos).getaName());
            controller.pref.setTrackPosition(pos);
            controller.playAudio(path);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        playAllView = view.findViewById(R.id.playAllView);
        shuffleView = view.findViewById(R.id.shuffleView);
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shuffleView.setOnClickListener(v -> {
            shufflePlay();
        });
        playAllView.setOnClickListener(v -> {
            playAll();
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Handler handler = new Handler();
        Runnable r = () -> {
            if (controller.tempAudioList.size() > 0) {
                setData(controller.tempAudioList);
            }
        };
        handler.postDelayed(r, 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
