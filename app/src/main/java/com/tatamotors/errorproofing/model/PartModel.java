package com.tatamotors.errorproofing.model;

import java.io.Serializable;

public class PartModel implements Serializable {
    String partNo;
    int serialNo;
    int partSize;
    String partFamily;
    String status;
    int familyId;
    String tag;
    String scannedValue;
    int sub_part_serial;
    int sub_part_total;
    String  msg;

    public PartModel(String partNo, int serialNo, int partSize, String partFamily,  int familyId, String tag, String scannedValue, int sub_part_serial, int sub_part_total,String msg) {
        this.partNo = partNo;
        this.serialNo = serialNo;
        this.partSize = partSize;
        this.partFamily = partFamily;
        this.familyId = familyId;
        this.tag = tag;
        this.scannedValue = scannedValue;
        this.sub_part_serial = sub_part_serial;
        this.sub_part_total = sub_part_total;
        this.msg=msg;
    }

    public PartModel(String partNo, String partFamily, String status) {
        this.partNo = partNo;
        this.partFamily = partFamily;
        this.status = status;
    }

    public PartModel(String partNo, int serialNo, int partSize,int familyId, String partFamily,String tag) {
        this.partNo = partNo;
        this.familyId = familyId;
        this.serialNo = serialNo;
        this.partSize = partSize;
        this.partFamily = partFamily;
        this.tag = tag;
    }

    public PartModel(String scannedValue,String partNo, int serialNo, int partSize,int familyId, String partFamily,String tag) {
        this.scannedValue = scannedValue;
        this.partNo = partNo;
        this.familyId = familyId;
        this.serialNo = serialNo;
        this.partSize = partSize;
        this.partFamily = partFamily;
        this.tag = tag;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }


    public String getPartFamily() {
        return partFamily;
    }

    public void setPartFamily(String partFamily) {
        this.partFamily = partFamily;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public int getPartSize() {
        return partSize;
    }

    public void setPartSize(int partSize) {
        this.partSize = partSize;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getScannedValue() {
        return scannedValue;
    }

    public void setScannedValue(String scannedValue) {
        this.scannedValue = scannedValue;
    }

    public int getSub_part_serial() {
        return sub_part_serial;
    }

    public void setSub_part_serial(int sub_part_serial) {
        this.sub_part_serial = sub_part_serial;
    }

    public int getSub_part_total() {
        return sub_part_total;
    }

    public void setSub_part_total(int sub_part_total) {
        this.sub_part_total = sub_part_total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
