package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;

public class LineDetails {

    @SerializedName("name")public String name;

    public LineDetails(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LineDetails{" +
                "name='" + name + '\'' +
                '}';
    }
}
