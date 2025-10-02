package com.example.hjk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private final List<GameRecord> records;

    public RecordAdapter(List<GameRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        GameRecord record = records.get(position);
        holder.accountName.setText(record.getAccountName());
        holder.score.setText("점수: " + record.getScore());
        holder.mistakes.setText("실수: " + record.getMistakes());
        holder.date.setText("날짜: " + record.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView accountName, score, mistakes, date;

        RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.recordAccountName);
            score = itemView.findViewById(R.id.recordScore);
            mistakes = itemView.findViewById(R.id.recordMistakes);
            date = itemView.findViewById(R.id.recordDate);
        }
    }
}
