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

import android.support.annotation.NonNull;

import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
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
                List<User> currentUser = new ArrayList<User>();
                User user = PredatorDatabase.getInstance()
                        .getUser(userId);
                if (user != null) {
                    currentUser.add(user);
                }

                UserProfileDataType currentUserData = new UserProfileDataType();
                currentUserData.setUsers(currentUser);
                currentUserData.setUnAvailable(user == null);
                currentUserData.setUserType(UserProfileContract.USER_TYPE.CURRENT);
                emitter.onNext(currentUserData);

                // If no current user was found, don't execute the future code that is dependent on
                // that user.
                if (currentUser.size() == 0) {
                    emitter.onComplete();
                    return;
                }

                // Fetch the voted posts of this user.
                List<Post> votedPosts = PredatorDatabase.getInstance()
                        .getVotedPosts(currentUser.get(0).getVotedPostIdsQuery());
                if (votedPosts != null && !votedPosts.isEmpty()) {
                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                    currentUserPostsData.setPosts(votedPosts);
                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.UPVOTES);
                    emitter.onNext(currentUserPostsData);
                }

                // Fetch the submitted posts of this user.
                List<Post> submittedPosts = PredatorDatabase.getInstance()
                        .getSubmittedPosts(currentUser.get(0).getHuntedPostIdsQuery());
                if (submittedPosts != null && !submittedPosts.isEmpty()) {
                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                    currentUserPostsData.setPosts(submittedPosts);
                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.SUBMITTED);
                    emitter.onNext(currentUserPostsData);
                }

                // Fetch the posts which this user made (maker).
                List<Post> madePosts = PredatorDatabase.getInstance()
                        .getMadePosts(currentUser.get(0).getMadePostIdsQuery());
                if (madePosts != null && !madePosts.isEmpty()) {
                    UserProfileDataType currentUserPostsData = new UserProfileDataType();
                    currentUserPostsData.setPosts(madePosts);
                    currentUserPostsData.setPostType(UserProfileContract.POST_TYPE.MADE);
                    emitter.onNext(currentUserPostsData);
                }

                // Fetch followers users.
                List<User> followerUsers = PredatorDatabase.getInstance()
                        .getFollowerUsers(currentUser.get(0).getFollowerUserIdsQuery());
                if (followerUsers != null && !followerUsers.isEmpty()) {
                    UserProfileDataType followerUsersData = new UserProfileDataType();
                    followerUsersData.setUsers(followerUsers);
                    followerUsersData.setUserType(UserProfileContract.USER_TYPE.FOLLOWERS);
                    emitter.onNext(followerUsersData);
                }

                // Fetch following users.
                List<User> followingUsers = PredatorDatabase.getInstance()
                        .getFollowingUsers(currentUser.get(0).getFollowingUserIdsQuery());
                if (followerUsers != null && !followerUsers.isEmpty()) {
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
                                PredatorDatabase.getInstance()
                                        .insertUser(PredatorDbValuesHelper.getContentValuesForCurrentUser(userProfileData));
                                // Save other users in db (followers and followings).
                                PredatorDatabase.getInstance()
                                        .insertUsers(PredatorDbValuesHelper.getBulkContentValuesForFollowingFollowerUsers(userProfileData));
                                // Save the posts in db.
                                PredatorDatabase.getInstance()
                                        .insertPosts(PredatorDbValuesHelper.getBulkContentValuesForPosts(userProfileData));
                                // Save the users associated with above posts.
                                PredatorDatabase.getInstance()
                                        .insertUsers(PredatorDbValuesHelper.getBulkContentValuesForPostUsers(userProfileData));

                                // Fetch current user.
                                User user = PredatorDatabase.getInstance()
                                        .getUser(userId);
                                List<User> currentUser = new ArrayList<User>();
                                currentUser.add(user);

                                UserProfileDataType currentUserData = new UserProfileDataType();
                                currentUserData.setUsers(currentUser);
                                currentUserData.setUserType(UserProfileContract.USER_TYPE.CURRENT);
                                emitter.onNext(currentUserData);

                                // Fetch the voted posts of this user.
                                List<Post> votedPosts = PredatorDatabase.getInstance()
                                        .getVotedPosts(currentUser.get(0).getVotedPostIdsQuery());
                                if (votedPosts != null && !votedPosts.isEmpty()) {
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
                                List<Post> submittedPosts = PredatorDatabase.getInstance()
                                        .getSubmittedPosts(currentUser.get(0).getHuntedPostIdsQuery());
                                if (submittedPosts != null && !submittedPosts.isEmpty()) {
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
                                List<Post> madePosts = PredatorDatabase.getInstance()
                                        .getMadePosts(currentUser.get(0).getMadePostIdsQuery());
                                if (madePosts != null && !madePosts.isEmpty()) {
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
                                List<User> followerUsers = PredatorDatabase.getInstance()
                                        .getFollowerUsers(currentUser.get(0).getFollowerUserIdsQuery());
                                if (followerUsers != null && !followerUsers.isEmpty()) {
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
                                List<User> followingUsers =PredatorDatabase.getInstance()
                                        .getFollowingUsers(currentUser.get(0).getFollowingUserIdsQuery());
                                if (followingUsers != null && !followingUsers.isEmpty()) {
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
                mView.unableToFetchDataOnline(e.getMessage());
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
