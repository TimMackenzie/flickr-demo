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

import android.app.Application;

import com.simplifynowsoftware.flickrdemo.retrofit.FlickrClient;
import com.squareup.otto.Bus;


/*
 * Application-wide instances:
 *  Our own userId, the FlickrClient, and the event bus
 */
public class FlickrDemo extends Application {
    protected static Bus mEventBus;
    protected static FlickrClient mFlickrClient;
    protected static String mOwnUserId;

    public static Bus getBusInstance() {
        return mEventBus;
    }

    public static FlickrClient getFlickrClient() {
        return mFlickrClient;
    }

    public static void setFlickrClient(final FlickrClient client) {
        mFlickrClient = client;
    }

    public static void setOwnUserId(final String userId) {
        mOwnUserId = userId;
    }

    public static String getOwnUserId() {
        return mOwnUserId;
    }

    public final void onCreate() {
        super.onCreate();
        mEventBus = new Bus();
    }
}