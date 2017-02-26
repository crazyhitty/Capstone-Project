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

package com.crazyhitty.chdev.ks.predator.events;

import com.crazyhitty.chdev.ks.predator.core.userProfile.UserProfileContract;
import com.crazyhitty.chdev.ks.predator.models.Post;

import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/23/2017 5:21 PM
 * Description: Event pojo for maintaining user posts.
 */

public class UserPostsEvent {
    private UserProfileContract.POST_TYPE postType;
    private List<Post> posts;
    private boolean forceReplace;

    public UserPostsEvent() {
    }

    public UserPostsEvent(UserProfileContract.POST_TYPE postType,
                          List<Post> posts,
                          boolean forceReplace) {
        this.postType = postType;
        this.posts = posts;
        this.forceReplace = forceReplace;
    }

    public UserProfileContract.POST_TYPE getPostType() {
        return postType;
    }

    public void setPostType(UserProfileContract.POST_TYPE postType) {
        this.postType = postType;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public boolean isForceReplace() {
        return forceReplace;
    }

    public void setForceReplace(boolean forceReplace) {
        this.forceReplace = forceReplace;
    }
}
