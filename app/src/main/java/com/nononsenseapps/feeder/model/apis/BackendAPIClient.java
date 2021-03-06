/*
 * Copyright (c) 2016 Jonas Kalderstam.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nononsenseapps.feeder.model.apis;

import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Client class for talking to the backend REST API.
 */
public class BackendAPIClient {
    public static final String DEFAULT_API_URL = "https://feeder.nononsenseapps.com";

    /**
     * @param server        The server's address
     * @param authorization Authorization header to use. Either "Bearer xxxxtokenxxxx" or
     *                      "Basic xxxxBASE64xxusername:passwordxxx"
     * @return a FeedAPI implementation
     */
    public static BackendAPI GetBackendAPI(final String server, final String authorization) {
        // Create a very simple REST adapter, with oauth header
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(server)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", authorization);
                    }
                }).build();
        // Create an instance of the interface
        BackendAPI api = restAdapter.create(BackendAPI.class);

        return api;
    }

    public interface BackendAPI {
        @GET("/feeds")
        FeedsResponse getFeeds(@Query("min_timestamp") String min_timestamp);

        @POST("/feeds")
        Feed putFeed(@Body FeedMessage feedMessage);

        @POST("/feeds/delete")
        VoidResponse deleteFeed(@Body DeleteMessage deleteMessage);
    }

    public static class FeedsResponse {
        public List<Feed> feeds;
        public List<Delete> deletes;
    }

    public static class FeedMessage extends Feed {
        public String regid;
    }

    public static class DeleteMessage {
        public String link;
    }

    public static class FeedItem {
        public String guid;
        public String title;
        public String title_stripped;
        public String description;
        public String snippet;
        public String link;
        public String image;
        public String published;
        public String author;
        public String comments;
        public String enclosure;
        public String json;
        public boolean read;
        public List<String> tags;
    }

    public static class Feed {
        public String link;
        public String title;
        public String description;
        public String published;
        public String tag;
        public String timestamp;
        public List<FeedItem> items;
    }

    public static class Delete {
        public String link;
        public String timestamp;
    }

    public static class VoidResponse {
    }
}
