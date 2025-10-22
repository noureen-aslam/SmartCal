package com.example.smartcal.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.Map;

public interface FoodApiService {
    @GET("search")
    Call<Map<String, Object>> searchFood(@Query("query") String q, @Query("api_key") String apiKey);

    @GET("barcode")
    Call<Map<String, Object>> lookupBarcode(@Query("barcode") String barcode, @Query("api_key") String apiKey);
}
