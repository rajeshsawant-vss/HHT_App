package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class ChildPartsAndFamily implements Serializable{


    public ChildPartsAndFamily() {

    }

    public ChildPartsAndFamily(int familyId) {
        this.familyId = familyId;
    }


//     "partDescription": "string",
//      "genealogyRequired": true,
//      "partNumberScanningRequired": true,



    @SerializedName("familyId")
    int familyId;

    @SerializedName("familyName")
    String familyDisplayName;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("scanJobResults")

    @Expose
    private List<Validation> validations = null;

    @SerializedName("quantity")
    int quantity;

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getFamilyDisplayName() {
        return familyDisplayName;
    }

    public void setFamilyDisplayName(String familyDisplayName) {
        this.familyDisplayName = familyDisplayName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public List<Validation> getValidations() {
        return validations;
    }

    public void setValidations(List<Validation> validations) {
        this.validations = validations;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ChildPartsAndFamily{" +
                "familyId=" + familyId +
                ", familyDisplayName='" + familyDisplayName + '\'' +
                ", partNumber='" + partNumber + '\'' +
                ", validations=" + validations +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
