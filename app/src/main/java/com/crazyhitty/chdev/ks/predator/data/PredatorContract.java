package com.crazyhitty.chdev.ks.predator.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/3/2017 10:20 AM
 * Description: Manages all the tables created for the sqlite database.
 */

public class PredatorContract {

    /**
     * Posts table definition.
     */
    public static class PostsEntry implements BaseColumns {
        /**
         * A list of possible paths that will be appended to the base URI for PostsEntry table.
         */
        public static final String PATH_POSTS = "posts";
        public static final String PATH_POSTS_ADD = "posts_add";
        public static final String PATH_POSTS_DELETE_ALL = "posts_delete";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_POSTS = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS)
                .build();

        public static final Uri CONTENT_URI_POSTS_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS_ADD)
                .build();

        public static final Uri CONTENT_URI_POSTS_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_POSTS + "/" + PATH_POSTS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_POSTS + "/" + PATH_POSTS;

        public static final String TABLE_NAME = "posts_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_DISCUSSION_URL = "discussion_url";
        public static final String COLUMN_REDIRECT_URL = "redirect_url";
        public static final String COLUMN_VOTES_COUNT = "votes_vount";
        public static final String COLUMN_THUMBNAIL_IMAGE_URL = "thumbnail_image_url";
        public static final String COLUMN_SCREENSHOT_URL_300PX = "screenshot_url_300px";
        public static final String COLUMN_SCREENSHOT_URL_850PX = "screenshot_url_800px";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_USERNAME = "user_username";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_IMAGE_URL_48PX = "user_image_url_48px";
        public static final String COLUMN_USER_IMAGE_URL_ORIGINAL = "user_image_url_original";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildPostsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_POSTS, id);
        }

    }
}
