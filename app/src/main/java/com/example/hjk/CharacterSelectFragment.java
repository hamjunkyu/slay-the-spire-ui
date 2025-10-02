package com.example.hjk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class CharacterSelectFragment extends Fragment {

    private String selectedCharacter = null;

    ConstraintLayout fragmentLayout;
    LinearLayout ascensionLayout;
    private ImageButton startGameButton;
    private TextView startGameText;

    TextView characterName, characterHP, characterGold, characterInfo, artifactName, artifactInfo;
    ImageView hpIcon, goldIcon, artifactImage;

    TextView selectCharacterText;
    ImageButton ironcladIcon, silentIcon, defectIcon, watcherIcon;

    TextView ascensionLevelText, ascensionDescription;
    Button ascensionLeftArrow, ascensionRightArrow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_select, container, false);

        fragmentLayout = view.findViewById(R.id.characterSelectLayout);
        ascensionLayout = view.findViewById(R.id.ascensionLayout);
        startGameButton = view.findViewById(R.id.startGameButton);
        startGameText = view.findViewById(R.id.startGameText);

        selectCharacterText = view.findViewById(R.id.selectCharacterText);
        ironcladIcon = view.findViewById(R.id.characterIronclad);
        silentIcon = view.findViewById(R.id.characterSilent);
        defectIcon = view.findViewById(R.id.characterDefect);
        watcherIcon = view.findViewById(R.id.characterWatcher);

        // 캐릭터 이름 및 정보 텍스트
        characterName = view.findViewById(R.id.characterName);
        hpIcon = view.findViewById(R.id.hpIcon);
        characterHP = view.findViewById(R.id.characterHP);
        goldIcon = view.findViewById(R.id.goldIcon);
        characterGold = view.findViewById(R.id.characterGold);
        characterInfo = view.findViewById(R.id.characterInfo);
        artifactImage = view.findViewById(R.id.artifactImage);
        artifactName = view.findViewById(R.id.artifactName);
        artifactInfo = view.findViewById(R.id.artifactInfo);

        ascensionLevelText = view.findViewById(R.id.ascensionLevelText);
        ascensionDescription = view.findViewById(R.id.ascensionDescription);
        ascensionLeftArrow = view.findViewById(R.id.ascensionLeftArrow);
        ascensionRightArrow = view.findViewById(R.id.ascensionRightArrow);

        hpIcon.setVisibility(View.GONE);
        goldIcon.setVisibility(View.GONE);

        ironcladIcon.setOnClickListener(v -> {
            updateUI(fragmentLayout,"아이언클래드", R.drawable.ironcladimage, 1);
        });
        silentIcon.setOnClickListener(v -> {
            updateUI(fragmentLayout,"사일런트", R.drawable.silentimage, 2);
        });
        defectIcon.setOnClickListener(v -> {
            updateUI(fragmentLayout,"디펙트", R.drawable.defectimage, 3);
        });
        watcherIcon.setOnClickListener(v -> {
            updateUI(fragmentLayout,"와쳐", R.drawable.watcherimage, 4);
        });

        ascensionLayout.setVisibility(View.GONE);
        // 기본 난이도
        final int[] ascensionLevel = {1}; // Mutable로 사용

        ascensionLeftArrow.setText("<");
        // 화살표 클릭 이벤트
        ascensionLeftArrow.setOnClickListener(v -> {
            if (ascensionLevel[0] > 1) {
                ascensionLevel[0]--;
                updateAscension(ascensionLevelText, ascensionDescription, ascensionLevel[0]);
            }
        });

        ascensionRightArrow.setText(">");
        ascensionRightArrow.setOnClickListener(v -> {
            if (ascensionLevel[0] < 20) { // 최대 20단계
                ascensionLevel[0]++;
                updateAscension(ascensionLevelText, ascensionDescription, ascensionLevel[0]);
            }
        });

        startGameButton.setVisibility(View.GONE);
        startGameButton.setOnClickListener(v -> startGame());
        startGameText.setVisibility(View.GONE);
        startGameText.setOnClickListener(v -> startGame());

        return view;
    }

    private void updateUI(ConstraintLayout layout,
                          String character, int backgroundResId, int ascensionLevel) {
        selectedCharacter = character;
        layout.setBackgroundResource(backgroundResId);

        // 캐릭터 선택 문구 숨기기
        selectCharacterText.setVisibility(View.GONE);

        // 캐릭터 정보 표시
        updateCharacterInfo(character);

        // 승천 레이아웃 보이기
        ascensionLayout.setVisibility(View.VISIBLE);
        updateAscensionDescription(ascensionDescription, 1);

        // 출정 버튼 보이기
        startGameButton.setVisibility(View.VISIBLE);
        startGameText.setVisibility(View.VISIBLE);
    }

    // 승천 단계 업데이트
    private void updateAscension(TextView levelText, TextView descriptionText, int level) {
        levelText.setText("단계" + String.valueOf(level));

        // 난이도 설명 업데이트
        updateAscensionDescription(descriptionText, level);
    }

    private void startGame() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.closeFragment();
            mainActivity.openFragment(new GameFragment());
            mainActivity.backButton.setVisibility(View.GONE);
            mainActivity.backButtonText.setVisibility(View.GONE);
        }
    }

    private void updateCharacterInfo(String character) {
        characterName.setText(character);
        hpIcon.setVisibility(View.VISIBLE);
        goldIcon.setVisibility(View.VISIBLE);
        characterGold.setText("골드: 99");
        if (character.equals("아이언클래드")) {       // 아이언클래드
            characterHP.setText("체력: 80/80");
            characterInfo.setText("아이언클래드의 살아남은 병사입니다.\n악마의 힘을 사용하기 위해 영혼을 팔았습니다.");
            artifactImage.setImageResource(R.drawable.ironcladartifact2);
            artifactName.setText("불타는 혈액");
            artifactInfo.setText("전투 종료 시 체력을 6 회복합니다.");
        } else if (character.equals("사일런트")) {       // 사일런트
            characterHP.setText("체력: 70/70");
            characterInfo.setText("안개 지대에서 온 치명적인 사냥꾼입니다.\n단검과 독으로 적들을 박멸합니다.");
            artifactImage.setImageResource(R.drawable.silentartifact2);
            artifactName.setText("뱀의 반지");
            artifactInfo.setText("첫 턴에만 2장의 카드를 추가로 뽑습니다.");
        } else if (character.equals("디펙트")) {       // 디펙트
            characterHP.setText("체력: 75/75");
            characterInfo.setText("자아를 깨달은 전투 자동인형입니다.\n고대의 기술로 구체를 만들 수 있습니다.");
            artifactImage.setImageResource(R.drawable.defectartifact2);
            artifactName.setText("부서진 핵");
            artifactInfo.setText("전투 시작 시 전기를 1번 영창합니다.");
        } else {                                      // 와쳐
            characterHP.setText("체력: 72/72");
            characterInfo.setText("첨탑을 \"평가\"하기 위해 찾아온 눈먼 수도사입니다.\n강림의 경지에 이른 고수입니다.");
            artifactImage.setImageResource(R.drawable.watcherartifact2);
            artifactName.setText("순수한 물");
            artifactInfo.setText("매 전투 시작시 기적을 손으로 가져옵니다.");
        }
    }

    private void updateAscensionDescription(TextView descriptionText, int level) {
        switch (level) {
            case 1:
                descriptionText.setText("엘리트가 더 많이 발생합니다.");
                break;
            case 2:
                descriptionText.setText("일반적인 적들이 더 많은 피해를 줍니다.");
                break;
            case 3:
                descriptionText.setText("엘리트들이 더 많은 피해를 줍니다.");
                break;
            case 4:
                descriptionText.setText("보스들이 더 많은 피해를 줍니다.");
                break;
            case 5:
                descriptionText.setText("보스 전투 이후에 회복량이 감소합니다.");
                break;
            case 6:
                descriptionText.setText("피해를 입은 채로 시작합니다.");
                break;
            case 7:
                descriptionText.setText("일반적인 적들이 더 많은 체력을 가집니다.");
                break;
            case 8:
                descriptionText.setText("엘리트들이 더 많은 체력을 가집니다.");
                break;
            case 9:
                descriptionText.setText("보스들이 더 많은 체력을 가집니다.");
                break;
            case 10:
                descriptionText.setText("저주받은 상태로 시작합니다");
                break;
            case 11:
                descriptionText.setText("포션 슬롯이 줄어듭니다");
                break;
            case 12:
                descriptionText.setText("강화 상태의 카드가 더 적게 나타납니다.");
                break;
            case 13:
                descriptionText.setText("보스가 가난해집니다.");
                break;
            case 14:
                descriptionText.setText("최대 체력이 감소합니다.");
                break;
            case 15:
                descriptionText.setText("이벤트가 부정적으로 변합니다.");
                break;
            case 16:
                descriptionText.setText("상점을 이용하기 위한 비용이 증가합니다.");
                break;
            case 17:
                descriptionText.setText("일반적인 적들이 더 난이도 있는 행동 패턴과 능력치를 지닙니다.");
                break;
            case 18:
                descriptionText.setText("엘리트들이 더 난이도 있는 행동 패턴과 능력치를 지닙니다.");
                break;
            case 19:
                descriptionText.setText("보스들이 더 난이도 있는 행동 패턴과 능력치를 지닙니다.");
                break;
            case 20:
                descriptionText.setText("(최고 단계) 두 마리의 마지막 보스.");
                break;
            default:
                descriptionText.setText("승천 단계: " + level);
                break;
        }
    }
}
