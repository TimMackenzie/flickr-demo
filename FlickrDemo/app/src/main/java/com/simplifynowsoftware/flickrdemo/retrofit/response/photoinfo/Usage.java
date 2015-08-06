
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo;

import com.google.gson.annotations.Expose;

public class Usage {

    @Expose
    private int candownload;
    @Expose
    private int canblog;
    @Expose
    private int canprint;
    @Expose
    private int canshare;

    /**
     * 
     * @return
     *     The candownload
     */
    public int getCandownload() {
        return candownload;
    }

    /**
     * 
     * @param candownload
     *     The candownload
     */
    public void setCandownload(int candownload) {
        this.candownload = candownload;
    }

    /**
     * 
     * @return
     *     The canblog
     */
    public int getCanblog() {
        return canblog;
    }

    /**
     * 
     * @param canblog
     *     The canblog
     */
    public void setCanblog(int canblog) {
        this.canblog = canblog;
    }

    /**
     * 
     * @return
     *     The canprint
     */
    public int getCanprint() {
        return canprint;
    }

    /**
     * 
     * @param canprint
     *     The canprint
     */
    public void setCanprint(int canprint) {
        this.canprint = canprint;
    }

    /**
     * 
     * @return
     *     The canshare
     */
    public int getCanshare() {
        return canshare;
    }

    /**
     * 
     * @param canshare
     *     The canshare
     */
    public void setCanshare(int canshare) {
        this.canshare = canshare;
    }

}
