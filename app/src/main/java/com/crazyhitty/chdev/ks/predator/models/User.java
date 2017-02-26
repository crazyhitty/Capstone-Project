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

package com.crazyhitty.chdev.ks.predator.models;

import android.text.TextUtils;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/14/2017 7:35 PM
 * Description: Unavailable
 */

public class User {
    private int id;
    private int userId;
    private String name;
    private String username;
    private String headline;
    private String websiteUrl;
    private String thumbnail;
    private String image;
    private String huntedPostIds;
    private String madePostIds;
    private String votedPostIds;
    private String followerUserIds;
    private String followingUserIds;
    private TYPE type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHuntedPostIds() {
        return huntedPostIds;
    }

    public void setHuntedPostIds(String huntedPostIds) {
        this.huntedPostIds = huntedPostIds;
    }

    public String getHuntedPostIdsQuery() {
        if (!TextUtils.isEmpty(huntedPostIds)) {
            String[] huntedPostIdsArr = huntedPostIds.split(",");
            for (int i = 0; i < huntedPostIdsArr.length; i++) {
                huntedPostIdsArr[i] = "'" + huntedPostIdsArr[i] + "'";
            }
            return TextUtils.join(", ", huntedPostIdsArr);
        }
        return huntedPostIds;
    }

    public String getMadePostIds() {
        return madePostIds;
    }

    public void setMadePostIds(String madePostIds) {
        this.madePostIds = madePostIds;
    }

    public String getMadePostIdsQuery() {
        if (!TextUtils.isEmpty(madePostIds)) {
            String[] madePostIdsArr = madePostIds.split(",");
            for (int i = 0; i < madePostIdsArr.length; i++) {
                madePostIdsArr[i] = "'" + madePostIdsArr[i] + "'";
            }
            return TextUtils.join(", ", madePostIdsArr);
        }
        return madePostIds;
    }

    public String getVotedPostIds() {
        return votedPostIds;
    }

    public void setVotedPostIds(String votedPostIds) {
        this.votedPostIds = votedPostIds;
    }

    public String getVotedPostIdsQuery() {
        if (!TextUtils.isEmpty(votedPostIds)) {
            String[] votedPostIdsArr = votedPostIds.split(",");
            for (int i = 0; i < votedPostIdsArr.length; i++) {
                votedPostIdsArr[i] = "'" + votedPostIdsArr[i] + "'";
            }
            return TextUtils.join(", ", votedPostIdsArr);
        }
        return votedPostIds;
    }

    public String getFollowerUserIds() {
        return followerUserIds;
    }

    public void setFollowerUserIds(String followerUserIds) {
        this.followerUserIds = followerUserIds;
    }

    public String getFollowerUserIdsQuery() {
        if (!TextUtils.isEmpty(followerUserIds)) {
            String[] followerUserIdsArr = followerUserIds.split(",");
            for (int i = 0; i < followerUserIdsArr.length; i++) {
                followerUserIdsArr[i] = "'" + followerUserIdsArr[i] + "'";
            }
            return TextUtils.join(", ", followerUserIdsArr);
        }
        return followerUserIds;
    }

    public String getFollowingUserIds() {
        return followingUserIds;
    }

    public void setFollowingUserIds(String followingUserIds) {
        this.followingUserIds = followingUserIds;
    }

    public String getFollowingUserIdsQuery() {
        if (!TextUtils.isEmpty(followingUserIds)) {
            String[] followingUserIdsArr = followingUserIds.split(",");
            for (int i = 0; i < followingUserIdsArr.length; i++) {
                followingUserIdsArr[i] = "'" + followingUserIdsArr[i] + "'";
            }
            return TextUtils.join(", ", followingUserIdsArr);
        }
        return followingUserIds;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "id: " + id +
                ", userId: " + userId +
                ", name: " + name +
                ", username: " + username +
                ", thumbnail: " + thumbnail +
                ", image: " + image +
                ", huntedPostIds: " + huntedPostIds +
                ", madePostIds: " + madePostIds +
                ", votedPostIds: " + votedPostIds +
                ", type: " + type;
    }

    public enum TYPE {
        HUNTER,
        MAKER,
        BOTH,
        UPVOTER
    }
}
