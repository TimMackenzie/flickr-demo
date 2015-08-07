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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.simplifynowsoftware.flickrdemo.display.PhotoAdapter;
import com.simplifynowsoftware.flickrdemo.events.UpdateRequest;
import com.simplifynowsoftware.flickrdemo.retrofit.FlickrClient;
import com.simplifynowsoftware.flickrdemo.retrofit.response.PhotoCommon;
import com.simplifynowsoftware.flickrdemo.retrofit.response.favorites.FavoritesResponse;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Show the favorites photo stream
 */
public class FavoritesFragment extends Fragment{
    public static final String FRAGMENT_TAG = "FavoritesFragment";

    protected String mUserId;

    public static String getTitle(final Context context) {
        return context.getString(R.string.fragment_title_favorites);
    }

    @Override public void onResume() {
        super.onResume();
        FlickrDemo.getBusInstance().register(this);

        // If we don't have data yet, prompt the parent activity to send it
        if(null == mUserId) {
            FlickrDemo.getBusInstance().post(new UpdateRequest());
        }
    }

    @Override public void onPause() {
        super.onPause();
        FlickrDemo.getBusInstance().unregister(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View userView = getActivity().getLayoutInflater().inflate(R.layout.favorites_fragment, null);

        return userView;
    }

    @Subscribe
    public void UserIdUpdateEvent(FlickrViewer.UserIdUpdate event) {
        final String userId = event.getUserId();

        if (FlickrDemoConstants.DEBUG_ENABLE) {
            Log.i("UserIdUpdateEvent", "Received event for ID " + userId);
        }

        // Skip reloading if userId is the same
        if(null == mUserId || mUserId.compareTo(userId) != 0) {
            setUserId(userId);
        }
    }

    protected void setUserId(final String userId) {
        mUserId = userId;

        // Set up the client
        FlickrClient flickerClient = ((FlickrViewer) getActivity()).getFlickrClient();

        if (null != flickerClient) {
            // Initiate the image set loading with callback
            flickerClient.getFavorites(userId, loadFavoritesCallback);
        } else if (FlickrDemoConstants.DEBUG_ENABLE) {
            Log.w("loadFavorites", "Failed to load data, FlickerClient is null");
        }
    }

    protected void showPhotos(List<PhotoCommon> photos) {
        if(null == photos) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.w("showPhotos", "Failed to show photos, null input");
            }
        } else {
            final GridView gridView = (GridView) getActivity().findViewById(R.id.favoritesFragment).findViewById(R.id.photosetGridView);

            gridView.setAdapter(new PhotoAdapter(getActivity(), photos));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    PhotoCommon photo = (PhotoCommon) gridView.getItemAtPosition(position);

                    final Intent intent = new Intent(getActivity(), PhotoViewer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(PhotoViewer.INTENT_EXTRA_PHOTO_URL, FlickrImageUrl.getUrl(photo, FlickrImageUrl.USE_FULL_SIZE));
                    intent.putExtra(PhotoViewer.INTENT_EXTRA_PHOTO_ID, photo.getId());
                    intent.putExtra(PhotoViewer.INTENT_EXTRA_PHOTO_SECRET, photo.getSecret());
                    startActivity(intent);

                    if (FlickrDemoConstants.DEBUG_ENABLE) {
                        Log.i("showPhotos", "Clicked on photo id " + photo.getId());
                    }
                }
            });
        }
    }


    protected retrofit.Callback loadFavoritesCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            FavoritesResponse favorites = (FavoritesResponse)o;
            ArrayList<PhotoCommon> list = new ArrayList<>();
            list.addAll(favorites.getPhotos().getPhoto());
            showPhotos(list);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("loadPhotosCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };
}
