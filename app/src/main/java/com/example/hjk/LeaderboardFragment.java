package com.example.hjk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private TextView leaderboardText;
    private RecyclerView leaderboardRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardText = view.findViewById(R.id.leaderboardText);
        leaderboardRecycler = view.findViewById(R.id.leaderboardRecycler);

        leaderboardText.setText("리더보드");

        // 리더보드 기록 가져오기
        List<GameRecord> leaderboardRecords = SharedPrefsHelper.getLeaderboardRecords(getContext());
        Collections.sort(leaderboardRecords, (r1, r2) -> Integer.compare(r2.getScore(), r1.getScore())); // 점수 내림차순

        GameRecordAdapter adapter = new GameRecordAdapter(leaderboardRecords);
        leaderboardRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderboardRecycler.setAdapter(adapter);

        return view;
    }
}
