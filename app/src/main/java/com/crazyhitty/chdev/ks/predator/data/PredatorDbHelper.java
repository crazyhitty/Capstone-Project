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
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;

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
    private static final int DATABASE_VERSION = 8;

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
        db.execSQL(getCreateUsersTableSqlQuery());
        db.execSQL(getCreateCommentsTableSqlQuery());
        db.execSQL(getCreateInstallLinksTableSqlQuery());
        db.execSQL(getCreateMediaTableSqlQuery());
        db.execSQL(getCreateCollectionsTableSqlQuery());
        db.execSQL(getCreateCategoryTableSqlQuery());
    }

    private String getCreatePostsTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.PostsEntry.TABLE_NAME + "(" +
                PredatorContract.PostsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.PostsEntry.COLUMN_POST_ID + " INTEGER UNIQUE, " +
                PredatorContract.PostsEntry.COLUMN_COLLECTION_ID + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_DAY + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_NAME + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_TAGLINE + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_CREATED_AT + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_REDIRECT_URL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_VOTES_COUNT + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_NAME + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_USERNAME + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_ID + " INTEGER, " +
                PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL + " TEXT, " +
                PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION + " INTEGER DEFAULT 0);";
    }

    private String getCreateUsersTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.UsersEntry.TABLE_NAME + "(" +
                PredatorContract.UsersEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.UsersEntry.COLUMN_USER_ID + " INTEGER UNIQUE, " +
                PredatorContract.UsersEntry.COLUMN_CREATED_AT + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_NAME + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_USERNAME + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_WEBSITE_URL + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS + " TEXT, " +
                PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS + " TEXT);";
    }

    private String getCreateCommentsTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.CommentsEntry.TABLE_NAME + "(" +
                PredatorContract.CommentsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.CommentsEntry.COLUMN_COMMENT_ID + " INTEGER UNIQUE, " +
                PredatorContract.CommentsEntry.COLUMN_BODY + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_CREATED_AT + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_POST_ID + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_USER_ID + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_USER_CREATED_AT + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_USER_NAME + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_USER_USERNAME + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_USER_HEADLINE + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_100PX + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL + " TEXT, " +
                PredatorContract.CommentsEntry.COLUMN_VOTES + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_IS_STICKY + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_IS_MAKER + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_IS_HUNTER + " INTEGER, " +
                PredatorContract.CommentsEntry.COLUMN_IS_LIVE_GUEST + " INTEGER);";
    }

    private String getCreateInstallLinksTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.InstallLinksEntry.TABLE_NAME + "(" +
                PredatorContract.InstallLinksEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID + " INTEGER UNIQUE, " +
                PredatorContract.InstallLinksEntry.COLUMN_POST_ID + " INTEGER, " +
                PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT + " TEXT, " +
                PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK + " INTEGER, " +
                PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL + " TEXT, " +
                PredatorContract.InstallLinksEntry.COLUMN_PLATFORM + " TEXT);";
    }

    private String getCreateMediaTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.MediaEntry.TABLE_NAME + "(" +
                PredatorContract.MediaEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.MediaEntry.COLUMN_MEDIA_ID + " INTEGER UNIQUE, " +
                PredatorContract.MediaEntry.COLUMN_POST_ID + " INTEGER, " +
                PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE + " TEXT, " +
                PredatorContract.MediaEntry.COLUMN_PLATFORM + " TEXT, " +
                PredatorContract.MediaEntry.COLUMN_VIDEO_ID + " TEXT, " +
                PredatorContract.MediaEntry.COLUMN_ORIGINAL_WIDTH + " INTEGER, " +
                PredatorContract.MediaEntry.COLUMN_ORIGINAL_HEIGHT + " INTEGER, " +
                PredatorContract.MediaEntry.COLUMN_IMAGE_URL + " TEXT);";
    }

    private String getCreateCollectionsTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.CollectionsEntry.TABLE_NAME + "(" +
                PredatorContract.CollectionsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID + " INTEGER UNIQUE, " +
                PredatorContract.CollectionsEntry.COLUMN_NAME + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_TITLE + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_CREATED_AT + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_UPDATED_AT + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_FEATURED_AT + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_SUBSCRIBER_COUNT + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                PredatorContract.CollectionsEntry.COLUMN_COLLECTION_URL + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_POST_COUNTS + " INTEGER, " +
                PredatorContract.CollectionsEntry.COLUMN_BACKGROUND_IMAGE_URL + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_USER_NAME + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_USER_USERNAME + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_USER_ID + " INTEGER, " +
                PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_100PX + " TEXT, " +
                PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL + " TEXT);";
    }

    private String getCreateCategoryTableSqlQuery() {
        return "CREATE TABLE " + PredatorContract.CategoryEntry.TABLE_NAME + "(" +
                PredatorContract.CategoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID + " INTEGER UNIQUE, " +
                PredatorContract.CategoryEntry.COLUMN_SLUG + " TEXT, " +
                PredatorContract.CategoryEntry.COLUMN_NAME + " TEXT, " +
                PredatorContract.CategoryEntry.COLUMN_COLOR + " TEXT, " +
                PredatorContract.CategoryEntry.COLUMN_ITEM_NAME + " TEXT);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Remove existing tables from database.
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.PostsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.UsersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.CommentsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.InstallLinksEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.MediaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.CollectionsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PredatorContract.CategoryEntry.TABLE_NAME);

        // Recreate the tables.
        onCreate(db);
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
            Logger.e(TAG, "Error while trying to add post to database", e);
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
            Logger.e(TAG, "Error while trying to delete posts from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }

    public int addOrUpdateUser(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            int id = contentValues.getAsInteger(PredatorContract.UsersEntry.COLUMN_USER_ID);

            Cursor cursor = db.query(PredatorContract.UsersEntry.TABLE_NAME,
                    null,
                    PredatorContract.UsersEntry.COLUMN_USER_ID + "=" + contentValues.getAsInteger(PredatorContract.UsersEntry.COLUMN_USER_ID),
                    null,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();

                String makerPostIds = CursorUtils.getString(cursor, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);
                if (contentValues.containsKey(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS) &&
                        !TextUtils.isEmpty(makerPostIds) &&
                        !TextUtils.isEmpty(contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS))) {
                    makerPostIds += "," + contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);
                    contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, makerPostIds);
                } else if (contentValues.containsKey(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS) &&
                        TextUtils.isEmpty(makerPostIds) &&
                        !TextUtils.isEmpty(contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS))) {
                    makerPostIds = contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);
                    contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, makerPostIds);
                }

                String hunterPostIds = CursorUtils.getString(cursor, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
                if (contentValues.containsKey(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS) &&
                        !TextUtils.isEmpty(hunterPostIds) &&
                        !TextUtils.isEmpty(contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS))) {
                    hunterPostIds += "," + contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
                    contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, hunterPostIds);
                } else if (contentValues.containsKey(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS) &&
                        TextUtils.isEmpty(hunterPostIds) &&
                        !TextUtils.isEmpty(contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS))) {
                    hunterPostIds = contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
                    contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, hunterPostIds);
                }

                String votedPostIds = CursorUtils.getString(cursor, PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS);
                if (contentValues.containsKey(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS) &&
                        !TextUtils.isEmpty(votedPostIds) &&
                        !TextUtils.isEmpty(contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS))) {
                    votedPostIds += "," + contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS);
                    contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, votedPostIds);
                } else if (contentValues.containsKey(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS) &&
                        TextUtils.isEmpty(votedPostIds) &&
                        !TextUtils.isEmpty(contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS))) {
                    votedPostIds = contentValues.getAsString(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS);
                    contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, votedPostIds);
                }

                cursor.close();

                db.update(PredatorContract.UsersEntry.TABLE_NAME,
                        contentValues,
                        PredatorContract.UsersEntry.COLUMN_USER_ID + "=" + id,
                        null);
            } else {
                db.insertOrThrow(PredatorContract.UsersEntry.TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to add/update user to database", e);
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.UsersEntry.COLUMN_USER_ID);
    }

    public Cursor getUsers(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.UsersEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllUsers(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.UsersEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to delete users from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }

    public int addComment(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            db.insertOrThrow(PredatorContract.CommentsEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to add comment to database", e);
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.CommentsEntry.COLUMN_COMMENT_ID);
    }

    public Cursor getComments(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.CommentsEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllComments(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.CommentsEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to delete comments from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }

    public int addInstallLinks(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            db.insertOrThrow(PredatorContract.InstallLinksEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to add install link to database", e);
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.InstallLinksEntry.COLUMN_POST_ID);
    }

    public Cursor getInstallLinks(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.InstallLinksEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllInstallLinks(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.InstallLinksEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to delete install links from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }

    public int addMedia(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            db.insertOrThrow(PredatorContract.MediaEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to add media to database", e);
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.MediaEntry.COLUMN_MEDIA_ID);
    }

    public Cursor getMedia(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.MediaEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllMedia(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.MediaEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to delete media from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }

    public int addCollection(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            db.insertOrThrow(PredatorContract.CollectionsEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to add collection to database", e);
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID);
    }

    public Cursor getCollections(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.CollectionsEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllCollections(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.CollectionsEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to delete collections from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }

    public int addCategory(ContentValues contentValues) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            db.insertOrThrow(PredatorContract.CategoryEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to add category to database", e);
        } finally {
            db.endTransaction();
        }
        return contentValues.getAsInteger(PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID);
    }

    public Cursor getCategories(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getReadableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        return db.query(PredatorContract.CategoryEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    public int deleteAllCategories(String selection, String[] selectionArgs) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        int numOfRowsAffected = 0;
        try {
            numOfRowsAffected = db.delete(PredatorContract.CategoryEntry.TABLE_NAME,
                    selection,
                    selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, "Error while trying to delete categories from database", e);
        } finally {
            db.endTransaction();
        }
        return numOfRowsAffected;
    }
}