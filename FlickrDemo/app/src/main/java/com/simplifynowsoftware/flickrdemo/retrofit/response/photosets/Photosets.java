
package com.simplifynowsoftware.flickrdemo.retrofit.response.photosets;

import com.google.gson.annotations.Expose;

public class Photosets {

    @Expose
    private Photosets_ photosets;
    @Expose
    private String stat;

    /**
     * 
     * @return
     *     The photosets
     */
    public Photosets_ getPhotosets() {
        return photosets;
    }

    /**
     * 
     * @param photosets
     *     The photosets
     */
    public void setPhotosets(Photosets_ photosets) {
        this.photosets = photosets;
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
