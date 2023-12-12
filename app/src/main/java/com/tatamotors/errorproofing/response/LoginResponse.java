package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;
import com.tatamotors.errorproofing.model.Plant;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    @SerializedName("token")public String token;
    @SerializedName("station")public StationDetails stationDetails;
    @SerializedName("line")public LineDetails lineDetails;

    @SerializedName("plant")public Plant plant;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public StationDetails getStationDetails() {
        return stationDetails;
    }

    public void setStationDetails(StationDetails stationDetails) {
        this.stationDetails = stationDetails;
    }

    public LineDetails getLineDetails() {
        return lineDetails;
    }

    public void setLineDetails(LineDetails lineDetails) {
        this.lineDetails = lineDetails;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", stationDetails=" + stationDetails +
                ", lineDetails=" + lineDetails +
                ", plant=" + plant +
                '}';
    }
}
