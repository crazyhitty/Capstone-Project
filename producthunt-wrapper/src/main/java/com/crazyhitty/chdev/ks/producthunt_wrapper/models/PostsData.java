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

package com.crazyhitty.chdev.ks.producthunt_wrapper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/26/2016 11:32 PM
 * Description: Unavailable
 */

public class PostsData {

    private List<Posts> posts;

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public static class Posts {
        /**
         * category_id : 1
         * day : 2016-12-29
         * id : 86596
         * name : Auto-Layout for Sketch
         * tagline : Easily see how your design looks on all screen sizes.
         * comments_count : 12
         * created_at : 2016-12-29T01:07:25.112-08:00
         * current_user : {"voted_for_post":false,"commented_on_post":false}
         * discussion_url : https://www.producthunt.com/posts/auto-layout-for-sketch?utm_campaign=producthunt-api&utm_medium=api&utm_source=Application%3A+Predator+%28ID%3A+3937%29
         * maker_inside : true
         * makers : [{"id":13869,"created_at":"2014-05-22T13:14:23.780-07:00","name":"Or Arbel","username":"orarbel","image_url":{"48px":"https://ph-avatars.imgix.net/13869/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/13869/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}]
         * platforms : []
         * topics : [{"id":44,"name":"Design Tools","slug":"design-tools"},{"id":352,"name":"Tech","slug":"tech"},{"id":271,"name":"Sketch","slug":"sketch"}]
         * redirect_url : https://www.producthunt.com/r/ab6d30968c0812/86596?app_id=3937
         * screenshot_url : {"300px":"https://api.url2png.com/v6/P5329C1FA0ECB6/214be07406f113edc738de73de07bf75/png/?thumbnail_max_width=300&url=https%3A%2F%2Fanimaapp.github.io%2FAuto-Layout%2F","850px":"https://api.url2png.com/v6/P5329C1FA0ECB6/fabbd143b0e0363a954bd8423ac226f5/png/?thumbnail_max_width=850&url=https%3A%2F%2Fanimaapp.github.io%2FAuto-Layout%2F"}
         * thumbnail : {"id":305696,"media_type":"image","image_url":"https://ph-files.imgix.net/b8a112b7-1311-4cfb-9098-74d72d5f592d?auto=format&fit=crop&h=570&w=430"}
         * user : {"id":347650,"name":"Umit Akcan","username":"umitakcn","image_url":{"48px":"https://ph-avatars.imgix.net/347650/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/347650/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}
         * votes_count : 306
         */

        @SerializedName("category_id")
        private int categoryId;
        private String day;
        private int id;
        private String name;
        private String tagline;
        @SerializedName("comments_count")
        private int commentsCount;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("current_user")
        private CurrentUser currentUser;
        @SerializedName("discussion_url")
        private String discussionUrl;
        @SerializedName("maker_inside")
        private boolean makerInside;
        @SerializedName("redirect_url")
        private String redirectUrl;
        @SerializedName("screenshot_url")
        private ScreenshotUrl screenshotUrl;
        private Thumbnail thumbnail;
        private User user;
        @SerializedName("votes_count")
        private int votesCount;
        private List<Makers> makers;
        private List<?> platforms;
        private List<Topics> topics;

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public int getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(int commentsCount) {
            this.commentsCount = commentsCount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public CurrentUser getCurrentUser() {
            return currentUser;
        }

        public void setCurrentUser(CurrentUser currentUser) {
            this.currentUser = currentUser;
        }

        public String getDiscussionUrl() {
            return discussionUrl;
        }

        public void setDiscussionUrl(String discussionUrl) {
            this.discussionUrl = discussionUrl;
        }

        public boolean isMakerInside() {
            return makerInside;
        }

        public void setMakerInside(boolean makerInside) {
            this.makerInside = makerInside;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public ScreenshotUrl getScreenshotUrl() {
            return screenshotUrl;
        }

        public void setScreenshotUrl(ScreenshotUrl screenshotUrl) {
            this.screenshotUrl = screenshotUrl;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public int getVotesCount() {
            return votesCount;
        }

        public void setVotesCount(int votesCount) {
            this.votesCount = votesCount;
        }

        public List<Makers> getMakers() {
            return makers;
        }

        public void setMakers(List<Makers> makers) {
            this.makers = makers;
        }

        public List<?> getPlatforms() {
            return platforms;
        }

        public void setPlatforms(List<?> platforms) {
            this.platforms = platforms;
        }

        public List<Topics> getTopics() {
            return topics;
        }

        public void setTopics(List<Topics> topics) {
            this.topics = topics;
        }

        public static class CurrentUser {
            /**
             * voted_for_post : false
             * commented_on_post : false
             */

            @SerializedName("voted_for_post")
            private boolean votedForPost;
            @SerializedName("commented_on_post")
            private boolean commentedOnPost;

            public boolean isVotedForPost() {
                return votedForPost;
            }

            public void setVotedForPost(boolean votedForPost) {
                this.votedForPost = votedForPost;
            }

            public boolean isCommentedOnPost() {
                return commentedOnPost;
            }

            public void setCommentedOnPost(boolean commentedOnPost) {
                this.commentedOnPost = commentedOnPost;
            }
        }

        public static class ScreenshotUrl {
            /**
             * 300px : https://api.url2png.com/v6/P5329C1FA0ECB6/214be07406f113edc738de73de07bf75/png/?thumbnail_max_width=300&url=https%3A%2F%2Fanimaapp.github.io%2FAuto-Layout%2F
             * 850px : https://api.url2png.com/v6/P5329C1FA0ECB6/fabbd143b0e0363a954bd8423ac226f5/png/?thumbnail_max_width=850&url=https%3A%2F%2Fanimaapp.github.io%2FAuto-Layout%2F
             */

            @SerializedName("300px")
            private String value300px;
            @SerializedName("850px")
            private String value850px;

            public String getValue300px() {
                return value300px;
            }

            public void setValue300px(String value300px) {
                this.value300px = value300px;
            }

            public String getValue850px() {
                return value850px;
            }

            public void setValue850px(String value850px) {
                this.value850px = value850px;
            }
        }

        public static class Thumbnail {
            /**
             * id : 305696
             * media_type : image
             * image_url : https://ph-files.imgix.net/b8a112b7-1311-4cfb-9098-74d72d5f592d?auto=format&fit=crop&h=570&w=430
             */

            private int id;
            @SerializedName("media_type")
            private String mediaType;
            @SerializedName("image_url")
            private String imageUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMediaType() {
                return mediaType;
            }

            public void setMediaType(String mediaType) {
                this.mediaType = mediaType;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }

        public static class User {
            /**
             * id : 347650
             * name : Umit Akcan
             * username : umitakcn
             * image_url : {"48px":"https://ph-avatars.imgix.net/347650/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/347650/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
             */

            private int id;
            private String name;
            private String username;
            @SerializedName("image_url")
            private ImageUrl imageUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public ImageUrl getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(ImageUrl imageUrl) {
                this.imageUrl = imageUrl;
            }

            public static class ImageUrl {
                /**
                 * 48px : https://ph-avatars.imgix.net/347650/original?auto=format&fit=crop&crop=faces&w=48&h=48
                 * original : https://ph-avatars.imgix.net/347650/original?auto=format&fit=crop&crop=faces&w=original&h=original
                 */

                @SerializedName("48px")
                private String value48px;
                private String original;

                public String getValue48px() {
                    return value48px;
                }

                public void setValue48px(String value48px) {
                    this.value48px = value48px;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }
            }
        }

        public static class Makers {
            /**
             * id : 13869
             * created_at : 2014-05-22T13:14:23.780-07:00
             * name : Or Arbel
             * username : orarbel
             * image_url : {"48px":"https://ph-avatars.imgix.net/13869/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/13869/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
             */

            private int id;
            @SerializedName("created_at")
            private String createdAt;
            private String name;
            private String username;
            @SerializedName("image_url")
            private ImageUrlMaker imageUrlMaker;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public ImageUrlMaker getImageUrlMaker() {
                return imageUrlMaker;
            }

            public void setImageUrlMaker(ImageUrlMaker imageUrlMaker) {
                this.imageUrlMaker = imageUrlMaker;
            }

            public static class ImageUrlMaker {
                /**
                 * 48px : https://ph-avatars.imgix.net/13869/original?auto=format&fit=crop&crop=faces&w=48&h=48
                 * original : https://ph-avatars.imgix.net/13869/original?auto=format&fit=crop&crop=faces&w=original&h=original
                 */

                @SerializedName("48px")
                private String value48px;
                private String original;

                public String getValue48px() {
                    return value48px;
                }

                public void setValue48px(String value48px) {
                    this.value48px = value48px;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }
            }
        }

        public static class Topics {
            /**
             * id : 44
             * name : Design Tools
             * slug : design-tools
             */

            private int id;
            private String name;
            private String slug;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }
        }
    }
}
