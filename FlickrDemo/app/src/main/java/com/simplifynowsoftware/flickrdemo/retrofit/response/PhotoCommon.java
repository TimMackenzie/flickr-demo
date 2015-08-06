package com.simplifynowsoftware.flickrdemo.retrofit.response;

/**
 * This interface attempts to make it easier to deal with the auto-generated collisions for Flickr
 *  data types (e.g. interestingness.Photo and Photoset.Photo
 */
public interface PhotoCommon {
    String getId();
    int getFarm();
    String getServer();
    String getSecret();
}
