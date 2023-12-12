package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SerialNovalidation implements Serializable {

    @SerializedName("sequenceNumber")
    String sequenceNumber;

    @SerializedName("parentPartNumber")
    String parentPartNumber;

    @SerializedName("parentPartDescription")
    String parentPartDescription;

    @SerializedName("scanJobDetails")
    @Expose
    private List<ChildPartsAndFamily> childPartsAndFamily = null;






    public String getParentPartNumber() {
        return parentPartNumber;
    }

    public void setParentPartNumber(String parentPartNumber) {
        this.parentPartNumber = parentPartNumber;
    }

    public String getParentPartDescription() {
        return parentPartDescription;
    }

    public void setParentPartDescription(String parentPartDescription) {
        this.parentPartDescription = parentPartDescription;
    }

    public List<ChildPartsAndFamily> getChildPartsAndFamily() {
        return childPartsAndFamily;
    }

    public void setChildPartsAndFamily(List<ChildPartsAndFamily> childPartsAndFamily) {
        this.childPartsAndFamily = childPartsAndFamily;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "SerialNovalidation{" +
                "parentPartNumber='" + parentPartNumber + '\'' +
                ", parentPartDescription='" + parentPartDescription + '\'' +
                ", childPartsAndFamily=" + childPartsAndFamily +

                '}';
    }
}
