package com.example.hjk;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class CharacterCardAdapter extends FragmentStateAdapter {

    private final List<List<Integer>> characterCards;

    public CharacterCardAdapter(@NonNull Fragment fragment, List<List<Integer>> characterCards) {
        super(fragment);
        this.characterCards = characterCards;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 캐릭터별 카드 슬라이드 프래그먼트 생성
        return CardSlideFragment.newInstance(characterCards.get(position));
    }

    @Override
    public int getItemCount() {
        return characterCards.size(); // 캐릭터 탭 수
    }
}
