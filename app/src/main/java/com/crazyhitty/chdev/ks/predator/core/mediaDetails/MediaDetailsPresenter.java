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

package com.crazyhitty.chdev.ks.predator.core.mediaDetails;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.core.postDetails.PostDetailsPresenter;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/23/2017 6:38 PM
 * Description: Unavailable
 */

public class MediaDetailsPresenter implements MediaDetailsContract.Presenter {
    @NonNull
    private MediaDetailsContract.View mView;

    private CompositeSubscription mCompositeSubscription;

    public MediaDetailsPresenter(@NonNull MediaDetailsContract.View view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getMedia(final int postId, final int mediaId) {
        Observable<MediaData> mediaObservable = Observable.create(new Observable.OnSubscribe<MediaData>() {
            @Override
            public void call(Subscriber<? super MediaData> subscriber) {
                // Fetch media from database.
                Cursor mediaCursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.MediaEntry.CONTENT_URI_MEDIA,
                                null,
                                PredatorContract.MediaEntry.COLUMN_POST_ID + "=" + postId + " AND " +
                                        PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE + "='" + Constants.Media.IMAGE + "'",
                                null,
                                null);

                int defaultPosition = 0;

                if (mediaCursor != null && mediaCursor.getCount() != 0) {
                    List<Media> media = new ArrayList<Media>();
                    for (int i = 0; i < mediaCursor.getCount(); i++) {
                        mediaCursor.moveToPosition(i);

                        Media mediaObj = new Media();
                        mediaObj.setId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ID));
                        mediaObj.setMediaId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_MEDIA_ID));
                        mediaObj.setPostId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_POST_ID));
                        mediaObj.setMediaType(CursorUtils.getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE));
                        mediaObj.setPlatform(CursorUtils.getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_PLATFORM));
                        mediaObj.setVideoId(CursorUtils.getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_VIDEO_ID));
                        mediaObj.setOriginalWidth(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ORIGINAL_WIDTH));
                        mediaObj.setOriginalHeight(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ORIGINAL_HEIGHT));
                        mediaObj.setImageUrl(CursorUtils.getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_IMAGE_URL));

                        if (mediaId == mediaObj.getMediaId()) {
                            defaultPosition = i;
                        }

                        media.add(mediaObj);
                    }
                    mediaCursor.close();
                    subscriber.onNext(new MediaData(media, defaultPosition));
                } else {
                    subscriber.onError(new PostDetailsPresenter.MediaUnavailableException());
                }
                subscriber.onCompleted();
            }
        });

        mediaObservable.subscribeOn(Schedulers.io());
        mediaObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(mediaObservable.subscribe(new Observer<MediaData>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                mView.unableToFetchMedia(e.getMessage());
            }

            @Override
            public void onNext(MediaData mediaData) {
                mView.showMedia(mediaData.getMedia(), mediaData.getDefaultPosition());
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
    }

    private static class MediaData {
        private List<Media> media;
        private int defaultPosition;

        public MediaData() {
        }

        public MediaData(List<Media> media, int defaultPosition) {
            this.media = media;
            this.defaultPosition = defaultPosition;
        }

        public List<Media> getMedia() {
            return media;
        }

        public void setMedia(List<Media> media) {
            this.media = media;
        }

        public int getDefaultPosition() {
            return defaultPosition;
        }

        public void setDefaultPosition(int defaultPosition) {
            this.defaultPosition = defaultPosition;
        }
    }
}
