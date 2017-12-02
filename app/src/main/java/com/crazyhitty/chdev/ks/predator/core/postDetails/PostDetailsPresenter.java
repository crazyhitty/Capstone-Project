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
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.predator.R;
import com.crazyhitty.chdev.ks.predator.data.PredatorDatabase;
import com.crazyhitty.chdev.ks.predator.data.PredatorDbValuesHelper;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.CommentTimeCalculator;
import com.crazyhitty.chdev.ks.predator.utils.CoreUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.UsersComparator;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostCommentsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostDetailsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
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

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 5:18 PM
 * Description: Unavailable
 */

public class PostDetailsPresenter implements PostDetailsContract.Presenter {
    private static final String TAG = "PostDetailsPresenter";

    @NonNull
    private PostDetailsContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public PostDetailsPresenter(@NonNull PostDetailsContract.View view) {
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getDetails(final int postId) {
        Observable<PostDetails> postDetailsObservable = Observable.create(new ObservableOnSubscribe<PostDetails>() {
            @Override
            public void subscribe(ObservableEmitter<PostDetails> emitter) throws Exception {
                PostDetails postDetails = PredatorDatabase.getInstance()
                        .getPostDetails(postId);
                if (postDetails != null) {
                    emitter.onNext(postDetails);
                } else {
                    emitter.onError(new NullPointerException("No offline post details available"));
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
                mView.showDetails(postDetails);
            }
        }));
    }

    /*@Override
    public void getUsers(final int postId) {
        Observable<List<User>> postUsersObservable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                List<User> users = PredatorDatabase.getInstance()
                        .getUsers(postId);
                if (users != null && !users.isEmpty()) {
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
    }*/

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
                                // Update post details.
                                PredatorDatabase.getInstance()
                                        .insertPost(PredatorDbValuesHelper.getContentValuesForPost(postDetailsData.getPost()));

                                // Get post details.
                                PostDetailsDataType postDetails = new PostDetailsDataType();
                                postDetails.setPostDetails(PredatorDatabase.getInstance()
                                        .getPostDetails(postId));
                                postDetails.setType(PostDetailsDataType.TYPE.POST_DETAILS);
                                emitter.onNext(postDetails);

                                // Add/update the hunter/makers for this post.
                                PredatorDatabase.getInstance()
                                        .insertUser(PredatorDbValuesHelper.getContentValuesForHunterUser(postId,
                                                postDetailsData.getPost().getUser()));
                                for (PostsData.Posts.Makers maker : postDetailsData.getPost().getMakers()) {
                                    PredatorDatabase.getInstance()
                                            .insertUser(PredatorDbValuesHelper.getContentValuesForMakerUser(postId,
                                                    maker));
                                }

                                // Add users who upvoted this post to database.
                                PredatorDatabase.getInstance()
                                        .insertUsers(PredatorDbValuesHelper.getBulkContentValuesForUsers(postId,
                                                postDetailsData.getPost().getVotes()));

                                List<User> users = PredatorDatabase.getInstance()
                                        .getAllUsersForPost(postId);
                                if (users != null && !users.isEmpty()) {
                                    // Sort the users list on basis of user type.
                                    Collections.sort(users, new UsersComparator());

                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS);
                                    postDetailsDataType.setUsers(users);
                                    emitter.onNext(postDetailsDataType);
                                } else {
                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS);
                                    postDetailsDataType.setUsers(null);
                                    emitter.onNext(postDetailsDataType);
                                }

                                // Clear media for that particular post.
                                PredatorDatabase.getInstance()
                                        .deleteMediaForPost(postId);

                                // Add media to database.
                                PredatorDatabase.getInstance()
                                        .insertBulkMedia(PredatorDbValuesHelper.getBulkContentValuesForMedia(postId, postDetailsData.getPost().getMedia()));

                                // Query the media available.
                                List<Media> media = PredatorDatabase.getInstance()
                                        .getMediaForPost(postId);
                                PostDetailsDataType postDetailsMedia = new PostDetailsDataType();
                                postDetailsMedia.setMedia(media);
                                postDetailsMedia.setType(PostDetailsDataType.TYPE.MEDIA);
                                emitter.onNext(postDetailsMedia);

                                // Clear comments for that particular post.
                                PredatorDatabase.getInstance()
                                        .deleteCommentsForPost(postId);

                                // Add comments to database.
                                addCommentsToDatabase(postDetailsData.getPost().getComments());

                                // Query the comments available.
                                List<Comment> comments = PredatorDatabase.getInstance()
                                        .getCommentsForPost(postId,
                                                0,
                                                new ArrayList<Comment>(),
                                                0,
                                                CommentTimeCalculator.getInstance());
                                PostDetailsDataType postDetailsComments = new PostDetailsDataType();
                                postDetailsComments.setType(PostDetailsDataType.TYPE.COMMENTS);
                                postDetailsComments.setComments(comments);
                                emitter.onNext(postDetailsComments);

                                // Clear install links for that particular post.
                                PredatorDatabase.getInstance()
                                        .deleteInstallLinksForPost(postId);

                                // Add install links to database.
                                PredatorDatabase.getInstance()
                                        .insertInstallLinks(PredatorDbValuesHelper.getBulkContentValuesForInstallLinks(postDetailsData.getPost().getInstallLinks()));

                                // Query the install links available.
                                List<InstallLink> installLinks = PredatorDatabase.getInstance()
                                        .getInstallLinksForPost(postId);
                                PostDetailsDataType postDetailsInstallLinks = new PostDetailsDataType();
                                postDetailsInstallLinks.setInstallLinks(installLinks);
                                postDetailsInstallLinks.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);
                                emitter.onNext(postDetailsInstallLinks);

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
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.dismissLoading();
            }

            @Override
            public void onNext(PostDetailsDataType postDetailsDataType) {
                switch (postDetailsDataType.getType()) {
                    case POST_DETAILS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchPostDetails("No post details available for provided post");
                        } else {
                            mView.showDetails(postDetailsDataType.getPostDetails());
                        }
                        break;
                    case MEDIA:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchMedia("No media available for provided post");
                        } else {
                            mView.showMedia(postDetailsDataType.getMedia());
                        }
                        break;
                    case COMMENTS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchComments("No comments available for provided post");
                        } else {
                            mView.showComments(postDetailsDataType.getComments());
                        }
                        break;
                    case INSTALL_LINKS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchInstallLinks("No install links available for provided post");
                        } else {
                            mView.attachInstallLinks(postDetailsDataType.getInstallLinks());
                        }
                        break;
                    case USERS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchAllUsers("No users available for provided post");
                        } else {
                            mView.showAllUsers(postDetailsDataType.getUsers());
                        }
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
                List<User> users = PredatorDatabase.getInstance()
                        .getAllUsersForPost(postId);
                if (users != null && !users.isEmpty()) {
                    // Sort the users list on basis of user type.
                    Collections.sort(users, new UsersComparator());

                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS);
                    postDetailsDataType.setUsers(users);
                    emitter.onNext(postDetailsDataType);
                } else {
                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS);
                    postDetailsDataType.setUsers(null);
                    emitter.onNext(postDetailsDataType);
                }

                // Query the media available.
                List<Media> media = PredatorDatabase.getInstance()
                        .getMediaForPost(postId);
                PostDetailsDataType postDetailsMedia = new PostDetailsDataType();
                postDetailsMedia.setMedia(media);
                postDetailsMedia.setType(PostDetailsDataType.TYPE.MEDIA);
                emitter.onNext(postDetailsMedia);

                // Query the comments available.
                List<Comment> comments = PredatorDatabase.getInstance()
                        .getCommentsForPost(postId,
                                0,
                                new ArrayList<Comment>(),
                                0,
                                CommentTimeCalculator.getInstance());
                PostDetailsDataType postDetailsComments = new PostDetailsDataType();
                postDetailsComments.setType(PostDetailsDataType.TYPE.COMMENTS);
                postDetailsComments.setComments(comments);
                emitter.onNext(postDetailsComments);

                // Query the install links available.
                List<InstallLink> installLinks = PredatorDatabase.getInstance()
                        .getInstallLinksForPost(postId);
                PostDetailsDataType postDetailsInstallLinks = new PostDetailsDataType();
                postDetailsInstallLinks.setInstallLinks(installLinks);
                postDetailsInstallLinks.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);
                emitter.onNext(postDetailsInstallLinks);

                if (users == null || users.isEmpty() ||
                        media == null || media.isEmpty() ||
                        comments == null || comments.isEmpty() ||
                        installLinks == null || installLinks.isEmpty()) {
                    emitter.onError(new NullPointerException("Complete offline data for this post is unavailable"));
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
                mView.noOfflineDataAvailable();
                mView.dismissLoading();
            }

            @Override
            public void onNext(PostDetailsDataType postDetailsDataType) {
                switch (postDetailsDataType.getType()) {
                    case POST_DETAILS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchPostDetails("No post details available for provided post");
                        } else {
                            mView.showDetails(postDetailsDataType.getPostDetails());
                        }
                        break;
                    case MEDIA:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchMedia("No media available for provided post");
                        } else {
                            mView.showMedia(postDetailsDataType.getMedia());
                        }
                        break;
                    case COMMENTS:
                        if (!postDetailsDataType.isEmpty()) {
                            mView.showComments(postDetailsDataType.getComments());
                        } else if (postDetailsDataType.isEmpty() && !mView.isInternetAvailable()) {
                            mView.unableToFetchComments("No comments available for provided post");
                        }
                        break;
                    case INSTALL_LINKS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchInstallLinks("No install links available for provided post");
                        } else {
                            mView.attachInstallLinks(postDetailsDataType.getInstallLinks());
                        }
                        break;
                    case USERS:
                        if (postDetailsDataType.isEmpty()) {
                            mView.unableToFetchAllUsers("No users available for provided post");
                        } else {
                            mView.showAllUsers(postDetailsDataType.getUsers());
                        }
                        break;
                }
                mView.dismissLoading();
            }
        }));
    }

    @Override
    public void setAsRead(final int postId) {
        Observable<Void> readPostsObservable = Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> emitter) throws Exception {
                PredatorDatabase.getInstance()
                        .setPostAsRead(postId);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(readPostsObservable.subscribeWith(new DisposableObserver<Void>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
            }

            @Override
            public void onNext(Void aVoid) {

            }
        }));
    }

    @Override
    public void setAsUnread(final int postId) {
        Observable<Void> unreadPostsObservable = Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> emitter) throws Exception {
                PredatorDatabase.getInstance()
                        .setPostAsUnread(postId);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(unreadPostsObservable.subscribeWith(new DisposableObserver<Void>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
            }

            @Override
            public void onNext(Void aVoid) {

            }
        }));
    }

    @Override
    public void getRedirectUrl(final int postId) {
        Observable<String> redirectUrlObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String redirectUrl = PredatorDatabase.getInstance()
                        .getPostRedirectUrl(postId);
                if (TextUtils.isEmpty(redirectUrl)) {
                    emitter.onError(new NullPointerException("No redirect url available"));
                } else {
                    emitter.onNext(redirectUrl);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(redirectUrlObservable.subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.noRedirectUrlAvailable();
            }

            @Override
            public void onNext(String value) {
                mView.openRedirectUrl(value);
            }
        }));
    }

    @Override
    public void getShareIntent(final int postId) {
        Observable<Intent> shareIntentObservable = Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override
            public void subscribe(ObservableEmitter<Intent> emitter) throws Exception {
                Post post = PredatorDatabase.getInstance()
                        .getPost(postId);
                if (TextUtils.isEmpty(post.getName())) {
                    emitter.onError(new NullPointerException("Post name unavailable"));
                    emitter.onComplete();
                    return;
                }

                String title = post.getName();
                String body = post.getTagline() + "\n" + post.getDiscussionUrl();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

                emitter.onNext(sharingIntent);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mCompositeDisposable.add(shareIntentObservable.subscribeWith(new DisposableObserver<Intent>() {
            @Override
            public void onComplete() {
                // Done
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(TAG, "onError: " + e.getMessage(), e);
                mView.noShareIntentAvailable();
            }

            @Override
            public void onNext(Intent value) {
                mView.fireShareIntent(value);
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

    private void addCommentsToDatabase(List<PostCommentsData.Comments> comments) {
        for (PostCommentsData.Comments comment : comments) {
            PredatorDatabase.getInstance()
                    .insertComment(PredatorDbValuesHelper.getContentValuesForComments(comment));
            addCommentsToDatabase(comment.getChildComments());
        }
    }

    /*public static class PostDetailsUnavailableException extends Throwable {
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
    }*/

    public static class PostDetailsDataType {
        private TYPE type;
        private PostDetails postDetails;
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

        public PostDetails getPostDetails() {
            return postDetails;
        }

        public void setPostDetails(PostDetails postDetails) {
            this.postDetails = postDetails;
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

        public boolean isEmpty() {
            switch (type) {
                case POST_DETAILS:
                    return postDetails == null;
                case MEDIA:
                    return media == null || media.isEmpty();
                case COMMENTS:
                    return comments == null || comments.isEmpty();
                case INSTALL_LINKS:
                    return installLinks == null || installLinks.isEmpty();
                case USERS:
                    return users == null || users.isEmpty();
                default:
                    return false;
            }
        }

        enum TYPE {
            POST_DETAILS,
            MEDIA,
            COMMENTS,
            INSTALL_LINKS,
            USERS
        }
    }
}
