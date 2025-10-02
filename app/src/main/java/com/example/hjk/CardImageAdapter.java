package com.example.hjk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardImageAdapter extends RecyclerView.Adapter<CardImageAdapter.CardViewHolder> {

    private List<Integer> cardImages;

    public CardImageAdapter(List<Integer> cardImages) {
        this.cardImages = cardImages;
    }

    // 카드 데이터 업데이트
    public void updateData(List<Integer> newCardImages) {
        this.cardImages = newCardImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(cardImages.get(position));
    }

    @Override
    public int getItemCount() {
        return cardImages.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cardImage;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImageView);
        }

        public void bind(int imageRes) {
            cardImage.setImageResource(imageRes);
        }
    }
}
