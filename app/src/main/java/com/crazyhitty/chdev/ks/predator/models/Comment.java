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

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/17/2017 9:13 PM
 * Description: Unavailable
 */

public class Comment {
    private int id;
    private int commentId;
    private int parentCommentId;
    private String body;
    private String createdAt;
    private long createdAtMillis;
    private int postId;
    private int userId;
    private String username;
    private String usernameAlternative;
    private String userHeadline;
    private String userImageThumbnailUrl;
    private String userWebsiteUrl;
    private String url;
    private int votes;
    private boolean isSticky;
    private boolean isMaker;
    private boolean isHunter;
    private boolean isLiveGuest;
    private int childSpaces;
    private long timeAgo;
    private TIME_UNIT timeUnit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(int parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public void setCreatedAtMillis(long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameAlternative() {
        return usernameAlternative;
    }

    public void setUsernameAlternative(String usernameAlternative) {
        this.usernameAlternative = usernameAlternative;
    }

    public String getUserHeadline() {
        return userHeadline;
    }

    public void setUserHeadline(String userHeadline) {
        this.userHeadline = userHeadline;
    }

    public String getUserImageThumbnailUrl() {
        return userImageThumbnailUrl;
    }

    public void setUserImageThumbnailUrl(String userImageThumbnailUrl) {
        this.userImageThumbnailUrl = userImageThumbnailUrl;
    }

    public String getUserWebsiteUrl() {
        return userWebsiteUrl;
    }

    public void setUserWebsiteUrl(String userWebsiteUrl) {
        this.userWebsiteUrl = userWebsiteUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    public boolean isMaker() {
        return isMaker;
    }

    public void setMaker(boolean maker) {
        isMaker = maker;
    }

    public boolean isHunter() {
        return isHunter;
    }

    public void setHunter(boolean hunter) {
        isHunter = hunter;
    }

    public boolean isLiveGuest() {
        return isLiveGuest;
    }

    public void setLiveGuest(boolean liveGuest) {
        isLiveGuest = liveGuest;
    }

    public int getChildSpaces() {
        return childSpaces;
    }

    public void setChildSpaces(int childSpaces) {
        this.childSpaces = childSpaces;
    }

    public long getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(long timeAgo) {
        this.timeAgo = timeAgo;
    }

    public TIME_UNIT getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TIME_UNIT timeUnit) {
        this.timeUnit = timeUnit;
    }

    public enum TIME_UNIT {
        SECOND_AGO,
        SECOND_AGO_PLURAL,
        MINUTE_AGO,
        MINUTE_AGO_PLURAL,
        HOUR_AGO,
        HOUR_AGO_PLURAL,
        DAY_AGO,
        DAY_AGO_PLURAL,
        MONTH_AGO,
        MONTH_AGO_PLURAL,
        YEAR_AGO,
        YEAR_AGO_PLURAL
    }
}
