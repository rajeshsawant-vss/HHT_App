package com.tatamotors.errorproofing.model;

public class ReportModel {
    String partFamily;
    boolean Status;
    String reason;

    public ReportModel(String partFamily, boolean status, String reason) {
        this.partFamily = partFamily;
        Status = status;
        this.reason = reason;
    }

    public String getPartFamily() {
        return partFamily;
    }

    public void setPartFamily(String partFamily) {
        this.partFamily = partFamily;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
