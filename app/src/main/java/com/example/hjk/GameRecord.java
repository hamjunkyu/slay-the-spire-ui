package com.example.hjk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameRecord {

    private String accountName;
    private int score;
    private int mistakes;
    private long timestamp; // 기록이 저장된 시간(밀리초 단위)

    public GameRecord(String accountName, int score, int mistakes, long timestamp) {
        this.accountName = accountName;
        this.score = score;
        this.mistakes = mistakes;
        this.timestamp = timestamp;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getScore() {
        return score;
    }

    public int getMistakes() {
        return mistakes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // 날짜 형식으로 변환
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }
}