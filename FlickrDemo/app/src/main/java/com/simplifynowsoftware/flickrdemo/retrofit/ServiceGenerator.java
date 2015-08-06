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

import com.simplifynowsoftware.flickrdemo.FlickrDemoConstants;
import com.simplifynowsoftware.flickrdemo.signing.FlickrSigningOkClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import se.akerfeldt.signpost.retrofit.RetrofitHttpOAuthConsumer;

public class ServiceGenerator {
    public static final String FLICKR_URL_BASE = "https://api.flickr.com";

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    // This service uses the previously retrieved OAuth token and secret for all calls
    public static <S> S createServiceSigned(Class<S> serviceClass, String baseUrl, final String token, final String tokenSecret) {
        RetrofitHttpOAuthConsumer oAuthConsumer = new RetrofitHttpOAuthConsumer(FlickrDemoConstants.FLICKR_API_KEY, FlickrDemoConstants.FLICKR_API_SECRET);
        oAuthConsumer.setTokenWithSecret(token, tokenSecret);

        OkClient client = new FlickrSigningOkClient(oAuthConsumer);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(client);//new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        if(FlickrDemoConstants.DEBUG_ENABLE_VERBOSE) {
            adapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }

        return adapter.create(serviceClass);
    }
}