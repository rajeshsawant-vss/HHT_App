package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecieveError implements Serializable {

    @SerializedName("status")
    int status;

    @SerializedName("path")
    String path;

    @SerializedName("message")
    String message;

    @SerializedName("timestamp")
    String timestamp;

    @SerializedName("error")
    String error;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
