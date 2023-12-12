package com.tatamotors.errorproofing.requests;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateValidation  implements Serializable {

    @SerializedName("familyId")
    Integer familyId;


    @SerializedName("scannedSerialNumber")//scannedSerialNumber
    String scannedValue;
//    @SerializedName("scannedValue")//scannedSerialNumber
//    String scannedValue;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("scannedPartNumber")
    String scannedPartNumber;



    @SerializedName("partDescription")
    String partDescription;

    @SerializedName("genealogyRequired")
    String genealogyRequired;

    @SerializedName("partNumberScanningRequired")
    String partNumberScanningRequired;

    public UpdateValidation(Integer familyId, String scannedValue, String partNumber) {
        this.familyId = familyId;
        this.scannedValue = scannedValue;
        this.partNumber = partNumber;
    }

    public UpdateValidation(Integer familyId, String scannedValue, String partNumber, String scannedPartNumber ) {
        this.familyId = familyId;
        this.scannedValue = scannedValue;
        this.partNumber = partNumber;
        this.scannedPartNumber = scannedPartNumber;
    }
}
