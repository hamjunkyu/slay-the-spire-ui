package com.example.hjk;

import android.annotation.SuppressLint;
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

import java.util.List;

public class ChallengeRecordFragment extends Fragment {

    private TextView challengeRecordText;
    private RecyclerView challengeRecordRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_record, container, false);

        challengeRecordText = view.findViewById(R.id.challengeRecordText);
        challengeRecordRecycler = view.findViewById(R.id.challengeRecordRecycler);

        challengeRecordText.setText("도전기록");

        // 현재 계정 도전 기록 가져오기
        String currentAccount = SharedPrefsHelper.getCurrentAccount(getContext());
        List<GameRecord> challengeRecords = SharedPrefsHelper.getChallengeRecords(getContext(), currentAccount);

        GameRecordAdapter adapter = new GameRecordAdapter(challengeRecords);
        challengeRecordRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        challengeRecordRecycler.setAdapter(adapter);

        return view;
    }
}
