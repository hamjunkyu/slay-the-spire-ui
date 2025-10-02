package com.example.hjk;

public class GameCard {
    private int imageResId;  // 카드의 이미지 리소스 ID
    private boolean isRevealed;  // 카드가 뒤집혀 있는지 여부
    private boolean isMatched;  // 카드가 매칭되었는지 여부

    public GameCard(int imageResId) {
        this.imageResId = imageResId;
        this.isRevealed = false;
        this.isMatched = false;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}
