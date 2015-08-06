
package com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Count {

    @SerializedName("_content")
    @Expose
    private int Content;

    /**
     * 
     * @return
     *     The Content
     */
    public int getContent() {
        return Content;
    }

    /**
     * 
     * @param Content
     *     The _content
     */
    public void setContent(int Content) {
        this.Content = Content;
    }

}
