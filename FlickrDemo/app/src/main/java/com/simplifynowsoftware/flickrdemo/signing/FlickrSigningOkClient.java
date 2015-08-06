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

package com.simplifynowsoftware.flickrdemo.signing;

import android.util.Log;

import com.simplifynowsoftware.flickrdemo.FlickrDemoConstants;

import java.io.IOException;

import retrofit.client.Request;
import retrofit.client.Response;
import se.akerfeldt.signpost.retrofit.RetrofitHttpOAuthConsumer;
import se.akerfeldt.signpost.retrofit.SigningOkClient;

/**
 * FlickrSigningOkClient extends Patrik Åkerfeldt's SigningOkClient.  It adds static parameters and
 *  then calculates and adds the api_sig calculated MD5 hash to the request URL before it is sent to
 *  Flickr.
 */
public class FlickrSigningOkClient extends SigningOkClient {
    protected String mTokenSecret;

    public FlickrSigningOkClient(RetrofitHttpOAuthConsumer consumer) {
        super(consumer);
        mTokenSecret = consumer.getTokenSecret();
    }

    @Override
    /*
     *  Intercept the request before it is sent to modify the URL
     */
    public Response execute(Request request) throws IOException {
        // Finalize the URL with static parameters
        String modifiedUrl = RequestSigner.addStaticParams(request.getUrl());
        // Now, update the URL with the Flickr signature (MD5 hash)
        modifiedUrl = RequestSigner.signRequest(modifiedUrl, mTokenSecret);
        final Request modifiedRequest = new Request(request.getMethod(), modifiedUrl, request.getHeaders(), request.getBody());

        if (FlickrDemoConstants.DEBUG_ENABLE_VERBOSE) {
            Log.i("FlickrSigningOkClient", "final signed request: " + modifiedUrl);
        }

        return super.execute(modifiedRequest);
    }
}
