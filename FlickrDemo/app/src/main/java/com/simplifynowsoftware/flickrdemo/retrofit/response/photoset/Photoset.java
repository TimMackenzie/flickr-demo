
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoset;

import com.google.gson.annotations.Expose;

public class Photoset {

    @Expose
    private Photoset_ photoset;
    @Expose
    private String stat;

    /**
     * 
     * @return
     *     The photoset
     */
    public Photoset_ getPhotoset() {
        return photoset;
    }

    /**
     * 
     * @param photoset
     *     The photoset
     */
    public void setPhotoset(Photoset_ photoset) {
        this.photoset = photoset;
    }

    /**
     * 
     * @return
     *     The stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * 
     * @param stat
     *     The stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

}
