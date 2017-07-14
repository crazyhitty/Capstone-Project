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
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.predator.MainApplication;
import com.crazyhitty.chdev.ks.predator.models.Collection;
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CollectionsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostCommentsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostDetailsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.UserProfileData;

import java.util.ArrayList;
import java.util.List;

import static com.crazyhitty.chdev.ks.predator.utils.CursorUtils.getInt;
import static com.crazyhitty.chdev.ks.predator.utils.CursorUtils.getString;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     7/9/17 9:21 PM
 * Description: Unavailable
 */

public class PredatorDbValuesHelper {
    private PredatorDbValuesHelper() {

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

        return posts;
    }

    public static PostDetails getPostDetailsFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        String title = getString(cursor, PredatorContract.PostsEntry.COLUMN_NAME);
        String description = getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE);
        String day = getString(cursor, PredatorContract.PostsEntry.COLUMN_DAY);
        String date = getString(cursor, PredatorContract.PostsEntry.COLUMN_CREATED_AT);
        String backdropUrl = getString(cursor, PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL);
        String redirectUrl = getString(cursor, PredatorContract.PostsEntry.COLUMN_REDIRECT_URL);
        String tagline = getString(cursor, PredatorContract.PostsEntry.COLUMN_TAGLINE);
        String discussionUrl = getString(cursor, PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL);
        int categoryId = getInt(cursor, PredatorContract.PostsEntry.COLUMN_CATEGORY_ID);
        int voteCount = getInt(cursor, PredatorContract.PostsEntry.COLUMN_VOTES_COUNT);

        PostDetails postDetails = new PostDetails();
        postDetails.setTitle(title);
        postDetails.setDescription(description);
        postDetails.setDay(day);
        postDetails.setDate(date);
        postDetails.setBackdropUrl(backdropUrl);
        postDetails.setRedirectUrl(redirectUrl);
        postDetails.setTagline(tagline);
        postDetails.setDiscussionUrl(discussionUrl);
        postDetails.setCategoryId(categoryId);
        postDetails.setVoteCount(voteCount);

        return postDetails;
    }

    public static List<User> getUsersFromCursor(Cursor cursorUsers, int postId) {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < cursorUsers.getCount(); i++) {
            cursorUsers.moveToPosition(i);

            User user = new User();
            user.setId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_ID));
            user.setUserId(CursorUtils.getInt(cursorUsers, PredatorContract.UsersEntry.COLUMN_USER_ID));
            user.setName(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_NAME));
            user.setUsername(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_USERNAME));
            user.setThumbnail(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
            user.setImage(getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

            // Check if user is hunter, maker, both or a user who just
            // upvoted this post.
            String hunterPostIds = getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS);
            String makersPostIds = getString(cursorUsers, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS);

            if (!TextUtils.isEmpty(hunterPostIds) &&
                    !TextUtils.isEmpty(makersPostIds) &&
                    hunterPostIds.contains(String.valueOf(postId)) &&
                    makersPostIds.contains(String.valueOf(postId))) {
                // User is both hunter and maker.
                user.setType(User.TYPE.BOTH);
            } else if (!TextUtils.isEmpty(hunterPostIds) &&
                    hunterPostIds.contains(String.valueOf(postId))) {
                // User is hunter.
                user.setType(User.TYPE.HUNTER);
            } else if (!TextUtils.isEmpty(makersPostIds) &&
                    makersPostIds.contains(String.valueOf(postId))) {
                // User is maker.
                user.setType(User.TYPE.MAKER);
            } else {
                // User upvoted this post.
                user.setType(User.TYPE.UPVOTER);
            }
            users.add(user);
        }

        return users;
    }

    public static List<User> getUsersFromCursor(Cursor cursor) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            User user = new User();
            user.setId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_ID));
            user.setUserId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_USER_ID));
            user.setName(getString(cursor, PredatorContract.UsersEntry.COLUMN_NAME));
            user.setHeadline(getString(cursor, PredatorContract.UsersEntry.COLUMN_HEADLINE));
            user.setUsername(getString(cursor, PredatorContract.UsersEntry.COLUMN_USERNAME));
            user.setThumbnail(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
            user.setImage(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));

            users.add(user);
        }

        cursor.close();

        return users;
    }

    public static User getUserFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        User user = new User();
        user.setId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_ID));
        user.setUserId(CursorUtils.getInt(cursor, PredatorContract.UsersEntry.COLUMN_USER_ID));
        user.setName(getString(cursor, PredatorContract.UsersEntry.COLUMN_NAME));
        user.setUsername(getString(cursor, PredatorContract.UsersEntry.COLUMN_USERNAME));
        user.setHeadline(getString(cursor, PredatorContract.UsersEntry.COLUMN_HEADLINE));
        user.setWebsiteUrl(getString(cursor, PredatorContract.UsersEntry.COLUMN_WEBSITE_URL));
        user.setThumbnail(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX));
        user.setImage(getString(cursor, PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL));
        user.setHuntedPostIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS));
        user.setMadePostIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS));
        user.setVotedPostIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS));
        user.setFollowerUserIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_FOLLOWER_USER_IDS));
        user.setFollowingUserIds(getString(cursor, PredatorContract.UsersEntry.COLUMN_FOLLOWING_USER_IDS));

        return user;
    }

    public static List<Media> getMediaFromCursor(Cursor mediaCursor) {
        List<Media> media = new ArrayList<Media>();

        for (int i = 0; i < mediaCursor.getCount(); i++) {
            mediaCursor.moveToPosition(i);

            Media mediaObj = new Media();
            mediaObj.setId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ID));
            mediaObj.setMediaId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_MEDIA_ID));
            mediaObj.setPostId(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_POST_ID));
            mediaObj.setMediaType(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE));
            mediaObj.setPlatform(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_PLATFORM));
            mediaObj.setVideoId(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_VIDEO_ID));
            mediaObj.setOriginalWidth(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ORIGINAL_WIDTH));
            mediaObj.setOriginalHeight(CursorUtils.getInt(mediaCursor, PredatorContract.MediaEntry.COLUMN_ORIGINAL_HEIGHT));
            mediaObj.setImageUrl(getString(mediaCursor, PredatorContract.MediaEntry.COLUMN_IMAGE_URL));

            media.add(mediaObj);
        }

        return media;
    }

    public static List<InstallLink> getInstallLinksFromCursor(Cursor installLinksCursor) {
        List<InstallLink> installLinks = new ArrayList<InstallLink>();

        for (int i = 0; i < installLinksCursor.getCount(); i++) {
            installLinksCursor.moveToPosition(i);

            InstallLink installLink = new InstallLink();
            installLink.setId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_ID));
            installLink.setInstallLinkId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID));
            installLink.setPostId(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_POST_ID));
            installLink.setCreatedAt(getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT));
            installLink.setPrimaryLink(CursorUtils.getInt(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK) == 1);
            installLink.setRedirectUrl(getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL));
            installLink.setPlatform(getString(installLinksCursor, PredatorContract.InstallLinksEntry.COLUMN_PLATFORM));

            installLinks.add(installLink);
        }

        return installLinks;
    }

    public static List<Collection> getCollectionsFromCursor(Cursor cursor) {
        List<Collection> collections = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            Collection collection = new Collection();
            collection.setId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_ID));
            collection.setCollectionId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID));
            collection.setName(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_NAME));
            collection.setTitle(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_TITLE));
            collection.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_CREATED_AT));
            collection.setUpdatedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_UPDATED_AT));
            collection.setFeaturedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_FEATURED_AT));
            collection.setSubscriberCount(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_SUBSCRIBER_COUNT));
            collection.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_CATEGORY_ID));
            collection.setCollectionUrl(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_COLLECTION_URL));
            collection.setPostCounts(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_POST_COUNTS));
            collection.setBackgroundImageUrl(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_BACKGROUND_IMAGE_URL));
            collection.setUsername(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_NAME));
            collection.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_USERNAME));
            collection.setUserId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_ID));
            collection.setUserImageUrl100px(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_100PX));
            collection.setUserImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL));
            collections.add(collection);
        }
        return collections;
    }

    public static Collection getCollectionFromCursor(Cursor cursor) {
        cursor.moveToFirst();

        Collection collection = new Collection();
        collection.setId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_ID));
        collection.setCollectionId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID));
        collection.setName(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_NAME));
        collection.setTitle(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_TITLE));
        collection.setCreatedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_CREATED_AT));
        collection.setUpdatedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_UPDATED_AT));
        collection.setFeaturedAt(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_FEATURED_AT));
        collection.setSubscriberCount(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_SUBSCRIBER_COUNT));
        collection.setCategoryId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_CATEGORY_ID));
        collection.setCollectionUrl(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_COLLECTION_URL));
        collection.setPostCounts(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_POST_COUNTS));
        collection.setBackgroundImageUrl(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_BACKGROUND_IMAGE_URL));
        collection.setUsername(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_NAME));
        collection.setUsernameAlternative(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_USERNAME));
        collection.setUserId(CursorUtils.getInt(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_ID));
        collection.setUserImageUrl100px(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_100PX));
        collection.setUserImageUrlOriginal(CursorUtils.getString(cursor, PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL));

        return collection;
    }

    public static ContentValues getContentValuesForPost(PostsData.Posts post) {
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

    public static ContentValues getContentValuesForPost(int collectionId, PostsData.Posts post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, post.getId());
        contentValues.put(PredatorContract.PostsEntry.COLUMN_COLLECTION_ID, collectionId);
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
        contentValues.put(PredatorContract.PostsEntry.COLUMN_IS_IN_COLLECTION, 1);
        return contentValues;
    }

    public static ContentValues[] getBulkContentValuesForPostUsers(UserProfileData userProfileData) {
        List<ContentValues> contentValues = new ArrayList<ContentValues>();

        for (UserProfileData.User.Votes vote : userProfileData.getUser().getVotes()) {
            contentValues.add(getContentValuesForHunterUser(vote.getPost().getId(), vote.getPost().getUser()));
            for (PostsData.Posts.Makers maker : vote.getPost().getMakers()) {
                contentValues.add(getContentValuesForMakerUser(vote.getPost().getId(), maker));
            }
        }

        for (PostsData.Posts post : userProfileData.getUser().getPosts()) {
            contentValues.add(getContentValuesForHunterUser(post.getId(), post.getUser()));
            for (PostsData.Posts.Makers maker : post.getMakers()) {
                contentValues.add(getContentValuesForMakerUser(post.getId(), maker));
            }
        }

        for (PostsData.Posts post : userProfileData.getUser().getMakerOf()) {
            contentValues.add(getContentValuesForHunterUser(post.getId(), post.getUser()));
            for (PostsData.Posts.Makers maker : post.getMakers()) {
                contentValues.add(getContentValuesForMakerUser(post.getId(), maker));
            }
        }

        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    public static ContentValues[] getBulkContentValuesForPosts(UserProfileData userProfileData) {
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        if (userProfileData.getUser().getVotes() != null) {
            for (UserProfileData.User.Votes vote : userProfileData.getUser().getVotes()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, vote.getPost().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, vote.getPost().getCategoryId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, vote.getPost().getDay());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, vote.getPost().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, vote.getPost().getTagline());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, vote.getPost().getCommentsCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, vote.getPost().getCreatedAt());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(vote.getPost().getCreatedAt()));
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, vote.getPost().getDiscussionUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, vote.getPost().getRedirectUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, vote.getPost().getVotesCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, vote.getPost().getThumbnail().getImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, vote.getPost().getThumbnail().getOriginalImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, vote.getPost().getScreenshotUrl().getValue300px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, vote.getPost().getScreenshotUrl().getValue850px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, vote.getPost().getUser().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, vote.getPost().getUser().getUsername());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, vote.getPost().getUser().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, vote.getPost().getUser().getImageUrl().getValue100px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, vote.getPost().getUser().getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        if (userProfileData.getUser().getMakerOf() != null) {
            for (PostsData.Posts makerOf : userProfileData.getUser().getMakerOf()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, makerOf.getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, makerOf.getCategoryId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, makerOf.getDay());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, makerOf.getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, makerOf.getTagline());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, makerOf.getCommentsCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, makerOf.getCreatedAt());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(makerOf.getCreatedAt()));
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, makerOf.getDiscussionUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, makerOf.getRedirectUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, makerOf.getVotesCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, makerOf.getThumbnail().getImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, makerOf.getThumbnail().getOriginalImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, makerOf.getScreenshotUrl().getValue300px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, makerOf.getScreenshotUrl().getValue850px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, makerOf.getUser().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, makerOf.getUser().getUsername());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, makerOf.getUser().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, makerOf.getUser().getImageUrl().getValue100px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, makerOf.getUser().getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        if (userProfileData.getUser().getPosts() != null) {
            for (PostsData.Posts submitted : userProfileData.getUser().getPosts()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.PostsEntry.COLUMN_POST_ID, submitted.getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CATEGORY_ID, submitted.getCategoryId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DAY, submitted.getDay());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_NAME, submitted.getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_TAGLINE, submitted.getTagline());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_COMMENT_COUNT, submitted.getCommentsCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT, submitted.getCreatedAt());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(submitted.getCreatedAt()));
                contentValues.put(PredatorContract.PostsEntry.COLUMN_DISCUSSION_URL, submitted.getDiscussionUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_REDIRECT_URL, submitted.getRedirectUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_VOTES_COUNT, submitted.getVotesCount());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL, submitted.getThumbnail().getImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_THUMBNAIL_IMAGE_URL_ORIGINAL, submitted.getThumbnail().getOriginalImageUrl());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_300PX, submitted.getScreenshotUrl().getValue300px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_SCREENSHOT_URL_850PX, submitted.getScreenshotUrl().getValue850px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_NAME, submitted.getUser().getName());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_USERNAME, submitted.getUser().getUsername());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_ID, submitted.getUser().getId());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_100PX, submitted.getUser().getImageUrl().getValue100px());
                contentValues.put(PredatorContract.PostsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, submitted.getUser().getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    public static ContentValues getContentValuesForCurrentUser(UserProfileData userProfileData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, userProfileData.getUser().getId());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, userProfileData.getUser().getCreatedAt());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, userProfileData.getUser().getName());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, userProfileData.getUser().getUsername());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, userProfileData.getUser().getHeadline());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, userProfileData.getUser().getWebsiteUrl());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, userProfileData.getUser().getImageUrl().getValue100px());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, userProfileData.getUser().getImageUrl().getOriginal());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_HUNTER_POST_IDS, userProfileData.getUser().getHuntedPostsIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_MAKER_POST_IDS, userProfileData.getUser().getMadePostsIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, userProfileData.getUser().getVotedPostsIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_FOLLOWER_USER_IDS, userProfileData.getUser().getFollowerUserIds());
        contentValues.put(PredatorContract.UsersEntry.COLUMN_FOLLOWING_USER_IDS, userProfileData.getUser().getFollowingUserIds());
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

    public static ContentValues[] getBulkContentValuesForFollowingFollowerUsers(UserProfileData userProfileData) {
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        if (userProfileData.getUser().getFollowers() != null) {
            for (UserProfileData.User.Followers follower : userProfileData.getUser().getFollowers()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, follower.getId());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, follower.getCreatedAt());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, follower.getName());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, follower.getUsername());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, follower.getHeadline());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, follower.getWebsiteUrl());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, follower.getImageUrl().getValue100px());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, follower.getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        if (userProfileData.getUser().getFollowings() != null) {
            for (UserProfileData.User.Followings following : userProfileData.getUser().getFollowings()) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, following.getId());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, following.getCreatedAt());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, following.getName());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, following.getUsername());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, following.getHeadline());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, following.getWebsiteUrl());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, following.getImageUrl().getValue100px());
                contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, following.getImageUrl().getOriginal());

                contentValuesList.add(contentValues);
            }
        }

        return contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
    }

    public static ContentValues[] getBulkContentValuesForUsers(int postId, List<PostDetailsData.PostDetails.Votes> votes) {
        ContentValues[] contentValuesArr = new ContentValues[votes.size()];
        for (int i = 0; i < votes.size(); i++) {
            PostDetailsData.PostDetails.Votes vote = votes.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.UsersEntry.COLUMN_USER_ID, vote.getUserId());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_CREATED_AT, vote.getUser().getCreatedAt());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_NAME, vote.getUser().getName());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_USERNAME, vote.getUser().getUsername());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_HEADLINE, vote.getUser().getHeadline());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_WEBSITE_URL, vote.getUser().getWebsiteUrl());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_100PX, vote.getUser().getImageUrl().getValue100px());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_IMAGE_URL_ORIGINAL, vote.getUser().getImageUrl().getOriginal());
            contentValues.put(PredatorContract.UsersEntry.COLUMN_VOTED_POST_IDS, postId);
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    public static ContentValues[] getBulkContentValuesForMedia(int postId, List<PostDetailsData.PostDetails.Media> media) {
        ContentValues[] contentValuesArr = new ContentValues[media.size()];
        for (int i = 0; i < media.size(); i++) {
            PostDetailsData.PostDetails.Media mediaObj = media.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.MediaEntry.COLUMN_MEDIA_ID, mediaObj.getId());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_POST_ID, postId);
            contentValues.put(PredatorContract.MediaEntry.COLUMN_MEDIA_TYPE, mediaObj.getMediaType());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_PLATFORM, mediaObj.getPlatform());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_VIDEO_ID, mediaObj.getVideoId());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_ORIGINAL_WIDTH, mediaObj.getOriginalWidth());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_ORIGINAL_HEIGHT, mediaObj.getOriginalHeight());
            contentValues.put(PredatorContract.MediaEntry.COLUMN_IMAGE_URL, mediaObj.getImageUrl());
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    public static ContentValues getContentValuesForComments(PostCommentsData.Comments comment) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_COMMENT_ID, comment.getId());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_BODY, comment.getBody());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_CREATED_AT, comment.getCreatedAt());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_CREATED_AT_MILLIS, DateUtils.predatorDateToMillis(comment.getCreatedAt()));
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_PARENT_COMMENT_ID, comment.getParentCommentIdInteger());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_POST_ID, comment.getPostId());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_ID, comment.getUserId());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_CREATED_AT, comment.getUser().getCreatedAt());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_NAME, comment.getUser().getName());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_USERNAME, comment.getUser().getUsername());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_HEADLINE, comment.getUser().getHeadline());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_100PX, comment.getUser().getImageUrl().getValue100px());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, comment.getUser().getImageUrl().getOriginal());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_VOTES, comment.getVotes());
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_STICKY, comment.isSticky() ? 1 : 0);
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_MAKER, comment.isMaker() ? 1 : 0);
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_HUNTER, comment.isHunter() ? 1 : 0);
        contentValues.put(PredatorContract.CommentsEntry.COLUMN_IS_LIVE_GUEST, comment.isLiveGuest() ? 1 : 0);
        return contentValues;
    }

    public static ContentValues[] getBulkContentValuesForInstallLinks(List<PostDetailsData.PostDetails.InstallLinks> installLinks) {
        ContentValues[] contentValuesArr = new ContentValues[installLinks.size()];
        for (int i = 0; i < installLinks.size(); i++) {
            PostDetailsData.PostDetails.InstallLinks installLink = installLinks.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_INSTALL_LINK_ID, installLink.getId());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_POST_ID, installLink.getPostId());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_CREATED_AT, installLink.getCreatedAt());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_IS_PRIMARY_LINK, installLink.isPrimaryLink() ? 1 : 0);
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_REDIRECT_URL, installLink.getRedirectUrl());
            contentValues.put(PredatorContract.InstallLinksEntry.COLUMN_PLATFORM, installLink.getPlatform());
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }

    public static ContentValues[] getBulkContentValuesForCollections(List<CollectionsData.Collections> collections) {
        ContentValues[] contentValuesArr = new ContentValues[collections.size()];
        for (int i = 0; i < collections.size(); i++) {
            CollectionsData.Collections collection = collections.get(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_COLLECTION_ID, collection.getId());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_NAME, collection.getName());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_TITLE, collection.getTitle());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_CREATED_AT, collection.getCreatedAt());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_UPDATED_AT, collection.getUpdatedAt());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_FEATURED_AT, collection.getFeaturedAt());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_SUBSCRIBER_COUNT, collection.getSubscriberCount());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_CATEGORY_ID, collection.getCategoryId());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_COLLECTION_URL, collection.getCollectionUrl());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_POST_COUNTS, collection.getPostsCount());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_BACKGROUND_IMAGE_URL, collection.getBackgroundImageUrl());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_NAME, collection.getUser().getName());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_USERNAME, collection.getUser().getUsername());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_ID, collection.getUser().getId());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_100PX, collection.getUser().getImageUrl().getValue100px());
            contentValues.put(PredatorContract.CollectionsEntry.COLUMN_USER_IMAGE_URL_ORIGINAL, collection.getUser().getImageUrl().getOriginal());
            contentValuesArr[i] = contentValues;
        }
        return contentValuesArr;
    }
}
