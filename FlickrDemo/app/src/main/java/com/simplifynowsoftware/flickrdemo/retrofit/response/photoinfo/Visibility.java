
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo;

import com.google.gson.annotations.Expose;

public class Visibility {

    @Expose
    private int ispublic;
    @Expose
    private int isfriend;
    @Expose
    private int isfamily;

    /**
     * 
     * @return
     *     The ispublic
     */
    public int getIspublic() {
        return ispublic;
    }

    /**
     * 
     * @param ispublic
     *     The ispublic
     */
    public void setIspublic(int ispublic) {
        this.ispublic = ispublic;
    }

    /**
     * 
     * @return
     *     The isfriend
     */
    public int getIsfriend() {
        return isfriend;
    }

    /**
     * 
     * @param isfriend
     *     The isfriend
     */
    public void setIsfriend(int isfriend) {
        this.isfriend = isfriend;
    }

    /**
     * 
     * @return
     *     The isfamily
     */
    public int getIsfamily() {
        return isfamily;
    }

    /**
     * 
     * @param isfamily
     *     The isfamily
     */
    public void setIsfamily(int isfamily) {
        this.isfamily = isfamily;
    }

}
