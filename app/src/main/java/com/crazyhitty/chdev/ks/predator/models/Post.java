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
 * Created:     1/20/2017 7:12 PM
 * Description: Unavailable
 */

public class Post {
    private int id;
    private int postId;
    private int categoryId;
    private String day;
    private String name;
    private String tagline;
    private int commentCount;
    private String createdAt;
    private int createdAtMillis;
    private String discussionUrl;
    private String redirectUrl;
    private int votesCount;
    private String thumbnailImageUrl;
    private String thumbnailImageUrlOriginal;
    private String screenshotUrl300px;
    private String screenshotUrl850px;
    private String username;
    private String usernameAlternative;
    private int userId;
    private String userImageUrl100px;
    private String userImageUrlOriginal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedAtMillis() {
        return createdAtMillis;
    }

    public void setCreatedAtMillis(int createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }

    public String getDiscussionUrl() {
        return discussionUrl;
    }

    public void setDiscussionUrl(String discussionUrl) {
        this.discussionUrl = discussionUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public String getThumbnailImageUrlOriginal() {
        return thumbnailImageUrlOriginal;
    }

    public void setThumbnailImageUrlOriginal(String thumbnailImageUrlOriginal) {
        this.thumbnailImageUrlOriginal = thumbnailImageUrlOriginal;
    }

    public String getScreenshotUrl300px() {
        return screenshotUrl300px;
    }

    public void setScreenshotUrl300px(String screenshotUrl300px) {
        this.screenshotUrl300px = screenshotUrl300px;
    }

    public String getScreenshotUrl850px() {
        return screenshotUrl850px;
    }

    public void setScreenshotUrl850px(String screenshotUrl850px) {
        this.screenshotUrl850px = screenshotUrl850px;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserImageUrl100px() {
        return userImageUrl100px;
    }

    public void setUserImageUrl100px(String userImageUrl100px) {
        this.userImageUrl100px = userImageUrl100px;
    }

    public String getUserImageUrlOriginal() {
        return userImageUrlOriginal;
    }

    public void setUserImageUrlOriginal(String userImageUrlOriginal) {
        this.userImageUrlOriginal = userImageUrlOriginal;
    }
}
