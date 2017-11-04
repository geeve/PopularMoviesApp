package com.example.android.popularmoviesapp;

import android.app.Activity;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.android.popularmoviesapp.data.MovieContract;
import com.example.android.popularmoviesapp.data.MoviePreferences;
import com.example.android.popularmoviesapp.sync.MovieSyncUtil;

/**
 * Created by Administrator on 2017/11/3 0003.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {

            preference.setSummary(stringValue);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Activity activity = getActivity();

        if (sharedPreferences.getInt(key,0) != MoviePreferences.PREF_ORDER_BY_FARI) {
            MovieSyncUtil.startImmediateSync(activity);
        } else{
            activity.getContentResolver().notifyChange(MovieContract.MovieEntry.CONTENT_URI, null);
        }
        Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences =getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();
        for (int i= 0;i<count;i++){
            Preference preference =preferenceScreen.getPreference(i);
            if(preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }
}
