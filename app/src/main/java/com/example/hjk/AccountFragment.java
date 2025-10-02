package com.example.hjk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class AccountFragment extends Fragment {

    private LinearLayout accountSlot1, accountSlot2, accountSlot3;

    private LinearLayout accountTextLayout1, accountTextLayout2, accountTextLayout3;

    private TextView accountName1, accountName2, accountName3;
    private TextView accountPlaytime1, accountPlaytime2, accountPlaytime3;
    private TextView accountPercent1, accountPercent2, accountPercent3;

    private ImageButton acnameChange1, acnameChange2, acnameChange3;
    private ImageButton accountDelete1, accountDelete2, accountDelete3;

    private SharedPreferences sharedPreferences;

    private String currentAccountName = ""; // 현재 선택된 계정명
    private int currentSlot = -1; // 현재 선택된 슬롯 번호 (1, 2, 3 중 하나)
    private long playtimeInSeconds = 0; // 현재 계정의 플레이타임

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("AccountData", Context.MODE_PRIVATE);

        initializeViews(view);

        loadAccountData();  // 저장된 계정 데이터 로드

        setSlotClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        accountSlot1 = view.findViewById(R.id.accountSlot1);
        accountSlot2 = view.findViewById(R.id.accountSlot2);
        accountSlot3 = view.findViewById(R.id.accountSlot3);

        accountTextLayout1 = view.findViewById(R.id.accountTextLayout1);
        accountTextLayout2 = view.findViewById(R.id.accountTextLayout2);
        accountTextLayout3 = view.findViewById(R.id.accountTextLayout3);

        accountName1 = view.findViewById(R.id.accountName1);
        accountName2 = view.findViewById(R.id.accountName2);
        accountName3 = view.findViewById(R.id.accountName3);

        accountPlaytime1 = view.findViewById(R.id.accountPlaytime1);
        accountPlaytime2 = view.findViewById(R.id.accountPlaytime2);
        accountPlaytime3 = view.findViewById(R.id.accountPlaytime3);

        accountPercent1 = view.findViewById(R.id.accountPercent1);
        accountPercent2 = view.findViewById(R.id.accountPercent2);
        accountPercent3 = view.findViewById(R.id.accountPercent3);

        acnameChange1 = view.findViewById(R.id.acnameChange1);
        acnameChange2 = view.findViewById(R.id.acnameChange2);
        acnameChange3 = view.findViewById(R.id.acnameChange3);

        accountDelete1 = view.findViewById(R.id.accountDelete1);
        accountDelete2 = view.findViewById(R.id.accountDelete2);
        accountDelete3 = view.findViewById(R.id.accountDelete3);
    }

    private void loadAccountData() {
        updateAccountSlot(1, accountTextLayout1, accountName1, accountPlaytime1, accountPercent1, acnameChange1, accountDelete1);
        updateAccountSlot(2, accountTextLayout2, accountName2, accountPlaytime2, accountPercent2, acnameChange2, accountDelete2);
        updateAccountSlot(3, accountTextLayout3, accountName3, accountPlaytime3, accountPercent3, acnameChange3, accountDelete3);

        // 현재 계정 정보 로드
        currentAccountName = sharedPreferences.getString("currentAccountName", "빈 슬롯");
        currentSlot = getCurrentSlotFromAccountName(currentAccountName);
        if (currentSlot != -1) {
            playtimeInSeconds = parsePlaytimeInSeconds(sharedPreferences.getString("accountPlaytime" + currentSlot, "00:00:00"));
        }
    }

    private void updateAccountSlot(int slot, LinearLayout textLayout, TextView nameView, TextView playtimeView, TextView percentView, ImageButton nameChangeButton, ImageButton deleteButton) {
        String name = sharedPreferences.getString("accountName" + slot, "빈 슬롯");
        String playtime = sharedPreferences.getString("accountPlaytime" + slot, "00:00:00");
        String percent = sharedPreferences.getString("accountPercent" + slot, "0.0");

        if ("빈 슬롯".equals(name)) {
            // 빈 슬롯일 경우 UI 설정
            updateLayoutParams(textLayout, 8); // layout_marginTop = 8dp
            nameView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            nameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            nameView.setText("빈 슬롯");
            // 플레이타임 및 퍼센트 텍스트 숨김
            playtimeView.setHeight(0);
            playtimeView.setText("");
            percentView.setText("");
            // 이름 변경 버튼 및 삭제 버튼 숨김
            nameChangeButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        } else {
            // 슬롯에 계정이 존재할 경우 UI 설정
            updateLayoutParams(textLayout, 0); // layout_marginTop = 0dp
            nameView.setHeight(dpToPx(22));
            nameView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nameView.setText(name);
            // 플레이타임 및 퍼센트 텍스트 복구
            playtimeView.setHeight(dpToPx(21));
            playtimeView.setText("플레이 시간 " + playtime);
            percentView.setText(percent + "%");
            // 이름 변경 버튼 및 삭제 버튼 표시
            nameChangeButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
        // 현재 계정 업데이트
        if (currentAccountName.equals("빈 슬롯") && !name.equals("빈 슬롯")) {
            currentAccountName = name;
            currentSlot = slot;
            sharedPreferences.edit().putString("currentAccountName", currentAccountName).apply();
            updateMainActivityAccountName();
        }
    }

    private void setSlotClickListeners() {
        accountSlot1.setOnClickListener(v -> handleSlotClick(1, accountTextLayout1, accountName1, accountPlaytime1, accountPercent1, acnameChange1, accountDelete1));
        accountSlot2.setOnClickListener(v -> handleSlotClick(2, accountTextLayout2, accountName2, accountPlaytime2, accountPercent2, acnameChange2, accountDelete2));
        accountSlot3.setOnClickListener(v -> handleSlotClick(3, accountTextLayout3, accountName3, accountPlaytime3, accountPercent3, acnameChange3, accountDelete3));

        acnameChange1.setOnClickListener(v -> showNameChangeDialog(1, accountTextLayout1, accountName1, accountPlaytime1, accountPercent1, acnameChange1, accountDelete1));
        acnameChange2.setOnClickListener(v -> showNameChangeDialog(2, accountTextLayout2, accountName2, accountPlaytime2, accountPercent2, acnameChange2, accountDelete2));
        acnameChange3.setOnClickListener(v -> showNameChangeDialog(3, accountTextLayout3, accountName3, accountPlaytime3, accountPercent3, acnameChange3, accountDelete3));

        accountDelete1.setOnClickListener(v -> deleteAccount(1, accountTextLayout1, accountName1, accountPlaytime1, accountPercent1, acnameChange1, accountDelete1));
        accountDelete2.setOnClickListener(v -> deleteAccount(2, accountTextLayout2, accountName2, accountPlaytime2, accountPercent2, acnameChange2, accountDelete2));
        accountDelete3.setOnClickListener(v -> deleteAccount(3, accountTextLayout3, accountName3, accountPlaytime3, accountPercent3, acnameChange3, accountDelete3));
    }

    // 계정 슬롯 선택
    private void handleSlotClick(int slot, LinearLayout textLayout, TextView nameView, TextView playtimeView, TextView percentView, ImageButton nameChangeButton, ImageButton deleteButton) {
        String slotAccountName = sharedPreferences.getString("accountName" + slot, "빈 슬롯");

        if ("빈 슬롯".equals(slotAccountName)) {
            // 빈 슬롯 -> 계정 생성
            showAccountCreationDialog(slot, textLayout, nameView, playtimeView, percentView, nameChangeButton, deleteButton);
        } else {
            // 이전 계정 상태 저장
            if (currentSlot != -1 && !currentAccountName.equals("빈 슬롯")) {
                saveCurrentSlotState();
            }
            // 새 계정으로 전환
            currentAccountName = slotAccountName;
            currentSlot = slot;
            playtimeInSeconds = parsePlaytimeInSeconds(sharedPreferences.getString("accountPlaytime" + slot, "00:00:00"));

            sharedPreferences.edit().putString("currentAccountName", currentAccountName).apply();
            // UI 업데이트
            loadAccountData();

            Toast.makeText(getContext(), "계정 전환: " + currentAccountName, Toast.LENGTH_SHORT).show();

            // MainActivity와 동기화
            updateMainActivityAccountName();

            restartPlaytimeUpdater();
        }
    }

    // 현재 슬롯(계정)의 플레이타임 저장
    private void saveCurrentSlotState() {
        String formattedTime = formatPlaytime(playtimeInSeconds);
        sharedPreferences.edit().putString("accountPlaytime" + currentSlot, formattedTime).apply();
    }

    // 핸들러 재설정
    private void restartPlaytimeUpdater() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.restartPlaytimeUpdaterForSlot(currentSlot, playtimeInSeconds);
        }
    }

    private int getCurrentSlotFromAccountName(String accountName) {
        for (int i = 1; i <= 3; i++) {
            if (accountName.equals(sharedPreferences.getString("accountName" + i, "빈 슬롯"))) {
                return i;
            }
        }
        return -1;
    }

    private long parsePlaytimeInSeconds(String playtime) {
        String[] parts = playtime.split(":");
        return Long.parseLong(parts[0]) * 3600 + Long.parseLong(parts[1]) * 60 + Long.parseLong(parts[2]);
    }

    private String formatPlaytime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    // 계정 생성
    private void showAccountCreationDialog(int slot, LinearLayout textLayout, TextView nameView, TextView playtimeView, TextView percentView, ImageButton nameChangeButton, ImageButton deleteButton) {
        EditText input = new EditText(getContext());
        input.setHint("계정명을 입력하세요");

        new AlertDialog.Builder(getContext())
                .setTitle("계정 생성")
                .setView(input)
                .setPositiveButton("확인", (dialog, which) -> {
                    String accountName = input.getText().toString().trim();
                    if (!accountName.isEmpty()) {
                        // 계정 데이터 초기화 및 저장
                        sharedPreferences.edit()
                                .putString("accountName" + slot, accountName)
                                .putString("accountPlaytime" + slot, "00:00:00")
                                .putString("accountPercent" + slot, "0.0")
                                .apply();

                        // 슬롯 UI 갱신
                        updateAccountSlot(slot, textLayout, nameView, playtimeView, percentView, nameChangeButton, deleteButton);

                        // 현재 계정이 없는 경우 새 계정을 현재 계정으로 설정
                        if (currentSlot == -1 || "빈 슬롯".equals(currentAccountName)) {
                            currentAccountName = accountName;
                            currentSlot = slot;
                            playtimeInSeconds = 0;
                            sharedPreferences.edit().putString("currentAccountName", currentAccountName).apply();
                            updateMainActivityAccountName();
                        }
                        Toast.makeText(getContext(), "계정이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    // 계정 이름 변경
    private void showNameChangeDialog(int slot, LinearLayout textLayout, TextView nameView, TextView playtimeView, TextView percentView, ImageButton nameChangeButton, ImageButton deleteButton) {
        EditText input = new EditText(getContext());
        input.setHint("새 계정명을 입력하세요");

        new AlertDialog.Builder(getContext())
                .setTitle("계정 이름 변경")
                .setView(input)
                .setPositiveButton("변경", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        String oldName = sharedPreferences.getString("accountName" + slot, "빈 슬롯");

                        // 변경된 경우에만 업데이트
                        if (!oldName.equals(newName)) {
                            sharedPreferences.edit()
                                    .putString("accountName" + slot, newName)
                                    .apply();

                            updateAccountSlot(slot, textLayout, nameView, playtimeView, percentView, nameChangeButton, deleteButton);

                            // 현재 계정의 이름이 변경된 경우 MainActivity도 갱신
                            if (currentSlot == slot) {
                                currentAccountName = newName;
                                sharedPreferences.edit()
                                        .putString("currentAccountName", currentAccountName)
                                        .apply();
                                updateMainActivityAccountName();
                            }

                            Toast.makeText(getContext(), "계정 이름이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "새 이름이 기존 이름과 같습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    // 계정 삭제
    private void deleteAccount(int slot, LinearLayout textLayout, TextView nameView, TextView playtimeView, TextView percentView, ImageButton nameChangeButton, ImageButton deleteButton) {
        new AlertDialog.Builder(getContext())
                .setTitle("계정 삭제")
                .setMessage("계정을 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> {
                    String deletedName = sharedPreferences.getString("accountName" + slot, "빈 슬롯");

                    // 삭제 처리
                    sharedPreferences.edit()
                            .remove("accountName" + slot)
                            .remove("accountPlaytime" + slot)
                            .remove("accountPercent" + slot)
                            .apply();

                    // UI 업데이트
                    updateAccountSlot(slot, textLayout, nameView, playtimeView, percentView, nameChangeButton, deleteButton);

                    // 현재 계정과 동일한 경우 "빈 슬롯"으로 전환
                    if (currentAccountName.equals(deletedName)) {
                        currentAccountName = "빈 슬롯";
                        currentSlot = -1; // 현재 선택된 슬롯 해제
                        playtimeInSeconds = 0; // 플레이타임 초기화
                        sharedPreferences.edit().putString("currentAccountName", currentAccountName).apply();
                        updateMainActivityAccountName();
                    }

                    Toast.makeText(getContext(), "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void updateLayoutParams(View view, int marginTopDp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = dpToPx(marginTopDp);
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // MainActivity의 계정 이름 업데이트
    private void updateMainActivityAccountName() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateAccountName(currentAccountName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateMainActivityAccountName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        updateMainActivityAccountName();
        // 현재 슬롯 상태 저장
        if (currentSlot != -1) {
            saveCurrentSlotState();
        }
    }
}
