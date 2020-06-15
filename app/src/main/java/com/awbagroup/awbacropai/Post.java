package com.awbagroup.awbacropai;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Predictions")
    @Expose
    private List<Prediction> Predictions;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Prediction> getPredictions() {
        return Predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        Predictions = predictions;
    }
}
