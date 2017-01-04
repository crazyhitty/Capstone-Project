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

package com.crazyhitty.chdev.ks.predator;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/4/2017 10:39 AM
 * Description: Unavailable
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.crazyhitty.chdev.ks.predator.data.PredatorContract;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class PredatorProviderTest {
    @Test
    public void insertPostTest() {
        ContentValues values = new ContentValues();
        values.put(PredatorContract.PostsEntry.COLUMN_POST_ID, 1);
        values.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, 1);
        values.put(PredatorContract.PostsEntry.COLUMN_DAY, "day");
        values.put(PredatorContract.PostsEntry.COLUMN_NAME, "name");
        values.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, "tagline");
        values.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, 2);
        values.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, "created at");
        values.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, "disc url");
        values.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, "redirect url");
        values.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, 5);

        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.getContentResolver().insert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD, values);

        Cursor cursor = appContext.getContentResolver().query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                null,
                null,
                null,
                null);

        assertNotSame("Error: Record not inserted into posts table during insert", 0, cursor.getCount());
        cursor.close();
    }

    @Test
    public void deletePostsTest() {
        ContentValues values = new ContentValues();
        values.put(PredatorContract.PostsEntry.COLUMN_POST_ID, 1);
        values.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, 1);
        values.put(PredatorContract.PostsEntry.COLUMN_DAY, "day");
        values.put(PredatorContract.PostsEntry.COLUMN_NAME, "name");
        values.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, "tagline");
        values.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, 2);
        values.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, "created at");
        values.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, "disc url");
        values.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, "redirect url");
        values.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, 5);
        //values.put(PredatorContract.PostsEntry.COLUMN_IS_BOOKMARKED, 0);

        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.getContentResolver().insert(PredatorContract.PostsEntry.CONTENT_URI_POSTS_ADD, values);

        Cursor cursor = appContext.getContentResolver().query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                null,
                null,
                null,
                null);

        assertNotSame("Error: Record not inserted into posts table during insert", 0, cursor.getCount());

        appContext.getContentResolver().delete(PredatorContract.PostsEntry.CONTENT_URI_POSTS_DELETE, null, null);

        cursor = appContext.getContentResolver().query(PredatorContract.PostsEntry.CONTENT_URI_POSTS,
                null,
                null,
                null,
                null);

        assertEquals("Error: Record not deleted from posts table during deletion", 0, cursor.getCount());

        cursor.close();
    }
}
