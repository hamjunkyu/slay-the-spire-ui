package com.example.hjk;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFragment extends Fragment {

    private RecyclerView gameCardsGrid;
    private TextView remainingTime;
    private TextView mistakeCounter;
    private ImageButton gameExitButton;

    private List<GameCard> cards;
    private GameCardAdapter cardAdapter;

    private int mistakes = 0;
    private CountDownTimer timer;

    private static final int GAME_TIME_MS = 120000; // 2분
    private static final int PENALTY_MS = 5000; // 잘못된 매칭 시 5초 감소
    private long remainingTimeMs = GAME_TIME_MS; // 남은 시간(ms)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        initializeViews(view);
        setupGame();
        startTimer();

        return view;
    }

    private void initializeViews(View view) {
        gameCardsGrid = view.findViewById(R.id.gameCardsGrid);
        remainingTime = view.findViewById(R.id.remainingTime);
        mistakeCounter = view.findViewById(R.id.mistakeCounter);
        gameExitButton = view.findViewById(R.id.gameExitButton);

        gameExitButton.setOnClickListener(v -> exitGame(false));
    }

    private void setupGame() {
        cards = createCards();
        Collections.shuffle(cards);

        gameCardsGrid.setLayoutManager(new GridLayoutManager(getContext(), 5));

        // 간격 조정
        gameCardsGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.set(64, 0, 64, 4); // 아이템 간격
            }
        });

        cardAdapter = new GameCardAdapter(cards, new GameCardAdapter.CardMatchListener() {
            @Override
            public void onMatchSuccess() {
                if (allCardsMatched()) {
                    endGame(true);
                }
            }

            @Override
            public void onMatchFail() {
                mistakes++;
                mistakeCounter.setText("매칭 실패: " + mistakes);
                reduceTime(PENALTY_MS);
            }
        });
        gameCardsGrid.setAdapter(cardAdapter);
    }

    private List<GameCard> createCards() {
        List<Integer> cardImages = List.of(
                R.drawable.card_ironclad_strike, R.drawable.card_ironclad_defend, R.drawable.card_ironclad_bash,
                R.drawable.card_silent_strike, R.drawable.card_silent_defend, R.drawable.card_silent_neutralize,
                R.drawable.card_defect_strike, R.drawable.card_defect_defend, R.drawable.card_defect_dualcast,
                R.drawable.card_watcher_strike, R.drawable.card_watcher_defend, R.drawable.card_watcher_eruption
        );

        // 랜덤으로 10개의 이미지를 선택
        List<Integer> selectedImages = new ArrayList<>(cardImages);
        Collections.shuffle(selectedImages);
        selectedImages = selectedImages.subList(0, 10);

        // 2장씩 복사하여 카드 생성
        List<GameCard> cards = new ArrayList<>();
        for (int image : selectedImages) {
            cards.add(new GameCard(image));
            cards.add(new GameCard(image));
        }

        // 카드를 섞어 반환
        Collections.shuffle(cards);
        return cards;
    }

    private boolean allCardsMatched() {
        for (GameCard card : cards) {
            if (!card.isMatched()) return false;
        }
        return true;
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel(); // 기존 타이머 취소
        }

        timer = new CountDownTimer(remainingTimeMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMs = millisUntilFinished;
                int seconds = (int) (millisUntilFinished / 1000);
                remainingTime.setText(String.format("%d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                remainingTimeMs = 0;
                endGame(false);
            }
        };
        timer.start();
    }

    private void reduceTime(int penaltyMs) {
        remainingTimeMs -= penaltyMs;
        if (remainingTimeMs <= 0) {
            remainingTimeMs = 0;
            endGame(false);
        } else {
            startTimer(); // 남은 시간을 반영한 새 타이머 시작
        }
    }

    private void endGame(boolean success) {
        if (timer != null) timer.cancel();

        int finalScore = (int) (remainingTimeMs / 1000.0 * 100); // 점수 계산

        if (success) {
            saveGameRecord(finalScore, true);
            Toast.makeText(getContext(), "게임 성공! 점수: " + finalScore, Toast.LENGTH_SHORT).show();
        } else {
            saveGameRecord(finalScore, false);
            Toast.makeText(getContext(), "게임 실패! 시간 초과.", Toast.LENGTH_SHORT).show();
        }

        exitGame(true);
    }

    private void saveGameRecord(int score, boolean isSuccess) {
        String currentAccount = SharedPrefsHelper.getCurrentAccount(getContext());
        if (currentAccount == null) return;

        GameRecord record = new GameRecord(currentAccount, score, mistakes, System.currentTimeMillis());
        SharedPrefsHelper.saveChallengeRecord(getContext(), record);

        if (isSuccess) {
            SharedPrefsHelper.saveLeaderboardRecord(getContext(), record);
        }
    }

    private void exitGame(boolean withSave) {
        if (timer != null) {
            timer.cancel();
        }

        getParentFragmentManager().popBackStack();

        if (!withSave) {
            Toast.makeText(getContext(), "게임 나가기", Toast.LENGTH_SHORT).show();
        }

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.closeFragment();
        }
    }
}
