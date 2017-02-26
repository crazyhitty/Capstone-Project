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
 * Created:     1/1/2017 5:42 PM
 * Description: Unavailable
 */

public class UserProfileData {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        private int id;
        @SerializedName("created_at")
        private String createdAt;
        private String name;
        private String username;
        private String headline;
        @SerializedName("twitter_username")
        private String twitterUsername;
        @SerializedName("website_url")
        private String websiteUrl;
        @SerializedName("profile_url")
        private String profileUrl;
        @SerializedName("image_url")
        private ImageUrl imageUrl;
        @SerializedName("collections_count")
        private int collectionsCount;
        @SerializedName("followed_topics_count")
        private int followedTopicsCount;
        @SerializedName("followers_count")
        private int followersCount;
        @SerializedName("followings_count")
        private int followingsCount;
        @SerializedName("header_image_url")
        private String headerImageUrl;
        @SerializedName("maker_of_count")
        private int makerOfCount;
        @SerializedName("posts_count")
        private int postsCount;
        @SerializedName("votes_count")
        private int votesCount;
        private List<Followers> followers;
        private List<Followings> followings;
        @SerializedName("maker_of")
        private List<PostsData.Posts> makerOf;
        private List<PostsData.Posts> posts;
        private List<Votes> votes;

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

        public String getTwitterUsername() {
            return twitterUsername;
        }

        public void setTwitterUsername(String twitterUsername) {
            this.twitterUsername = twitterUsername;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public ImageUrl getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(ImageUrl imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getCollectionsCount() {
            return collectionsCount;
        }

        public void setCollectionsCount(int collectionsCount) {
            this.collectionsCount = collectionsCount;
        }

        public int getFollowedTopicsCount() {
            return followedTopicsCount;
        }

        public void setFollowedTopicsCount(int followedTopicsCount) {
            this.followedTopicsCount = followedTopicsCount;
        }

        public int getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(int followersCount) {
            this.followersCount = followersCount;
        }

        public int getFollowingsCount() {
            return followingsCount;
        }

        public void setFollowingsCount(int followingsCount) {
            this.followingsCount = followingsCount;
        }

        public String getHeaderImageUrl() {
            return headerImageUrl;
        }

        public void setHeaderImageUrl(String headerImageUrl) {
            this.headerImageUrl = headerImageUrl;
        }

        public int getMakerOfCount() {
            return makerOfCount;
        }

        public void setMakerOfCount(int makerOfCount) {
            this.makerOfCount = makerOfCount;
        }

        public int getPostsCount() {
            return postsCount;
        }

        public void setPostsCount(int postsCount) {
            this.postsCount = postsCount;
        }

        public int getVotesCount() {
            return votesCount;
        }

        public void setVotesCount(int votesCount) {
            this.votesCount = votesCount;
        }

        public List<Followers> getFollowers() {
            return followers;
        }

        public void setFollowers(List<Followers> followers) {
            this.followers = followers;
        }

        public List<Followings> getFollowings() {
            return followings;
        }

        public void setFollowings(List<Followings> followings) {
            this.followings = followings;
        }

        public List<PostsData.Posts> getMakerOf() {
            return makerOf;
        }

        public void setMakerOf(List<PostsData.Posts> makerOf) {
            this.makerOf = makerOf;
        }

        public List<PostsData.Posts> getPosts() {
            return posts;
        }

        public void setPosts(List<PostsData.Posts> posts) {
            this.posts = posts;
        }

        public List<Votes> getVotes() {
            return votes;
        }

        public void setVotes(List<Votes> votes) {
            this.votes = votes;
        }

        public String getHuntedPostsIds() {
            if (posts != null && posts.size() != 0) {
                String huntedPostsIds = "";
                for (PostsData.Posts post : posts) {
                    huntedPostsIds += post.getId() + ",";
                }
                return huntedPostsIds.substring(0, huntedPostsIds.length() - 2);
            }
            return "";
        }

        public String getMadePostsIds() {
            if (makerOf != null && makerOf.size() != 0) {
                String madePostsIds = "";
                for (PostsData.Posts post : makerOf) {
                    madePostsIds += post.getId() + ",";
                }
                return madePostsIds.substring(0, madePostsIds.length() - 1);
            }
            return "";
        }

        public String getVotedPostsIds() {
            if (votes != null && votes.size() != 0) {
                String votedPostsIds = "";
                for (Votes vote : votes) {
                    votedPostsIds += vote.getPost().getId() + ",";
                }
                return votedPostsIds.substring(0, votedPostsIds.length() - 1);
            }
            return "";
        }

        public String getFollowerUserIds() {
            if (followers != null && followers.size() != 0) {
                String followerUserIds = "";
                for (Followers follower : followers) {
                    followerUserIds += follower.getId() + ",";
                }
                return followerUserIds.substring(0, followerUserIds.length() - 1);
            }
            return "";
        }

        public String getFollowingUserIds() {
            if (followings != null && followings.size() != 0) {
                String followingUserIds = "";
                for (Followings following : followings) {
                    followingUserIds += following.getId() + ",";
                }
                return followingUserIds.substring(0, followingUserIds.length() - 1);
            }
            return "";
        }

        public static class ImageUrl {
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

        public static class Followers {
            private int id;
            @SerializedName("created_at")
            private String createdAt;
            private String name;
            private String username;
            private String headline;
            @SerializedName("twitter_username")
            private String twitterUsername;
            @SerializedName("website_url")
            private String websiteUrl;
            @SerializedName("profile_url")
            private String profileUrl;
            @SerializedName("image_url")
            private ImageUrl imageUrl;

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

            public String getTwitterUsername() {
                return twitterUsername;
            }

            public void setTwitterUsername(String twitterUsername) {
                this.twitterUsername = twitterUsername;
            }

            public String getWebsiteUrl() {
                return websiteUrl;
            }

            public void setWebsiteUrl(String websiteUrl) {
                this.websiteUrl = websiteUrl;
            }

            public String getProfileUrl() {
                return profileUrl;
            }

            public void setProfileUrl(String profileUrl) {
                this.profileUrl = profileUrl;
            }

            public ImageUrl getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(ImageUrl imageUrl) {
                this.imageUrl = imageUrl;
            }

            public static class ImageUrl {
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

        public static class Followings {
            private int id;
            @SerializedName("created_at")
            private String createdAt;
            private String name;
            private String username;
            private String headline;
            @SerializedName("twitter_username")
            private String twitterUsername;
            @SerializedName("website_url")
            private String websiteUrl;
            @SerializedName("profile_url")
            private String profileUrl;
            @SerializedName("image_url")
            private ImageUrl imageUrl;

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

            public String getTwitterUsername() {
                return twitterUsername;
            }

            public void setTwitterUsername(String twitterUsername) {
                this.twitterUsername = twitterUsername;
            }

            public String getWebsiteUrl() {
                return websiteUrl;
            }

            public void setWebsiteUrl(String websiteUrl) {
                this.websiteUrl = websiteUrl;
            }

            public String getProfileUrl() {
                return profileUrl;
            }

            public void setProfileUrl(String profileUrl) {
                this.profileUrl = profileUrl;
            }

            public ImageUrl getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(ImageUrl imageUrl) {
                this.imageUrl = imageUrl;
            }

            public static class ImageUrl {
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

        public static class Votes {
            private int id;
            @SerializedName("created_at")
            private String createdAt;
            @SerializedName("user_id")
            private int userId;
            @SerializedName("post_id")
            private int postId;
            private PostsData.Posts post;

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

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getPostId() {
                return postId;
            }

            public void setPostId(int postId) {
                this.postId = postId;
            }

            public PostsData.Posts getPost() {
                return post;
            }

            public void setPost(PostsData.Posts post) {
                this.post = post;
            }
        }
    }
}
