package com.hst.ops.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageDetailGrid implements Serializable {

    @SerializedName("gallery_id")
    @Expose
    private String id;

    @SerializedName("gallery_url")
    @Expose
    private String galleryUrl;

    @SerializedName("size")
    @Expose
    private int size = 3;


    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The galleryUrl
     */
    public String getGalleryUrl() {
        return galleryUrl;
    }

    /**
     * @param galleryUrl The galleryUrl
     */
    public void setGalleryUrl(String galleryUrl) {
        this.galleryUrl = galleryUrl;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}