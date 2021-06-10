package com.yoshualukas.weatherapp.retrofit;

import com.yoshualukas.weatherapp.model.History;
import com.yoshualukas.weatherapp.model.MainModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
public interface ApiEndpoint {
    @GET("current.json?key=56c5cbdb9c8c471c8cc84631210806&q=jakarta&aqi=yes")
    Call<MainModel> getCurrentData();
    @GET("forecast.json?key=56c5cbdb9c8c471c8cc84631210806&q=Jakarta&days=5&aqi=no&alerts=no")
    Call<MainModel> getForecastData();
    @GET Call<History> getHistoryData(@Url String url);
}
