package me.kamili.rachid.weathercityapp;

import me.kamili.rachid.weathercityapp.model.City;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class APIManager {

    private static final String URL = "http://api.openweathermap.org/data/2.5/";

    public static APIService getApiService() {
        Retrofit restAdapter = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return restAdapter.create(APIService.class);
    }

    public interface APIService {
        @GET("weather")
        Call<City> getWeatherInfo(@Query("q") String citySearched,
                                  @Query("appid") String appid);
    }
}
