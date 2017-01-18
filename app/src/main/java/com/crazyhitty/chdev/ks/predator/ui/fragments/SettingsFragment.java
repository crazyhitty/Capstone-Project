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
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.core.settings.SettingsContract;
import com.crazyhitty.chdev.ks.predator.core.settings.SettingsPresenter;
import com.crazyhitty.chdev.ks.predator.ui.preferences.PredatorDialogPreference;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/15/2017 8:17 PM
 * Description: Unavailable
 */

public class SettingsFragment extends PreferenceFragment implements SettingsContract.View {
    private PredatorDialogPreference mPredatorDialogPreferenceClearCache;

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

        manageCachePreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSettingsPresenter.unSubscribe();
    }

    private void bindPreferences() {
        mPredatorDialogPreferenceClearCache = (PredatorDialogPreference) findPreference(getString(R.string.settings_clear_cache_key));
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
