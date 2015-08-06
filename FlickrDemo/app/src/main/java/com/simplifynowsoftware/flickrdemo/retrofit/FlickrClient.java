/*
 * Copyright (C) 2015 Simplify Now Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simplifynowsoftware.flickrdemo.retrofit;

import com.simplifynowsoftware.flickrdemo.retrofit.response.StatusResponse;
import com.simplifynowsoftware.flickrdemo.retrofit.response.TokenVerify;
import com.simplifynowsoftware.flickrdemo.retrofit.response.favorites.FavoritesResponse;
import com.simplifynowsoftware.flickrdemo.retrofit.response.interestingness.Interestingness;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo.PhotoInfo;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photoset.Photoset;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photosets.Photosets;
import com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo.UserData;
import com.simplifynowsoftware.flickrdemo.retrofit.response.username.UsernameResponse;
import com.simplifynowsoftware.flickrdemo.retrofit.response.validate.TokenTest;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/*
 * Retrofit-annotated calls to the Flickr API
 *
 * Note that static data (format, api key, etc.) as well as the API signature are added by the
 *  request interceptor in FlickrSigningOkClient and the helper class RequestSigner.
 */
public interface FlickrClient {

    String FLICKR_URL_BASE = "https://api.flickr.com";


    @GET("/services/rest/?method=flickr.auth.oauth.checkToken")
    TokenVerify validateToken(@Query("oauth_token") String oauth_token);

    @GET("/services/rest/?method=flickr.auth.oauth.checkToken")
    void validateToken(@Query("oauth_token") String oauth_token, Callback<TokenVerify> cb);


    /*
     * Retrieve userId of logged in user
     */
    @GET("/services/rest/?method=flickr.test.login")
    void testToken(Callback<TokenTest> cb);

    @GET("/services/rest/?method=flickr.people.getInfo")
    UserData getUserData(@Query("user_id") String userID);

    @GET("/services/rest/?method=flickr.people.getInfo")
    void getUserData(@Query("user_id") String userID, Callback<UserData> cb);

    @GET("/services/rest/?method=flickr.people.findByUsername")
    void getUserIdFromName(@Query("username") String username, Callback<UsernameResponse> cb);



    /*
     * Get photo list from a named set and user.  No signature required.
     * TODO handle extras such as extras, per_page, page, privacy_filter, and media
     */
    @GET("/services/rest/?method=flickr.photosets.getPhotos")
    Photoset getPhotoset(@Query("photoset_id") String photosetId,  @Query("user_id") String userId);

    @GET("/services/rest/?method=flickr.photosets.getPhotos")
    void getPhotoset(@Query("photoset_id") String photosetId,  @Query("user_id") String userId, Callback<Photoset> cb);


    /*
     * Get list of photosets for a particular user; current user if no ID is specified
     * Optional params not yet implemented: user_id, page, per_page, primary_photo_extras
     */
    @GET("/services/rest/?method=flickr.photosets.getList")
    void getPhotosetList(@Query("user_id") String userId, Callback<Photosets> cb);


    /*
     * Drink from the Interestingness firehose of photos
     * There are other options not yet implemented in this call
     * Without per_page and page, defaults are 100 per page and 5 pages (500 total images)
     */
    @GET("/services/rest/?method=flickr.interestingness.getList")
    void getInterestingness(@Query("per_page") String perPage,  @Query("page") String page, Callback<Interestingness> cb);


    // Photo details
    @GET("/services/rest/?method=flickr.photos.getInfo")
    void getPhotoInfo(@Query("photo_id") String photoId,  @Query("secret") String secret, Callback<PhotoInfo> cb);


    /*
     * Favorites
     * Not yet implemented optional arguments: min_fave_date, max_fave_date, extras, per_page, page
     */
    @GET("/services/rest/?method=flickr.favorites.getList")
    void getFavorites(@Query("user_id") String userId, Callback<FavoritesResponse> cb);


    @GET("/services/rest/?method=flickr.favorites.add")
    void addFavorite(@Query("photo_id") String photoId, Callback<StatusResponse> cb);

    @GET("/services/rest/?method=flickr.favorites.remove")
    void removeFavorite(@Query("photo_id") String photoId, Callback<StatusResponse> cb);
}

