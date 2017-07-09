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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/9/17 4:09 PM
 * Description: Unavailable
 */

public class PredatorDatabase {
    private static PredatorDatabase sPredatorDatabase;

    private ContentResolver mContentResolver;

    public static void init(Context context) {
        sPredatorDatabase = new PredatorDatabase(context);
    }

    public static PredatorDatabase getInstance() {
        if (sPredatorDatabase == null) {
            throw new NullPointerException("Unable to get PredatorDatabase object as it is null. " +
                    "Looks like you haven't used PredatorDatabase.init().");
        }
        return sPredatorDatabase;
    }

    private PredatorDatabase(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public void insertPost(ContentValues contentValues) {
        mContentResolver.insert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD, contentValues);
    }

    public void insertUser(ContentValues contentValues) {
        mContentResolver.insert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD, contentValues);
    }

    public List<Post> getPosts() {
        Cursor cursor = MainApplication.getContentResolverInstance()
                .query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                        null,
                        PredatorContract.PostsEntry.COLUMN_FOR_DASHBOARD + "=1",
                        null,
                        PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
        return PredatorContentValuesHelper.getPostsFromCursor(cursor);
    }

    public void deleteAllPosts() {
        mContentResolver.delete(PredatorContract.PostsEntry.CONTENT_URI_POSTS_DELETE,
                        null,
                        null);
    }

    public void deleteAllUsers() {
        mContentResolver.delete(PredatorContract.UsersEntry.CONTENT_URI_USERS_DELETE,
                null,
                null);
    }

    public void deleteAllComments() {
        mContentResolver.delete(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS_DELETE,
                null,
                null);
    }

    public void deleteAllMedia() {
        mContentResolver.delete(PredatorContract.MediaEntry.CONTENT_URI_MEDIA_DELETE,
                null,
                null);
    }
}
