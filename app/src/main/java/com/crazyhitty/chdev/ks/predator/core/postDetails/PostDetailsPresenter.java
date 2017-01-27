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

package com.crazyhitty.chdev.ks.predator.core.postDetails;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.Constants;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.ui.activities.MediaFullScreenActivity;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.UsersComparator;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostCommentsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostDetailsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.crazyhitty.chdev.ks.predator.data.Constants.Media.YOUTUBE_PATH;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 5:18 PM
 * Description: Unavailable
 */

public class PostDetailsPresenter implements PostDetailsContract.Presenter {
    private static final String TAG = "PostDetailsPresenter";
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
    private PostDetailsContract.View mView;
    private CompositeSubscription mCompositeSubscription;
    private Cursor mPostDetailsCursor;

    public PostDetailsPresenter(@NonNull PostDetailsContract.View view) {
        this.mView = view;
        mCompositeSubscription = new CompositeSubscription();
        mCustomTabsIntent = new CustomTabsIntent.Builder()
                .enableUrlBarHiding()
                .setShowTitle(true)
                .build();
    }

    @Override
    public void getDetails(final int postId) {
        Observable<Cursor> postDetailsObservable = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor cursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                PredatorContract.PostsEntry.COLUMN_POST_ID + "=" + postId,
                                null,
                                null);

                if (cursor != null && cursor.getCount() != 0) {
                    subscriber.onNext(cursor);
                } else {
                    subscriber.onError(new PostDetailsUnavailableException());
                }
                subscriber.onCompleted();
            }
        });

        postDetailsObservable.subscribeOn(Schedulers.io());
        postDetailsObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postDetailsObservable.subscribe(new Observer<Cursor>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                mView.unableToFetchPostDetails(e.getMessage());
            }

            @Override
            public void onNext(Cursor cursor) {
                mPostDetailsCursor = cursor;
                Logger.d(TAG, "onNext: " + mPostDetailsCursor.getCount());
                mPostDetailsCursor.moveToFirst();
                mView.showDetails(mPostDetailsCursor);
            }
        }));
    }

    @Override
    public void getUsers(final int postId) {
        Observable<List<User>> postUsersObservable = Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                Cursor cursorUsers = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                null,
                                PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS + " LIKE '%" + postId + "%' OR " + PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS + " LIKE '%" + postId + "%'",
                                null,
                                null);

                if (cursorUsers != null && cursorUsers.getCount() != 0) {
                    List<User> users = new ArrayList<User>();
                    for (int i = 0; i < cursorUsers.getCount(); i++) {
                        cursorUsers.moveToPosition(i);

                        User user = new User();
                        user.setId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_ID));
                        user.setUserId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_USER_ID));
                        user.setName(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_NAME));
                        user.setUsername(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_USERNAME));
                        user.setThumbnail(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
                        user.setImage(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

                        // Check if user is hunter, maker or both.
                        String hunterPostIds = CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
                        String makersPostIds = CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);

                        if (!TextUtils.isEmpty(hunterPostIds) &&
                                !TextUtils.isEmpty(makersPostIds) &&
                                hunterPostIds.contains(String.valueOf(postId)) &&
                                makersPostIds.contains(String.valueOf(postId))) {
                            // User is both hunter and maker.
                            user.setType(User.TYPE.BOTH);
                        } else if (!TextUtils.isEmpty(hunterPostIds) &&
                                hunterPostIds.contains(String.valueOf(postId))) {
                            // User is hunter.
                            user.setType(User.TYPE.HUNTER);
                        } else if (!TextUtils.isEmpty(makersPostIds) &&
                                makersPostIds.contains(String.valueOf(postId))) {
                            // User is maker.
                            user.setType(User.TYPE.MAKER);
                        }
                        users.add(user);
                    }
                    cursorUsers.close();

                    // Sort the users list on basis of user type.
                    Collections.sort(users, new UsersComparator());

                    subscriber.onNext(users);
                } else {
                    subscriber.onError(new UsersUnavailableException());
                }
                subscriber.onCompleted();
            }
        });

        postUsersObservable.subscribeOn(Schedulers.computation());
        postUsersObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postUsersObservable.subscribe(new Observer<List<User>>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                mView.unableToFetchUsers(e.getMessage());
            }

            @Override
            public void onNext(List<User> users) {
                mView.showUsers(users);
            }
        }));
    }

    @Override
    public void getExtraDetails(String token, final int postId) {
        Observable<PostDetailsDataType> postDetailsCursorTypeObservable = ProductHuntRestApi.getApi()
                .getPostDetails(CoreUtils.getAuthToken(token), postId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(new Func1<PostDetailsData, Observable<PostDetailsDataType>>() {
                    @Override
                    public Observable<PostDetailsDataType> call(final PostDetailsData postDetailsData) {
                        return Observable.create(new Observable.OnSubscribe<PostDetailsDataType>() {
                            @Override
                            public void call(Subscriber<? super PostDetailsDataType> subscriber) {
                                // Add users who upvoted this post to database.
                                MainApplication.getContentResolverInstance()
                                        .bulkInsert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                                getBulkContentValuesForUsers(postId, postDetailsData.getPost().getVotes()));

                                Cursor cursorUsers = MainApplication.getContentResolverInstance()
                                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                                null,
                                                PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS +
                                                        " LIKE '%" +
                                                        postId +
                                                        "%' OR " +
                                                        PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS +
                                                        " LIKE '%" +
                                                        postId +
                                                        "%' OR " +
                                                        PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS +
                                                        " LIKE '%" +
                                                        postId +
                                                        "%'",
                                                null,
                                                null);

                                if (cursorUsers != null && cursorUsers.getCount() != 0) {
                                    List<User> users = new ArrayList<User>();
                                    for (int i = 0; i < cursorUsers.getCount(); i++) {
                                        cursorUsers.moveToPosition(i);

                                        User user = new User();
                                        user.setId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_ID));
                                        user.setUserId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_USER_ID));
                                        user.setName(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_NAME));
                                        user.setUsername(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_USERNAME));
                                        user.setThumbnail(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
                                        user.setImage(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

                                        // Check if user is hunter, maker, both or a user who just
                                        // upvoted this post.
                                        String hunterPostIds = CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
                                        String makersPostIds = CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);

                                        if (!TextUtils.isEmpty(hunterPostIds) &&
                                                !TextUtils.isEmpty(makersPostIds) &&
                                                hunterPostIds.contains(String.valueOf(postId)) &&
                                                makersPostIds.contains(String.valueOf(postId))) {
                                            // User is both hunter and maker.
                                            user.setType(User.TYPE.BOTH);
                                        } else if (!TextUtils.isEmpty(hunterPostIds) &&
                                                hunterPostIds.contains(String.valueOf(postId))) {
                                            // User is hunter.
                                            user.setType(User.TYPE.HUNTER);
                                        } else if (!TextUtils.isEmpty(makersPostIds) &&
                                                makersPostIds.contains(String.valueOf(postId))) {
                                            // User is maker.
                                            user.setType(User.TYPE.MAKER);
                                        } else {
                                            // User upvoted this post.
                                            user.setType(User.TYPE.UPVOTER);
                                        }
                                        users.add(user);
                                    }
                                    cursorUsers.close();

                                    // Sort the users list on basis of user type.
                                    Collections.sort(users, new UsersComparator());

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS_VOTED);
                                    postDetailsDataType.setUsers(users);

                                    subscriber.onNext(postDetailsDataType);
                                } else {
                                    subscriber.onError(new VotedUsersUnavailableException());
                                }

                                // Clear media for that particular post.
                                MainApplication.getContentResolverInstance()
                                        .delete(PredatorContract.MediaEntry.CONTENT_URI_MEDIA_DELETE,
                                                PredatorContract.MediaEntry.COLUMN_POST_ID + " = " + postId,
                                                null);

                                // Add media to database.
                                MainApplication.getContentResolverInstance()
                                        .bulkInsert(PredatorContract.MediaEntry.CONTENT_URI_MEDIA_ADD,
                                                getBulkContentValuesForMedia(postId, postDetailsData.getPost().getMedia()));

                                // Query the media available.
                                Cursor mediaCursor = MainApplication.getContentResolverInstance()
                                        .query(PredatorContract.MediaEntry.CONTENT_URI_MEDIA,
                                                null,
                                                PredatorContract.MediaEntry.COLUMN_POST_ID + " = " + postId,
                                                null,
                                                null);

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

                                        media.add(mediaObj);
                                    }

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setMedia(media);
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.MEDIA);

                                    subscriber.onNext(postDetailsDataType);
                                    mediaCursor.close();
                                } else {
                                    subscriber.onError(new MediaUnavailableException());
                                }

                                // Clear comments for that particular post.
                                MainApplication.getContentResolverInstance()
                                        .delete(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS_DELETE,
                                                PredatorContract.CommentsEntry.COLUMN_POST_ID + " = " + postId,
                                                null);

                                // Add comments to database.
                                addCommentsToDatabase(postDetailsData.getPost().getComments());

                                // Query the comments available.
                                List<Comment> comments = getComments(postId, 0, new ArrayList<Comment>(), 0);
                                if (comments != null && comments.size() != 0) {
                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.COMMENTS);
                                    postDetailsDataType.setComments(comments);
                                    subscriber.onNext(postDetailsDataType);
                                } else {
                                    subscriber.onError(new CommentsUnavailableException());
                                }

                                // Clear install links for that particular post.
                                MainApplication.getContentResolverInstance()
                                        .delete(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS_DELETE,
                                                PredatorContract.InstallLinksEntry.COLUMN_POST_ID + " = " + postId,
                                                null);

                                // Add install links to database.
                                MainApplication.getContentResolverInstance()
                                        .bulkInsert(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS_ADD,
                                                getBulkContentValuesForInstallLinks(postDetailsData.getPost().getInstallLinks()));

                                // Query the install links available.
                                Cursor installLinksCursor = MainApplication.getContentResolverInstance()
                                        .query(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS,
                                                null,
                                                PredatorContract.InstallLinksEntry.COLUMN_POST_ID + " = " + postId,
                                                null,
                                                null);

                                if (installLinksCursor != null && installLinksCursor.getCount() != 0) {
                                    List<InstallLink> installLinks = new ArrayList<InstallLink>();

                                    for (int i = 0; i < installLinksCursor.getCount(); i++) {
                                        installLinksCursor.moveToPosition(i);

                                        InstallLink installLink = new InstallLink();
                                        installLink.setId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_ID));
                                        installLink.setInstallLinkId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID));
                                        installLink.setPostId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_POST_ID));
                                        installLink.setCreatedAt(CursorUtils.getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT));
                                        installLink.setPrimaryLink(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK) == 1);
                                        installLink.setRedirectUrl(CursorUtils.getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL));
                                        installLink.setPlatform(CursorUtils.getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_PLATFORM));

                                        installLinks.add(installLink);
                                    }

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setInstallLinks(installLinks);
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);

                                    subscriber.onNext(postDetailsDataType);
                                    installLinksCursor.close();
                                } else {
                                    subscriber.onError(new InstallLinksUnavailableException());
                                }

                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postDetailsCursorTypeObservable.subscribe(new Observer<PostDetailsDataType>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MediaUnavailableException) {
                    mView.unableToFetchMedia(e.getMessage());
                } else if (e instanceof CommentsUnavailableException) {
                    mView.unableToFetchComments(e.getMessage());
                } else if (e instanceof InstallLinksUnavailableException) {
                    mView.unableToFetchInstallLinks(e.getMessage());
                } else if (e instanceof VotedUsersUnavailableException) {
                    mView.unableToFetchAllUsers(e.getMessage());
                }
                mView.dismissLoading();
            }

            @Override
            public void onNext(PostDetailsDataType postDetailsDataType) {
                switch (postDetailsDataType.getType()) {
                    case MEDIA:
                        mView.showMedia(postDetailsDataType.getMedia());
                        break;
                    case COMMENTS:
                        mView.showComments(postDetailsDataType.getComments());
                        break;
                    case INSTALL_LINKS:
                        mView.attachInstallLinks(postDetailsDataType.getInstallLinks());
                        break;
                    case USERS_VOTED:
                        mView.showAllUsers(postDetailsDataType.getUsers());
                        break;
                }
                mView.dismissLoading();
            }
        }));
    }

    @Override
    public void getExtraDetailsOffline(final int postId) {
        Observable<PostDetailsDataType> postDetailsCursorTypeObservable = Observable.create(new Observable.OnSubscribe<PostDetailsDataType>() {
            @Override
            public void call(Subscriber<? super PostDetailsDataType> subscriber) {
                Cursor cursorUsers = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                null,
                                PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS +
                                        " LIKE '%" +
                                        postId +
                                        "%' OR " +
                                        PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS +
                                        " LIKE '%" +
                                        postId +
                                        "%' OR " +
                                        PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS +
                                        " LIKE '%" +
                                        postId +
                                        "%'",
                                null,
                                null);

                if (cursorUsers != null && cursorUsers.getCount() != 0) {
                    List<User> users = new ArrayList<User>();
                    for (int i = 0; i < cursorUsers.getCount(); i++) {
                        cursorUsers.moveToPosition(i);

                        User user = new User();
                        user.setId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_ID));
                        user.setUserId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_USER_ID));
                        user.setName(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_NAME));
                        user.setUsername(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_USERNAME));
                        user.setThumbnail(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
                        user.setImage(CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

                        // Check if user is hunter, maker, both or a user who just
                        // upvoted this post.
                        String hunterPostIds = CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
                        String makersPostIds = CursorUtils.getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);

                        if (!TextUtils.isEmpty(hunterPostIds) &&
                                !TextUtils.isEmpty(makersPostIds) &&
                                hunterPostIds.contains(String.valueOf(postId)) &&
                                makersPostIds.contains(String.valueOf(postId))) {
                            // User is both hunter and maker.
                            user.setType(User.TYPE.BOTH);
                        } else if (!TextUtils.isEmpty(hunterPostIds) &&
                                hunterPostIds.contains(String.valueOf(postId))) {
                            // User is hunter.
                            user.setType(User.TYPE.HUNTER);
                        } else if (!TextUtils.isEmpty(makersPostIds) &&
                                makersPostIds.contains(String.valueOf(postId))) {
                            // User is maker.
                            user.setType(User.TYPE.MAKER);
                        } else {
                            // User upvoted this post.
                            user.setType(User.TYPE.UPVOTER);
                        }
                        users.add(user);
                    }
                    cursorUsers.close();

                    // Sort the users list on basis of user type.
                    Collections.sort(users, new UsersComparator());

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS_VOTED);
                    postDetailsDataType.setUsers(users);

                    subscriber.onNext(postDetailsDataType);
                } else {
                    subscriber.onError(new VotedUsersUnavailableException());
                    return;
                }

                // Query the media available.
                Cursor mediaCursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.MediaEntry.CONTENT_URI_MEDIA,
                                null,
                                PredatorContract.MediaEntry.COLUMN_POST_ID + " = " + postId,
                                null,
                                null);

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

                        media.add(mediaObj);
                    }

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setMedia(media);
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.MEDIA);

                    subscriber.onNext(postDetailsDataType);
                    mediaCursor.close();
                } else {
                    subscriber.onError(new MediaUnavailableException());
                    return;
                }

                // Query the comments available.
                List<Comment> comments = getComments(postId, 0, new ArrayList<Comment>(), 0);
                if (comments != null && comments.size() != 0) {
                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.COMMENTS);
                    postDetailsDataType.setComments(comments);
                    subscriber.onNext(postDetailsDataType);
                } else {
                    subscriber.onError(new CommentsUnavailableException());
                    return;
                }

                // Query the install links available.
                Cursor installLinksCursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS,
                                null,
                                PredatorContract.InstallLinksEntry.COLUMN_POST_ID + " = " + postId,
                                null,
                                null);

                if (installLinksCursor != null && installLinksCursor.getCount() != 0) {
                    List<InstallLink> installLinks = new ArrayList<InstallLink>();

                    for (int i = 0; i < installLinksCursor.getCount(); i++) {
                        installLinksCursor.moveToPosition(i);

                        InstallLink installLink = new InstallLink();
                        installLink.setId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_ID));
                        installLink.setInstallLinkId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID));
                        installLink.setPostId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_POST_ID));
                        installLink.setCreatedAt(CursorUtils.getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT));
                        installLink.setPrimaryLink(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK) == 1);
                        installLink.setRedirectUrl(CursorUtils.getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL));
                        installLink.setPlatform(CursorUtils.getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_PLATFORM));

                        installLinks.add(installLink);
                    }

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setInstallLinks(installLinks);
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);

                    subscriber.onNext(postDetailsDataType);
                    installLinksCursor.close();
                } else {
                    subscriber.onError(new InstallLinksUnavailableException());
                }

                subscriber.onCompleted();
            }
        });
        postDetailsCursorTypeObservable.subscribeOn(Schedulers.computation());
        postDetailsCursorTypeObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeSubscription.add(postDetailsCursorTypeObservable.subscribe(new Observer<PostDetailsDataType>() {
            @Override
            public void onCompleted() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MediaUnavailableException) {
                    mView.unableToFetchMedia(e.getMessage());
                } else if (e instanceof CommentsUnavailableException) {
                    mView.unableToFetchComments(e.getMessage());
                } else if (e instanceof InstallLinksUnavailableException) {
                    mView.unableToFetchInstallLinks(e.getMessage());
                } else if (e instanceof VotedUsersUnavailableException) {
                    mView.unableToFetchAllUsers(e.getMessage());
                }
                mView.noOfflineDataAvailable();
                mView.dismissLoading();
            }

            @Override
            public void onNext(PostDetailsDataType postDetailsDataType) {
                switch (postDetailsDataType.getType()) {
                    case MEDIA:
                        mView.showMedia(postDetailsDataType.getMedia());
                        break;
                    case COMMENTS:
                        mView.showComments(postDetailsDataType.getComments());
                        break;
                    case INSTALL_LINKS:
                        mView.attachInstallLinks(postDetailsDataType.getInstallLinks());
                        break;
                    case USERS_VOTED:
                        mView.showAllUsers(postDetailsDataType.getUsers());
                        break;
                }
                mView.dismissLoading();
            }
        }));
    }

    @Override
    public void openRedirectUrl(Activity activity) {
        String redirectUrl = CursorUtils.getString(mPostDetailsCursor, PredatorContract.PostsEntry.COLUMN_REDIRECT_URL);

        CustomTabsHelperFragment.open(activity,
                mCustomTabsIntent,
                Uri.parse(redirectUrl),
                mCustomTabsFallback);
    }

    @Override
    public void sharePostDetails(Context context) {
        String title = CursorUtils.getString(mPostDetailsCursor, PredatorContract.PostsEntry.COLUMN_NAME);
        String body = CursorUtils.getString(mPostDetailsCursor, PredatorContract.PostsEntry.COLUMN_TAGLINE) + "\n" +
                CursorUtils.getString(mPostDetailsCursor, PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_using)));
    }

    @Override
    public void openMedia(Context context, Media media) {
        switch (media.getMediaType()) {
            case Constants.Media.IMAGE:
                MediaFullScreenActivity.startActivity(context,
                        media.getMediaId(),
                        media.getPostId());
                break;
            case Constants.Media.VIDEO:
                Intent videoIntent = new Intent();
                videoIntent.setAction(Intent.ACTION_VIEW);
                videoIntent.setData(Uri.parse(YOUTUBE_PATH.concat(media.getVideoId())));
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(videoIntent);
                break;
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeSubscription.clear();
        Logger.d(TAG, "unSubscribe: cursor closed");
        if (mPostDetailsCursor != null) {
            mPostDetailsCursor.close();
        }
    }

    private void addCommentsToDatabase(List<PostCommentsData.Comments> comments) {
        for (PostCommentsData.Comments comment : comments) {
            MainApplication.getContentResolverInstance()
                    .insert(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS_ADD, getContentValuesForComments(comment));
            addCommentsToDatabase(comment.getChildComments());
        }
    }

    private List<Comment> getComments(int postId, int parentCommentId, List<Comment> comments, int childSpaces) {
        String sortOrder = null;
        if (childSpaces == 0) {
            // If top level comments are being fetched, then sort them according to the vote count.
            sortOrder = PredatorContract.CommentsEntry.COLUMN_VOTES + " DESC";
        } else {
            // If child comment, then sort according the time of their creation.
            sortOrder = PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS + " ASC";
        }

        Cursor cursor = MainApplication.getContentResolverInstance()
                .query(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS,
                        null,
                        PredatorContract.CommentsEntry.COLUMN_POST_ID + " = " + postId + " AND " +
                                PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID + " = " + parentCommentId,
                        null,
                        sortOrder);
        if (cursor != null && cursor.getCount() != 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);

                Comment comment = new Comment();
                comment.setId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_ID));
                comment.setCommentId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_COMMENT_ID));
                comment.setParentCommentId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID));
                comment.setBody(CursorUtils.getString(cursor, PredatorContract.CommentsEntry.COLUMN_BODY));
                comment.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.CommentsEntry.COLUMN_CREATED_AT));
                comment.setCreatedAtMillis(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS));
                comment.setPostId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_POST_ID));
                comment.setUserId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_USER_ID));
                comment.setUsername(CursorUtils.getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_NAME));
                comment.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_USERNAME));
                comment.setUserHeadline(CursorUtils.getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_HEADLINE));
                comment.setUserImageThumbnailUrl(CursorUtils.getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_100PX));
                comment.setVotes(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_VOTES));
                comment.setSticky(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_STICKY) == 1);
                comment.setMaker(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_MAKER) == 1);
                comment.setHunter(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_HUNTER) == 1);
                comment.setLiveGuest(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_LIVE_GUEST) == 1);
                comment.setChildSpaces(childSpaces);
                comments.add(comment);

                getComments(postId, comment.getCommentId(), comments, comment.getChildSpaces() + 1);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return comments;
    }

    private ContentValues getContentValuesForComments(PostCommentsData.Comments comment) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_COMMENT_ID, comment.getId());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_BODY, comment.getBody());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_CREATED_AT, comment.getCreatedAt());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(comment.getCreatedAt()));
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID, comment.getParentCommentIdInteger());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_POST_ID, comment.getPostId());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_ID, comment.getUserId());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_CREATED_AT, comment.getUser().getCreatedAt());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_NAME, comment.getUser().getName());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_USERNAME, comment.getUser().getUsername());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_HEADLINE, comment.getUser().getHeadline());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_100PX, comment.getUser().getImageUrl().getValue100px());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, comment.getUser().getImageUrl().getOriginal());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_VOTES, comment.getVotes());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_STICKY, comment.isSticky() ? 1 : 0);
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_MAKER, comment.isMaker() ? 1 : 0);
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_HUNTER, comment.isHunter() ? 1 : 0);
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_LIVE_GUEST, comment.isLiveGuest() ? 1 : 0);
        return contentValues;
    }

    private ContentValues[] getBulkContentValuesForInstallLinks(List<PostDetailsData.PostDetails.InstallLinks> installLinks) {
        ContentValues[] contentValuesArr = new ContentValues[installLinks.size()];
        for (int i = 0; i < installLinks.size(); i++) {
            PostDetailsData.PostDetails.InstallLinks installLink = installLinks.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID, installLink.getId());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_POST_ID, installLink.getPostId());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT, installLink.getCreatedAt());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK, installLink.isPrimaryLink() ? 1 : 0);
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL, installLink.getRedirectUrl());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_PLATFORM, installLink.getPlatform());
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    private ContentValues[] getBulkContentValuesForMedia(int postId, List<PostDetailsData.PostDetails.Media> media) {
        ContentValues[] contentValuesArr = new ContentValues[media.size()];
        for (int i = 0; i < media.size(); i++) {
            PostDetailsData.PostDetails.Media mediaObj = media.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.MediaEntry.COLUMN_MEDIA_ID, mediaObj.getId());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_POST_ID, postId);
            contentValues.put(PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE, mediaObj.getMediaType());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_PLATFORM, mediaObj.getPlatform());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_VIDEO_ID, mediaObj.getVideoId());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_ORIGINAL_WIDTH, mediaObj.getOriginalWidth());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_ORIGINAL_HEIGHT, mediaObj.getOriginalHeight());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_IMAGE_URL, mediaObj.getImageUrl());
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    private ContentValues[] getBulkContentValuesForUsers(int postId, List<PostDetailsData.PostDetails.Votes> votes) {
        ContentValues[] contentValuesArr = new ContentValues[votes.size()];
        for (int i = 0; i < votes.size(); i++) {
            PostDetailsData.PostDetails.Votes vote = votes.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, vote.getUserId());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, vote.getUser().getCreatedAt());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, vote.getUser().getName());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, vote.getUser().getUsername());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, vote.getUser().getWebsiteUrl());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, vote.getUser().getImageUrl().getValue100px());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, vote.getUser().getImageUrl().getOriginal());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, postId);
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    public static class PostDetailsUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "Post details unavailable for the provided id.";
        }
    }

    public static class UsersUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "Users unavailable for the provided post.";
        }
    }

    public static class MediaUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "Media unavailable for the provided post.";
        }
    }

    public static class CommentsUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "Comments unavailable for the provided post.";
        }
    }

    public static class InstallLinksUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "Install links unavailable for the provided post.";
        }
    }

    public static class VotedUsersUnavailableException extends Throwable {
        @Override
        public String getMessage() {
            return "Voted users unavailable for the provided post.";
        }
    }

    private static class PostDetailsDataType {
        private TYPE type;
        private List<User> users;
        private List<Comment> comments;
        private List<Media> media;
        private List<InstallLink> installLinks;

        public TYPE getType() {
            return type;
        }

        public void setType(TYPE type) {
            this.type = type;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        public List<Media> getMedia() {
            return media;
        }

        public void setMedia(List<Media> media) {
            this.media = media;
        }

        public List<InstallLink> getInstallLinks() {
            return installLinks;
        }

        public void setInstallLinks(List<InstallLink> installLinks) {
            this.installLinks = installLinks;
        }

        enum TYPE {
            MEDIA,
            COMMENTS,
            INSTALL_LINKS,
            USERS_VOTED
        }
    }
}
