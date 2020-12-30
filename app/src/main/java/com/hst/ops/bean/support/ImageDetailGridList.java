package com.hst.ops.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageDetailGridList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("image_result")
    @Expose
    private ArrayList<ImageDetailGrid> galleryArrayList = new ArrayList<>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The galleryArrayList
     */
    public ArrayList<ImageDetailGrid> getGalleryArrayList() {
        return galleryArrayList;
    }

    /**
     * @param galleryArrayList The galleryArrayList
     */
    public void setGalleryArrayList(ArrayList<ImageDetailGrid> galleryArrayList) {
        this.galleryArrayList = galleryArrayList;
    }
}
