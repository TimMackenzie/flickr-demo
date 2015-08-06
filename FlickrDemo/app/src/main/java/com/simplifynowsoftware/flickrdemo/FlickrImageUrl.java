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

import com.simplifynowsoftware.flickrdemo.retrofit.response.PhotoCommon;
import com.simplifynowsoftware.flickrdemo.retrofit.response.photosets.Photoset;

/*
 * Helper class to generate the download URL for the selected photo
 *
 * Things start to get a little messy due to the similar - but not identical - POJOs from Flickr
 *  for things like "Photo".  These files are generated from the JSON from Flickr, and unfortunately
 *  do not match identically-named objects.
 */
public class FlickrImageUrl {
    protected static final String SIZE_SMALL            = "q";//s"; //TODO use s for smaller devices to save bandwidth
    protected static final String SIZE_MEDIUM           = "b";

    // Params: farm, server, id, secret, size.  All are strings except for the farm
    protected static final String URL_FORMAT_STRING     = "https://farm%d.staticflickr.com/%s/%s_%s_%s.jpg";

    protected static final String BUDDY_ICON_PREFIX     = "https://flickr.com/buddyicons/";
    protected static final String BUDDY_ICON_POSTFIX    = ".jpg";


    /*
     * PhotoCommon interface handles
     *  photoset.Photo
     *  interestingness.Photo
     */
    public static String getUrl(final PhotoCommon photo, final boolean isThumbnail) {
        return getUrl(  photo.getFarm(),
                        photo.getServer(),
                        photo.getId(),
                        photo.getSecret(),
                        isThumbnail);
    }

    // Photoset, which shows primary image
    public static String getUrl(final Photoset photoset, final boolean isThumbnail) {
        return getUrl(  photoset.getFarm(),
                photoset.getServer(),
                photoset.getPrimary(), // primary is the primary photo in the set
                photoset.getSecret(),
                isThumbnail);
    }
    /*
     * Generate URL for image, using Photo object
     *
     * https://www.flickr.com/services/api/misc.urls.html
     * The URL takes the following format:
     *  https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
     *  https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
     *  https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{o-secret}_o.(jpg|gif|png)
     *
     *  Size Suffixes:
     *  Size Suffixes
     *      s	small square 75x75
     *      q	large square 150x150
     *      t	thumbnail, 100 on longest side
     *      m	small, 240 on longest side
     *      n	small, 320 on longest side
     *      -	medium, 500 on longest side
     *      z	medium 640, 640 on longest side
     *      c	medium 800, 800 on longest side†
     *      b	large, 1024 on longest side*
     *      h	large 1600, 1600 on longest side†
     *      k	large 2048, 2048 on longest side†
     *      o	original image, either a jpg, gif or png, depending on source format
     */
    public static String getUrl(final int farm, final String server, final String id, final String secret, final boolean isThumbnail) {
        String size;
        if(isThumbnail) {
            size = SIZE_SMALL;
        } else {
            size = SIZE_MEDIUM; // TBR
        }

        // Params: farm, server, id, secret, size
        final String url = String.format(   URL_FORMAT_STRING,
                                            farm,
                                            server,
                                            id,
                                            secret,
                                            size);

        return url;
    }

    // use the shortcut URL: flickr.com/buddyicons/{NSID}.jpg
    public static String getBuddyIconUrl(final String userId) {
        return BUDDY_ICON_PREFIX + userId + BUDDY_ICON_POSTFIX;
    }
}