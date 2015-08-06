
package com.simplifynowsoftware.flickrdemo.retrofit.response.photoinfo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Tags {

    @Expose
    private List<Object> tag = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The tag
     */
    public List<Object> getTag() {
        return tag;
    }

    /**
     * 
     * @param tag
     *     The tag
     */
    public void setTag(List<Object> tag) {
        this.tag = tag;
    }

}
