package com.example.hjk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

public class SettingFragment extends Fragment {

    private SeekBar seekBarVolume1, seekBarVolume2, seekBarVolume3;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8;
    private Spinner spinnerLanguage;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        initializeUI(view);

        loadSettings();

        setupListeners();

        return view;
    }

    private void initializeUI(View view) {
        seekBarVolume1 = view.findViewById(R.id.seekBarVolume1);
        seekBarVolume2 = view.findViewById(R.id.seekBarVolume2);
        seekBarVolume3 = view.findViewById(R.id.seekBarVolume3);

        checkBox1 = view.findViewById(R.id.checkBox1);
        checkBox2 = view.findViewById(R.id.checkBox2);
        checkBox3 = view.findViewById(R.id.checkBox3);
        checkBox4 = view.findViewById(R.id.checkBox4);
        checkBox5 = view.findViewById(R.id.checkBox5);
        checkBox6 = view.findViewById(R.id.checkBox6);
        checkBox7 = view.findViewById(R.id.checkBox7);
        checkBox8 = view.findViewById(R.id.checkBox8);

        spinnerLanguage = view.findViewById(R.id.spinnerLanguage);
    }

    private void loadSettings() {
        seekBarVolume1.setProgress(sharedPreferences.getInt("volume1", 100));
        seekBarVolume2.setProgress(sharedPreferences.getInt("volume2", 100));
        seekBarVolume3.setProgress(sharedPreferences.getInt("volume3", 100));

        checkBox1.setChecked(sharedPreferences.getBoolean("checkbox1", false));
        checkBox2.setChecked(sharedPreferences.getBoolean("checkbox2", false));
        checkBox3.setChecked(sharedPreferences.getBoolean("checkbox3", false));
        checkBox4.setChecked(sharedPreferences.getBoolean("checkbox4", false));
        checkBox5.setChecked(sharedPreferences.getBoolean("checkbox5", false));
        checkBox6.setChecked(sharedPreferences.getBoolean("checkbox6", false));
        checkBox7.setChecked(sharedPreferences.getBoolean("checkbox7", false));
        checkBox8.setChecked(sharedPreferences.getBoolean("checkbox8", false));

        spinnerLanguage.setSelection(sharedPreferences.getInt("language_position", 0));
    }

    private void setupListeners() {
        setSeekBarListener(seekBarVolume1, "volume1");
        setSeekBarListener(seekBarVolume2, "volume2");
        setSeekBarListener(seekBarVolume3, "volume3");

        setCheckBoxListener(checkBox1, "checkbox1");
        setCheckBoxListener(checkBox2, "checkbox2");
        setCheckBoxListener(checkBox3, "checkbox3");
        setCheckBoxListener(checkBox4, "checkbox4");
        setCheckBoxListener(checkBox5, "checkbox5");
        setCheckBoxListener(checkBox6, "checkbox6");
        setCheckBoxListener(checkBox7, "checkbox7");
        setCheckBoxListener(checkBox8, "checkbox8");

        spinnerLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putInt("language_position", position).apply();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void setSeekBarListener(SeekBar seekBar, String key) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharedPreferences.edit().putInt(key, progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setCheckBoxListener(CheckBox checkBox, String key) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(key, isChecked).apply();
        });
    }
}
