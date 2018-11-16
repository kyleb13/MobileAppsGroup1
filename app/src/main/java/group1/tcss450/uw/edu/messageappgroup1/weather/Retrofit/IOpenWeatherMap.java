package group1.tcss450.uw.edu.messageappgroup1.weather.Retrofit;

import group1.tcss450.uw.edu.messageappgroup1.model.WeatherResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLon(@Query("lat") String lat,
                                                 @Query("lon")String lon,
                                                 @Query("appid")String appid,
                                                 @Query("units")String unit);
}
