
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo;

import com.google.gson.annotations.Expose;

public class Publiceditability {

    @Expose
    private int cancomment;
    @Expose
    private int canaddmeta;

    /**
     * 
     * @return
     *     The cancomment
     */
    public int getCancomment() {
        return cancomment;
    }

    /**
     * 
     * @param cancomment
     *     The cancomment
     */
    public void setCancomment(int cancomment) {
        this.cancomment = cancomment;
    }

    /**
     * 
     * @return
     *     The canaddmeta
     */
    public int getCanaddmeta() {
        return canaddmeta;
    }

    /**
     * 
     * @param canaddmeta
     *     The canaddmeta
     */
    public void setCanaddmeta(int canaddmeta) {
        this.canaddmeta = canaddmeta;
    }

}
