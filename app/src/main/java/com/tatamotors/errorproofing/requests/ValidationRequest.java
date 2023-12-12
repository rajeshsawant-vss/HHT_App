package com.tatamotors.errorproofing.requests;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ValidationRequest implements Serializable {


    @SerializedName("familyId")
    Integer familyId;

//    @SerializedName("scannedValue")//scannedSerialNumber
//    String scannedValue;

    @SerializedName("scannedSerialNumber")//scannedSerialNumber
    String scannedValue;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("scannedPartNumber")//
    String scannedPartNumber;


    public ValidationRequest(Integer familyId, String scannedValue,String partNumber) {
        this.familyId = familyId;
        this.scannedValue = scannedValue;
        this.partNumber = partNumber;
    }

    public ValidationRequest(Integer familyId, String scannedValue, String partNumber, String scannedPartNumber) {
        this.familyId = familyId;
        this.scannedValue = scannedValue;
        this.partNumber = partNumber;
        this.scannedPartNumber = scannedPartNumber;
    }
}
