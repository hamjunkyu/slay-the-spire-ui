package com.example.hjk;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.GameCardViewHolder> {

    private final List<GameCard> cards;
    private final CardMatchListener matchListener; // 매칭 결과를 알림
    private GameCard firstCard = null;
    private GameCard secondCard = null;
    private boolean isLocked = false; // 클릭 잠금 상태
    private final Handler handler = new Handler();

    public GameCardAdapter(List<GameCard> cards, CardMatchListener matchListener) {
        this.cards = cards;
        this.matchListener = matchListener;
    }

    @NonNull
    @Override
    public GameCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card_item, parent, false);
        return new GameCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameCardViewHolder holder, int position) {
        GameCard card = cards.get(position);

        // 카드 상태에 따라 이미지 표시
        if (card.isRevealed() || card.isMatched()) {
            holder.cardImage.setImageResource(card.getImageResId());
        } else {
            holder.cardImage.setImageResource(R.drawable.card_back); // 카드 뒷면
        }

        // 클릭 이벤트 설정
        holder.itemView.setOnClickListener(v -> {
            if (isLocked || card.isRevealed() || card.isMatched()) return;

            card.setRevealed(true);
            notifyDataSetChanged();

            if (firstCard == null) {
                firstCard = card;
            } else if (secondCard == null) {
                secondCard = card;
                checkMatch();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private void checkMatch() {
        isLocked = true;

        boolean isMatch = firstCard.getImageResId() == secondCard.getImageResId();

        handler.postDelayed(() -> {
            if (isMatch) {
                firstCard.setMatched(true);
                secondCard.setMatched(true);
                matchListener.onMatchSuccess();
            } else {
                firstCard.setRevealed(false);
                secondCard.setRevealed(false);
                matchListener.onMatchFail();
            }

            firstCard = null;
            secondCard = null;
            isLocked = false;

            notifyDataSetChanged();
        }, 1000); // 1초 지연
    }

    static class GameCardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;

        public GameCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
        }
    }

    public interface CardMatchListener {
        void onMatchSuccess();
        void onMatchFail();
    }
}
