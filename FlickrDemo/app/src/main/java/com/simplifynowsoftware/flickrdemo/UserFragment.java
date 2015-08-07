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
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplifynowsoftware.flickrdemo.display.PhotosetAdapter;
import com.simplifynowsoftware.flickrdemo.events.UpdateRequest;
import com.simplifynowsoftware.flickrdemo.retrofit.FlickrClient;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photosets.Photoset;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photosets.Photosets;
import com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo.Person;
import com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo.UserData;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Show the user's data, photos, etc.
 */
public class UserFragment extends Fragment{
    public static final String FRAGMENT_TAG = "UserFragment";

    protected UserData  mUserData;
    protected Photosets mPhotosets;
    protected String    mUserId;

    public static String getTitle(final Context context) {
        return context.getString(R.string.fragment_title_user_data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View userView = getActivity().getLayoutInflater().inflate(R.layout.user_fragment, null);

        final View infoButton = userView.findViewById(R.id.user_bar_info);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShowUserInfo(v);
            }
        });

        return userView;
    }

    @Override
    public void onResume() {
        super.onResume();
        FlickrDemo.getBusInstance().register(this);

        // If we don't have data yet, prompt the parent activity to send it
        if(null == mUserId) {
            FlickrDemo.getBusInstance().post(new UpdateRequest());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FlickrDemo.getBusInstance().unregister(this);
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

    public void updateData(final UserData userData) {
        final View view = getView();
        final Person user = userData.getPerson();

        if(user == null) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i(FRAGMENT_TAG, "No user in UserData response");
            }
        } else {
            final String userId = user.getNsid();
            final String userName = user.getUsername().getContent();
            final String iconUrl = FlickrImageUrl.getBuddyIconUrl(userId);

            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i(FRAGMENT_TAG, "Getting data for NSID " + userId + " with URL " + iconUrl);
            }

            final ImageView profileIconView = (ImageView) view.findViewById(R.id.user_bar_icon);
            final TextView profileNameView = (TextView) view.findViewById(R.id.user_bar_name);

            Picasso.with(getActivity()).load(iconUrl).into(profileIconView);
            profileNameView.setText(userName);
        }
    }

    public void onClickShowUserInfo(final View v) {
        if(null != mUserData) {
            final Person user = mUserData.getPerson();

            // Assemble user data for display
            SpannableStringBuilder builder = new SpannableStringBuilder();

            StaticUtil.addPairToSpannable(builder, "Real Name", user.getRealname().getContent());
            StaticUtil.addPairToSpannable(builder, "User Name", user.getUsername().getContent());
            StaticUtil.addPairToSpannable(builder, "Profile URL", user.getProfileurl().getContent());
            StaticUtil.addPairToSpannable(builder, "Description", user.getDescription().getContent());

            StaticUtil.showOkAlert(getActivity(), new SpannableString(builder));
        }
    }

    protected void showPhotosets(Photosets photosets) {
        if(null == photosets) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.w("showPhotosets", "Failed to show photosets, null input");
            }
        } else {
            final GridView gridView = (GridView) getActivity().findViewById(R.id.userFragment).findViewById(R.id.photosetGridView);

            gridView.setAdapter(new PhotosetAdapter(getActivity(), photosets));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    // Now, show the photoset
                    Photoset photoset = (Photoset) gridView.getItemAtPosition(position);

                    if (FlickrDemoConstants.DEBUG_ENABLE) {
                        Log.i("showPhotosets", "Clicked on photoset id " + photoset.getId());
                    }

                    final Intent intent = new Intent(getActivity(), PhotosetViewer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(PhotosetViewer.INTENT_EXTRA_PHOTOSET_ID, photoset.getId());
                    intent.putExtra(PhotosetViewer.INTENT_EXTRA_USER_ID, mUserId);
                    startActivity(intent);
                }
            });
        }
    }


    protected retrofit.Callback loadPhotosetsCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            showPhotosets((Photosets) o);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("loadPhotosetsCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };

    protected retrofit.Callback loadUserDataCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            mUserData = (UserData)o;
            updateData(mUserData);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("loadUserDataCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };

    public void setUserId(final String userId) {
        mUserId = userId;

        FlickrClient flickerClient = ((FlickrViewer) getActivity()).getFlickrClient();

        if (FlickrDemoConstants.DEBUG_ENABLE) {
            Log.i(FRAGMENT_TAG, "Now loading data for userId " + userId);
        }

        if (null != flickerClient) {
            // Initiate the image set loading with callback
            flickerClient.getPhotosetList(userId, loadPhotosetsCallback);

            // Initiate user data loading with callback
            flickerClient.getUserData(userId, loadUserDataCallback);
        } else if (FlickrDemoConstants.DEBUG_ENABLE) {
            Log.w("onClickLoad", "Failed to load data, FlickerClient is null");
            mUserData = null;
            mPhotosets = null;
            // TODO remove stale data from view
        }
    }
}
