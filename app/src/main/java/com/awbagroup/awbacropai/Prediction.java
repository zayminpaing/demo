package com.awbagroup.awbacropai;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Prediction implements Serializable {
    @SerializedName("probability")
    @Expose
    private double probability;
    @SerializedName("tagId")
    @Expose
    private String tagId;
    @SerializedName("tagName")
    @Expose
    private String tagName;
    @SerializedName("boundingBox")
    @Expose
    private String boundingBox;

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }
}
