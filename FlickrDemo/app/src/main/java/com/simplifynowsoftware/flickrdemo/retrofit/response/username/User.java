
package com.simplifynowsoftware.flickrdemo.retrofit.response.username;

import com.google.gson.annotations.Expose;

public class User {

    @Expose
    private String id;
    @Expose
    private String nsid;
    @Expose
    private Username username;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The nsid
     */
    public String getNsid() {
        return nsid;
    }

    /**
     * 
     * @param nsid
     *     The nsid
     */
    public void setNsid(String nsid) {
        this.nsid = nsid;
    }

    /**
     * 
     * @return
     *     The username
     */
    public Username getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    public void setUsername(Username username) {
        this.username = username;
    }

}
