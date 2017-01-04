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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/3/2017 12:56 PM
 * Description: Unavailable
 */

public class PredatorDbHelper extends SQLiteOpenHelper {
    /**
     * The Content Authority is a name for the entire content provider, similar to the relationship
     * between a domain name and its website. A convenient string to use for content authority is
     * the package name for the app, since it is guaranteed to be unique on the device.
     */
    public static final String CONTENT_AUTHORITY = "com.crazyhitty.chdev.ks.predator";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String DATABASE_NAME = "predator.db";
    private static final int DATABASE_VERSION = 1;

    private static PredatorDbHelper sPredatorDbHelper;

    public PredatorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static PredatorDbHelper getInstance(Context context) {
        if (sPredatorDbHelper == null) {
            sPredatorDbHelper = new PredatorDbHelper(context.getApplicationContext());
        }
        return sPredatorDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreatePostsTableSqlQuery());
    }

    private String getCreatePostsTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.PostsEntry.TABLE_NAME + "(" +
                PredatorContract.PostsEntry.COLUMN_POST_ID + " INTEGER PRIMARY KEY, " +
                PredatorContract.PostsEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_DAY + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_NAME + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_TAGLINE + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_CREATED_AT + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_REDIRECT_URL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_VOTES_COUNT + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_NAME + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_USERNAME + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_ID + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_48PX + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL + " TEXT);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.PostsEntry.TABLE_NAME);
    }

    public int addPost(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            db.insertOrThrow(PredatorContract.PostsEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add posts to database");
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.PostsEntry.COLUMN_POST_ID);
    }

    public Cursor getPosts(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.PostsEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllPosts(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.PostsEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add posts to database");
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }
}