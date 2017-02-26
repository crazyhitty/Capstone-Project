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

package com.crazyhitty.chdev.ks.predator.core.userProfile;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.data.PredatorContract;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.UserProfileData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import java.util.ArrayList;
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

import static com.crazyhitty.chdev.ks.predator.MainApplication.getContentResolverInstance;
import static com.crazyhitty.chdev.ks.predator.utils.CursorUtils.getString;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/17/2017 3:39 PM
 * Description: Unavailable
 */

public class UserProfilePresenter implements UserProfileContract.Presenter {
    private static final String TAG = "UserProfilePresenter";

    @NonNull
    private UserProfileContract.View mView;

    private CompositeDisposable mCompositeDisposable;

    public UserProfilePresenter(@NonNull UserProfileContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getOfflineData(final int userId) {
        mView.offlineLoadingStart();
        Observable<UserProfileDataType> observableProfile = Observable.create(new ObservableOnSubscribe<UserProfileDataType>() {
            @Override
            public void subscribe(ObservableEmitter<UserProfileDataType> emitter) throws Exception {
                // Fetch current user.
                Cursor cursorCurrentUser = getContentResolverInstance()
                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                null,
                                PredatorContract.UsersEntry.COLUMN_USER_ID + "=" + userId,
                                null,
                                null);
                List<User> currentUser = new ArrayList<User>();
                currentUser.add(getCurrentUserFromCursor(cursorCurrentUser));

                UserProfileDataType currentUserData = new UserProfileDataType();
                currentUserData.setUsers(currentUser);
                currentUserData.setUserType(UserProfileContract.USER_TYPE.CURRENT);
                emitter.onNext(currentUserData);

                // Fetch the voted posts of this user.
                Cursor cursorVotedPosts = getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                PredatorContract.PostsEntry.COLUMN_POST_ID + " in (" + currentUser.get(0).getVotedPostIdsQuery() + ")",
                                null,
                                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
                if (cursorVotedPosts != null && cursorVotedPosts.getCount() != 0) {
                    List<Post> votedPosts = getPostsFromCursor(cursorVotedPosts);
                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                    currentUserPostsData.setPosts(votedPosts);
                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.UPVOTES);
                    emitter.onNext(currentUserPostsData);
                }

                // Fetch the submitted posts of this user.
                Cursor cursorSubmittedPosts = getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                PredatorContract.PostsEntry.COLUMN_POST_ID + " in (" + currentUser.get(0).getHuntedPostIdsQuery() + ")",
                                null,
                                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
                if (cursorSubmittedPosts != null && cursorSubmittedPosts.getCount() != 0) {
                    List<Post> submittedPosts = getPostsFromCursor(cursorSubmittedPosts);
                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                    currentUserPostsData.setPosts(submittedPosts);
                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.SUBMITTED);
                    emitter.onNext(currentUserPostsData);
                }

                // Fetch the posts which this user made (maker).
                Cursor cursorMadePosts = getContentResolverInstance()
                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                null,
                                PredatorContract.PostsEntry.COLUMN_POST_ID + " in (" + currentUser.get(0).getMadePostIdsQuery() + ")",
                                null,
                                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
                if (cursorMadePosts != null && cursorMadePosts.getCount() != 0) {
                    List<Post> madePosts = getPostsFromCursor(cursorMadePosts);
                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                    currentUserPostsData.setPosts(madePosts);
                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.MADE);
                    emitter.onNext(currentUserPostsData);
                }

                // Fetch followers users.
                Cursor cursorFollowers = getContentResolverInstance()
                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                null,
                                PredatorContract.UsersEntry.COLUMN_USER_ID + " in (" + currentUser.get(0).getFollowerUserIdsQuery() + ")",
                                null,
                                null);
                if (cursorFollowers != null && cursorFollowers.getCount() != 0) {
                    List<User> followerUsers = getUsersFromCursor(cursorFollowers);
                    UserProfileDataType followerUsersData = new UserProfileDataType();
                    followerUsersData.setUsers(followerUsers);
                    followerUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWERS);
                    emitter.onNext(followerUsersData);
                }

                // Fetch following users.
                Cursor cursorFollowing = getContentResolverInstance()
                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                null,
                                PredatorContract.UsersEntry.COLUMN_USER_ID + " in (" + currentUser.get(0).getFollowingUserIdsQuery() + ")",
                                null,
                                null);
                if (cursorFollowing != null && cursorFollowing.getCount() != 0) {
                    List<User> followingUsers = getUsersFromCursor(cursorFollowing);
                    UserProfileDataType followingUsersData = new UserProfileDataType();
                    followingUsersData.setUsers(followingUsers);
                    followingUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWING);
                    emitter.onNext(followingUsersData);
                }

                emitter.onComplete();
            }
        });

        observableProfile.subscribeOn(Schedulers.io());
        observableProfile.observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(observableProfile.subscribeWith(new DisposableObserver<UserProfileDataType>() {
            @Override
            public void onNext(UserProfileDataType userProfileDataType) {
                Logger.d(TAG, "offline: onNext: datatype fetched: user: " + userProfileDataType.getUserType() + " ; post: " + userProfileDataType.getPostType() + " ; unavailable: " + userProfileDataType.isUnAvailable());
                if (userProfileDataType.getUserType() != null) {
                    switch (userProfileDataType.getUserType()) {
                        case CURRENT:
                            if (userProfileDataType.isUnAvailable()) {
                                mView.unableToFetchUserDetails();
                            } else {
                                mView.showUserDetails(userProfileDataType.getUsers().get(0));
                            }
                            break;
                        case FOLLOWERS:
                            if (userProfileDataType.isUnAvailable()) {
                                mView.unableToFetchUsers(userProfileDataType.getUserType());
                            } else {
                                mView.showUsers(userProfileDataType.getUserType(), userProfileDataType.getUsers(), false);
                            }
                            break;
                        case FOLLOWING:
                            if (userProfileDataType.isUnAvailable()) {
                                mView.unableToFetchUsers(userProfileDataType.getUserType());
                            } else {
                                mView.showUsers(userProfileDataType.getUserType(), userProfileDataType.getUsers(), false);
                            }
                            break;
                    }
                } else if (userProfileDataType.getPostType() != null) {
                    if (userProfileDataType.isUnAvailable()) {
                        mView.unableToFetchPosts(userProfileDataType.getPostType());
                    } else {
                        mView.showPosts(userProfileDataType.getPostType(), userProfileDataType.getPosts(), false);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "offline: onError: " + e.getMessage(), e);
                mView.offlineLoadingComplete();
            }

            @Override
            public void onComplete() {
                // Done
                Logger.d(TAG, "offline: onComplete: fetched user details");
                mView.offlineLoadingComplete();
            }
        }));
    }

    @Override
    public void getLatestData(String token, final int userId, final boolean refresh) {
        mView.onlineLoadingStart();
        Observable<UserProfileDataType> observableProfile = ProductHuntRestApi.getApi()
                .getUserProfile(CoreUtils.getAuthToken(token), userId)
                .flatMap(new Function<UserProfileData, ObservableSource<UserProfileDataType>>() {
                    @Override
                    public ObservableSource<UserProfileDataType> apply(final UserProfileData userProfileData) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<UserProfileDataType>() {
                            @Override
                            public void subscribe(ObservableEmitter<UserProfileDataType> emitter) throws Exception {
                                // Save current user in db.
                                getContentResolverInstance()
                                        .insert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                                getContentValuesForCurrentUser(userProfileData));

                                // Save other users in db (followers and followings).
                                getContentResolverInstance()
                                        .bulkInsert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                                getBulkContentValuesForFollowingFollowerUsers(userProfileData));

                                // Save the posts in db.
                                getContentResolverInstance()
                                        .bulkInsert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD,
                                                getBulkContentValuesForPosts(userProfileData));

                                // Save the users associated with above posts.
                                getContentResolverInstance()
                                        .bulkInsert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD,
                                                getBulkContentValuesForPostUsers(userProfileData));

                                // Fetch current user.
                                Cursor cursorCurrentUser = getContentResolverInstance()
                                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                                null,
                                                PredatorContract.UsersEntry.COLUMN_USER_ID + "=" + userId,
                                                null,
                                                null);
                                List<User> currentUser = new ArrayList<User>();
                                currentUser.add(getCurrentUserFromCursor(cursorCurrentUser));

                                UserProfileDataType currentUserData = new UserProfileDataType();
                                currentUserData.setUsers(currentUser);
                                currentUserData.setUserType(UserProfileContract.USER_TYPE.CURRENT);
                                emitter.onNext(currentUserData);

                                // Fetch the voted posts of this user.
                                Cursor cursorVotedPosts = getContentResolverInstance()
                                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                                null,
                                                PredatorContract.PostsEntry.COLUMN_POST_ID + " in (" + currentUser.get(0).getVotedPostIdsQuery() + ")",
                                                null,
                                                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
                                if (cursorVotedPosts != null && cursorVotedPosts.getCount() != 0) {
                                    List<Post> votedPosts = getPostsFromCursor(cursorVotedPosts);
                                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                                    currentUserPostsData.setPosts(votedPosts);
                                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.UPVOTES);
                                    emitter.onNext(currentUserPostsData);
                                } else {
                                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                                    currentUserPostsData.setUnAvailable(true);
                                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.UPVOTES);
                                    emitter.onNext(currentUserPostsData);
                                }

                                // Fetch the submitted posts of this user.
                                Cursor cursorSubmittedPosts = getContentResolverInstance()
                                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                                null,
                                                PredatorContract.PostsEntry.COLUMN_POST_ID + " in (" + currentUser.get(0).getHuntedPostIdsQuery() + ")",
                                                null,
                                                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
                                if (cursorSubmittedPosts != null && cursorSubmittedPosts.getCount() != 0) {
                                    List<Post> submittedPosts = getPostsFromCursor(cursorSubmittedPosts);
                                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                                    currentUserPostsData.setPosts(submittedPosts);
                                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.SUBMITTED);
                                    emitter.onNext(currentUserPostsData);
                                } else {
                                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                                    currentUserPostsData.setUnAvailable(true);
                                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.SUBMITTED);
                                    emitter.onNext(currentUserPostsData);
                                }

                                // Fetch the posts which this user made (maker).
                                Cursor cursorMadePosts = getContentResolverInstance()
                                        .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                                                null,
                                                PredatorContract.PostsEntry.COLUMN_POST_ID + " in (" + currentUser.get(0).getMadePostIdsQuery() + ")",
                                                null,
                                                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
                                if (cursorMadePosts != null && cursorMadePosts.getCount() != 0) {
                                    List<Post> madePosts = getPostsFromCursor(cursorMadePosts);
                                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                                    currentUserPostsData.setPosts(madePosts);
                                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.MADE);
                                    emitter.onNext(currentUserPostsData);
                                } else {
                                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                                    currentUserPostsData.setUnAvailable(true);
                                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.MADE);
                                    emitter.onNext(currentUserPostsData);
                                }

                                // Fetch followers users.
                                Cursor cursorFollowers = getContentResolverInstance()
                                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                                null,
                                                PredatorContract.UsersEntry.COLUMN_USER_ID + " in (" + currentUser.get(0).getFollowerUserIdsQuery() + ")",
                                                null,
                                                null);
                                if (cursorFollowers != null && cursorFollowers.getCount() != 0) {
                                    List<User> followerUsers = getUsersFromCursor(cursorFollowers);
                                    UserProfileDataType followerUsersData = new UserProfileDataType();
                                    followerUsersData.setUsers(followerUsers);
                                    followerUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWERS);
                                    emitter.onNext(followerUsersData);
                                } else {
                                    UserProfileDataType followerUsersData = new UserProfileDataType();
                                    followerUsersData.setUnAvailable(true);
                                    followerUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWERS);
                                    emitter.onNext(followerUsersData);
                                }

                                // Fetch following users.
                                Cursor cursorFollowing = getContentResolverInstance()
                                        .query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                                                null,
                                                PredatorContract.UsersEntry.COLUMN_USER_ID + " in (" + currentUser.get(0).getFollowingUserIdsQuery() + ")",
                                                null,
                                                null);
                                if (cursorFollowing != null && cursorFollowing.getCount() != 0) {
                                    List<User> followingUsers = getUsersFromCursor(cursorFollowing);
                                    UserProfileDataType followingUsersData = new UserProfileDataType();
                                    followingUsersData.setUsers(followingUsers);
                                    followingUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWING);
                                    emitter.onNext(followingUsersData);
                                } else {
                                    UserProfileDataType followingUsersData = new UserProfileDataType();
                                    followingUsersData.setUnAvailable(true);
                                    followingUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWING);
                                    emitter.onNext(followingUsersData);
                                }

                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(observableProfile.subscribeWith(new DisposableObserver<UserProfileDataType>() {
            @Override
            public void onNext(UserProfileDataType userProfileDataType) {
                Logger.d(TAG, "onNext: datatype fetched ; Thread: " + Thread.currentThread().getName());
                Logger.d(TAG, "onNext: datatype fetched: user: " + userProfileDataType.getUserType() + " ; post: " + userProfileDataType.getPostType() + " ; unavailable: " + userProfileDataType.isUnAvailable());
                if (userProfileDataType.getUserType() != null) {
                    switch (userProfileDataType.getUserType()) {
                        case CURRENT:
                            if (userProfileDataType.isUnAvailable()) {
                                mView.unableToFetchUserDetails();
                            } else {
                                mView.showUserDetails(userProfileDataType.getUsers().get(0));
                            }
                            break;
                        case FOLLOWERS:
                            if (userProfileDataType.isUnAvailable()) {
                                mView.unableToFetchUsers(userProfileDataType.getUserType());
                            } else {
                                mView.showUsers(userProfileDataType.getUserType(), userProfileDataType.getUsers(), refresh);
                            }
                            break;
                        case FOLLOWING:
                            if (userProfileDataType.isUnAvailable()) {
                                mView.unableToFetchUsers(userProfileDataType.getUserType());
                            } else {
                                mView.showUsers(userProfileDataType.getUserType(), userProfileDataType.getUsers(), refresh);
                            }
                            break;
                    }
                } else if (userProfileDataType.getPostType() != null) {
                    if (userProfileDataType.isUnAvailable()) {
                        mView.unableToFetchPosts(userProfileDataType.getPostType());
                    } else {
                        mView.showPosts(userProfileDataType.getPostType(), userProfileDataType.getPosts(), refresh);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.onlineLoadingComplete();
            }

            @Override
            public void onComplete() {
                // Done
                Logger.d(TAG, "onComplete: fetched user details");
                mView.onlineLoadingComplete();
                if (refresh) {
                    mView.onRefreshComplete();
                }
            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }

    private ContentValues[] getBulkContentValuesForPosts(UserProfileData userProfileData) {
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        if (userProfileData.getUser().getVotes() != null) {
            for (UserProfileData.User.Votes vote : userProfileData.getUser().getVotes()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, vote.getPost().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, vote.getPost().getCategoryId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, vote.getPost().getDay());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, vote.getPost().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, vote.getPost().getTagline());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, vote.getPost().getCommentsCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, vote.getPost().getCreatedAt());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(vote.getPost().getCreatedAt()));
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, vote.getPost().getDiscussionUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, vote.getPost().getRedirectUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, vote.getPost().getVotesCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, vote.getPost().getThumbnail().getImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, vote.getPost().getThumbnail().getOriginalImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, vote.getPost().getScreenshotUrl().getValue300px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, vote.getPost().getScreenshotUrl().getValue850px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, vote.getPost().getUser().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, vote.getPost().getUser().getUsername());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, vote.getPost().getUser().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, vote.getPost().getUser().getImageUrl().getValue100px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, vote.getPost().getUser().getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        if (userProfileData.getUser().getMakerOf() != null) {
            for (PostsData.Posts makerOf : userProfileData.getUser().getMakerOf()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, makerOf.getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, makerOf.getCategoryId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, makerOf.getDay());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, makerOf.getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, makerOf.getTagline());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, makerOf.getCommentsCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, makerOf.getCreatedAt());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(makerOf.getCreatedAt()));
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, makerOf.getDiscussionUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, makerOf.getRedirectUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, makerOf.getVotesCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, makerOf.getThumbnail().getImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, makerOf.getThumbnail().getOriginalImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, makerOf.getScreenshotUrl().getValue300px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, makerOf.getScreenshotUrl().getValue850px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, makerOf.getUser().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, makerOf.getUser().getUsername());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, makerOf.getUser().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, makerOf.getUser().getImageUrl().getValue100px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, makerOf.getUser().getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        if (userProfileData.getUser().getPosts() != null) {
            for (PostsData.Posts submitted : userProfileData.getUser().getPosts()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, submitted.getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, submitted.getCategoryId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, submitted.getDay());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, submitted.getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, submitted.getTagline());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, submitted.getCommentsCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, submitted.getCreatedAt());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(submitted.getCreatedAt()));
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, submitted.getDiscussionUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, submitted.getRedirectUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, submitted.getVotesCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, submitted.getThumbnail().getImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, submitted.getThumbnail().getOriginalImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, submitted.getScreenshotUrl().getValue300px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, submitted.getScreenshotUrl().getValue850px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, submitted.getUser().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, submitted.getUser().getUsername());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, submitted.getUser().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, submitted.getUser().getImageUrl().getValue100px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, submitted.getUser().getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    private ContentValues getContentValuesForCurrentUser(UserProfileData userProfileData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, userProfileData.getUser().getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, userProfileData.getUser().getCreatedAt());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, userProfileData.getUser().getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, userProfileData.getUser().getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, userProfileData.getUser().getHeadline());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, userProfileData.getUser().getWebsiteUrl());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, userProfileData.getUser().getImageUrl().getValue100px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, userProfileData.getUser().getImageUrl().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, userProfileData.getUser().getHuntedPostsIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, userProfileData.getUser().getMadePostsIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, userProfileData.getUser().getVotedPostsIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_FOLLOWER_USER_IDS, userProfileData.getUser().getFollowerUserIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_FOLLOWING_USER_IDS, userProfileData.getUser().getFollowingUserIds());
        return contentValues;
    }

    private ContentValues[] getBulkContentValuesForPostUsers(UserProfileData userProfileData) {
        List<ContentValues> contentValues = new ArrayList<ContentValues>();

        for (UserProfileData.User.Votes vote : userProfileData.getUser().getVotes()) {
            contentValues.add(getContentValuesForHunterUser(vote.getPost().getId(), vote.getPost().getUser()));
            for (PostsData.Posts.Makers maker : vote.getPost().getMakers()) {
                contentValues.add(getContentValuesForMakerUser(vote.getPost().getId(), maker));
            }
        }

        for (PostsData.Posts post : userProfileData.getUser().getPosts()) {
            contentValues.add(getContentValuesForHunterUser(post.getId(), post.getUser()));
            for (PostsData.Posts.Makers maker : post.getMakers()) {
                contentValues.add(getContentValuesForMakerUser(post.getId(), maker));
            }
        }

        for (PostsData.Posts post : userProfileData.getUser().getMakerOf()) {
            contentValues.add(getContentValuesForHunterUser(post.getId(), post.getUser()));
            for (PostsData.Posts.Makers maker : post.getMakers()) {
                contentValues.add(getContentValuesForMakerUser(post.getId(), maker));
            }
        }

        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    private ContentValues getContentValuesForHunterUser(int postId, PostsData.Posts.User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, user.getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, user.getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, user.getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, user.getHeadline());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, user.getWebsiteUrl());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, user.getImageUrl().getValue100px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, user.getImageUrl().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, postId);
        return contentValues;
    }

    private ContentValues getContentValuesForMakerUser(int postId, PostsData.Posts.Makers maker) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, maker.getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, maker.getCreatedAt());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, maker.getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, maker.getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, maker.getHeadline());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, maker.getWebsiteUrl());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, maker.getImageUrlMaker().getValue48px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, maker.getImageUrlMaker().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, postId);
        return contentValues;
    }

    private ContentValues[] getBulkContentValuesForFollowingFollowerUsers(UserProfileData userProfileData) {
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        if (userProfileData.getUser().getFollowers() != null) {
            for (UserProfileData.User.Followers follower : userProfileData.getUser().getFollowers()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, follower.getId());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, follower.getCreatedAt());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, follower.getName());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, follower.getUsername());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, follower.getHeadline());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, follower.getWebsiteUrl());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, follower.getImageUrl().getValue100px());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, follower.getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        if (userProfileData.getUser().getFollowings() != null) {
            for (UserProfileData.User.Followings following : userProfileData.getUser().getFollowings()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, following.getId());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, following.getCreatedAt());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, following.getName());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, following.getUsername());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, following.getHeadline());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, following.getWebsiteUrl());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, following.getImageUrl().getValue100px());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, following.getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    private User getCurrentUserFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        User user = new User();
        user.setId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_ID));
        user.setUserId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_USER_ID));
        user.setName(getString(cursor, PredatorContract.UsersEntry.COLUMN_NAME));
        user.setUsername(getString(cursor, PredatorContract.UsersEntry.COLUMN_USERNAME));
        user.setHeadline(getString(cursor, PredatorContract.UsersEntry.COLUMN_HEADLINE));
        user.setWebsiteUrl(getString(cursor, PredatorContract.UsersEntry.COLUMN_WEBSITE_URL));
        user.setThumbnail(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
        user.setImage(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));
        user.setHuntedPostIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS));
        user.setMadePostIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS));
        user.setVotedPostIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS));
        user.setFollowerUserIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_FOLLOWER_USER_IDS));
        user.setFollowingUserIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_FOLLOWING_USER_IDS));

        cursor.close();

        return user;
    }

    private List<User> getUsersFromCursor(Cursor cursor) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            User user = new User();
            user.setId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_ID));
            user.setUserId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_USER_ID));
            user.setName(getString(cursor, PredatorContract.UsersEntry.COLUMN_NAME));
            user.setHeadline(getString(cursor, PredatorContract.UsersEntry.COLUMN_HEADLINE));
            user.setUsername(getString(cursor, PredatorContract.UsersEntry.COLUMN_USERNAME));
            user.setThumbnail(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
            user.setImage(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

            users.add(user);
        }

        cursor.close();

        return users;
    }

    private List<Post> getPostsFromCursor(Cursor cursor) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Post post = new Post();
            post.setId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_ID));
            post.setPostId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_POST_ID));
            post.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_CATEGORY_ID));
            post.setDay(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY));
            post.setName(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_NAME));
            post.setTagline(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE));
            post.setCommentCount(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT));
            post.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT));
            post.setCreatedAtMillis(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS));
            post.setDiscussionUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL));
            post.setRedirectUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_REDIRECT_URL));
            post.setVotesCount(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_VOTES_COUNT));
            post.setThumbnailImageUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL));
            post.setThumbnailImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL));
            post.setScreenshotUrl300px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX));
            post.setScreenshotUrl850px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX));
            post.setUsername(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_NAME));
            post.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_USERNAME));
            post.setUserId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_USER_ID));
            post.setUserImageUrl100px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX));
            post.setUserImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL));
            posts.add(post);
        }

        cursor.close();

        return posts;
    }

    private static class UserProfileDataType {
        private List<Post> posts;
        private List<User> users;
        private UserProfileContract.POST_TYPE postType;
        private UserProfileContract.USER_TYPE userType;
        private boolean unAvailable;

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public UserProfileContract.POST_TYPE getPostType() {
            return postType;
        }

        public void setPostType(UserProfileContract.POST_TYPE postType) {
            this.postType = postType;
        }

        public UserProfileContract.USER_TYPE getUserType() {
            return userType;
        }

        public void setUserType(UserProfileContract.USER_TYPE userType) {
            this.userType = userType;
        }

        public boolean isUnAvailable() {
            return unAvailable;
        }

        public void setUnAvailable(boolean unAvailable) {
            this.unAvailable = unAvailable;
        }
    }
}
