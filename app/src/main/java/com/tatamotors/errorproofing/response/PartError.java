package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;

public class PartError {

    @SerializedName("status")
    String status;

    @SerializedName("path")
    String path;

    @SerializedName("message")
    String message;

    @SerializedName("timestamp")
    String timestamp;







    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    //    public Error getError() {
//        return error;
//    }
//
//    public void setError(Error error) {
//        this.error = error;
//    }
}
