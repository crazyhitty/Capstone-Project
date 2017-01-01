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
 * Created:     1/1/2017 9:56 AM
 * Description: Unavailable
 */

public class CollectionsData {

    private List<Collections> collections;

    public List<Collections> getCollections() {
        return collections;
    }

    public void setCollections(List<Collections> collections) {
        this.collections = collections;
    }

    public static class Collections {
        /**
         * id : 160191
         * name : Cool
         * title : null
         * created_at : 2016-12-31T19:56:29.380-08:00
         * updated_at : 2016-12-31T19:56:29.676-08:00
         * featured_at : null
         * subscriber_count : 0
         * category_id : 1
         * collection_url : https://www.producthunt.com/@joseph_bradley_barnes/collections/cool?utm_campaign=producthunt-api&utm_medium=api&utm_source=Application%3A+Predator+%28ID%3A+3937%29
         * posts_count : 1
         * user : {"id":773438,"name":"Joseph Bradley Barnes","username":"joseph_bradley_barnes","headline":"R&D Engineer","image_url":{"48px":"https://ph-avatars.imgix.net/773438/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/773438/original?auto=format&fit=crop&crop=faces&w=original&h=original"}}
         * background_image_url : null
         */

        private int id;
        private String name;
        private Object title;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("featured_at")
        private Object featuredAt;
        @SerializedName("subscriber_count")
        private int subscriberCount;
        @SerializedName("category_id")
        private int categoryId;
        @SerializedName("collection_url")
        private String collectionUrl;
        @SerializedName("posts_count")
        private int postsCount;
        private User user;
        private Object background_image_url;

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

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getFeaturedAt() {
            return featuredAt;
        }

        public void setFeaturedAt(Object featuredAt) {
            this.featuredAt = featuredAt;
        }

        public int getSubscriberCount() {
            return subscriberCount;
        }

        public void setSubscriberCount(int subscriberCount) {
            this.subscriberCount = subscriberCount;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getCollectionUrl() {
            return collectionUrl;
        }

        public void setCollectionUrl(String collectionUrl) {
            this.collectionUrl = collectionUrl;
        }

        public int getPostsCount() {
            return postsCount;
        }

        public void setPostsCount(int postsCount) {
            this.postsCount = postsCount;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Object getBackground_image_url() {
            return background_image_url;
        }

        public void setBackground_image_url(Object background_image_url) {
            this.background_image_url = background_image_url;
        }

        public static class User {
            /**
             * id : 773438
             * name : Joseph Bradley Barnes
             * username : joseph_bradley_barnes
             * headline : R&D Engineer
             * image_url : {"48px":"https://ph-avatars.imgix.net/773438/original?auto=format&fit=crop&crop=faces&w=48&h=48","original":"https://ph-avatars.imgix.net/773438/original?auto=format&fit=crop&crop=faces&w=original&h=original"}
             */

            private int id;
            private String name;
            private String username;
            private String headline;
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

            public static class ImageUrl {
                /**
                 * 48px : https://ph-avatars.imgix.net/773438/original?auto=format&fit=crop&crop=faces&w=48&h=48
                 * original : https://ph-avatars.imgix.net/773438/original?auto=format&fit=crop&crop=faces&w=original&h=original
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
    }
}
