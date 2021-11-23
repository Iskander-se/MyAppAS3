package com.example.myappas3.ui.main;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.myappas3.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}