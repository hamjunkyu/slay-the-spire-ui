package com.example.hjk;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsHelper {

    private static final String PREFS_NAME = "GameRecords";
    private static final String LEADERBOARD_KEY = "Leaderboard";
    private static final String CHALLENGE_RECORD_KEY_PREFIX = "ChallengeRecord_";

    // 현재 계정 이름 가져오기
    public static String getCurrentAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AccountData", Context.MODE_PRIVATE);
        return prefs.getString("currentAccountName", null);
    }

    // 리더보드 기록 저장
    public static void saveLeaderboardRecord(Context context, GameRecord record) {
        List<GameRecord> records = getLeaderboardRecords(context);
        records.add(record);
        saveToPrefs(context, LEADERBOARD_KEY, records);
    }

    // 리더보드 기록 가져오기
    public static List<GameRecord> getLeaderboardRecords(Context context) {
        return getFromPrefs(context, LEADERBOARD_KEY, new TypeToken<List<GameRecord>>() {});
    }

    // 도전 기록 저장
    public static void saveChallengeRecord(Context context, GameRecord record) {
        String key = CHALLENGE_RECORD_KEY_PREFIX + record.getAccountName();
        List<GameRecord> records = getChallengeRecords(context, record.getAccountName());
        records.add(0, record); // 최신 기록을 위로
        saveToPrefs(context, key, records);
    }

    // 도전 기록 가져오기
    public static List<GameRecord> getChallengeRecords(Context context, String accountName) {
        String key = CHALLENGE_RECORD_KEY_PREFIX + accountName;
        return getFromPrefs(context, key, new TypeToken<List<GameRecord>>() {});
    }

    private static <T> void saveToPrefs(Context context, String key, List<T> data) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(data);
        prefs.edit().putString(key, json).apply();
    }

    private static <T> List<T> getFromPrefs(Context context, String key, TypeToken<List<T>> typeToken) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(key, null);
        if (json == null) return new ArrayList<>();
        return new Gson().fromJson(json, typeToken.getType());
    }
}
