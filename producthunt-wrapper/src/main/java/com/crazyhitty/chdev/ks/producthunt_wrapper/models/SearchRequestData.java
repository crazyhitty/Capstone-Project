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

import com.crazyhitty.chdev.ks.producthunt_wrapper.utils.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     10/10/17 4:39 PM
 * Description: Unavailable
 */

public class SearchRequestData {

    private List<Request> requests;

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public static class Request {
        /**
         * indexName : Post_production
         * params : query=work&facetFilters=%5B%5B%22is_featured%3Atrue%22%5D%5D&numericFilters=%5B%5D&page=0&hitsPerPage=10
         */

        private String indexName;
        private String params;

        public String getIndexName() {
            return indexName;
        }

        public void setIndexName(String indexName) {
            this.indexName = indexName;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }

    public static SearchRequestData getDefaultRequest(String query) {
        SearchRequestData searchRequestData = new SearchRequestData();

        List<Request> requests = new ArrayList<>();

        QueryBuilder builder = new QueryBuilder()
                .appendQueryParameter("query", query)
                .appendQueryParameter("facetFilters", "[]")
                .appendQueryParameter("numericFilters", "[]")
                .appendQueryParameter("page", "0")
                .appendQueryParameter("hitsPerPage", "10");

        Request requestPost = new Request();
        requestPost.setIndexName("Post_production");
        requestPost.setParams(builder.build());

        requests.add(requestPost);

        Request requestCollection = new Request();
        requestCollection.setIndexName("Collection_production");
        requestCollection.setParams(builder.build());
        requests.add(requestCollection);

        searchRequestData.setRequests(requests);

        return searchRequestData;
    }

    public static SearchRequestData getPostRequest(String query, int page) {
        SearchRequestData searchRequestData = new SearchRequestData();

        QueryBuilder builder = new QueryBuilder()
                .appendQueryParameter("query", query)
                .appendQueryParameter("facetFilters", "[]")
                .appendQueryParameter("numericFilters", "[]")
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("hitsPerPage", "10");

        List<Request> requests = new ArrayList<>();

        Request requestPost = new Request();
        requestPost.setIndexName("Post_production");
        requestPost.setParams(builder.build());
        requests.add(requestPost);

        searchRequestData.setRequests(requests);

        return searchRequestData;
    }

    public static SearchRequestData getCollectionRequest(String query, int page) {
        SearchRequestData searchRequestData = new SearchRequestData();

        List<Request> requests = new ArrayList<>();

        QueryBuilder builder = new QueryBuilder()
                .appendQueryParameter("query", query)
                .appendQueryParameter("facetFilters", "[]")
                .appendQueryParameter("numericFilters", "[]")
                .appendQueryParameter("page", String.valueOf(page))
                .appendQueryParameter("hitsPerPage", "10");

        Request requestCollection = new Request();
        requestCollection.setIndexName("Collection_production");
        requestCollection.setParams(builder.build());
        requests.add(requestCollection);

        searchRequestData.setRequests(requests);

        return searchRequestData;
    }
}
