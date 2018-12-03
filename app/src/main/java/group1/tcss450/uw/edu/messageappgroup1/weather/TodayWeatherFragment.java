//package group1.tcss450.uw.edu.messageappgroup1.weather;
package group1.tcss450.uw.edu.messageappgroup1.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;
import com.squareup.picasso.Picasso;

import group1.tcss450.uw.edu.messageappgroup1.R;
import group1.tcss450.uw.edu.messageappgroup1.common.Common;
import group1.tcss450.uw.edu.messageappgroup1.model.WeatherResult;
import group1.tcss450.uw.edu.messageappgroup1.retrofit.IOpenWeatherMap;
import group1.tcss450.uw.edu.messageappgroup1.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;



//import group1.tcss450.uw.edu.messageappgroup1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    ImageView img_weather;
    TextView text_city_name, text_wind, text_humidity, text_pressure,
            text_sunrise, text_sunset, text_temperature,
            text_description, text_date_time, text_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    ToggleSwitch toggle_switch_temp;
    double temp;

    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance(){
        if(instance == null)
            instance = new TodayWeatherFragment();
        return instance;
    }

    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);
        img_weather = (ImageView)itemView.findViewById(R.id.img_weather);
        text_city_name = (TextView) itemView.findViewById(R.id.text_city_name);
        text_date_time = (TextView)itemView.findViewById(R.id.text_date_time);
        text_description = (TextView)itemView.findViewById(R.id.text_description);
        text_geo_coord = (TextView)itemView.findViewById(R.id.text_geo_coord);
        text_humidity = (TextView)itemView.findViewById(R.id.text_humidity);
        text_sunrise = (TextView)itemView.findViewById(R.id.text_sunrise);
        text_sunset = (TextView)itemView.findViewById(R.id.text_sunset);
        text_temperature = (TextView)itemView.findViewById(R.id.text_temperature);
        text_wind = (TextView)itemView.findViewById(R.id.text_wind);
        text_pressure= (TextView)itemView.findViewById(R.id.text_pressure);

        weather_panel = (LinearLayout)itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar)itemView.findViewById(R.id.loading);

        toggle_switch_temp = (ToggleSwitch) itemView.findViewById(R.id.toggle_switch_temp);

        getWeatherInformation();
        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        //Load image
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                            .append(weatherResult.getWeather().get(0).getIcon())
                                            .append(".png").toString()).into(img_weather);

                        //Load information
                        text_city_name.setText(weatherResult.getName());
                        text_description.setText(new StringBuilder("Weather in ").append(weatherResult.getName()).toString());
                        temp = weatherResult.getMain().getTemp();
                        text_temperature.setText(new StringBuilder(String.valueOf(Math.round(temp)))
                                .append("°C").toString());
                        toggle_switch_temp.setOnChangeListener(new ToggleSwitch.OnChangeListener(){
                            @Override
                            public void onToggleSwitchChanged(int position) {
                                if (position == 0) {
                                    text_temperature.setText(new StringBuilder(String.valueOf(Math.round(temp)))
                                            .append("°C").toString());
                                } else {
                                    text_temperature.setText(new StringBuilder(String.valueOf(Math.round(temp*9/5+32)))
                                            .append("°F").toString());
                                }
                            }
                        });
                        text_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        text_wind.setText(new StringBuilder("Speed: " + String.valueOf(weatherResult.getWind().getSpeed()))
                                                            .append(" Deg: " + String.valueOf(weatherResult.getWind().getDeg())));
                        text_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure()))
                                                .append(" hpa").toString());
                        text_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity()))
                                                .append(" %").toString());
                        text_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        text_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        text_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());

                        //Display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }, throwable -> Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show())
        );
    }
    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
