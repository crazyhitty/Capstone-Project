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
import com.crazyhitty.chdev.ks.predator.models.InstallLink;
import com.crazyhitty.chdev.ks.predator.models.Media;
import com.crazyhitty.chdev.ks.predator.models.Post;
import com.crazyhitty.chdev.ks.predator.models.PostDetails;
import com.crazyhitty.chdev.ks.predator.models.User;
import com.crazyhitty.chdev.ks.predator.utils.CursorUtils;
import com.crazyhitty.chdev.ks.predator.utils.DateUtils;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostCommentsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostDetailsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;

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
}
