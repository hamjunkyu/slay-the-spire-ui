package com.example.hjk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GameRecordAdapter extends RecyclerView.Adapter<GameRecordAdapter.GameRecordViewHolder> {

    private final List<GameRecord> records;

    public GameRecordAdapter(List<GameRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public GameRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new GameRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameRecordViewHolder holder, int position) {
        GameRecord record = records.get(position);
        holder.accountName.setText(record.getAccountName());
        holder.score.setText("점수: " + record.getScore());
        holder.mistakes.setText("매치 실패: " + record.getMistakes());

        // 날짜 형식 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        holder.date.setText("날짜: " + sdf.format(record.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class GameRecordViewHolder extends RecyclerView.ViewHolder {
        TextView accountName, score, mistakes, date;

        public GameRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.recordAccountName);
            score = itemView.findViewById(R.id.recordScore);
            mistakes = itemView.findViewById(R.id.recordMistakes);
            date = itemView.findViewById(R.id.recordDate);
        }
    }
}
