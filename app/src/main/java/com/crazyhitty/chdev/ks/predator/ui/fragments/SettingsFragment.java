/*
 * MIT License
 *
 * Copyright (c) 2016 Kartik Sharma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.crazyhitty.chdev.ks.predator.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.settings.SettingsContract;
import com.crazyhitty.chdev.ks.predator.core.settings.SettingsPresenter;
import com.crazyhitty.chdev.ks.predator.data.PredatorSharedPreferences;
import com.crazyhitty.chdev.ks.predator.data.PredatorSyncAdapter;
import com.crazyhitty.chdev.ks.predator.ui.activities.SettingsActivity;
import com.crazyhitty.chdev.ks.predator.ui.preferences.PredatorDialogPreference;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/15/2017 8:17 PM
 * Description: Unavailable
 */

public class SettingsFragment extends PreferenceFragment implements SettingsContract.View {
    private ListPreference mListPreferenceManageThemes, mListPreferenceChangeFont;
    private PredatorDialogPreference mPredatorDialogPreferenceClearCache;
    private SwitchPreference mSwitchPreferenceEnableExperimentalFeatures,
            mSwitchPreferenceBackgroundSync,
            mSwitchPreferenceNotifications;
    private ListPreference mListPreferenceSyncInterval;

    private SettingsContract.Presenter mSettingsPresenter;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new SettingsPresenter(this));

        mSettingsPresenter.subscribe();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_preferences);

        bindPreferences();

        manageThemesPreferences();

        manageEnableExperimentalFeaturesPreferences();

        manageFontsPreferences();

        manageCachePreferences();

        manageBackgroundSyncPreferences();

        manageSyncIntervalPreferences();

        manageNotificationsPreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSettingsPresenter.unSubscribe();
    }

    private void bindPreferences() {
        mListPreferenceManageThemes = (ListPreference) findPreference(getString(R.string.settings_manage_themes_key));
        mPredatorDialogPreferenceClearCache = (PredatorDialogPreference) findPreference(getString(R.string.settings_clear_cache_key));
        mSwitchPreferenceEnableExperimentalFeatures = (SwitchPreference) findPreference(getString(R.string.settings_enable_experimental_features_key));
        mListPreferenceChangeFont = (ListPreference) findPreference(getString(R.string.settings_change_font_key));
        mSwitchPreferenceBackgroundSync = (SwitchPreference) findPreference(getString(R.string.settings_background_sync_key));
        mListPreferenceSyncInterval = (ListPreference) findPreference(getString(R.string.settings_sync_interval_key));
        mSwitchPreferenceNotifications = (SwitchPreference) findPreference(getString(R.string.settings_notifications_key));
    }

    private void manageThemesPreferences() {
        mListPreferenceManageThemes.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SettingsActivity.startActivity(getActivity(), true);
                return true;
            }
        });
    }

    private void manageEnableExperimentalFeaturesPreferences() {
        mSwitchPreferenceEnableExperimentalFeatures.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean status = (Boolean) newValue;
                mListPreferenceChangeFont.setEnabled(status);
                if (!status) {
                    PredatorSharedPreferences.restoreDefaultFont(getActivity().getApplicationContext());
                    MainApplication.reInitializeCalligraphy(getActivity().getApplicationContext(),
                            getString(R.string.settings_change_font_default_value));
                    SettingsActivity.startActivity(getActivity(), false);
                }
                return true;
            }
        });
    }

    private void manageFontsPreferences() {
        mListPreferenceChangeFont.setEnabled(PredatorSharedPreferences.isExperimentalFeaturesEnabled(getActivity().getApplicationContext()));

        mListPreferenceChangeFont.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MainApplication.reInitializeCalligraphy(getActivity().getApplicationContext(), (String)newValue);
                SettingsActivity.startActivity(getActivity(), true);
                return true;
            }
        });
    }

    private void manageCachePreferences() {
        mPredatorDialogPreferenceClearCache.setOnPositiveButtonClickListener(new PredatorDialogPreference.OnPositiveButtonClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                // Clear cache.
                mSettingsPresenter.clearCache();
            }
        });
    }

    private void manageBackgroundSyncPreferences() {
        mSwitchPreferenceBackgroundSync.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean status = (Boolean) newValue;
                mListPreferenceSyncInterval.setEnabled(status);
                mSwitchPreferenceNotifications.setEnabled(status);

                if (!status) {
                    mSwitchPreferenceNotifications.setChecked(false);
                }

                if (status) {
                    PredatorSyncAdapter.initializePeriodicSync(getActivity().getApplicationContext());
                } else {
                    PredatorSyncAdapter.removePeriodicSync(getActivity().getApplicationContext());
                }
                return true;
            }
        });
    }

    private void manageSyncIntervalPreferences() {
        mListPreferenceSyncInterval.setEnabled(PredatorSharedPreferences.isSyncEnabled(getActivity().getApplicationContext()));

        mListPreferenceSyncInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String hours = (String) newValue;
                PredatorSyncAdapter.initializePeriodicSync(getActivity().getApplicationContext(),
                        DateUtils.hoursToSeconds(hours));
                return true;
            }
        });
    }

    private void manageNotificationsPreferences() {
        mSwitchPreferenceNotifications.setEnabled(PredatorSharedPreferences.isSyncEnabled(getActivity().getApplicationContext()));
    }

    @Override
    public void cacheCleared() {
        Toast.makeText(getActivity().getApplicationContext(), R.string.settings_clear_cache_success_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void unableToWipeCache() {
        Toast.makeText(getActivity().getApplicationContext(), R.string.settings_clear_cache_failure_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mSettingsPresenter = presenter;
    }
}
