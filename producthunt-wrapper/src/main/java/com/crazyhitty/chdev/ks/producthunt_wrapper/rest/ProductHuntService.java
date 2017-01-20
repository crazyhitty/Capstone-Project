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

package com.crazyhitty.chdev.ks.producthunt_wrapper.rest;

import com.crazyhitty.chdev.ks.producthunt_wrapper.models.CollectionsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.MyProfileData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.OAuthData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostCommentsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostDetailsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.PostsData;
import com.crazyhitty.chdev.ks.producthunt_wrapper.models.UserProfileData;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     12/26/2016 9:49 PM
 * Description: Unavailable
 */

public interface ProductHuntService {
    @GET(ApiUrls.GET_POSTS)
    Observable<PostsData> getPosts(@Header(Constants.AUTHORIZATION) String authorization,
                                   @Query(Constants.PAGE) int page,
                                   @Query(Constants.PER_PAGE) int perPage);

    @GET(ApiUrls.GET_POSTS_BY_CATEGORY_DAYWISE)
    Observable<PostsData> getPostsCategoryWise(@Header(Constants.AUTHORIZATION) String authorization,
                                               @Path(Constants.CATEGORY_NAME) String categoryName,
                                               @Query(Constants.DAY) String day);

    @GET(ApiUrls.GET_POSTS_DETAILS)
    Observable<PostDetailsData> getPostDetails(@Header(Constants.AUTHORIZATION) String authorization,
                                               @Path(Constants.POST_ID) int postId);

    @GET(ApiUrls.GET_POST_COMMENTS)
    Observable<PostCommentsData> getPostComments(@Header(Constants.AUTHORIZATION) String authorization,
                                                 @Path(Constants.POST_ID) int postId,
                                                 @Query(Constants.PAGE) int page,
                                                 @Query(Constants.PER_PAGE) int perPage);

    @GET(ApiUrls.GET_COLLECTIONS)
    Observable<CollectionsData> getCollections(@Header(Constants.AUTHORIZATION) String authorization,
                                               @Query(Constants.PAGE) int page,
                                               @Query(Constants.PER_PAGE) int perPage,
                                               @Query(Constants.SEARCH_FEATURED) boolean isFeatured);

    @GET(ApiUrls.MY_PROFILE)
    Observable<MyProfileData> getMyProfile(@Header(Constants.AUTHORIZATION) String authorization);

    @GET(ApiUrls.GET_USER_PROFILE)
    Observable<UserProfileData> getUserProfile(@Header(Constants.AUTHORIZATION) String authorization,
                                               @Path(Constants.USER_ID) int userId);

    @POST(ApiUrls.OAUTH_GET_TOKEN)
    Observable<OAuthData> oAuthClient(@Body HashMap<String, String> params);

    @POST(ApiUrls.OAUTH_GET_TOKEN)
    Observable<OAuthData> oAuthUser(@Body HashMap<String, String> params);
}
