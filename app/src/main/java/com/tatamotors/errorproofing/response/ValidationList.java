package com.tatamotors.errorproofing.response;

import com.google.gson.annotations.SerializedName;

public class ValidationList {


    @SerializedName("familyId")
    int familyId;

    @SerializedName("familyDisplayName")
    String familyDisplayName;

    @SerializedName("partNumber")
    String partNumber;

}
