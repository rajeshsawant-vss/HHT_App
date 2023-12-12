package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Error implements Serializable {

    @SerializedName("errorCode")
    String errorCode;

    @SerializedName("message")
    String message;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
