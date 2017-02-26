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
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;

import static com.crazyhitty.chdev.ks.predator.utils.CursorUtils.getInt;
import static com.crazyhitty.chdev.ks.predator.utils.CursorUtils.getString;

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
    private CompositeDisposable mCompositeDisposable;
    private PostDetails mPostDetails;

    public PostDetailsPresenter(@NonNull PostDetailsContract.View view) {
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mCustomTabsIntent = new CustomTabsIntent.Builder()
                .enableUrlBarHiding()
                .setShowTitle(true)
                .build();
    }

    @Override
    public void getDetails(final int postId) {
        Observable<PostDetails> postDetailsObservable = Observable.create(new ObservableOnSubscribe<PostDetails>() {
            @Override
            public void subscribe(ObservableEmitter<PostDetails> emitter) throws Exception {
                Cursor cursor = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                PredatorContract.PostsEntry.COLUMN_POST_ID + "=" + postId,
                                null,
                                null);

                if (cursor != null && cursor.getCount() != 0) {
                    emitter.onNext(getPostDetailsFromCursor(cursor));
                } else {
                    emitter.onError(new PostDetailsUnavailableException());
                }
                emitter.onComplete();
            }
        });

        postDetailsObservable.subscribeOn(Schedulers.io());
        postDetailsObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postDetailsObservable.subscribeWith(new DisposableObserver<PostDetails>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                mView.unableToFetchPostDetails(e.getMessage());
            }

            @Override
            public void onNext(PostDetails postDetails) {
                Logger.d(TAG, "onNext: postDetails: " + postDetails.toString());
                mPostDetails = postDetails;
                mView.showDetails(mPostDetails);
            }
        }));
    }

    @Override
    public void getUsers(final int postId) {
        Observable<List<User>> postUsersObservable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                Cursor cursorUsers = MainApplication.getContentResolverInstance()
                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                null,
                                PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS + " LIKE '%" + postId + "%' OR " + PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS + " LIKE '%" + postId + "%'",
                                null,
                                null);

                if (cursorUsers != null && cursorUsers.getCount() != 0) {
                    List<User> users = getUsersFromCursor(cursorUsers, postId);

                    // Sort the users list on basis of user type.
                    Collections.sort(users, new UsersComparator());

                    emitter.onNext(users);
                } else {
                    emitter.onError(new UsersUnavailableException());
                }
                emitter.onComplete();
            }
        });

        postUsersObservable.subscribeOn(Schedulers.computation());
        postUsersObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postUsersObservable.subscribeWith(new DisposableObserver<List<User>>() {
            @Override
            public void onComplete() {
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
                .flatMap(new Function<PostDetailsData, ObservableSource<PostDetailsDataType>>() {
                    @Override
                    public ObservableSource<PostDetailsDataType> apply(final PostDetailsData postDetailsData) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<PostDetailsDataType>() {
                            @Override
                            public void subscribe(ObservableEmitter<PostDetailsDataType> emitter) throws Exception {
                                // Add users who upvoted this post to database.
                                MainApplication.getContentResolverInstance()
                                        .bulkInsert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                                getBulkContentValuesForUsers(postId, postDetailsData.getPost().getVotes()));

                                Cursor usersCursor = MainApplication.getContentResolverInstance()
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

                                if (usersCursor != null && usersCursor.getCount() != 0) {
                                    List<User> users = getUsersFromCursor(usersCursor, postId);

                                    // Sort the users list on basis of user type.
                                    Collections.sort(users, new UsersComparator());

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS_VOTED);
                                    postDetailsDataType.setUsers(users);

                                    emitter.onNext(postDetailsDataType);
                                } else {
                                    emitter.onError(new VotedUsersUnavailableException());
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
                                    List<Media> media = getMediaFromCursor(mediaCursor);

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setMedia(media);
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.MEDIA);

                                    emitter.onNext(postDetailsDataType);
                                    mediaCursor.close();
                                } else {
                                    emitter.onError(new MediaUnavailableException());
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
                                    emitter.onNext(postDetailsDataType);
                                } else {
                                    emitter.onError(new CommentsUnavailableException());
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
                                    List<InstallLink> installLinks = getInstallLinksFromCursor(installLinksCursor);

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setInstallLinks(installLinks);
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);

                                    emitter.onNext(postDetailsDataType);
                                    installLinksCursor.close();
                                } else {
                                    emitter.onError(new InstallLinksUnavailableException());
                                }

                                emitter.onComplete();
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postDetailsCursorTypeObservable.subscribeWith(new DisposableObserver<PostDetailsDataType>() {
            @Override
            public void onComplete() {
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
        Observable<PostDetailsDataType> postDetailsCursorTypeObservable = Observable.create(new ObservableOnSubscribe<PostDetailsDataType>() {
            @Override
            public void subscribe(ObservableEmitter<PostDetailsDataType> emitter) throws Exception {
                Cursor usersCursor = MainApplication.getContentResolverInstance()
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

                if (usersCursor != null && usersCursor.getCount() != 0) {
                    List<User> users = getUsersFromCursor(usersCursor, postId);

                    // Sort the users list on basis of user type.
                    Collections.sort(users, new UsersComparator());

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS_VOTED);
                    postDetailsDataType.setUsers(users);

                    emitter.onNext(postDetailsDataType);
                } else {
                    emitter.onError(new VotedUsersUnavailableException());
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
                    List<Media> media = getMediaFromCursor(mediaCursor);

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setMedia(media);
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.MEDIA);

                    emitter.onNext(postDetailsDataType);
                    mediaCursor.close();
                } else {
                    emitter.onError(new MediaUnavailableException());
                    return;
                }

                // Query the comments available.
                List<Comment> comments = getComments(postId, 0, new ArrayList<Comment>(), 0);
                if (comments != null && comments.size() != 0) {
                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.COMMENTS);
                    postDetailsDataType.setComments(comments);
                    emitter.onNext(postDetailsDataType);
                } else {
                    emitter.onError(new CommentsUnavailableException());
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
                    List<InstallLink> installLinks = getInstallLinksFromCursor(installLinksCursor);

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setInstallLinks(installLinks);
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);

                    emitter.onNext(postDetailsDataType);
                    installLinksCursor.close();
                } else {
                    emitter.onError(new InstallLinksUnavailableException());
                }

                emitter.onComplete();
            }
        });
        postDetailsCursorTypeObservable.subscribeOn(Schedulers.computation());
        postDetailsCursorTypeObservable.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(postDetailsCursorTypeObservable.subscribeWith(new DisposableObserver<PostDetailsDataType>() {
            @Override
            public void onComplete() {
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
        if (mPostDetails == null ||
                TextUtils.isEmpty(mPostDetails.getRedirectUrl())) {
            Toast.makeText(activity.getApplicationContext(), R.string.post_details_no_redirect_url_available, Toast.LENGTH_SHORT).show();
            return;
        }
        String redirectUrl = mPostDetails.getRedirectUrl();

        CustomTabsHelperFragment.open(activity,
                mCustomTabsIntent,
                Uri.parse(redirectUrl),
                mCustomTabsFallback);
    }

    @Override
    public PostDetails getPostDetails() {
        return mPostDetails;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    private PostDetails getPostDetailsFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        String title = getString(cursor, PredatorContract.PostsEntry.COLUMN_NAME);
        String description = getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE);
        String day = getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY);
        String date = getString(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT);
        String backdropUrl = getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL);
        String redirectUrl = getString(cursor, PredatorContract.PostsEntry.COLUMN_REDIRECT_URL);
        String tagline = getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE);
        String discussionUrl = getString(cursor, PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL);
        String category = getCategoryName(getInt(cursor, PredatorContract.PostsEntry.COLUMN_CATEGORY_ID));
        int voteCount = getInt(cursor, PredatorContract.PostsEntry.COLUMN_VOTES_COUNT);

        PostDetails postDetails = new PostDetails();
        postDetails.setTitle(title);
        postDetails.setDescription(description);
        postDetails.setDay(day);
        postDetails.setDate(date);
        postDetails.setBackdropUrl(backdropUrl);
        postDetails.setRedirectUrl(redirectUrl);
        postDetails.setTagline(tagline);
        postDetails.setDiscussionUrl(discussionUrl);
        postDetails.setCategory(category);
        postDetails.setVoteCount(voteCount);

        cursor.close();

        return postDetails;
    }

    private List<User> getUsersFromCursor(Cursor cursorUsers, int postId) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < cursorUsers.getCount(); i++) {
            cursorUsers.moveToPosition(i);

            User user = new User();
            user.setId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_ID));
            user.setUserId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_USER_ID));
            user.setName(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_NAME));
            user.setUsername(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_USERNAME));
            user.setThumbnail(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
            user.setImage(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

            // Check if user is hunter, maker, both or a user who just
            // upvoted this post.
            String hunterPostIds = getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
            String makersPostIds = getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);

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

        return users;
    }

    private List<Media> getMediaFromCursor(Cursor mediaCursor) {
        List<Media> media = new ArrayList<Media>();

        for (int i = 0; i < mediaCursor.getCount(); i++) {
            mediaCursor.moveToPosition(i);

            Media mediaObj = new Media();
            mediaObj.setId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ID));
            mediaObj.setMediaId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_MEDIA_ID));
            mediaObj.setPostId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_POST_ID));
            mediaObj.setMediaType(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE));
            mediaObj.setPlatform(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_PLATFORM));
            mediaObj.setVideoId(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_VIDEO_ID));
            mediaObj.setOriginalWidth(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ORIGINAL_WIDTH));
            mediaObj.setOriginalHeight(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ORIGINAL_HEIGHT));
            mediaObj.setImageUrl(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_IMAGE_URL));

            media.add(mediaObj);
        }

        return media;
    }

    private List<InstallLink> getInstallLinksFromCursor(Cursor installLinksCursor) {
        List<InstallLink> installLinks = new ArrayList<InstallLink>();

        for (int i = 0; i < installLinksCursor.getCount(); i++) {
            installLinksCursor.moveToPosition(i);

            InstallLink installLink = new InstallLink();
            installLink.setId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_ID));
            installLink.setInstallLinkId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID));
            installLink.setPostId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_POST_ID));
            installLink.setCreatedAt(getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT));
            installLink.setPrimaryLink(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK) == 1);
            installLink.setRedirectUrl(getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL));
            installLink.setPlatform(getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_PLATFORM));

            installLinks.add(installLink);
        }

        return installLinks;
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
                comment.setBody(getString(cursor, PredatorContract.CommentsEntry.COLUMN_BODY));
                comment.setCreatedAt(getString(cursor, PredatorContract.CommentsEntry.COLUMN_CREATED_AT));
                comment.setCreatedAtMillis(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS));
                comment.setPostId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_POST_ID));
                comment.setUserId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_USER_ID));
                comment.setUsername(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_NAME));
                comment.setUsernameAlternative(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_USERNAME));
                comment.setUserHeadline(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_HEADLINE));
                comment.setUserImageThumbnailUrl(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_100PX));
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
            contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, vote.getUser().getHeadline());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, vote.getUser().getWebsiteUrl());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, vote.getUser().getImageUrl().getValue100px());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, vote.getUser().getImageUrl().getOriginal());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, postId);
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    private String getCategoryName(int categoryId) {
        Cursor cursor = MainApplication.getContentResolverInstance()
                .query(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY,
                        null,
                        PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID + " = " + categoryId,
                        null,
                        null);

        cursor.moveToFirst();

        String categoryName = getString(cursor, PredatorContract.CategoryEntry.COLUMN_NAME);

        cursor.close();

        return categoryName;
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
