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
import com.crazyhitty.chdev.ks.producthunt_wrapper.rest.ProductHuntRestApi;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
                PostDetails postDetails = PredatorDatabase.getInstance()
                        .getPostDetails(postId);
                if (postDetails != null) {
                    emitter.onNext(postDetails);
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
                                // Update post details.
                                PredatorDatabase.getInstance()
                                        .insertPost(PredatorDbValuesHelper.getContentValuesForPost(postDetailsData.getPost()));

                                // Get post details.
                                PostDetailsDataType postDetails = new PostDetailsDataType();
                                postDetails.setPostDetails(PredatorDatabase.getInstance()
                                        .getPostDetails(postId));
                                postDetails.setType(PostDetailsDataType.TYPE.POST_DETAILS);
                                emitter.onNext(postDetails);

                                // Add users who upvoted this post to database.
                                PredatorDatabase.getInstance()
                                        .insertUsers(PredatorDbValuesHelper.getBulkContentValuesForUsers(postId,
                                                postDetailsData.getPost().getVotes()));

                                List<User> users = PredatorDatabase.getInstance()
                                        .getAllUsersForPost(postId);
                                if (users != null && !users.isEmpty()) {
                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.USERS_VOTED);
                                    postDetailsDataType.setUsers(users);

                                    emitter.onNext(postDetailsDataType);
                                } else {
                                    emitter.onError(new VotedUsersUnavailableException());
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
                                if (media != null && !media.isEmpty()) {
                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setMedia(media);
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.MEDIA);

                                    emitter.onNext(postDetailsDataType);
                                } else {
                                    emitter.onError(new MediaUnavailableException());
                                }

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
                                if (comments != null && comments.size() != 0) {
                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.COMMENTS);
                                    postDetailsDataType.setComments(comments);
                                    emitter.onNext(postDetailsDataType);
                                } else {
                                    emitter.onError(new CommentsUnavailableException());
                                }

                                // Clear install links for that particular post.
                                PredatorDatabase.getInstance()
                                        .deleteInstallLinksForPost(postId);

                                // Add install links to database.
                                PredatorDatabase.getInstance()
                                        .insertInstallLinks(PredatorDbValuesHelper.getBulkContentValuesForInstallLinks(postDetailsData.getPost().getInstallLinks()));

                                // Query the install links available.
                                List<InstallLink> installLinks = PredatorDatabase.getInstance()
                                        .getInstallLinksForPost(postId);

                                if (installLinks != null && !installLinks.isEmpty()) {
                                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                                    postDetailsDataType.setInstallLinks(installLinks);
                                    postDetailsDataType.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);

                                    emitter.onNext(postDetailsDataType);
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
                    case POST_DETAILS:
                        mView.showDetails(postDetailsDataType.getPostDetails());
                        break;
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
                List<User> users = PredatorDatabase.getInstance()
                        .getAllUsersForPost(postId);
                if (users != null && !users.isEmpty()) {
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
                List<Media> media = PredatorDatabase.getInstance()
                        .getMediaForPost(postId);
                if (media != null && !media.isEmpty()) {
                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setMedia(media);
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.MEDIA);

                    emitter.onNext(postDetailsDataType);
                } else {
                    emitter.onError(new MediaUnavailableException());
                    return;
                }

                // Query the comments available.
                List<Comment> comments = PredatorDatabase.getInstance()
                        .getCommentsForPost(postId,
                                0,
                                new ArrayList<Comment>(),
                                0,
                                CommentTimeCalculator.getInstance());
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
                List<InstallLink> installLinks = PredatorDatabase.getInstance()
                        .getInstallLinksForPost(postId);
                if (installLinks != null && !installLinks.isEmpty()) {
                    PostDetailsDataType postDetailsDataType = new PostDetailsDataType();
                    postDetailsDataType.setInstallLinks(installLinks);
                    postDetailsDataType.setType(PostDetailsDataType.TYPE.INSTALL_LINKS);

                    emitter.onNext(postDetailsDataType);
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

    private void addCommentsToDatabase(List<PostCommentsData.Comments> comments) {
        for (PostCommentsData.Comments comment : comments) {
            PredatorDatabase.getInstance()
                    .insertComment(PredatorDbValuesHelper.getContentValuesForComments(comment));
            addCommentsToDatabase(comment.getChildComments());
        }
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

        enum TYPE {
            POST_DETAILS,
            MEDIA,
            COMMENTS,
            INSTALL_LINKS,
            USERS_VOTED
        }
    }
}
