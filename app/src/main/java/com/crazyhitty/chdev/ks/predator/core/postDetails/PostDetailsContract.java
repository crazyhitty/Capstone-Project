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

import com.crazyhitty.chdev.ks.predator.core.BasePresenter;
import com.crazyhitty.chdev.ks.predator.core.BaseView;
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;

import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/12/2017 5:13 PM
 * Description: Unavailable
 */

public interface PostDetailsContract {
    interface View extends BaseView<Presenter> {
        void showDetails(PostDetails postDetails);

        void showUsers(List<User> users);

        void showMedia(List<Media> media);

        void attachInstallLinks(List<InstallLink> installLinks);

        void showAllUsers(List<User> users);

        void showComments(List<Comment> comments);

        void unableToFetchPostDetails(String errorMessage);

        //void unableToFetchUsers(String errorMessage);

        void unableToFetchMedia(String errorMessage);

        void unableToFetchInstallLinks(String errorMessage);

        void unableToFetchAllUsers(String errorMessage);

        void unableToFetchComments(String errorMessage);

        void noOfflineDataAvailable();

        void dismissLoading();
    }

    interface Presenter extends BasePresenter {
        void getDetails(int postId);

        //void getUsers(int postId);

        void getExtraDetails(String token, int postId);

        void getExtraDetailsOffline(int postId);

        void openRedirectUrl(Activity activity);

        void setAsRead(int postId);

        void setAsUnread(int postId);

        PostDetails getPostDetails();
    }
}
