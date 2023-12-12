package com.tatamotors.errorproofing.requests;

import com.google.gson.annotations.SerializedName;

public class SkipRequest {


    @SerializedName("familyId")
    Integer familyId;

    @SerializedName("partNumber")
    String partNumber;

    @SerializedName("scannedValue")
    String scannedValue;

    @SerializedName("reason")
    String reason;

    public SkipRequest(Integer familyId, String partNumber, String scannedValue, String reason) {
        this.familyId = familyId;
        this.partNumber = partNumber;
        this.scannedValue = scannedValue;
        this.reason = reason;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getScannedValue() {
        return scannedValue;
    }

    public void setScannedValue(String scannedValue) {
        this.scannedValue = scannedValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
