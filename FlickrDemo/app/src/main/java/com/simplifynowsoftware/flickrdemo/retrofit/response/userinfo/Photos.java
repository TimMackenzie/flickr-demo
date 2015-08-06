
package com.simplifynowsoftware.flickrdemo.retrofit.response.userinfo;

import com.google.gson.annotations.Expose;

public class Photos {

    @Expose
    private Firstdatetaken firstdatetaken;
    @Expose
    private Firstdate firstdate;
    @Expose
    private Count count;

    /**
     * 
     * @return
     *     The firstdatetaken
     */
    public Firstdatetaken getFirstdatetaken() {
        return firstdatetaken;
    }

    /**
     * 
     * @param firstdatetaken
     *     The firstdatetaken
     */
    public void setFirstdatetaken(Firstdatetaken firstdatetaken) {
        this.firstdatetaken = firstdatetaken;
    }

    /**
     * 
     * @return
     *     The firstdate
     */
    public Firstdate getFirstdate() {
        return firstdate;
    }

    /**
     * 
     * @param firstdate
     *     The firstdate
     */
    public void setFirstdate(Firstdate firstdate) {
        this.firstdate = firstdate;
    }

    /**
     * 
     * @return
     *     The count
     */
    public Count getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(Count count) {
        this.count = count;
    }

}
