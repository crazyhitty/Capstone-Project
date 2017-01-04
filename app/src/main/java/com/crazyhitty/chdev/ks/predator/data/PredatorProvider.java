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

package com.crazyhitty.chdev.ks.predator.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/3/2017 3:29 PM
 * Description: Unavailable
 */

public class PredatorProvider extends ContentProvider {
    private static final String TAG = "PredatorProvider";

    // Use an int for each URI we will run, this represents the different queries
    private static final int POSTS_ADD = 100;
    private static final int POSTS_DELETE = 101;
    private static final int POSTS_GET = 102;
    private static final int POSTS_GET_BY_ID = 103;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PredatorDbHelper mPredatorDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS_ADD, POSTS_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS_DELETE_ALL, POSTS_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS, POSTS_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS + "/#", POSTS_GET_BY_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mPredatorDbHelper = PredatorDbHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case POSTS_GET:
                return mPredatorDbHelper.getPosts(projection, selection, selectionArgs, sortOrder);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case POSTS_GET:
                return PredatorContract.PostsEntry.CONTENT_TYPE;
            case POSTS_GET_BY_ID:
                return PredatorContract.PostsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case POSTS_ADD:
                id = mPredatorDbHelper.addPost(values);
                if (id > 0) {
                    returnUri = PredatorContract.PostsEntry.buildPostsUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                return returnUri;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case POSTS_DELETE:
                return mPredatorDbHelper.deleteAllPosts(selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
