package com.example.hjk;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton accountButton;
    public ImageButton backButton;
    private TextView accountName, accountText;
    public TextView backButtonText;
    private Button playButton, encycButton, statsButton, settingButton;
    private ImageView gameLogo;

    private boolean isFragmentOpen = false;

    // 계정 관련
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable playtimeUpdater;
    private SharedPreferences sharedPreferences;
    private int currentSlot = -1; // 현재 선택된 슬롯
    private long playtimeInSeconds = 0; // 현재 계정의 플레이타임

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("AccountData", MODE_PRIVATE);

        // 현재 계정 정보 로드
        String currentAccountName = sharedPreferences.getString("currentAccountName", "빈 슬롯");
        currentSlot = getCurrentAccountSlot(currentAccountName);

        if (currentSlot != -1) {
            playtimeInSeconds = parsePlaytimeInSeconds(sharedPreferences.getString("accountPlaytime" + currentSlot, "00:00:00"));
        }
        startPlaytimeUpdater();

        initializeViews();

        setButtonListeners();
    }

    private void initializeViews() {
        accountButton = findViewById(R.id.accountButton);
        accountName = findViewById(R.id.accountName);
        accountText = findViewById(R.id.accountText);
        playButton = findViewById(R.id.playButton);
        encycButton = findViewById(R.id.encycButton);
        statsButton = findViewById(R.id.statsButton);
        settingButton = findViewById(R.id.settingButton);
        backButton = findViewById(R.id.backButton);
        backButtonText = findViewById(R.id.backButtonText);
        gameLogo = findViewById(R.id.logo);

        // 초기 계정 이름 로드
        SharedPreferences sharedPreferences = getSharedPreferences("AccountData", MODE_PRIVATE);
        String currentAccountName = sharedPreferences.getString("currentAccountName", "빈 슬롯");
        accountName.setText(currentAccountName);

        accountText.setText("수정하시려면 탭하세요");
        playButton.setText("게임 시작");
        encycButton.setText("백과사전");
        statsButton.setText("통계");
        settingButton.setText("설정");

        backButton.setVisibility(View.GONE);
        backButtonText.setVisibility(View.GONE);
        gameLogo.setAlpha(1.0f);

    }

    private void setButtonListeners() {
        accountButton.setOnClickListener(v -> openFragment(new AccountFragment()));     // 계정 관리 화면
        accountName.setOnClickListener(v -> openFragment(new AccountFragment()));
        accountText.setOnClickListener(v -> openFragment(new AccountFragment()));

        playButton.setOnClickListener(v -> openFragment(new CharacterSelectFragment()));// 캐릭터 선택 화면

        encycButton.setOnClickListener(v -> openFragment(new EncyclopediaFragment()));  // 백과사전 화면

        statsButton.setOnClickListener(v -> openFragment(new StatisticsFragment()));    // 통계 화면

        settingButton.setOnClickListener(v -> openFragment(new SettingFragment()));     // 설정 화면

        backButton.setOnClickListener(v -> closeFragment()); // 이전 버튼
        backButtonText.setOnClickListener(v -> closeFragment());
    }

    // 프래그먼트 열기
    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        // 버튼 상태 업데이트
        toggleButtons(false);

        // 프래그먼트 열기 시 계정 이름 유지
        SharedPreferences sharedPreferences = getSharedPreferences("AccountData", MODE_PRIVATE);
        String currentAccountName = sharedPreferences.getString("currentAccountName", "빈 슬롯");
        updateAccountName(currentAccountName);

        accountButton.setClickable(false);
        accountName.setClickable(false);
        accountText.setClickable(false);
        backButton.setVisibility(View.VISIBLE);
        backButtonText.setVisibility(View.VISIBLE);
        isFragmentOpen = true;

        gameLogo.setAlpha(0.3f);
    }

    // 프래그먼트 닫기
    public void closeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (isFragmentOpen) {
            fragmentManager.popBackStack();

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            // 프래그먼트 닫기 시 계정 이름 동기화
            SharedPreferences sharedPreferences = getSharedPreferences("AccountData", MODE_PRIVATE);
            String currentAccountName = sharedPreferences.getString("currentAccountName", "빈 슬롯");
            updateAccountName(currentAccountName);

            toggleButtons(true);
            accountButton.setClickable(true);
            accountName.setClickable(true);
            accountText.setClickable(true);
            backButton.setVisibility(View.GONE);
            backButtonText.setVisibility(View.GONE);
            isFragmentOpen = false;

            gameLogo.animate().alpha(1.0f).setDuration(400).start();
        }
    }

    // 버튼 상태 토글
    private void toggleButtons(boolean showMainButtons) {
        int visibility = showMainButtons ? View.VISIBLE : View.GONE;
        playButton.setVisibility(visibility);
        encycButton.setVisibility(visibility);
        statsButton.setVisibility(visibility);
        settingButton.setVisibility(visibility);
    }

    // 계정 이름 갱신
    public void updateAccountName(String newAccountName) {
        accountName.setText(newAccountName);
    }

    // AccountFragment에서 호출해 플레이 타임과 진행 퍼센트 가져오기
    public String getCurrentPlaytime() {
        return sharedPreferences.getString("accountPlaytime" + currentSlot, "00:00:00");
    }

    public String getCurrentPercent() {
        return sharedPreferences.getString("accountPercent" + currentSlot, "0.0");
    }

    // 계정 플레이 타임
    private void startPlaytimeUpdater() {
        playtimeUpdater = new Runnable() {
            @Override
            public void run() {
                if (currentSlot != -1) {
                    // 현재 슬롯의 플레이타임 업데이트
                    playtimeInSeconds++;
                    sharedPreferences.edit().putString("accountPlaytime" + currentSlot, formatPlaytime(playtimeInSeconds)).apply();
                    // 진행 퍼센트 증가 (6분마다 0.1%)
                    if (playtimeInSeconds % 360 == 0) {
                        float currentPercent = Float.parseFloat(sharedPreferences.getString("accountPercent" + currentSlot, "0.0"));
                        currentPercent += 0.1f;
                        sharedPreferences.edit().putString("accountPercent" + currentSlot, String.format(Locale.US, "%.1f", currentPercent)).apply();
                    }
                }
                // 1초마다 반복 실행
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(playtimeUpdater);
    }

    // 현재 슬롯 정보를 기반으로 핸들러 재설정
    public void restartPlaytimeUpdaterForSlot(int newSlot, long initialPlaytime) {
        // 기존 핸들러 콜백 제거
        if (handler != null) {
            handler.removeCallbacks(playtimeUpdater);
        }

        // 새 슬롯 정보로 업데이트
        currentSlot = newSlot;
        playtimeInSeconds = initialPlaytime;

        // 핸들러 재설정
        startPlaytimeUpdater();
    }

    private int getCurrentAccountSlot(String accountName) {
        for (int i = 1; i <= 3; i++) {
            String name = sharedPreferences.getString("accountName" + i, "빈 슬롯");
            if (name.equals(accountName)) {
                return i;
            }
        }
        return -1;
    }

    private long parsePlaytimeInSeconds(String playtime) {
        String[] parts = playtime.split(":");
        if (parts.length == 3) {
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            return hours * 3600 + minutes * 60 + seconds;
        }
        return 0;
    }

    private String formatPlaytime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 핸들러의 콜백 제거
        if (handler != null) {
            handler.removeCallbacks(playtimeUpdater);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("AccountData", MODE_PRIVATE);
        String currentAccountName = sharedPreferences.getString("currentAccountName", "빈 슬롯");
        accountName.setText(currentAccountName);
        // 핸들러 재개
        startPlaytimeUpdater();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 현재 상태 저장
        if (currentSlot != -1) {
            sharedPreferences.edit().putString("accountPlaytime" + currentSlot, formatPlaytime(playtimeInSeconds)).apply();
        }
        if (handler != null) {
            handler.removeCallbacks(playtimeUpdater);
        }
    }

    @Override
    public void onBackPressed() {
        if (isFragmentOpen) {
            closeFragment();
        } else {
            super.onBackPressed();
        }
    }
}