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

package com.crazyhitty.chdev.ks.predator.core.about;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.models.About;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

import org.chromium.customtabsclient.CustomTabsActivityHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/25/2017 11:54 AM
 * Description: Unavailable
 */

public class AboutPresenter implements AboutContract.Presenter {
    private static final String TAG = "AboutPresenter";
    private final CustomTabsIntent mCustomTabsIntent;
    private final CustomTabsActivityHelper.CustomTabsFallback mCustomTabsFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, R.string.no_application_available_to_open_this_url, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            };
    @NonNull
    private AboutContract.View mView;
    private CompositeSubscription mCompositeSubscription;
    private CustomTabsHelperFragment mCustomTabsHelperFragment;

    public AboutPresenter(@NonNull AboutContract.View view) {
        mView = view;
        mCompositeSubscription = new CompositeSubscription();
        mCustomTabsIntent = new CustomTabsIntent.Builder()
                .enableUrlBarHiding()
                .setShowTitle(true)
                .build();
    }

    @Override
    public void subscribe() {
        prepareChromeCustomTabs(Constants.About.URL_GITHUB);
    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }

    @Override
    public void initChromeCustomTabs(Fragment fragment) {
        mCustomTabsHelperFragment = CustomTabsHelperFragment.attachTo(fragment);
    }

    @Override
    public void initChromeCustomTabs(FragmentActivity fragmentActivity) {
        mCustomTabsHelperFragment = CustomTabsHelperFragment.attachTo(fragmentActivity);
    }

    @Override
    public void fetchAboutData(final Context context) {
        Observable<List<About>> aboutObservable = Observable.create(new Observable.OnSubscribe<List<About>>() {
            @Override
            public void call(Subscriber<? super List<About>> subscriber) {
                List<About> about = new ArrayList<About>();

                About titleDeveloper = new About();
                titleDeveloper.setType(About.TYPE.TITLE);
                titleDeveloper.setTitle(context.getString(R.string.activity_about_title_developer));
                about.add(titleDeveloper);

                About developer = new About();
                developer.setUsername(context.getString(R.string.activity_about_developer_username));
                developer.setUserType(context.getString(R.string.activity_about_developer_type));
                developer.setType(About.TYPE.DEVELOPER);
                about.add(developer);

                About titleSpecialThanks = new About();
                titleSpecialThanks.setTitle(context.getString(R.string.activity_about_special_thanks_title));
                titleSpecialThanks.setType(About.TYPE.TITLE);
                about.add(titleSpecialThanks);

                About specialThanks = new About();
                specialThanks.setSpecialThanks(context.getString(R.string.activity_about_special_thanks_desc));
                specialThanks.setType(About.TYPE.SPECIAL_THANKS);
                about.add(specialThanks);

                About titleLibrary = new About();
                titleLibrary.setTitle(context.getString(R.string.activity_about_library_title));
                titleLibrary.setType(About.TYPE.TITLE);
                about.add(titleLibrary);

                // Add libraries.
                // Fetch libraries from libraries.json located in res/raw.
                InputStream inputStream = context.getResources().openRawResource(R.raw.libraries);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String librariesJsonStr = "";
                try {
                    String value;
                    while ((value = reader.readLine()) != null) {
                        librariesJsonStr = librariesJsonStr.concat(value);
                    }
                    reader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Convert librariesJsonStr to jsonObject for further parsing.
                try {
                    JSONObject jsonObjectLibraries = new JSONObject(librariesJsonStr);
                    JSONArray jsonArrayLibraries = jsonObjectLibraries.getJSONArray(Constants.Library.LIBRARIES);
                    List<About> aboutLibraries = new ArrayList<About>();
                    for (int i = 0; i < jsonArrayLibraries.length(); i++) {
                        JSONObject jsonObjectLibrary = jsonArrayLibraries.getJSONObject(i);

                        String libraryTitle = jsonObjectLibrary.getString(Constants.Library.LIBRARY_TITLE);
                        String libraryCreator = jsonObjectLibrary.getString(Constants.Library.LIBRARY_CREATOR);
                        String libraryLicenseType = jsonObjectLibrary.getString(Constants.Library.LIBRARY_LICENSE_TYPE);
                        String libraryRedirectUrl = jsonObjectLibrary.getString(Constants.Library.LIBRARY_REDIRECT_URL);

                        About library = new About();
                        library.setLibraryTitle(libraryTitle);
                        library.setLibraryCreator(libraryCreator);
                        library.setLibraryLicenseType(libraryLicenseType);
                        library.setLibraryRedirectUrl(libraryRedirectUrl);
                        library.setType(About.TYPE.LIBRARY);
                        aboutLibraries.add(library);
                    }
                    about.addAll(aboutLibraries);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                subscriber.onNext(about);
                subscriber.onCompleted();
            }
        });
        aboutObservable.subscribeOn(Schedulers.io());
        aboutObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(aboutObservable.subscribe(new Observer<List<About>>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.unableToShowAboutData(e.getMessage());
            }

            @Override
            public void onNext(List<About> about) {
                mView.showAboutData(about);
                for (About aboutObj : about) {
                    if (!TextUtils.isEmpty(aboutObj.getLibraryRedirectUrl())) {
                        prepareChromeCustomTabs(aboutObj.getLibraryRedirectUrl());
                    }
                }
            }
        }));
    }

    @Override
    public void openGithub(Activity activity) {
        CustomTabsHelperFragment.open(activity,
                mCustomTabsIntent,
                Uri.parse(Constants.About.URL_GITHUB),
                mCustomTabsFallback);
    }

    @Override
    public void openGooglePlus(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.About.URL_GOOGLE_PLUS));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void openMail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", Constants.About.MAIL_ID, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.activity_about_mail_subject));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.activity_about_mail_share_intent_title)));
    }

    @Override
    public void openLibraryRedirectUrl(Activity activity, String redirectUrl) {
        CustomTabsHelperFragment.open(activity,
                mCustomTabsIntent,
                Uri.parse(redirectUrl),
                mCustomTabsFallback);
    }

    private void prepareChromeCustomTabs(String url) {
        final Uri uri = Uri.parse(url);
        mCustomTabsHelperFragment.setConnectionCallback(
                new CustomTabsActivityHelper.ConnectionCallback() {
                    @Override
                    public void onCustomTabsConnected() {
                        mCustomTabsHelperFragment.mayLaunchUrl(uri,
                                null,
                                null);
                    }

                    @Override
                    public void onCustomTabsDisconnected() {
                    }
                });
    }
}
