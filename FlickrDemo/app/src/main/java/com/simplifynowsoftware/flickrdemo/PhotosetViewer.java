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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.simplifynowsoftware.flickrdemo.display.PhotoAdapter;
import com.simplifynowsoftware.flickrdemo.retrofit.response.PhotoCommon;
import com.simplifynowsoftware.flickrdemo.retrofit.response.interestingness.Interestingness;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photoset.Photoset;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Show a photoset, or the interestingness stream
 */
public class PhotosetViewer extends AppCompatActivity {
    public static final String INTENT_EXTRA_PHOTOSET_ID     = "INTENT_EXTRA_PHOTOSET_ID";
    public static final String INTENT_EXTRA_USER_ID         = "INTENT_EXTRA_USER_ID";

    public static final String INTERESTINGNESS_PHOTOSET_ID  = "INTERESTINGNESS_USER_ID";
    public static final String INTERESTINGNESS_USER_ID      = "INTERESTINGNESS_USER_ID";
    protected static final String INTERESTINGNESS_TITLE     = "Interestingness stream";
    protected static final int ARG_PER_PAGE                 = 100;

    protected String mPhotosetId;
    protected String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoset_fragment);

        // Set up action bar
        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        if(null != intent) {
            mPhotosetId = intent.getStringExtra(INTENT_EXTRA_PHOTOSET_ID);
            mUserId     = intent.getStringExtra(INTENT_EXTRA_USER_ID);

            if(INTERESTINGNESS_PHOTOSET_ID.equals(mPhotosetId) && INTERESTINGNESS_USER_ID.equals(mUserId)) {
                int page = 1; // TODO handle paging
                // Initiate the image set loading with callback
                FlickrDemo.getFlickrClient().getInterestingness(Integer.toString(ARG_PER_PAGE), Integer.toString(page), interestingnessCallback);
            } else {
                FlickrDemo.getFlickrClient().getPhotoset(mPhotosetId, mUserId, photosetCallback);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                /*
                 * navigateUpFromSameTask is the recommended way to walk back up the Activity stack,
                 * but it seems to trash the state data and cause an unnecessary reload.  Using the
                 * old-fashioned method (finish) for now.
                 */
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected retrofit.Callback photosetCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.i("photosetCallback", "Received photoset");
            }

            Photoset photoset = (Photoset)o;

            final String title = photoset.getPhotoset().getTitle();
            getSupportActionBar().setTitle("Photoset: " + title);

            ArrayList<PhotoCommon> list = new ArrayList<>();
            list.addAll(photoset.getPhotoset().getPhoto());
            showPhotos(list);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("photosetCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };

    protected retrofit.Callback interestingnessCallback = new retrofit.Callback() {
        @Override
        public void success(Object o, Response response) {
            getSupportActionBar().setTitle(INTERESTINGNESS_TITLE);

            Interestingness interestingness = (Interestingness)o;
            ArrayList<PhotoCommon> list = new ArrayList<>();
            list.addAll(interestingness.getPhotos().getPhoto());
            showPhotos(list);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.e("loadPhotosCallback", "RetrofitError" + retrofitError.getMessage());
            }
        }
    };


    protected void showPhotos(List<PhotoCommon> photos) {
        if(null == photos) {
            if (FlickrDemoConstants.DEBUG_ENABLE) {
                Log.w("showPhotos", "Failed to show photos, null input");
            }
        } else {
            final GridView gridView = (GridView) findViewById(R.id.photosetGridView);

            gridView.setAdapter(new PhotoAdapter(this, photos));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    PhotoCommon photo = (PhotoCommon) gridView.getItemAtPosition(position);

                    final Intent intent = new Intent(PhotosetViewer.this, PhotoViewer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(PhotoViewer.INTENT_EXTRA_PHOTO_URL, FlickrImageUrl.getUrl(photo, false));
                    intent.putExtra(PhotoViewer.INTENT_EXTRA_PHOTO_ID, photo.getId());
                    intent.putExtra(PhotoViewer.INTENT_EXTRA_PHOTO_SECRET, photo.getSecret());
                    if(mUserId != null && mUserId.compareTo(FlickrDemo.getOwnUserId()) == 0) {
                        intent.putExtra(PhotoViewer.INTENT_EXTRA_HIDE_FAVORITE, true);
                    }
                    startActivity(intent);

                    if (FlickrDemoConstants.DEBUG_ENABLE) {
                        Log.i("showPhotos", "Clicked on photo id " + photo.getId());
                    }
                }
            });
        }
    }
}
