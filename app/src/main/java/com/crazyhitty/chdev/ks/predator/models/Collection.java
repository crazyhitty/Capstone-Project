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
import android.text.Spannable;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/20/2017 7:57 PM
 * Description: Unavailable
 */

public class Collection implements Parcelable {
    private int id;
    private int collectionId;
    private String name;
    private String title;
    private String createdAt;
    private String updatedAt;
    private String featuredAt;
    private String subscriberCount;
    private int categoryId;
    private String collectionUrl;
    private int postCounts;
    private String backgroundImageUrl;
    private String username;
    private String usernameAlternative;
    private int userId;
    private String userImageUrl100px;
    private String userImageUrlOriginal;
    private Spannable nameSpannable;
    private Spannable titleSpannable;

    public Collection() {

    }

    protected Collection(Parcel in) {
        id = in.readInt();
        collectionId = in.readInt();
        name = in.readString();
        title = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        featuredAt = in.readString();
        subscriberCount = in.readString();
        categoryId = in.readInt();
        collectionUrl = in.readString();
        postCounts = in.readInt();
        backgroundImageUrl = in.readString();
        username = in.readString();
        usernameAlternative = in.readString();
        userId = in.readInt();
        userImageUrl100px = in.readString();
        userImageUrlOriginal = in.readString();
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFeaturedAt() {
        return featuredAt;
    }

    public void setFeaturedAt(String featuredAt) {
        this.featuredAt = featuredAt;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCollectionUrl() {
        return collectionUrl;
    }

    public void setCollectionUrl(String collectionUrl) {
        this.collectionUrl = collectionUrl;
    }

    public int getPostCounts() {
        return postCounts;
    }

    public void setPostCounts(int postCounts) {
        this.postCounts = postCounts;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
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

    public Spannable getNameSpannable() {
        return nameSpannable;
    }

    public void setNameSpannable(Spannable nameSpannable) {
        this.nameSpannable = nameSpannable;
    }

    public Spannable getTitleSpannable() {
        return titleSpannable;
    }

    public void setTitleSpannable(Spannable titleSpannable) {
        this.titleSpannable = titleSpannable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(collectionId);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(featuredAt);
        dest.writeString(subscriberCount);
        dest.writeInt(categoryId);
        dest.writeString(collectionUrl);
        dest.writeInt(postCounts);
        dest.writeString(backgroundImageUrl);
        dest.writeString(username);
        dest.writeString(usernameAlternative);
        dest.writeInt(userId);
        dest.writeString(userImageUrl100px);
        dest.writeString(userImageUrlOriginal);
    }
}
