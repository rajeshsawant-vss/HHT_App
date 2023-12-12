package com.tatamotors.errorproofing.networkcall;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyOkHttpInterceptorMain implements Interceptor {

    String accessToken;

    public MyOkHttpInterceptorMain() {
    }

    public MyOkHttpInterceptorMain(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type","application/json");
        builder.addHeader("Accept", "application/json");
//        try {
//            if(!accessToken.equalsIgnoreCase("")){
//                builder.addHeader("Content-Type","application/json");
//                builder.addHeader("Authorization","Bearer "+accessToken);
//            }
//            else{
//                builder.addHeader("Content-Type","application/json");
//               // builder.addHeader("request-id","b997a77ae2b3f30c1bed151c3e42434f");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        return chain.proceed(builder.build());
    }
}
