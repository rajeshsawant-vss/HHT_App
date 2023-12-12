package com.tatamotors.errorproofing.networkcall;

import android.content.Intent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tatamotors.errorproofing.model.LoginModel;
import com.tatamotors.errorproofing.requests.SkipRequest;
import com.tatamotors.errorproofing.requests.UpdateValidation;
import com.tatamotors.errorproofing.requests.ValidationRequest;
import com.tatamotors.errorproofing.response.LoginResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.ValidationResponse;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

//    @FormUrlEncoded
//    @POST("auth/signin")
//    Call<JsonObject> login(@Field("username") String userName,
//                           @Field("password")String passWord);
//
    @POST("auth/signin")
    Call<LoginResponse> login(@Body RequestBody req);
    //    @POST("station/part-validation/{serialNumber}/validate")
///station/part-validation/{sequenceNumber}/validate
    @POST("station/part-validation/{sequenceNumber}/validate")
    Call<SerialNovalidation> validateSerialno(@Header("authorization") String auth,
                                              @Path("sequenceNumber") String serial_id,
                                              @Body ValidationRequest req);


    ///station/part-validation/{sequenceNumber}/update-validation

    @POST("station/part-validation/{sequenceNumber}/update-validation/{idToUpdate}")
    Call<SerialNovalidation> updateValidation(@Header("authorization") String auth,
                                   @Path("sequenceNumber") String serial_id,
                                   @Path("idToUpdate") String idToUpdate,
                                   @Body UpdateValidation req);


    @GET("station/part-validation/{serialNumber}")
    Call<SerialNovalidation> startSerialNo(@Header("authorization") String auth,
                                           @Path("serialNumber") String serialNumber);


    @POST("station/part-validation/{sequenceNumber}/skip")
    Call<SerialNovalidation> skipPartNo(@Header("authorization") String auth,
                                        @Path("sequenceNumber") String serial_id,
                                        @Body SkipRequest skipRequest);

    @POST("/station/book/{sequenceNumber}")///station/receive/{serialNumber}
    Call<Void> book(@Header("authorization") String auth,
                                        @Path("sequenceNumber") String sequenceNo);

    @POST("/station/receive/{serialNumber}")
    Call<Void> recive(@Header("authorization") String auth,
              @Path("serialNumber") String sequenceNo);

    @POST("/station/complete/{sequenceNumber}")
    Call<Void> complete(@Header("authorization") String auth,
                @Path("sequenceNumber") String sequenceNo);


//    @POST("auth/signin")
//    Call<LoginModel> login(@Body LoginModel req);

}
