package com.example.hjk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class StatisticsFragment extends Fragment {

    private ImageView characterStats, leaderboard, challengeRecord;
    private ImageButton backButton2;
    private TextView backButton2Text;

    private boolean isFragmentOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        initializeViews(view);

        setButtonListeners();

        return view;
    }

    private void initializeViews(View view) {
        characterStats = view.findViewById(R.id.cardCollection);
        leaderboard = view.findViewById(R.id.artifactCollection);
        challengeRecord = view.findViewById(R.id.potionLab);

        backButton2 = view.findViewById(R.id.backButton2);
        backButton2Text = view.findViewById(R.id.backButton2Text);

        backButton2.setVisibility(View.GONE);
        backButton2Text.setVisibility(View.GONE);
    }

    private void setButtonListeners() {
        // characterStats.setOnClickListener(v -> openFragment(new CharacterStatsFragment()));
        leaderboard.setOnClickListener(v -> openFragment(new LeaderboardFragment()));
        challengeRecord.setOnClickListener(v -> openFragment(new ChallengeRecordFragment()));

        backButton2.setOnClickListener(v -> closeFragment()); // 뒤로가기 버튼
        backButton2Text.setOnClickListener(v -> closeFragment());
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        characterStats.setVisibility(View.GONE);
        leaderboard.setVisibility(View.GONE);
        challengeRecord.setVisibility(View.GONE);

        backButton2.setVisibility(View.VISIBLE);
        backButton2Text.setVisibility(View.VISIBLE);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).findViewById(R.id.backButton).setVisibility(View.GONE);
            ((MainActivity) getActivity()).findViewById(R.id.backButtonText).setVisibility(View.GONE);
        }
        isFragmentOpen = true;
    }

    private void closeFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();

        if (isFragmentOpen && fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();

            // Back 버튼 숨김
            if (fragmentManager.getBackStackEntryCount() == 1) { // root만 남았을 때
                characterStats.setVisibility(View.VISIBLE);
                leaderboard.setVisibility(View.VISIBLE);
                challengeRecord.setVisibility(View.VISIBLE);

                backButton2.setVisibility(View.GONE);
                backButton2Text.setVisibility(View.GONE);
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).findViewById(R.id.backButton).setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).findViewById(R.id.backButtonText).setVisibility(View.VISIBLE);
                }
                isFragmentOpen = false;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 하위 프래그먼트를 관리 중일 때 BackStack 비우기
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
