package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Validation implements Serializable {

//     "id": "string",
//             "scannedSerialNumber": "string",
//             "scannedPartNumber": "string",
//             "skipReason": "string"



//    @SerializedName("scannedValue")  //scannedSerialNumber
//    @Expose
//    private String scannedValue;



    @SerializedName("id")  //scannedSerialNumber
    @Expose
    private String id;

    @SerializedName("scannedSerialNumber")  //scannedSerialNumber
    @Expose
    private String scannedValue;

//    @SerializedName("derivedPartNumber")//scannedPartNumber
//    @Expose
//    private String derivedPartNumber;


    @SerializedName("scannedPartNumber")//scannedPartNumber
    @Expose
    private String derivedPartNumber;

    @SerializedName("skipReason")
    @Expose
    private String skipReason;


    public Validation(String scannedValue, String derivedPartNumber, String skipReason) {
        this.scannedValue = scannedValue;
        this.derivedPartNumber = derivedPartNumber;
        this.skipReason = skipReason;
    }

    public String getScannedValue() {
        return scannedValue;
    }

    public void setScannedValue(String scannedValue) {
        this.scannedValue = scannedValue;
    }

    public String getDerivedPartNumber() {
        return derivedPartNumber;
    }

    public void setDerivedPartNumber(String derivedPartNumber) {
        this.derivedPartNumber = derivedPartNumber;
    }

    public String getSkipReason() {
        return skipReason;
    }

    public void setSkipReason(String skipReason) {
        this.skipReason = skipReason;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
