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
import com.crazyhitty.chdev.ks.predator.models.User;

import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     2/23/2017 5:21 PM
 * Description: Event pojo for maintaining user following/followers.
 */

public class UserFollowersFollowingEvent {
    private UserProfileContract.USER_TYPE userType;
    private List<User> users;
    private boolean forceReplace;

    public UserFollowersFollowingEvent() {
    }

    public UserFollowersFollowingEvent(UserProfileContract.USER_TYPE userType,
                                       List<User> users,
                                       boolean forceReplace) {
        this.userType = userType;
        this.users = users;
        this.forceReplace = forceReplace;
    }

    public UserProfileContract.USER_TYPE getUserType() {
        return userType;
    }

    public void setUserType(UserProfileContract.USER_TYPE userType) {
        this.userType = userType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isForceReplace() {
        return forceReplace;
    }

    public void setForceReplace(boolean forceReplace) {
        this.forceReplace = forceReplace;
    }
}
