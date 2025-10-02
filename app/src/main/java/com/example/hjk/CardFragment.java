package com.example.hjk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CardImageAdapter adapter;

    // 캐릭터별 카드 데이터
    private final List<List<Integer>> characterCards = prepareCharacterCards();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // 탭 및 카드 설정
        setupTabsAndCards();

        return view;
    }

    private void setupTabsAndCards() {
        String[] tabTitles = {"빨강", "초록", "파랑", "보라"};
        int[] tabColors = {R.color.ironclad, R.color.silent, R.color.defect, R.color.watcher};

        // 초기 데이터 설정
        adapter = new CardImageAdapter(characterCards.get(0));
        viewPager.setAdapter(adapter);

        // 탭 추가
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitles[i]);
            tabLayout.addTab(tab);
        }

        // TabLayout과 ViewPager 연결
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                adapter.updateData(characterCards.get(position)); // 데이터 변경
                viewPager.setCurrentItem(0, false); // 첫 번째 카드로 초기화

                // TabLayout 배경 색 변경
                tabLayout.setBackgroundColor(getResources().getColor(tabColors[position]));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 초기 배경 색 설정
        tabLayout.setBackgroundColor(getResources().getColor(tabColors[0]));
    }



    private List<List<Integer>> prepareCharacterCards() {
        List<List<Integer>> characterCards = new ArrayList<>();

        // 빨강(아이언클래드) 카드 리스트
        List<Integer> ironcladCards = new ArrayList<>();
        ironcladCards.add(R.drawable.card_ironclad_strike);
        ironcladCards.add(R.drawable.card_ironclad_defend);
        ironcladCards.add(R.drawable.card_ironclad_bash);

        // 초록(사일런트) 카드 리스트
        List<Integer> silentCards = new ArrayList<>();
        silentCards.add(R.drawable.card_silent_strike);
        silentCards.add(R.drawable.card_silent_defend);
        silentCards.add(R.drawable.card_silent_neutralize);
        silentCards.add(R.drawable.card_silent_survivor);

        // 파랑(디펙트) 카드 리스트
        List<Integer> defectCards = new ArrayList<>();
        defectCards.add(R.drawable.card_defect_strike);
        defectCards.add(R.drawable.card_defect_defend);
        defectCards.add(R.drawable.card_defect_dualcast);
        defectCards.add(R.drawable.card_defect_zap);

        // 보라(와쳐) 카드 리스트
        List<Integer> watcherCards = new ArrayList<>();
        watcherCards.add(R.drawable.card_watcher_strike);
        watcherCards.add(R.drawable.card_watcher_defend);
        watcherCards.add(R.drawable.card_watcher_eruption);
        watcherCards.add(R.drawable.card_watcher_vigilance);

        characterCards.add(ironcladCards);
        characterCards.add(silentCards);
        characterCards.add(defectCards);
        characterCards.add(watcherCards);

        return characterCards;
    }
}
