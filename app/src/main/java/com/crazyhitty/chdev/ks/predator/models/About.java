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
 * Created:     1/25/2017 11:57 AM
 * Description: Unavailable
 */

public class About {
    private String title;
    private String username;
    private String userType;
    private int userImageResource;
    private String specialThanks;
    private String libraryTitle;
    private String libraryCreator;
    private String libraryLicenseType;
    private String libraryRedirectUrl;
    private TYPE type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserImageResource() {
        return userImageResource;
    }

    public void setUserImageResource(int userImageResource) {
        this.userImageResource = userImageResource;
    }

    public String getSpecialThanks() {
        return specialThanks;
    }

    public void setSpecialThanks(String specialThanks) {
        this.specialThanks = specialThanks;
    }

    public String getLibraryTitle() {
        return libraryTitle;
    }

    public void setLibraryTitle(String libraryTitle) {
        this.libraryTitle = libraryTitle;
    }

    public String getLibraryCreator() {
        return libraryCreator;
    }

    public void setLibraryCreator(String libraryCreator) {
        this.libraryCreator = libraryCreator;
    }

    public String getLibraryLicenseType() {
        return libraryLicenseType;
    }

    public void setLibraryLicenseType(String libraryLicenseType) {
        this.libraryLicenseType = libraryLicenseType;
    }

    public String getLibraryRedirectUrl() {
        return libraryRedirectUrl;
    }

    public void setLibraryRedirectUrl(String libraryRedirectUrl) {
        this.libraryRedirectUrl = libraryRedirectUrl;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE {
        TITLE,
        DEVELOPER,
        SPECIAL_THANKS,
        LIBRARY
    }
}
