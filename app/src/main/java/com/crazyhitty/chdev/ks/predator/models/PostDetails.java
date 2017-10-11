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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/13/2017 9:01 AM
 * Description: Useful for sending post details.
 */

public class PostDetails implements Parcelable {
    private String title;
    private String description;
    private String day;
    private String date;
    private String backdropUrl;
    private String redirectUrl;
    private String tagline;
    private String discussionUrl;
    private int categoryId;
    private String category;
    private int voteCount;

    public PostDetails() {
    }

    protected PostDetails(Parcel in) {
        title = in.readString();
        description = in.readString();
        day = in.readString();
        date = in.readString();
        backdropUrl = in.readString();
        redirectUrl = in.readString();
        tagline = in.readString();
        discussionUrl = in.readString();
        categoryId = in.readInt();
        category = in.readString();
        voteCount = in.readInt();
    }

    public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
        @Override
        public PostDetails createFromParcel(Parcel in) {
            return new PostDetails(in);
        }

        @Override
        public PostDetails[] newArray(int size) {
            return new PostDetails[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDiscussionUrl() {
        return discussionUrl;
    }

    public void setDiscussionUrl(String discussionUrl) {
        this.discussionUrl = discussionUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public static PostDetails fromPost(Post post) {
        PostDetails postDetails = new PostDetails();
        postDetails.setTitle(post.getName());
        postDetails.setDescription(post.getTagline());
        postDetails.setDay(post.getDay());
        postDetails.setDate(post.getCreatedAt());
        postDetails.setBackdropUrl(post.getThumbnailImageUrl());
        postDetails.setRedirectUrl(post.getRedirectUrl());
        postDetails.setTagline(post.getTagline());
        postDetails.setDiscussionUrl(post.getDiscussionUrl());
        postDetails.setCategoryId(post.getCategoryId());
        postDetails.setVoteCount(post.getVotesCount());
        return postDetails;
    }

    @Override
    public String toString() {
        return "title: " + title +
                ", description: " + description +
                ", day: " + day +
                ", date: " + date +
                ", backdropUrl: " + backdropUrl +
                ", redirectUrl: " + redirectUrl +
                ", tagline: " + tagline +
                ", discussionUrl: " + discussionUrl +
                ", categoryId: " + categoryId +
                ", category: " + category +
                ", voteCount: " + voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(day);
        dest.writeString(date);
        dest.writeString(backdropUrl);
        dest.writeString(redirectUrl);
        dest.writeString(tagline);
        dest.writeString(discussionUrl);
        dest.writeInt(categoryId);
        dest.writeString(category);
        dest.writeInt(voteCount);
    }
}
