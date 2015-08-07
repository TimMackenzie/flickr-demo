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

package com.simplifynowsoftware.flickrdemo;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;
import android.webkit.URLUtil;

import com.simplifynowsoftware.flickrdemo.oauth.OAuthHandler;
import com.simplifynowsoftware.flickrdemo.retrofit.FlickrClient;
import com.simplifynowsoftware.flickrdemo.retrofit.ServiceGenerator;
import com.simplifynowsoftware.flickrdemo.retrofit.response.TokenVerify;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photoset.Photoset;
import com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo.Person;
import com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo.UserData;
import com.wuman.android.auth.oauth.OAuthHmacCredential;

/**
 * Validate proper Retrofit interaction
 * Requires that OAuth token is stored locally, so must use a device that has previously run
 *  the app and saved the credential.
 */
public class RetrofitTest extends AndroidTestCase {
    protected Context mContext;
    protected OAuthHandler mOAuthHandler;
    protected FlickrClient mFlickrClient;

    @Override
    public void setUp() throws Exception {
        mOAuthHandler = new OAuthHandler();

        mContext = getContext();

        if(mOAuthHandler.checkPreviousAuthorization(mContext, FlickrDemoConstants.SINGLE_USER_ID)) {
            initializeFlickrClient();
        } else {
            Log.w("RetrofitTest", "Not previously authorized, failed precondition");
        }

        assertNotNull(mFlickrClient);
    }


    public void testPreconditions() {
        assertNotNull(mFlickrClient);
    }

    public void testTokenVerify() {
        TokenVerify tokenVerify = mFlickrClient.validateToken(mOAuthHandler.getCredential().getAccessToken());
        assertNotNull(tokenVerify);
        Log.i("RetrofitTest", "token is " + tokenVerify.getStat() + " because: " + tokenVerify.getMessage());
    }

    public void testUserData() {
        UserData response = mFlickrClient.getUserData(FlickrDemoTestData.USER_ID);
        assertNotNull(response);
        final String id = response.getPerson().getId();
        Log.i("RetrofitTest", "ID = " + id);

        formatUserData(response); // don't need result, just generate for logging
    }

    public void testPhotoset() {
        Photoset photoset = mFlickrClient.getPhotoset(FlickrDemoTestData.PHOTOSET_ID, FlickrDemoTestData.USER_ID);
        assertNotNull(photoset);
        assert(photoset.getStat().compareTo(FlickrDemoConstants.FLICKR_RESPONSE_OK) == 0);

        Log.i("getPhotoset", "Photos count: " + photoset.getPhotoset().getTotal());
        Log.i("getPhotoset", "First photo ID: " + photoset.getPhotoset().getPhoto().get(0).getId()); // FIRST_PHOTO_ID
        String url = FlickrImageUrl.getUrl(photoset.getPhotoset().getPhoto().get(0), FlickrImageUrl.USE_THUMBNAIL_SIZE);
        assert(URLUtil.isValidUrl(url)); // doesn't check if url points to actual image, just that url is valid
    }



    /*
     * ****************** Helper functions *************************
     */

    protected String formatUserData(final UserData userData) {
        Person user = userData.getPerson();

        StringBuilder builder = new StringBuilder();

        builder.append("Account Information:\n");
        builder.append("\nUser Name: " + user.getUsername().getContent());
        builder.append("\nReal Name: " + user.getRealname().getContent());
        builder.append("\nUserID: " + user.getNsid());
        builder.append("\nProfile URL: " + user.getProfileurl().getContent());
        builder.append("\nTotal Photos: " + user.getPhotos().getCount().getContent());

        final String userDataString = builder.toString();

        if(FlickrDemoConstants.DEBUG_ENABLE) {
            Log.i("formatUserData", userDataString);
        }

        return userDataString;
    }

    protected boolean initializeFlickrClient() {
        OAuthHmacCredential credential = (OAuthHmacCredential) mOAuthHandler.getCredential();

        mFlickrClient = ServiceGenerator.createServiceSigned(FlickrClient.class,
                ServiceGenerator.FLICKR_URL_BASE,
                credential.getAccessToken(),
                credential.getTokenSharedSecret());

        return (null != mFlickrClient);
    }
}
