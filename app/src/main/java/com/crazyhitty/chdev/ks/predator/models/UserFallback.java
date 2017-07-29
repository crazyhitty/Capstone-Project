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
 * Created:     7/23/17 12:06 PM
 * Description: Unavailable
 */

public class UserFallback implements Parcelable {
    private String name;
    private String username;
    private String headline;
    private String websiteUrl;
    private String thumbnail;
    private String image;

    public UserFallback() {
        
    }

    protected UserFallback(Parcel in) {
        name = in.readString();
        username = in.readString();
        headline = in.readString();
        websiteUrl = in.readString();
        thumbnail = in.readString();
        image = in.readString();
    }

    public static final Creator<UserFallback> CREATOR = new Creator<UserFallback>() {
        @Override
        public UserFallback createFromParcel(Parcel in) {
            return new UserFallback(in);
        }

        @Override
        public UserFallback[] newArray(int size) {
            return new UserFallback[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(headline);
        dest.writeString(websiteUrl);
        dest.writeString(thumbnail);
        dest.writeString(image);
    }

    @Override
    public String toString() {
        return "name: " + name +
                ", username: " + username +
                ", headline: " + headline +
                ", websiteUrl: " + websiteUrl +
                ", thumbnail: " + thumbnail +
                ", image: " + image;
    }
}
