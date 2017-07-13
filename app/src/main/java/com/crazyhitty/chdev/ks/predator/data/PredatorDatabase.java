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
import com.crazyhitty.chdev.ks.predator.models.Comment;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.Logger;
import com.crazyhitty.chdev.ks.predator.utils.UsersComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.crazyhitty.chdev.ks.predator.utils.CursorUtils.getString;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/9/17 4:09 PM
 * Description: Unavailable
 */

public class PredatorDatabase {
    private static final String TAG = PredatorDatabase.class.getSimpleName();

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

    public void insertUsers(ContentValues[] bulkContentValues) {
        mContentResolver.bulkInsert(PredatorContract.UsersEntry.CONTENT_URI_USERS_ADD, bulkContentValues);
    }

    public void insertBulkMedia(ContentValues[] bulkContentValues) {
        mContentResolver.bulkInsert(PredatorContract.MediaEntry.CONTENT_URI_MEDIA_ADD,
                bulkContentValues);
    }

    public void insertComment(ContentValues contentValues) {
        mContentResolver.insert(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS_ADD,
                contentValues);
    }

    public void insertInstallLinks(ContentValues[] bulkContentValues) {
        mContentResolver.bulkInsert(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS_ADD,
                        bulkContentValues);
    }

    public List<Post> getPosts() {
        Cursor cursor = mContentResolver.query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                        null,
                        PredatorContract.PostsEntry.COLUMN_FOR_DASHBOARD + "=1",
                        null,
                        PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS + " DESC");
        List<Post> posts = PredatorDbValuesHelper.getPostsFromCursor(cursor);
        closeCursor(cursor);
        return posts;
    }

    public PostDetails getPostDetails(int postId) {
        Cursor cursor = mContentResolver.query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                        null,
                        PredatorContract.PostsEntry.COLUMN_POST_ID + "=" + postId,
                        null,
                        null);

        PostDetails postDetails = null;
        if (cursor != null && cursor.getCount() != 0) {
            postDetails = PredatorDbValuesHelper.getPostDetailsFromCursor(cursor);
            postDetails.setCategory(getCategoryName(postDetails.getCategoryId()));
        }
        closeCursor(cursor);

        return postDetails;
    }

    public List<User> getUsers(int postId) {
        Cursor cursorUsers = mContentResolver.query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                        null,
                        PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS + "" +
                                " LIKE '%" +
                                postId +
                                "%' OR " +
                                PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS +
                                " LIKE '%" +
                                postId +
                                "%'",
                        null,
                        null);

        List<User> users = new ArrayList<>();
        if (cursorUsers != null && cursorUsers.getCount() != 0) {
            users = PredatorDbValuesHelper.getUsersFromCursor(cursorUsers, postId);
            // Sort the users list on basis of user type.
            Collections.sort(users, new UsersComparator());
        }
        closeCursor(cursorUsers);

        return users;
    }

    public List<User> getAllUsersForPost(int postId) {
        Cursor usersCursor = mContentResolver.query(PredatorContract.UsersEntry.CONTENT_URI_USERS,
                        null,
                        PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS +
                                " LIKE '%" +
                                postId +
                                "%' OR " +
                                PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS +
                                " LIKE '%" +
                                postId +
                                "%' OR " +
                                PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS +
                                " LIKE '%" +
                                postId +
                                "%'",
                        null,
                        null);

        List<User> users = new ArrayList<>();
        if (usersCursor != null && usersCursor.getCount() != 0) {
            users = PredatorDbValuesHelper.getUsersFromCursor(usersCursor, postId);
            // Sort the users list on basis of user type.
            Collections.sort(users, new UsersComparator());
        }
        closeCursor(usersCursor);

        return users;
    }

    public String getCategoryName(int categoryId) {
        Cursor cursor = mContentResolver.query(PredatorContract.CategoryEntry.CONTENT_URI_CATEGORY,
                        null,
                        PredatorContract.CategoryEntry.COLUMN_CATEGORY_ID + " = " + categoryId,
                        null,
                        null);

        cursor.moveToFirst();
        String categoryName = getString(cursor, PredatorContract.CategoryEntry.COLUMN_NAME);
        closeCursor(cursor);

        return categoryName;
    }

    public List<Media> getMediaForPost(int postId) {
        // Query the media available.
        Cursor mediaCursor = mContentResolver.query(PredatorContract.MediaEntry.CONTENT_URI_MEDIA,
                        null,
                        PredatorContract.MediaEntry.COLUMN_POST_ID + " = " + postId,
                        null,
                        null);

        List<Media> media = new ArrayList<>();
        if (mediaCursor != null && mediaCursor.getCount() != 0) {
            media = PredatorDbValuesHelper.getMediaFromCursor(mediaCursor);
        }
        closeCursor(mediaCursor);

        return media;
    }

    public List<Comment> getCommentsForPost(int postId, int parentCommentId, List<Comment> comments, int childSpaces) {
        String sortOrder = null;
        if (childSpaces == 0) {
            // If top level comments are being fetched, then sort them according to the vote count.
            sortOrder = PredatorContract.CommentsEntry.COLUMN_VOTES + " DESC";
        } else {
            // If child comment, then sort according the time of their creation.
            sortOrder = PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS + " ASC";
        }

        Cursor cursor = mContentResolver.query(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS,
                        null,
                        PredatorContract.CommentsEntry.COLUMN_POST_ID + " = " + postId + " AND " +
                                PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID + " = " + parentCommentId,
                        null,
                        sortOrder);
        if (cursor != null && cursor.getCount() != 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);

                Comment comment = new Comment();
                comment.setId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_ID));
                comment.setCommentId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_COMMENT_ID));
                comment.setParentCommentId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID));
                comment.setBody(getString(cursor, PredatorContract.CommentsEntry.COLUMN_BODY));
                comment.setCreatedAt(getString(cursor, PredatorContract.CommentsEntry.COLUMN_CREATED_AT));
                comment.setCreatedAtMillis(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS));
                comment.setPostId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_POST_ID));
                comment.setUserId(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_USER_ID));
                comment.setUsername(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_NAME));
                comment.setUsernameAlternative(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_USERNAME));
                comment.setUserHeadline(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_HEADLINE));
                comment.setUserImageThumbnailUrl(getString(cursor, PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_100PX));
                comment.setVotes(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_VOTES));
                comment.setSticky(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_STICKY) == 1);
                comment.setMaker(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_MAKER) == 1);
                comment.setHunter(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_HUNTER) == 1);
                comment.setLiveGuest(CursorUtils.getInt(cursor, PredatorContract.CommentsEntry.COLUMN_IS_LIVE_GUEST) == 1);
                comment.setChildSpaces(childSpaces);
                comments.add(comment);

                getCommentsForPost(postId, comment.getCommentId(), comments, comment.getChildSpaces() + 1);
            }
        }
        closeCursor(cursor);

        return comments;
    }

    public List<InstallLink> getInstallLinksForPost(int postId) {
        Cursor installLinksCursor = mContentResolver.query(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS,
                        null,
                        PredatorContract.InstallLinksEntry.COLUMN_POST_ID + " = " + postId,
                        null,
                        null);

        List<InstallLink> installLinks = new ArrayList<>();
        if (installLinksCursor != null && installLinksCursor.getCount() != 0) {
            installLinks = PredatorDbValuesHelper.getInstallLinksFromCursor(installLinksCursor);
        }
        closeCursor(installLinksCursor);

        return installLinks;
    }

    public void deleteMediaForPost(int postId) {
        mContentResolver.delete(PredatorContract.MediaEntry.CONTENT_URI_MEDIA_DELETE,
                        PredatorContract.MediaEntry.COLUMN_POST_ID + " = " + postId,
                        null);
    }

    public void deleteCommentsForPost(int postId) {
        mContentResolver.delete(PredatorContract.CommentsEntry.CONTENT_URI_COMMENTS_DELETE,
                        PredatorContract.CommentsEntry.COLUMN_POST_ID + " = " + postId,
                        null);
    }

    public void deleteInstallLinksForPost(int postId) {
        mContentResolver.delete(PredatorContract.InstallLinksEntry.CONTENT_URI_INSTALL_LINKS_DELETE,
                        PredatorContract.InstallLinksEntry.COLUMN_POST_ID + " = " + postId,
                        null);
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

    /**
     * This method closes the cursor.
     *
     * @param cursor    Cursor to be closed.
     * @return
     * True, if closed successfully, otherwise false.
     */
    private boolean closeCursor(Cursor cursor) {
        if (cursor == null) {
            Logger.e(TAG, "closeCursor: Unable to close cursor as it is null");
            return false;
        }

        if (cursor.isClosed()) {
            return true;
        }

        cursor.close();
        return true;
    }
}
