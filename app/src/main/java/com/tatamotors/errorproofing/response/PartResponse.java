package com.tatamotors.errorproofing.response;

public class PartResponse {

    int seriel_no;
    String partFamily;
    String partNo;
    String status;

    public PartResponse() {

    }

    public PartResponse(int seriel_no, String partFamily, String partNo, String status) {
        this.seriel_no = seriel_no;
        this.partFamily = partFamily;
        this.partNo = partNo;
        this.status = status;
    }

    public int getSeriel_no() {
        return seriel_no;
    }

    public void setSeriel_no(int seriel_no) {
        this.seriel_no = seriel_no;
    }

    public String getPartFamily() {
        return partFamily;
    }

    public void setPartFamily(String partFamily) {
        this.partFamily = partFamily;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PartResponse{" +
                "seriel_no=" + seriel_no +
                ", partFamily='" + partFamily + '\'' +
                ", partNo='" + partNo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
