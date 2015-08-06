
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo;

import com.google.gson.annotations.Expose;

public class Dates {

    @Expose
    private String posted;
    @Expose
    private String taken;
    @Expose
    private int takengranularity;
    @Expose
    private int takenunknown;
    @Expose
    private String lastupdate;

    /**
     * 
     * @return
     *     The posted
     */
    public String getPosted() {
        return posted;
    }

    /**
     * 
     * @param posted
     *     The posted
     */
    public void setPosted(String posted) {
        this.posted = posted;
    }

    /**
     * 
     * @return
     *     The taken
     */
    public String getTaken() {
        return taken;
    }

    /**
     * 
     * @param taken
     *     The taken
     */
    public void setTaken(String taken) {
        this.taken = taken;
    }

    /**
     * 
     * @return
     *     The takengranularity
     */
    public int getTakengranularity() {
        return takengranularity;
    }

    /**
     * 
     * @param takengranularity
     *     The takengranularity
     */
    public void setTakengranularity(int takengranularity) {
        this.takengranularity = takengranularity;
    }

    /**
     * 
     * @return
     *     The takenunknown
     */
    public int getTakenunknown() {
        return takenunknown;
    }

    /**
     * 
     * @param takenunknown
     *     The takenunknown
     */
    public void setTakenunknown(int takenunknown) {
        this.takenunknown = takenunknown;
    }

    /**
     * 
     * @return
     *     The lastupdate
     */
    public String getLastupdate() {
        return lastupdate;
    }

    /**
     * 
     * @param lastupdate
     *     The lastupdate
     */
    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

}
