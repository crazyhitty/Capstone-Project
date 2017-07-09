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
import android.database.Cursor;

import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/9/17 9:21 PM
 * Description: Unavailable
 */

public class PredatorContentValuesHelper {
    private PredatorContentValuesHelper() {

    }

    public static List<Post> getPostsFromCursor(Cursor cursor) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Post post = new Post();
            post.setId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_ID));
            post.setPostId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_POST_ID));
            post.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_CATEGORY_ID));
            post.setDay(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY));
            post.setName(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_NAME));
            post.setTagline(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE));
            post.setCommentCount(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT));
            post.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT));
            post.setCreatedAtMillis(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS));
            post.setDiscussionUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL));
            post.setRedirectUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_REDIRECT_URL));
            post.setVotesCount(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_VOTES_COUNT));
            post.setThumbnailImageUrl(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL));
            post.setThumbnailImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL));
            post.setScreenshotUrl300px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX));
            post.setScreenshotUrl850px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX));
            post.setUsername(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_NAME));
            post.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_USERNAME));
            post.setUserId(CursorUtils.getInt(cursor, PredatorContract.PostsEntry.COLUMN_USER_ID));
            post.setUserImageUrl100px(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX));
            post.setUserImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL));
            posts.add(post);
        }
        cursor.close();
        return posts;
    }

    public static ContentValues getContentValuesForPosts(PostsData.Posts post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, post.getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, post.getCategoryId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, post.getDay());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, post.getName());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, post.getTagline());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, post.getCommentsCount());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, post.getCreatedAt());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(post.getCreatedAt()));
        contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, post.getDiscussionUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, post.getRedirectUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, post.getVotesCount());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, post.getThumbnail().getImageUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, post.getThumbnail().getOriginalImageUrl());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, post.getScreenshotUrl().getValue300px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, post.getScreenshotUrl().getValue850px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, post.getUser().getName());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, post.getUser().getUsername());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, post.getUser().getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, post.getUser().getImageUrl().getValue100px());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, post.getUser().getImageUrl().getOriginal());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_FOR_DASHBOARD, 1);
        return contentValues;
    }

    public static ContentValues getContentValuesForHunterUser(int postId, PostsData.Posts.User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, user.getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, user.getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, user.getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, user.getHeadline());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, user.getWebsiteUrl());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, user.getImageUrl().getValue100px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, user.getImageUrl().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, postId);
        return contentValues;
    }

    public static ContentValues getContentValuesForMakerUser(int postId, PostsData.Posts.Makers maker) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, maker.getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, maker.getCreatedAt());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, maker.getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, maker.getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, maker.getHeadline());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, maker.getWebsiteUrl());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, maker.getImageUrlMaker().getValue48px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, maker.getImageUrlMaker().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, postId);
        return contentValues;
    }
}
