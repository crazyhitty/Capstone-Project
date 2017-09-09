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
        // A list of possible paths that will be appended to the base URI for PostsEntry table.
        public static final String PATH_POSTS = "posts";
        public static final String PATH_POSTS_UPDATE = "posts_update";
        public static final String PATH_POSTS_ADD = "posts_add";
        public static final String PATH_POSTS_DELETE_ALL = "posts_delete";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_POSTS = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS)
                .build();

        public static final Uri CONTENT_URI_POSTS_UPDATE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS_UPDATE)
                .build();

        public static final Uri CONTENT_URI_POSTS_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS_ADD)
                .build();

        public static final Uri CONTENT_URI_POSTS_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POSTS_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_POSTS + "/" + PATH_POSTS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_POSTS + "/" + PATH_POSTS;

        public static final String TABLE_NAME = "posts_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_COLLECTION_ID = "collection_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_CREATED_AT_MILLIS = "created_at_millis";
        public static final String COLUMN_DISCUSSION_URL = "discussion_url";
        public static final String COLUMN_REDIRECT_URL = "redirect_url";
        public static final String COLUMN_VOTES_COUNT = "votes_count";
        public static final String COLUMN_THUMBNAIL_IMAGE_URL = "thumbnail_image_url";
        public static final String COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL = "thumbnail_image_url_original";
        public static final String COLUMN_SCREENSHOT_URL_300PX = "screenshot_url_300px";
        public static final String COLUMN_SCREENSHOT_URL_850PX = "screenshot_url_800px";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_USERNAME = "user_username";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_IMAGE_URL_100PX = "user_image_url_100px";
        public static final String COLUMN_USER_IMAGE_URL_ORIGINAL = "user_image_url_original";
        public static final String COLUMN_IS_IN_COLLECTION = "is_in_collection";
        public static final String COLUMN_FOR_DASHBOARD = "for_dashboard";
        public static final String COLUMN_NOTIFICATION_SHOWN = "notification_shown";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildPostsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_POSTS, id);
        }

    }

    /**
     * User table definition.
     */
    public static class UsersEntry implements BaseColumns {
        // A list of possible paths that will be appended to the base URI for UsersEntry table.
        public static final String PATH_USERS = "users";
        public static final String PATH_USERS_ADD = "users_add";
        public static final String PATH_USERS_DELETE_ALL = "users_delete_all";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_USERS = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_USERS)
                .build();

        public static final Uri CONTENT_URI_USERS_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_USERS_ADD)
                .build();

        public static final Uri CONTENT_URI_USERS_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_USERS_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_USERS + "/" + PATH_USERS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_USERS + "/" + PATH_USERS;

        public static final String TABLE_NAME = "users_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_WEBSITE_URL = "website_url";
        public static final String COLUMN_IMAGE_URL_100PX = "image_url_100px";
        public static final String COLUMN_IMAGE_URL_ORIGINAL = "image_url_original";
        public static final String COLUMN_MAKER_POST_IDS = "maker_post_ids";
        public static final String COLUMN_HUNTER_POST_IDS = "hunter_post_ids";
        public static final String COLUMN_VOTED_POST_IDS = "voted_post_ids";
        public static final String COLUMN_FOLLOWER_USER_IDS = "follower_user_ids";
        public static final String COLUMN_FOLLOWING_USER_IDS = "following_user_ids";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildUsersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_USERS, id);
        }
    }

    /**
     * Comments table definition.
     */
    public static class CommentsEntry implements BaseColumns {
        // A list of possible paths that will be appended to the base URI for CommentsEntry table.
        public static final String PATH_COMMENTS = "comments";
        public static final String PATH_COMMENTS_ADD = "comments_add";
        public static final String PATH_COMMENTS_DELETE_ALL = "comments_delete_all";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_COMMENTS = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_COMMENTS)
                .build();

        public static final Uri CONTENT_URI_COMMENTS_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_COMMENTS_ADD)
                .build();

        public static final Uri CONTENT_URI_COMMENTS_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_COMMENTS_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_COMMENTS + "/" + PATH_COMMENTS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_COMMENTS + "/" + PATH_COMMENTS;

        public static final String TABLE_NAME = "comments_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_COMMENT_ID = "comment_id";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_CREATED_AT_MILLIS = "created_at_millis";
        public static final String COLUMN_PARENT_COMMENT_ID = "parent_comment_id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_CREATED_AT = "user_created_at";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_USERNAME = "user_username";
        public static final String COLUMN_USER_HEADLINE = "user_headline";
        public static final String COLUMN_USER_IMAGE_URL_100PX = "user_image_url_100px";
        public static final String COLUMN_USER_IMAGE_URL_ORIGINAL = "user_image_url_original";
        public static final String COLUMN_USER_WEBSITE_URL = "user_website_url";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_IS_STICKY = "is_sticky";
        public static final String COLUMN_IS_MAKER = "is_maker";
        public static final String COLUMN_IS_HUNTER = "is_hunter";
        public static final String COLUMN_IS_LIVE_GUEST = "is_live_guest";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildCommentsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_COMMENTS, id);
        }
    }

    /**
     * InstallLinksEntry table definition.
     */
    public static class InstallLinksEntry implements BaseColumns {
        // A list of possible paths that will be appended to the base URI for InstallLinksEntry table.
        public static final String PATH_INSTALL_LINKS = "install_links";
        public static final String PATH_INSTALL_LINKS_ADD = "install_links_add";
        public static final String PATH_INSTALL_LINKS_DELETE_ALL = "install_links_delete_all";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_INSTALL_LINKS = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_INSTALL_LINKS)
                .build();

        public static final Uri CONTENT_URI_INSTALL_LINKS_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_INSTALL_LINKS_ADD)
                .build();

        public static final Uri CONTENT_URI_INSTALL_LINKS_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_INSTALL_LINKS_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_INSTALL_LINKS + "/" + PATH_INSTALL_LINKS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_INSTALL_LINKS + "/" + PATH_INSTALL_LINKS;

        public static final String TABLE_NAME = "install_links_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_INSTALL_LINK_ID = "install_link_id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_IS_PRIMARY_LINK = "is_primary_link";
        public static final String COLUMN_REDIRECT_URL = "redirect_url";
        public static final String COLUMN_PLATFORM = "platform";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildInstallLinksUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_INSTALL_LINKS, id);
        }
    }

    /**
     * MediaEntry table definition.
     */
    public static class MediaEntry implements BaseColumns {
        // A list of possible paths that will be appended to the base URI for MediaEntry table.
        public static final String PATH_MEDIA = "media";
        public static final String PATH_MEDIA_ADD = "media_add";
        public static final String PATH_MEDIA_DELETE_ALL = "media_delete_all";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_MEDIA = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MEDIA)
                .build();

        public static final Uri CONTENT_URI_MEDIA_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MEDIA_ADD)
                .build();

        public static final Uri CONTENT_URI_MEDIA_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MEDIA_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_MEDIA + "/" + PATH_MEDIA;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_MEDIA + "/" + PATH_MEDIA;

        public static final String TABLE_NAME = "media_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MEDIA_ID = "media_id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_MEDIA_TYPE = "media_type";
        public static final String COLUMN_PLATFORM = "platform";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_ORIGINAL_WIDTH = "original_width";
        public static final String COLUMN_ORIGINAL_HEIGHT = "original_height";
        public static final String COLUMN_IMAGE_URL = "image_url";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildMediaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_MEDIA, id);
        }
    }

    /**
     * CollectionsEntry table definition.
     */
    public static class CollectionsEntry implements BaseColumns {
        // A list of possible paths that will be appended to the base URI for CollectionsEntry table.
        public static final String PATH_COLLECTIONS = "collections";
        public static final String PATH_COLLECTIONS_ADD = "collections_add";
        public static final String PATH_COLLECTIONS_DELETE_ALL = "collections_delete_all";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_COLLECTIONS = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_COLLECTIONS)
                .build();

        public static final Uri CONTENT_URI_COLLECTIONS_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_COLLECTIONS_ADD)
                .build();

        public static final Uri CONTENT_URI_COLLECTIONS_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_COLLECTIONS_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_COLLECTIONS + "/" + PATH_COLLECTIONS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_COLLECTIONS + "/" + PATH_COLLECTIONS;

        public static final String TABLE_NAME = "collections_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_COLLECTION_ID = "collection_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_FEATURED_AT = "featured_at";
        public static final String COLUMN_SUBSCRIBER_COUNT = "subscriber_count";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_COLLECTION_URL = "collection_url";
        public static final String COLUMN_POST_COUNTS = "post_counts";
        public static final String COLUMN_BACKGROUND_IMAGE_URL = "background_image_url";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_USERNAME = "user_username";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_IMAGE_URL_100PX = "user_image_url_100px";
        public static final String COLUMN_USER_IMAGE_URL_ORIGINAL = "user_image_url_original";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildCollectionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_COLLECTIONS, id);
        }
    }

    /**
     * CategoryEntry table definition.
     */
    public static class CategoryEntry implements BaseColumns {
        // A list of possible paths that will be appended to the base URI for CategoryEntry table.
        public static final String PATH_CATEGORY = "category";
        public static final String PATH_CATEGORY_ADD = "category_add";
        public static final String PATH_CATEGORY_DELETE_ALL = "category_delete_all";

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI_CATEGORY = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_CATEGORY)
                .build();

        public static final Uri CONTENT_URI_CATEGORY_ADD = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_CATEGORY_ADD)
                .build();

        public static final Uri CONTENT_URI_CATEGORY_DELETE = PredatorDbHelper.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_CATEGORY_DELETE_ALL)
                .build();

        // These are special type prefixes that specify if a URI returns a list or a specific item.
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI_CATEGORY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI_CATEGORY + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME = "category_table";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_SLUG = "slug";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_ITEM_NAME = "item_name";

        // Define a function to build a URI to find a specific post by it's identifier
        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_CATEGORY, id);
        }
    }
}
