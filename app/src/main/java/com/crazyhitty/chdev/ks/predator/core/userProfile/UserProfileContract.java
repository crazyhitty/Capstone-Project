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

import com.crazyhitty.chdev.ks.predator.core.BasePresenter;
import com.crazyhitty.chdev.ks.predator.core.BaseView;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.User;

import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/17/2017 3:28 PM
 * Description: Unavailable
 */

public interface UserProfileContract {
    enum POST_TYPE {
        UPVOTES,
        SUBMITTED,
        MADE
    }

    enum USER_TYPE {
        CURRENT,
        FOLLOWERS,
        FOLLOWING
    }

    interface View extends BaseView<Presenter> {
        void offlineLoadingStart();

        void onlineLoadingStart();

        void showUserDetails(User currentUser);

        void unableToFetchDataOnline(String errorMessage);

        void unableToFetchUserDetails();

        void showPosts(POST_TYPE postType, List<Post> posts, boolean refresh);

        void showUsers(USER_TYPE userType, List<User> users, boolean refresh);

        void unableToFetchPosts(POST_TYPE postType);

        void unableToFetchUsers(USER_TYPE userType);

        void offlineLoadingComplete();

        void onlineLoadingComplete();

        void onRefreshComplete();

        void websiteAvailable(String url);

        void websiteUnavailable();
    }

    interface Presenter extends BasePresenter {
        void getOfflineData(int userId);

        void getLatestData(String token, int userId, boolean refresh);

        void getWebsite(int userId);
    }
}
