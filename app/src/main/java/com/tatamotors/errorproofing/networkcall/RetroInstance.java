package com.tatamotors.errorproofing.networkcall;

import com.tatamotors.errorproofing.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {

 //   private static final String Base_Url="https://api.ep-dev.tatamotors.com/assembly/";
   // private static final String Base_Url="https://assembly-pune.ep-dev.tatamotors.com/";
  //  private static final String Base_Url="https://assembly-dharwad.ep-dev.tatamotors.com/";
    private static final String Base_Url= BuildConfig.BASE_URL;
    //https://api.ep-dev.tatamotors.com/assembly/station/part-validation/KYZE00001
    private static Retrofit retrofit;

    public static final int REQUEST_TIMEOUT = 60;



    public static Retrofit getRetrofit(){


//        System.out.println("retrofitToken "+accessToken);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                //  .add("eguruskin.api.tatamotors", "sha256/Cms+gNanRPlE8LDQFgWPntgQjDZlZGg5IfAZPYqxChE=")
                //  .add("skinqa-cv.api.tatamotors", "sha256/Cms+gNanRPlE8LDQFgWPntgQjDZlZGg5IfAZPYqxChE=")

                .build();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new MyOkHttpInterceptorMain());
        httpClient.addInterceptor(logging);
        //  httpClient.certificatePinner(certificatePinner);
        httpClient.connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);




        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(Base_Url)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;

    }



}
