
package com.simplifynowsoftware.flickrdemo.retrofit.response.photosets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photoset {

    @Expose
    private String id;
    @Expose
    private String primary;
    @Expose
    private String secret;
    @Expose
    private String server;
    @Expose
    private int farm;
    @Expose
    private int photos;
    @Expose
    private int videos;
    @Expose
    private Title title;
    @Expose
    private Description description;
    @SerializedName("needs_interstitial")
    @Expose
    private int needsInterstitial;
    @SerializedName("visibility_can_see_set")
    @Expose
    private int visibilityCanSeeSet;
    @SerializedName("count_views")
    @Expose
    private int countViews;
    @SerializedName("count_comments")
    @Expose
    private int countComments;
    @SerializedName("can_comment")
    @Expose
    private int canComment;
    @SerializedName("date_create")
    @Expose
    private String dateCreate;
    @SerializedName("date_update")
    @Expose
    private int dateUpdate;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The primary
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * 
     * @param primary
     *     The primary
     */
    public void setPrimary(String primary) {
        this.primary = primary;
    }

    /**
     * 
     * @return
     *     The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 
     * @param secret
     *     The secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 
     * @return
     *     The server
     */
    public String getServer() {
        return server;
    }

    /**
     * 
     * @param server
     *     The server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * 
     * @return
     *     The farm
     */
    public int getFarm() {
        return farm;
    }

    /**
     * 
     * @param farm
     *     The farm
     */
    public void setFarm(int farm) {
        this.farm = farm;
    }

    /**
     * 
     * @return
     *     The photos
     */
    public int getPhotos() {
        return photos;
    }

    /**
     * 
     * @param photos
     *     The photos
     */
    public void setPhotos(int photos) {
        this.photos = photos;
    }

    /**
     * 
     * @return
     *     The videos
     */
    public int getVideos() {
        return videos;
    }

    /**
     * 
     * @param videos
     *     The videos
     */
    public void setVideos(int videos) {
        this.videos = videos;
    }

    /**
     * 
     * @return
     *     The title
     */
    public Title getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The needsInterstitial
     */
    public int getNeedsInterstitial() {
        return needsInterstitial;
    }

    /**
     * 
     * @param needsInterstitial
     *     The needs_interstitial
     */
    public void setNeedsInterstitial(int needsInterstitial) {
        this.needsInterstitial = needsInterstitial;
    }

    /**
     * 
     * @return
     *     The visibilityCanSeeSet
     */
    public int getVisibilityCanSeeSet() {
        return visibilityCanSeeSet;
    }

    /**
     * 
     * @param visibilityCanSeeSet
     *     The visibility_can_see_set
     */
    public void setVisibilityCanSeeSet(int visibilityCanSeeSet) {
        this.visibilityCanSeeSet = visibilityCanSeeSet;
    }

    /**
     * 
     * @return
     *     The countViews
     */
    public int getCountViews() {
        return countViews;
    }

    /**
     * 
     * @param countViews
     *     The count_views
     */
    public void setCountViews(int countViews) {
        this.countViews = countViews;
    }

    /**
     * 
     * @return
     *     The countComments
     */
    public int getCountComments() {
        return countComments;
    }

    /**
     * 
     * @param countComments
     *     The count_comments
     */
    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }

    /**
     * 
     * @return
     *     The canComment
     */
    public int getCanComment() {
        return canComment;
    }

    /**
     * 
     * @param canComment
     *     The can_comment
     */
    public void setCanComment(int canComment) {
        this.canComment = canComment;
    }

    /**
     * 
     * @return
     *     The dateCreate
     */
    public String getDateCreate() {
        return dateCreate;
    }

    /**
     * 
     * @param dateCreate
     *     The date_create
     */
    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    /**
     * 
     * @return
     *     The dateUpdate
     */
    public int getDateUpdate() {
        return dateUpdate;
    }

    /**
     * 
     * @param dateUpdate
     *     The date_update
     */
    public void setDateUpdate(int dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

}
