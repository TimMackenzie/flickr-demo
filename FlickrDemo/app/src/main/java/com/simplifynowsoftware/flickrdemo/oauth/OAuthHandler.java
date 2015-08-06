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

package com.simplifynowsoftware.flickrdemo.oauth;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Lists;
import com.simplifynowsoftware.flickrdemo.FlickrDemoConstants;
import com.wuman.android.auth.oauth.OAuthHmacCredential;
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore;

import java.io.IOException;


public class OAuthHandler {
    protected static final String TAG = "OAuthHandler";

    public static final String TEMPORARY_TOKEN_REQUEST_URL = "http://m.flickr.com/services/oauth/request_token";
    public static final String AUTHORIZATION_VERIFIER_SERVER_URL = "http://m.flickr.com/services/oauth/authorize";
    public static final String TOKEN_SERVER_URL = "http://m.flickr.com/services/oauth/access_token";
    public static final String REDIRECT_URL = "http://localhost/Callback";

    protected OAuth mOauth;
    protected Credential mCredential; // retain stored credential in memory while app is running

    public Credential getCredential() {
        return mCredential;
    }

    // Get 1.0a OAuth access token if stored, else start new request
    public boolean getOAuthToken(FragmentActivity activity, final String userId) {
        boolean retVal = false;

        mOauth = OAuth.newInstance(activity.getApplicationContext(),
                activity.getSupportFragmentManager(),
                new ClientParametersAuthentication(FlickrDemoConstants.FLICKR_API_KEY,
                        FlickrDemoConstants.FLICKR_API_SECRET),
                AUTHORIZATION_VERIFIER_SERVER_URL,
                TOKEN_SERVER_URL,
                REDIRECT_URL,
                Lists.<String>newArrayList(),
                TEMPORARY_TOKEN_REQUEST_URL);

        try {
            mCredential = mOauth.authorize10a(
                    userId).getResult();
            if(FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i(TAG, "token: " + mCredential.getAccessToken());
            }

            if(mCredential instanceof OAuthHmacCredential) {
                if(FlickrDemoConstants.DEBUG_ENABLE) {
                    Log.i(TAG, "tokenSecret: " + ((OAuthHmacCredential) mCredential).getTokenSharedSecret());
                }

                retVal = true;
            }

        } catch(IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return retVal;
    }

    public boolean removeCredential(final Context context, final String userId) {
        boolean status = false;

        SharedPreferencesCredentialStore credentialStore =
                new SharedPreferencesCredentialStore(context,
                        OAuth.CREDENTIALS_STORE_PREF_FILE, OAuth.JSON_FACTORY);

        try{
            credentialStore.delete(userId, mCredential); // value of Credential is not used
            mCredential = null;
            status = true; // presume success if no exception
        } catch (IOException e) {
            if(FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e(TAG, "Exception deleting credential", e);
            }
        }

        return status;
    }


    // Check if credential was previously stored; load it if so.
    public boolean checkPreviousAuthorization(final Context context, final String userId) {
        boolean status = false;

        SharedPreferencesCredentialStore credentialStore =
                new SharedPreferencesCredentialStore(context,
                        OAuth.CREDENTIALS_STORE_PREF_FILE, OAuth.JSON_FACTORY);

        if(mCredential == null) {
            // Need a OAuthHmacCredential object.  Data will be overwritten
            mCredential = new OAuthHmacCredential.Builder(BearerToken.authorizationHeaderAccessMethod(), "", "").build();
        }

        try{
            status = credentialStore.load(userId, mCredential);
        } catch (IOException e) {
            if(FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e(TAG, "Exception loading credential", e);
            }
        }

        return status;
    }
}
