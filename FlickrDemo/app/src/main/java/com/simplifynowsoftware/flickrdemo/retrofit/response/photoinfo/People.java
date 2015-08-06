
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo;

import com.google.gson.annotations.Expose;

public class People {

    @Expose
    private int haspeople;

    /**
     * 
     * @return
     *     The haspeople
     */
    public int getHaspeople() {
        return haspeople;
    }

    /**
     * 
     * @param haspeople
     *     The haspeople
     */
    public void setHaspeople(int haspeople) {
        this.haspeople = haspeople;
    }

}
