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
 * Created:     10/7/17 9:37 PM
 * Description: Unavailable
 */

public class SearchData {

    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public static class Results {
        /**
         * hits : [{"comments_count":89,"name":"Workplace by Facebook","tagline":"Connect everyone in your company and turn ideas into action","id":78728,"user_id":16056,"created_at":"2016-10-10T10:02:26.033-07:00","featured_at":"2016-10-10T10:21:07.827-07:00","posted_date":1476120067,"shortened_url":"/r/p/78728","thumbnail":{"media_type":"image","original_height":175,"id":264838,"original_width":175,"image_url":"https://ph-files.imgix.net/9fab37c6-b4e2-4f82-a800-1849011f9459?auto=format&fit=crop&h=570&w=430"},"url":"/posts/workplace-by-facebook","user":{"username":"erictwillis","id":16056,"twitter_username":"erictwillis","name":"Eric Willis","headline":"Working on something new","image_urls":{"60":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=60&h=60","160":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160"},"link":"/@erictwillis","avatar_url":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160","is_maker":true},"vote_count":1490,"objectID":"78728","_highlightResult":{"name":{"value":"<em>Work<\/em>place by Facebook","matchLevel":"full","fullyHighlighted":false,"matchedWords":["work"]},"tagline":{"value":"Connect everyone in your company and turn ideas into action","matchLevel":"none","matchedWords":[]}}}]
         * nbHits : 8776
         * page : 0
         * nbPages : 100
         * hitsPerPage : 10
         * processingTimeMS : 13
         * exhaustiveNbHits : true
         * query : work
         * params : query=work&facetFilters=%5B%5B%22is_featured%3Atrue%22%5D%5D&numericFilters=%5B%5D&page=0&hitsPerPage=10
         * index : Post_production
         */

        private int nbHits;
        private int page;
        private int nbPages;
        private int hitsPerPage;
        private int processingTimeMS;
        private boolean exhaustiveNbHits;
        private String query;
        private String params;
        private String index;
        private List<Hits> hits;

        public int getNbHits() {
            return nbHits;
        }

        public void setNbHits(int nbHits) {
            this.nbHits = nbHits;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getNbPages() {
            return nbPages;
        }

        public void setNbPages(int nbPages) {
            this.nbPages = nbPages;
        }

        public int getHitsPerPage() {
            return hitsPerPage;
        }

        public void setHitsPerPage(int hitsPerPage) {
            this.hitsPerPage = hitsPerPage;
        }

        public int getProcessingTimeMS() {
            return processingTimeMS;
        }

        public void setProcessingTimeMS(int processingTimeMS) {
            this.processingTimeMS = processingTimeMS;
        }

        public boolean isExhaustiveNbHits() {
            return exhaustiveNbHits;
        }

        public void setExhaustiveNbHits(boolean exhaustiveNbHits) {
            this.exhaustiveNbHits = exhaustiveNbHits;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public List<Hits> getHits() {
            return hits;
        }

        public void setHits(List<Hits> hits) {
            this.hits = hits;
        }

        public static class Hits {

            /**
             * comments_count : 89
             * name : Workplace by Facebook
             * tagline : Connect everyone in your company and turn ideas into action
             * id : 78728
             * user_id : 16056
             * created_at : 2016-10-10T10:02:26.033-07:00
             * featured_at : 2016-10-10T10:21:07.827-07:00
             * posted_date : 1476120067
             * shortened_url : /r/p/78728
             * thumbnail : {"media_type":"image","original_height":175,"id":264838,"original_width":175,"image_url":"https://ph-files.imgix.net/9fab37c6-b4e2-4f82-a800-1849011f9459?auto=format&fit=crop&h=570&w=430"}
             * url : /posts/workplace-by-facebook
             * user : {"username":"erictwillis","id":16056,"twitter_username":"erictwillis","name":"Eric Willis","headline":"Working on something new","image_urls":{"60":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=60&h=60","160":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160"},"link":"/@erictwillis","avatar_url":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160","is_maker":true}
             * vote_count : 1490
             * objectID : 78728
             * _highlightResult : {"name":{"value":"<em>Work<\/em>place by Facebook","matchLevel":"full","fullyHighlighted":false,"matchedWords":["work"]},"tagline":{"value":"Connect everyone in your company and turn ideas into action","matchLevel":"none","fullyHighlighted":false,"matchedWords":["work"]}}
             * description : Throw on iPunch smart gloves and work on that right hook, or start slow with a mobile game like Real Boxing 2 CREED. Float like a butterfly and sting like a bee. 👊
             * followers_count : 159
             */

            @SerializedName("comments_count")
            private int commentsCount;
            private String name;
            private String tagline;
            private int id;
            @SerializedName("user_id")
            private int userId;
            @SerializedName("created_at")
            private String createdAt;
            @SerializedName("featured_at")
            private String featuredAt;
            @SerializedName("posted_date")
            private int postedDate;
            @SerializedName("shortened_url")
            private String shortenedUrl;
            private Thumbnail thumbnail;
            private String url;
            private User user;
            @SerializedName("vote_count")
            private int voteCount;
            private String objectID;
            @SerializedName("_highlightResult")
            private HighlightResult highlightResult;
            private String description;
            @SerializedName("followers_count")
            private int followersCount;

            /**
             * background_image_banner_url : https://ph-files.imgix.net/6998f53c-f251-4056-871b-ce9adcc61de6?auto=format
             * category_id : 1
             * title : User testing and user behavior monitoring tools for your websites and apps
             * has_curator : true
             * posts_count : 21
             * color : blue
             */

            @SerializedName("background_image_banner_url")
            private String backgroundImageBannerUrl;
            @SerializedName("category_id")
            private int categoryId;
            private String title;
            @SerializedName("has_curator")
            private boolean hasCurator;
            @SerializedName("posts_count")
            private int postsCount;
            private String color;

            public int getCommentsCount() {
                return commentsCount;
            }

            public void setCommentsCount(int commentsCount) {
                this.commentsCount = commentsCount;
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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getFeaturedAt() {
                return featuredAt;
            }

            public void setFeaturedAt(String featuredAt) {
                this.featuredAt = featuredAt;
            }

            public int getPostedDate() {
                return postedDate;
            }

            public void setPostedDate(int postedDate) {
                this.postedDate = postedDate;
            }

            public String getShortenedUrl() {
                return shortenedUrl;
            }

            public void setShortenedUrl(String shortenedUrl) {
                this.shortenedUrl = shortenedUrl;
            }

            public Thumbnail getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(Thumbnail thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }

            public int getVoteCount() {
                return voteCount;
            }

            public void setVoteCount(int voteCount) {
                this.voteCount = voteCount;
            }

            public String getObjectID() {
                return objectID;
            }

            public void setObjectID(String objectID) {
                this.objectID = objectID;
            }

            public HighlightResult getHighlightResult() {
                return highlightResult;
            }

            public void setHighlightResult(HighlightResult highlightResult) {
                this.highlightResult = highlightResult;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getFollowersCount() {
                return followersCount;
            }

            public void setFollowersCount(int followersCount) {
                this.followersCount = followersCount;
            }

            public String getBackgroundImageBannerUrl() {
                return backgroundImageBannerUrl;
            }

            public void setBackgroundImageBannerUrl(String backgroundImageBannerUrl) {
                this.backgroundImageBannerUrl = backgroundImageBannerUrl;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isHasCurator() {
                return hasCurator;
            }

            public void setHasCurator(boolean hasCurator) {
                this.hasCurator = hasCurator;
            }

            public int getPostsCount() {
                return postsCount;
            }

            public void setPostsCount(int postsCount) {
                this.postsCount = postsCount;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public static class Thumbnail {
                /**
                 * media_type : image
                 * original_height : 175
                 * id : 264838
                 * original_width : 175
                 * image_url : https://ph-files.imgix.net/9fab37c6-b4e2-4f82-a800-1849011f9459?auto=format&fit=crop&h=570&w=430
                 */

                @SerializedName("media_type")
                private String mediaType;
                @SerializedName("original_height")
                private int originalHeight;
                private int id;
                @SerializedName("original_width")
                private int originalWidth;
                @SerializedName("image_url")
                private String imageUrl;

                public String getMediaType() {
                    return mediaType;
                }

                public void setMediaType(String mediaType) {
                    this.mediaType = mediaType;
                }

                public int getOriginalHeight() {
                    return originalHeight;
                }

                public void setOriginalHeight(int originalHeight) {
                    this.originalHeight = originalHeight;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getOriginalWidth() {
                    return originalWidth;
                }

                public void setOriginalWidth(int originalWidth) {
                    this.originalWidth = originalWidth;
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
                 * username : erictwillis
                 * id : 16056
                 * twitter_username : erictwillis
                 * name : Eric Willis
                 * headline : Working on something new
                 * image_urls : {"60":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=60&h=60","160":"https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160"}
                 * link : /@erictwillis
                 * avatar_url : https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160
                 * is_maker : true
                 */

                private String username;
                private int id;
                @SerializedName("twitter_username")
                private String twitterUsername;
                private String name;
                private String headline;
                @SerializedName("image_urls")
                private ImageUrls imageUrls;
                private String link;
                @SerializedName("avatar_url")
                private String avatarUrl;
                @SerializedName("is_maker")
                private boolean isMaker;

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTwitterUsername() {
                    return twitterUsername;
                }

                public void setTwitterUsername(String twitterUsername) {
                    this.twitterUsername = twitterUsername;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getHeadline() {
                    return headline;
                }

                public void setHeadline(String headline) {
                    this.headline = headline;
                }

                public ImageUrls getImageUrls() {
                    return imageUrls;
                }

                public void setImageUrls(ImageUrls imageUrls) {
                    this.imageUrls = imageUrls;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getAvatarUrl() {
                    return avatarUrl;
                }

                public void setAvatarUrl(String avatarUrl) {
                    this.avatarUrl = avatarUrl;
                }

                public boolean isIsMaker() {
                    return isMaker;
                }

                public void setIsMaker(boolean isMaker) {
                    this.isMaker = isMaker;
                }

                public static class ImageUrls {
                    /**
                     * 60 : https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=60&h=60
                     * 160 : https://ph-avatars.imgix.net/16056/original?auto=format&fit=crop&crop=faces&w=160&h=160
                     */

                    @SerializedName("60")
                    private String imageUrl60;
                    @SerializedName("160")
                    private String imageUrl160;

                    public String getImageUrl60() {
                        return imageUrl60;
                    }

                    public void setImageUrl60(String imageUrl60) {
                        this.imageUrl60 = imageUrl60;
                    }

                    public String getImageUrl160() {
                        return imageUrl160;
                    }

                    public void setImageUrl160(String imageUrl160) {
                        this.imageUrl160 = imageUrl160;
                    }
                }
            }

            public static class HighlightResult {
                /**
                 * name : {"value":"<em>Work<\/em>place by Facebook","matchLevel":"full","fullyHighlighted":false,"matchedWords":["work"]}
                 * tagline : {"value":"Connect everyone in your company and turn ideas into action","matchLevel":"none","fullyHighlighted":false,"matchedWords":["work"]}
                 */

                private Name name;
                private Tagline tagline;

                public Name getName() {
                    return name;
                }

                public void setName(Name name) {
                    this.name = name;
                }

                public Tagline getTagline() {
                    return tagline;
                }

                public void setTagline(Tagline tagline) {
                    this.tagline = tagline;
                }

                public static class Name {
                    /**
                     * value : <em>Work</em>place by Facebook
                     * matchLevel : full
                     * fullyHighlighted : false
                     * matchedWords : ["work"]
                     */

                    private String value;
                    private String matchLevel;
                    private boolean fullyHighlighted;
                    private List<String> matchedWords;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public String getMatchLevel() {
                        return matchLevel;
                    }

                    public void setMatchLevel(String matchLevel) {
                        this.matchLevel = matchLevel;
                    }

                    public boolean isFullyHighlighted() {
                        return fullyHighlighted;
                    }

                    public void setFullyHighlighted(boolean fullyHighlighted) {
                        this.fullyHighlighted = fullyHighlighted;
                    }

                    public List<String> getMatchedWords() {
                        return matchedWords;
                    }

                    public void setMatchedWords(List<String> matchedWords) {
                        this.matchedWords = matchedWords;
                    }
                }

                public static class Tagline {
                    /**
                     * value : Connect everyone in your company and turn ideas into action
                     * matchLevel : none
                     * fullyHighlighted : false
                     * matchedWords : ["work"]
                     */

                    private String value;
                    private String matchLevel;
                    private boolean fullyHighlighted;
                    private List<String> matchedWords;

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }

                    public String getMatchLevel() {
                        return matchLevel;
                    }

                    public void setMatchLevel(String matchLevel) {
                        this.matchLevel = matchLevel;
                    }

                    public boolean isFullyHighlighted() {
                        return fullyHighlighted;
                    }

                    public void setFullyHighlighted(boolean fullyHighlighted) {
                        this.fullyHighlighted = fullyHighlighted;
                    }

                    public List<String> getMatchedWords() {
                        return matchedWords;
                    }

                    public void setMatchedWords(List<String> matchedWords) {
                        this.matchedWords = matchedWords;
                    }
                }
            }


        }
    }
}
