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

    /**
     * user : {"id":425173,"created_at":"2016-01-21T09:01:06.746-08:00","name":"Kartik","username":"cr42yh17m4n","headline":"Android dev","twitter_username":"cr42yh17m4n","website_url":"http://crazyhitty.com/","profile_url":"https://www.producthunt.com/@cr42yh17m4n","image_url":{"48px":"https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces&w=original&h=original"},"collections_count":2,"followed_topics_count":1,"followers":[{"id":459411,"created_at":"2016-02-16T14:39:43.843-08:00","name":"Aleksandr Sereda","username":"aleksandrsereda","headline":"Design | Illustrate | Web | Brands | SMM","twitter_username":"AleksandrSereda","website_url":null,"profile_url":"https://www.producthunt.com/@aleksandrsereda","image_url":{"48px":"https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}],"followers_count":1,"followings":[{"id":74,"created_at":"2013-12-04T17:32:15.241-08:00","name":"Hunter Walk","username":"hunterwalk","headline":"Partner, Homebrew","twitter_username":"hunterwalk","website_url":"http://www.homebrew.co","profile_url":"https://www.producthunt.com/@hunterwalk","image_url":{"48px":"https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}],"followings_count":11,"header_image_url":"https://ph-files.imgix.net/1fb4456d-dcfb-4a53-877f-9255340d3d41?auto=format","maker_of":[],"maker_of_count":0,"posts":[],"posts_count":0,"votes":[{"id":3198886,"created_at":"2016-01-21T09:01:49.198-08:00","post_id":15138,"user_id":425173,"post":{"category_id":1,"day":"2015-03-09","id":15138,"name":"I need a Resume","product_state":"default","tagline":"Create beautiful free resumes in seconds.","comments_count":21,"created_at":"2015-03-09T01:09:40.038-07:00","current_user":{"voted_for_post":true,"commented_on_post":false},"discussion_url":"https://www.producthunt.com/posts/i-need-a-resume?utm_campaign=producthunt-api&utm_medium=api&utm_source=Application%3A+Predator+%28ID%3A+3937%29","exclusive":null,"featured":true,"maker_inside":true,"makers":[{"id":170025,"created_at":"2015-03-05T00:18:15.995-08:00","name":"Mitch","username":"sleumasm","headline":"Creator of ineedaresu.me","twitter_username":"sleumasm","website_url":"http://nnnorth.com","profile_url":"https://www.producthunt.com/@sleumasm","image_url":{"48px":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}],"platforms":[],"topics":[{"id":41,"name":"Hiring and Recruiting","slug":"hiring-and-recruiting"},{"id":352,"name":"Tech","slug":"tech"},{"id":21,"name":"Web","slug":"web"}],"redirect_url":"https://www.producthunt.com/r/4c743609ff/15138?app_id=3937","screenshot_url":{"300px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bd029bfefdd3364545f84a71d7fea418/png/?thumbnail_max_width=300&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt","850px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bc68d4bb8febeb71e99d4dd30bd71258/png/?thumbnail_max_width=850&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt"},"thumbnail":{"id":81263,"media_type":"image","image_url":"https://ph-files.imgix.net/6029035a-d663-49bf-9d6a-4110360eadf6?auto=format&fit=crop&h=570&w=430","metadata":{}},"user":{"id":79,"created_at":"2013-12-05T07:01:30.884-08:00","name":"Kevin William David","username":"kwdinc","headline":"Community @Siftery.","twitter_username":"kwdinc","website_url":"http://kevinwilliamdavid.com/","profile_url":"https://www.producthunt.com/@kwdinc","image_url":{"48px":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=original&h=original"}},"votes_count":1103}}],"votes_count":2}
     */

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        /**
         * id : 425173
         * created_at : 2016-01-21T09:01:06.746-08:00
         * name : Kartik
         * username : cr42yh17m4n
         * headline : Android dev
         * twitter_username : cr42yh17m4n
         * website_url : http://crazyhitty.com/
         * profile_url : https://www.producthunt.com/@cr42yh17m4n
         * image_url : {"48px":"https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
         * collections_count : 2
         * followed_topics_count : 1
         * followers : [{"id":459411,"created_at":"2016-02-16T14:39:43.843-08:00","name":"Aleksandr Sereda","username":"aleksandrsereda","headline":"Design | Illustrate | Web | Brands | SMM","twitter_username":"AleksandrSereda","website_url":null,"profile_url":"https://www.producthunt.com/@aleksandrsereda","image_url":{"48px":"https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}]
         * followers_count : 1
         * followings : [{"id":74,"created_at":"2013-12-04T17:32:15.241-08:00","name":"Hunter Walk","username":"hunterwalk","headline":"Partner, Homebrew","twitter_username":"hunterwalk","website_url":"http://www.homebrew.co","profile_url":"https://www.producthunt.com/@hunterwalk","image_url":{"48px":"https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}]
         * followings_count : 11
         * header_image_url : https://ph-files.imgix.net/1fb4456d-dcfb-4a53-877f-9255340d3d41?auto=format
         * maker_of : []
         * maker_of_count : 0
         * posts : []
         * posts_count : 0
         * votes : [{"id":3198886,"created_at":"2016-01-21T09:01:49.198-08:00","post_id":15138,"user_id":425173,"post":{"category_id":1,"day":"2015-03-09","id":15138,"name":"I need a Resume","product_state":"default","tagline":"Create beautiful free resumes in seconds.","comments_count":21,"created_at":"2015-03-09T01:09:40.038-07:00","current_user":{"voted_for_post":true,"commented_on_post":false},"discussion_url":"https://www.producthunt.com/posts/i-need-a-resume?utm_campaign=producthunt-api&utm_medium=api&utm_source=Application%3A+Predator+%28ID%3A+3937%29","exclusive":null,"featured":true,"maker_inside":true,"makers":[{"id":170025,"created_at":"2015-03-05T00:18:15.995-08:00","name":"Mitch","username":"sleumasm","headline":"Creator of ineedaresu.me","twitter_username":"sleumasm","website_url":"http://nnnorth.com","profile_url":"https://www.producthunt.com/@sleumasm","image_url":{"48px":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}],"platforms":[],"topics":[{"id":41,"name":"Hiring and Recruiting","slug":"hiring-and-recruiting"},{"id":352,"name":"Tech","slug":"tech"},{"id":21,"name":"Web","slug":"web"}],"redirect_url":"https://www.producthunt.com/r/4c743609ff/15138?app_id=3937","screenshot_url":{"300px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bd029bfefdd3364545f84a71d7fea418/png/?thumbnail_max_width=300&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt","850px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bc68d4bb8febeb71e99d4dd30bd71258/png/?thumbnail_max_width=850&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt"},"thumbnail":{"id":81263,"media_type":"image","image_url":"https://ph-files.imgix.net/6029035a-d663-49bf-9d6a-4110360eadf6?auto=format&fit=crop&h=570&w=430","metadata":{}},"user":{"id":79,"created_at":"2013-12-05T07:01:30.884-08:00","name":"Kevin William David","username":"kwdinc","headline":"Community @Siftery.","twitter_username":"kwdinc","website_url":"http://kevinwilliamdavid.com/","profile_url":"https://www.producthunt.com/@kwdinc","image_url":{"48px":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=original&h=original"}},"votes_count":1103}}]
         * votes_count : 2
         */

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
        private List<?> makerOf;
        private List<?> posts;
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

        public List<?> getMakerOf() {
            return makerOf;
        }

        public void setMakerOf(List<?> makerOf) {
            this.makerOf = makerOf;
        }

        public List<?> getPosts() {
            return posts;
        }

        public void setPosts(List<?> posts) {
            this.posts = posts;
        }

        public List<Votes> getVotes() {
            return votes;
        }

        public void setVotes(List<Votes> votes) {
            this.votes = votes;
        }

        public static class ImageUrl {
            /**
             * 48px : https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces&w=48&h=48
             * original : https://ph-avatars.imgix.net/425173/original?auto=format&fit=crop&crop=faces&w=original&h=original
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

        public static class Followers {
            /**
             * id : 459411
             * created_at : 2016-02-16T14:39:43.843-08:00
             * name : Aleksandr Sereda
             * username : aleksandrsereda
             * headline : Design | Illustrate | Web | Brands | SMM
             * twitter_username : AleksandrSereda
             * website_url : null
             * profile_url : https://www.producthunt.com/@aleksandrsereda
             * image_url : {"48px":"https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
             */

            private int id;
            @SerializedName("created_at")
            private String createdAt;
            private String name;
            private String username;
            private String headline;
            @SerializedName("twitter_username")
            private String twitterUsername;
            @SerializedName("website_url")
            private Object websiteUrl;
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

            public Object getWebsiteUrl() {
                return websiteUrl;
            }

            public void setWebsiteUrl(Object websiteUrl) {
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
                /**
                 * 48px : https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=48&h=48
                 * original : https://ph-avatars.imgix.net/459411/original?auto=format&fit=crop&crop=faces&w=original&h=original
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

        public static class Followings {
            /**
             * id : 74
             * created_at : 2013-12-04T17:32:15.241-08:00
             * name : Hunter Walk
             * username : hunterwalk
             * headline : Partner, Homebrew
             * twitter_username : hunterwalk
             * website_url : http://www.homebrew.co
             * profile_url : https://www.producthunt.com/@hunterwalk
             * image_url : {"48px":"https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
             */

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
                /**
                 * 48px : https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=48&h=48
                 * original : https://ph-avatars.imgix.net/74/original?auto=format&fit=crop&crop=faces&w=original&h=original
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

        public static class Votes {
            /**
             * id : 3198886
             * created_at : 2016-01-21T09:01:49.198-08:00
             * post_id : 15138
             * user_id : 425173
             * post : {"category_id":1,"day":"2015-03-09","id":15138,"name":"I need a Resume","product_state":"default","tagline":"Create beautiful free resumes in seconds.","comments_count":21,"created_at":"2015-03-09T01:09:40.038-07:00","current_user":{"voted_for_post":true,"commented_on_post":false},"discussion_url":"https://www.producthunt.com/posts/i-need-a-resume?utm_campaign=producthunt-api&utm_medium=api&utm_source=Application%3A+Predator+%28ID%3A+3937%29","exclusive":null,"featured":true,"maker_inside":true,"makers":[{"id":170025,"created_at":"2015-03-05T00:18:15.995-08:00","name":"Mitch","username":"sleumasm","headline":"Creator of ineedaresu.me","twitter_username":"sleumasm","website_url":"http://nnnorth.com","profile_url":"https://www.producthunt.com/@sleumasm","image_url":{"48px":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}],"platforms":[],"topics":[{"id":41,"name":"Hiring and Recruiting","slug":"hiring-and-recruiting"},{"id":352,"name":"Tech","slug":"tech"},{"id":21,"name":"Web","slug":"web"}],"redirect_url":"https://www.producthunt.com/r/4c743609ff/15138?app_id=3937","screenshot_url":{"300px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bd029bfefdd3364545f84a71d7fea418/png/?thumbnail_max_width=300&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt","850px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bc68d4bb8febeb71e99d4dd30bd71258/png/?thumbnail_max_width=850&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt"},"thumbnail":{"id":81263,"media_type":"image","image_url":"https://ph-files.imgix.net/6029035a-d663-49bf-9d6a-4110360eadf6?auto=format&fit=crop&h=570&w=430","metadata":{}},"user":{"id":79,"created_at":"2013-12-05T07:01:30.884-08:00","name":"Kevin William David","username":"kwdinc","headline":"Community @Siftery.","twitter_username":"kwdinc","website_url":"http://kevinwilliamdavid.com/","profile_url":"https://www.producthunt.com/@kwdinc","image_url":{"48px":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=original&h=original"}},"votes_count":1103}
             */

            private int id;
            @SerializedName("created_at")
            private String createdAt;
            @SerializedName("post_id")
            private int postId;
            @SerializedName("user_id")
            private int userId;
            private Post post;

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

            public int getPostId() {
                return postId;
            }

            public void setPostId(int postId) {
                this.postId = postId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public Post getPost() {
                return post;
            }

            public void setPost(Post post) {
                this.post = post;
            }

            public static class Post {
                /**
                 * category_id : 1
                 * day : 2015-03-09
                 * id : 15138
                 * name : I need a Resume
                 * product_state : default
                 * tagline : Create beautiful free resumes in seconds.
                 * comments_count : 21
                 * created_at : 2015-03-09T01:09:40.038-07:00
                 * current_user : {"voted_for_post":true,"commented_on_post":false}
                 * discussion_url : https://www.producthunt.com/posts/i-need-a-resume?utm_campaign=producthunt-api&utm_medium=api&utm_source=Application%3A+Predator+%28ID%3A+3937%29
                 * exclusive : null
                 * featured : true
                 * maker_inside : true
                 * makers : [{"id":170025,"created_at":"2015-03-05T00:18:15.995-08:00","name":"Mitch","username":"sleumasm","headline":"Creator of ineedaresu.me","twitter_username":"sleumasm","website_url":"http://nnnorth.com","profile_url":"https://www.producthunt.com/@sleumasm","image_url":{"48px":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}]
                 * platforms : []
                 * topics : [{"id":41,"name":"Hiring and Recruiting","slug":"hiring-and-recruiting"},{"id":352,"name":"Tech","slug":"tech"},{"id":21,"name":"Web","slug":"web"}]
                 * redirect_url : https://www.producthunt.com/r/4c743609ff/15138?app_id=3937
                 * screenshot_url : {"300px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bd029bfefdd3364545f84a71d7fea418/png/?thumbnail_max_width=300&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt","850px":"https://api.url2png.com/v6/P5329C1FA0ECB6/bc68d4bb8febeb71e99d4dd30bd71258/png/?thumbnail_max_width=850&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt"}
                 * thumbnail : {"id":81263,"media_type":"image","image_url":"https://ph-files.imgix.net/6029035a-d663-49bf-9d6a-4110360eadf6?auto=format&fit=crop&h=570&w=430","metadata":{}}
                 * user : {"id":79,"created_at":"2013-12-05T07:01:30.884-08:00","name":"Kevin William David","username":"kwdinc","headline":"Community @Siftery.","twitter_username":"kwdinc","website_url":"http://kevinwilliamdavid.com/","profile_url":"https://www.producthunt.com/@kwdinc","image_url":{"48px":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}
                 * votes_count : 1103
                 */

                @SerializedName("category_id")
                private int categoryId;
                private String day;
                private int id;
                private String name;
                @SerializedName("product_state")
                private String productState;
                private String tagline;
                @SerializedName("comments_count")
                private int commentsCount;
                @SerializedName("created_at")
                private String createdAt;
                @SerializedName("current_user")
                private CurrentUser currentUser;
                @SerializedName("discussion_url")
                private String discussionUrl;
                private Object exclusive;
                private boolean featured;
                @SerializedName("maker_inside")
                private boolean makerInside;
                @SerializedName("redirect_url")
                private String redirectUrl;
                @SerializedName("screenshot_url")
                private ScreenshotUrl screenshotUrl;
                private Thumbnail thumbnail;
                private PostCreatorUser user;
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

                public String getProductState() {
                    return productState;
                }

                public void setProductState(String productState) {
                    this.productState = productState;
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

                public Object getExclusive() {
                    return exclusive;
                }

                public void setExclusive(Object exclusive) {
                    this.exclusive = exclusive;
                }

                public boolean isFeatured() {
                    return featured;
                }

                public void setFeatured(boolean featured) {
                    this.featured = featured;
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

                public PostCreatorUser getUser() {
                    return user;
                }

                public void setUser(PostCreatorUser user) {
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
                     * voted_for_post : true
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
                     * 300px : https://api.url2png.com/v6/P5329C1FA0ECB6/bd029bfefdd3364545f84a71d7fea418/png/?thumbnail_max_width=300&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt
                     * 850px : https://api.url2png.com/v6/P5329C1FA0ECB6/bc68d4bb8febeb71e99d4dd30bd71258/png/?thumbnail_max_width=850&url=http%3A%2F%2Fineedaresu.me%2F%23%2Fproducthunt
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
                     * id : 81263
                     * media_type : image
                     * image_url : https://ph-files.imgix.net/6029035a-d663-49bf-9d6a-4110360eadf6?auto=format&fit=crop&h=570&w=430
                     * metadata : {}
                     */

                    private int id;
                    @SerializedName("media_type")
                    private String mediaType;
                    @SerializedName("image_url")
                    private String imageUrl;
                    private Metadata metadata;

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

                    public Metadata getMetadata() {
                        return metadata;
                    }

                    public void setMetadata(Metadata metadata) {
                        this.metadata = metadata;
                    }

                    public static class Metadata {
                    }
                }

                public static class PostCreatorUser {
                    /**
                     * id : 79
                     * created_at : 2013-12-05T07:01:30.884-08:00
                     * name : Kevin William David
                     * username : kwdinc
                     * headline : Community @Siftery.
                     * twitter_username : kwdinc
                     * website_url : http://kevinwilliamdavid.com/
                     * profile_url : https://www.producthunt.com/@kwdinc
                     * image_url : {"48px":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
                     */

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
                        /**
                         * 48px : https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=48&h=48
                         * original : https://ph-avatars.imgix.net/79/original?auto=format&fit=crop&crop=faces&w=original&h=original
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
                     * id : 170025
                     * created_at : 2015-03-05T00:18:15.995-08:00
                     * name : Mitch
                     * username : sleumasm
                     * headline : Creator of ineedaresu.me
                     * twitter_username : sleumasm
                     * website_url : http://nnnorth.com
                     * profile_url : https://www.producthunt.com/@sleumasm
                     * image_url : {"48px":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
                     */

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
                        /**
                         * 48px : https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=48&h=48
                         * original : https://ph-avatars.imgix.net/170025/original?auto=format&fit=crop&crop=faces&w=original&h=original
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
                     * id : 41
                     * name : Hiring and Recruiting
                     * slug : hiring-and-recruiting
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
    }
}
