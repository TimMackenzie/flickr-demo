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

package com.simplifynowsoftware.flickrdemo.display;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.simplifynowsoftware.flickrdemo.FlickrImageUrl;
import com.simplifynowsoftware.flickrdemo.R;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photosets.Photoset;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photosets.Photosets;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class PhotosetAdapter extends BaseAdapter {
    protected final Context context;

    protected final List<Photoset> mPhotosets;

    public PhotosetAdapter(Context context, final Photosets photosets) {
        this.context = context;

        mPhotosets = photosets.getPhotosets().getPhotoset();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Photoset photoset = mPhotosets.get(position);

        final String url = FlickrImageUrl.getUrl(photoset);

        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(context);
            view.setScaleType(CENTER_CROP);
        }

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_action_picture)
                .error(R.drawable.ic_action_warning)
                .fit()
                .tag(context)
                .into(view);

        return view;
    }

    @Override public int getCount() {
        return mPhotosets.size();
    }

    @Override public Photoset getItem(int position) {
     return mPhotosets.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }
}