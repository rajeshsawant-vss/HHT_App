package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StationDetails implements Serializable {

    @SerializedName("name")public String name;
    @SerializedName("operations")public String[] operation;

    public StationDetails(String name,String[] operation) {
        this.name = name;
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOperation() {
        return operation;
    }

    public void setOperation(String[] operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "StationDetails{" +
                "name='" + name + '\'' +
                ", operation=" + operation +
                '}';
    }
}
