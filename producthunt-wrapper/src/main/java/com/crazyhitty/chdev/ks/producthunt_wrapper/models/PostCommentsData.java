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
 * Created:     12/29/2016 11:28 PM
 * Description: Unavailable
 */

public class PostCommentsData {

    private List<Comments> comments;

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public static class Comments {
        /**
         * id : 408138
         * body : We are very happy to announce that Product Hunt is now available for the Google Assistant. 🚀
         * <p>
         * Just say "Ok Google, talk to Product Hunt" to your Google Home device and you will get the top products for the day. See it in action here:
         * <p>
         * - https://twitter.com/Nivo0o0/status/811798647499173888
         * - https://twitter.com/stvmcg/status/809804721259048960
         * - https://twitter.com/ManeeshJuneja/status/810257238254350336
         * <p>
         * Exploring and building for the Google Assistant was a lot of fun. Many thanks to the amazing team at Google and especially Chris Reade, Naomi Makofsky and Minh Nguyen.
         * <p>
         * Eager to get everyone's feedback! Happy holidays!
         * created_at : 2016-12-28T06:17:35.833-08:00
         * parent_comment_id : null
         * user_id : 8412
         * votes : 28
         * subject_id : 86558
         * child_comments_count : 4
         * url : https://www.producthunt.com/posts/product-hunt-for-google-home#comment-408138
         * post_id : 86558
         * subject_type : Post
         * sticky : false
         * user : {"id":8412,"created_at":"2014-03-26T01:59:31.728-07:00","name":"Veselin","username":"vesln","headline":"Tech @ProductHunt ✌️","image_url":{"48px":"https://ph-avatars.imgix.net/8412/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/8412/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}
         * child_comments : [{"id":408161}]
         * maker : true
         * hunter : true
         * live_guest : false
         */

        private int id;
        private String body;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("parent_comment_id")
        private Object parentCommentId;
        @SerializedName("user_id")
        private int userId;
        private int votes;
        @SerializedName("subject_id")
        private int subjectId;
        @SerializedName("child_comments_count")
        private int childCommentsCount;
        private String url;
        @SerializedName("post_id")
        private int postId;
        @SerializedName("subject_type")
        private String subjectType;
        private boolean sticky;
        private User user;
        private boolean maker;
        private boolean hunter;
        @SerializedName("live_guest")
        private boolean liveGuest;
        @SerializedName("child_comments")
        private List<Comments> childComments;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(Object parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public int getParentCommentIdInteger() {
            if (parentCommentId != null) {
                return Double.valueOf(String.valueOf(parentCommentId)).intValue();
            }
            return 0;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getVotes() {
            return votes;
        }

        public void setVotes(int votes) {
            this.votes = votes;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public int getChildCommentsCount() {
            return childCommentsCount;
        }

        public void setChildCommentsCount(int childCommentsCount) {
            this.childCommentsCount = childCommentsCount;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }

        public String getSubjectType() {
            return subjectType;
        }

        public void setSubjectType(String subjectType) {
            this.subjectType = subjectType;
        }

        public boolean isSticky() {
            return sticky;
        }

        public void setSticky(boolean sticky) {
            this.sticky = sticky;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public boolean isMaker() {
            return maker;
        }

        public void setMaker(boolean maker) {
            this.maker = maker;
        }

        public boolean isHunter() {
            return hunter;
        }

        public void setHunter(boolean hunter) {
            this.hunter = hunter;
        }

        public boolean isLiveGuest() {
            return liveGuest;
        }

        public void setLiveGuest(boolean liveGuest) {
            this.liveGuest = liveGuest;
        }

        public List<Comments> getChildComments() {
            return childComments;
        }

        public void setChildComments(List<Comments> childComments) {
            this.childComments = childComments;
        }

        public static class User {
            /**
             * id : 8412
             * created_at : 2014-03-26T01:59:31.728-07:00
             * name : Veselin
             * username : vesln
             * headline : Tech @ProductHunt ✌️
             * image_url : {"48px":"https://ph-avatars.imgix.net/8412/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/8412/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
             */

            private int id;
            @SerializedName("created_at")
            private String createdAt;
            private String name;
            private String username;
            private String headline;
            @SerializedName("image_url")
            private ImageUrl imageUrl;
            @SerializedName("website_url")
            private String websiteUrl;

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

            public String getHeadline() {
                return headline;
            }

            public void setHeadline(String headline) {
                this.headline = headline;
            }

            public ImageUrl getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(ImageUrl imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getWebsiteUrl() {
                return websiteUrl;
            }

            public void setWebsiteUrl(String websiteUrl) {
                this.websiteUrl = websiteUrl;
            }

            public static class ImageUrl {
                /**
                 * 100px : https://ph-avatars.imgix.net/8412/original?auto=format&fit=crop&crop=faces&w=100&h=100
                 * original : https://ph-avatars.imgix.net/8412/original?auto=format&fit=crop&crop=faces&w=original&h=original
                 */

                @SerializedName("100px")
                private String value100px;
                private String original;

                public String getValue100px() {
                    return value100px;
                }

                public void setValue100px(String value100px) {
                    this.value100px = value100px;
                }

                public String getOriginal() {
                    return original;
                }

                public void setOriginal(String original) {
                    this.original = original;
                }
            }
        }
    }
}
