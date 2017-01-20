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
    private static final int USERS_ADD = 200;
    private static final int USERS_DELETE = 201;
    private static final int USERS_GET = 202;
    private static final int USERS_GET_BY_ID = 203;
    private static final int COMMENTS_ADD = 300;
    private static final int COMMENTS_DELETE = 301;
    private static final int COMMENTS_GET = 302;
    private static final int COMMENTS_GET_BY_ID = 303;
    private static final int INSTALL_LINKS_ADD = 400;
    private static final int INSTALL_LINKS_DELETE = 401;
    private static final int INSTALL_LINKS_GET = 402;
    private static final int INSTALL_LINKS_GET_BY_ID = 403;
    private static final int MEDIA_ADD = 500;
    private static final int MEDIA_DELETE = 501;
    private static final int MEDIA_GET = 502;
    private static final int MEDIA_GET_BY_ID = 503;
    private static final int COLLECTIONS_ADD = 600;
    private static final int COLLECTIONS_DELETE = 601;
    private static final int COLLECTIONS_GET = 602;
    private static final int COLLECTIONS_GET_BY_ID = 603;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PredatorDbHelper mPredatorDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS_ADD, POSTS_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS_DELETE_ALL, POSTS_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS, POSTS_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.PostsEntry.PATH_POSTS + "/#", POSTS_GET_BY_ID);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.UsersEntry.PATH_USERS_ADD, USERS_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.UsersEntry.PATH_USERS_DELETE_ALL, USERS_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.UsersEntry.PATH_USERS, USERS_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.UsersEntry.PATH_USERS + "/#", USERS_GET_BY_ID);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CommentsEntry.PATH_COMMENTS_ADD, COMMENTS_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CommentsEntry.PATH_COMMENTS_DELETE_ALL, COMMENTS_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CommentsEntry.PATH_COMMENTS, COMMENTS_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CommentsEntry.PATH_COMMENTS + "/#", COMMENTS_GET_BY_ID);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.InstallLinksEntry.PATH_INSTALL_LINKS_ADD, INSTALL_LINKS_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.InstallLinksEntry.PATH_INSTALL_LINKS_DELETE_ALL, INSTALL_LINKS_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.InstallLinksEntry.PATH_INSTALL_LINKS, INSTALL_LINKS_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.InstallLinksEntry.PATH_INSTALL_LINKS + "/#", INSTALL_LINKS_GET_BY_ID);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.MediaEntry.PATH_MEDIA_ADD, MEDIA_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.MediaEntry.PATH_MEDIA_DELETE_ALL, MEDIA_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.MediaEntry.PATH_MEDIA, MEDIA_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.MediaEntry.PATH_MEDIA + "/#", MEDIA_GET_BY_ID);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CollectionsEntry.PATH_COLLECTIONS_ADD, COLLECTIONS_ADD);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CollectionsEntry.PATH_COLLECTIONS_DELETE_ALL, COLLECTIONS_DELETE);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CollectionsEntry.PATH_COLLECTIONS, COLLECTIONS_GET);
        uriMatcher.addURI(PredatorDbHelper.CONTENT_AUTHORITY, PredatorContract.CollectionsEntry.PATH_COLLECTIONS + "/#", COLLECTIONS_GET_BY_ID);
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
            case USERS_GET:
                return mPredatorDbHelper.getUsers(projection, selection, selectionArgs, sortOrder);
            case COMMENTS_GET:
                return mPredatorDbHelper.getComments(projection, selection, selectionArgs, sortOrder);
            case INSTALL_LINKS_GET:
                return mPredatorDbHelper.getInstallLinks(projection, selection, selectionArgs, sortOrder);
            case MEDIA_GET:
                return mPredatorDbHelper.getMedia(projection, selection, selectionArgs, sortOrder);
            case COLLECTIONS_GET:
                return mPredatorDbHelper.getCollections(projection, selection, selectionArgs, sortOrder);
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
            case USERS_GET:
                return PredatorContract.UsersEntry.CONTENT_TYPE;
            case USERS_GET_BY_ID:
                return PredatorContract.UsersEntry.CONTENT_ITEM_TYPE;
            case COMMENTS_GET:
                return PredatorContract.CommentsEntry.CONTENT_TYPE;
            case COMMENTS_GET_BY_ID:
                return PredatorContract.CommentsEntry.CONTENT_ITEM_TYPE;
            case INSTALL_LINKS_GET:
                return PredatorContract.InstallLinksEntry.CONTENT_TYPE;
            case INSTALL_LINKS_GET_BY_ID:
                return PredatorContract.InstallLinksEntry.CONTENT_ITEM_TYPE;
            case MEDIA_GET:
                return PredatorContract.MediaEntry.CONTENT_TYPE;
            case MEDIA_GET_BY_ID:
                return PredatorContract.MediaEntry.CONTENT_ITEM_TYPE;
            case COLLECTIONS_GET:
                return PredatorContract.CollectionsEntry.CONTENT_TYPE;
            case COLLECTIONS_GET_BY_ID:
                return PredatorContract.CollectionsEntry.CONTENT_ITEM_TYPE;
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
            case USERS_ADD:
                id = mPredatorDbHelper.addOrUpdateUser(values);
                if (id > 0) {
                    returnUri = PredatorContract.UsersEntry.buildUsersUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                return returnUri;
            case COMMENTS_ADD:
                id = mPredatorDbHelper.addComment(values);
                if (id > 0) {
                    returnUri = PredatorContract.CommentsEntry.buildCommentsUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                return returnUri;
            case INSTALL_LINKS_ADD:
                id = mPredatorDbHelper.addInstallLinks(values);
                if (id > 0) {
                    returnUri = PredatorContract.InstallLinksEntry.buildInstallLinksUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                return returnUri;
            case MEDIA_ADD:
                id = mPredatorDbHelper.addMedia(values);
                if (id > 0) {
                    returnUri = PredatorContract.MediaEntry.buildMediaUri(id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                return returnUri;
            case COLLECTIONS_ADD:
                id = mPredatorDbHelper.addCollection(values);
                if (id > 0) {
                    returnUri = PredatorContract.CollectionsEntry.buildCollectionUri(id);
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
            case USERS_DELETE:
                return mPredatorDbHelper.deleteAllUsers(selection, selectionArgs);
            case COMMENTS_DELETE:
                return mPredatorDbHelper.deleteAllComments(selection, selectionArgs);
            case INSTALL_LINKS_DELETE:
                return mPredatorDbHelper.deleteAllInstallLinks(selection, selectionArgs);
            case MEDIA_DELETE:
                return mPredatorDbHelper.deleteAllMedia(selection, selectionArgs);
            case COLLECTIONS_DELETE:
                return mPredatorDbHelper.deleteAllCollections(selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
