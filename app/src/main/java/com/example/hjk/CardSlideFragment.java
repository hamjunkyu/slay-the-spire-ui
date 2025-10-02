package com.example.hjk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class CardSlideFragment extends Fragment {

    private static final String ARG_CARDS = "cards";
    private List<Integer> cardImages;

    public static CardSlideFragment newInstance(List<Integer> cardImages) {
        CardSlideFragment fragment = new CardSlideFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_CARDS, new ArrayList<>(cardImages));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardImages = getArguments().getIntegerArrayList(ARG_CARDS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_slide, container, false);

        ViewPager2 cardPager = view.findViewById(R.id.cardPager);
        cardPager.setAdapter(new CardImageAdapter(cardImages));

        return view;
    }
}
