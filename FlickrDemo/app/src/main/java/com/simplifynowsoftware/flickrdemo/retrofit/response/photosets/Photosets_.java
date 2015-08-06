
package com.simplifynowsoftware.flickrdemo.retrofit.response.photosets;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Photosets_ {

    @Expose
    private int page;
    @Expose
    private int pages;
    @Expose
    private int perpage;
    @Expose
    private int total;
    @Expose
    private List<Photoset> photoset = new ArrayList<Photoset>();

    /**
     * 
     * @return
     *     The page
     */
    public int getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The pages
     */
    public int getPages() {
        return pages;
    }

    /**
     * 
     * @param pages
     *     The pages
     */
    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * 
     * @return
     *     The perpage
     */
    public int getPerpage() {
        return perpage;
    }

    /**
     * 
     * @param perpage
     *     The perpage
     */
    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    /**
     * 
     * @return
     *     The total
     */
    public int getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The photoset
     */
    public List<Photoset> getPhotoset() {
        return photoset;
    }

    /**
     * 
     * @param photoset
     *     The photoset
     */
    public void setPhotoset(List<Photoset> photoset) {
        this.photoset = photoset;
    }

}
