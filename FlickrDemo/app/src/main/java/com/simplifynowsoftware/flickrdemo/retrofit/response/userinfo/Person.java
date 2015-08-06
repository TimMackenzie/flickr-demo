
package com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {

    @Expose
    private String id;
    @Expose
    private String nsid;
    @Expose
    private int ispro;
    @SerializedName("can_buy_pro")
    @Expose
    private int canBuyPro;
    @Expose
    private int iconserver;
    @Expose
    private int iconfarm;
    @SerializedName("path_alias")
    @Expose
    private String pathAlias;
    @SerializedName("has_stats")
    @Expose
    private int hasStats;
    @Expose
    private Username username;
    @Expose
    private Realname realname;
    @Expose
    private Location location;
    @Expose
    private Description description;
    @Expose
    private Photosurl photosurl;
    @Expose
    private Profileurl profileurl;
    @Expose
    private Mobileurl mobileurl;
    @Expose
    private Photos photos;

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
     *     The nsid
     */
    public String getNsid() {
        return nsid;
    }

    /**
     * 
     * @param nsid
     *     The nsid
     */
    public void setNsid(String nsid) {
        this.nsid = nsid;
    }

    /**
     * 
     * @return
     *     The ispro
     */
    public int getIspro() {
        return ispro;
    }

    /**
     * 
     * @param ispro
     *     The ispro
     */
    public void setIspro(int ispro) {
        this.ispro = ispro;
    }

    /**
     * 
     * @return
     *     The canBuyPro
     */
    public int getCanBuyPro() {
        return canBuyPro;
    }

    /**
     * 
     * @param canBuyPro
     *     The can_buy_pro
     */
    public void setCanBuyPro(int canBuyPro) {
        this.canBuyPro = canBuyPro;
    }

    /**
     * 
     * @return
     *     The iconserver
     */
    public int getIconserver() {
        return iconserver;
    }

    /**
     * 
     * @param iconserver
     *     The iconserver
     */
    public void setIconserver(int iconserver) {
        this.iconserver = iconserver;
    }

    /**
     * 
     * @return
     *     The iconfarm
     */
    public int getIconfarm() {
        return iconfarm;
    }

    /**
     * 
     * @param iconfarm
     *     The iconfarm
     */
    public void setIconfarm(int iconfarm) {
        this.iconfarm = iconfarm;
    }

    /**
     * 
     * @return
     *     The pathAlias
     */
    public String getPathAlias() {
        return pathAlias;
    }

    /**
     * 
     * @param pathAlias
     *     The path_alias
     */
    public void setPathAlias(String pathAlias) {
        this.pathAlias = pathAlias;
    }

    /**
     * 
     * @return
     *     The hasStats
     */
    public int getHasStats() {
        return hasStats;
    }

    /**
     * 
     * @param hasStats
     *     The has_stats
     */
    public void setHasStats(int hasStats) {
        this.hasStats = hasStats;
    }

    /**
     * 
     * @return
     *     The username
     */
    public Username getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    public void setUsername(Username username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     The realname
     */
    public Realname getRealname() {
        return realname;
    }

    /**
     * 
     * @param realname
     *     The realname
     */
    public void setRealname(Realname realname) {
        this.realname = realname;
    }

    /**
     * 
     * @return
     *     The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    public void setLocation(Location location) {
        this.location = location;
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
     *     The photosurl
     */
    public Photosurl getPhotosurl() {
        return photosurl;
    }

    /**
     * 
     * @param photosurl
     *     The photosurl
     */
    public void setPhotosurl(Photosurl photosurl) {
        this.photosurl = photosurl;
    }

    /**
     * 
     * @return
     *     The profileurl
     */
    public Profileurl getProfileurl() {
        return profileurl;
    }

    /**
     * 
     * @param profileurl
     *     The profileurl
     */
    public void setProfileurl(Profileurl profileurl) {
        this.profileurl = profileurl;
    }

    /**
     * 
     * @return
     *     The mobileurl
     */
    public Mobileurl getMobileurl() {
        return mobileurl;
    }

    /**
     * 
     * @param mobileurl
     *     The mobileurl
     */
    public void setMobileurl(Mobileurl mobileurl) {
        this.mobileurl = mobileurl;
    }

    /**
     * 
     * @return
     *     The photos
     */
    public Photos getPhotos() {
        return photos;
    }

    /**
     * 
     * @param photos
     *     The photos
     */
    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

}
