package group1.tcss450.uw.edu.messageappgroup1.weather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;

import group1.tcss450.uw.edu.messageappgroup1.R;
import group1.tcss450.uw.edu.messageappgroup1.adapter.WeatherForecastAdapter;
import group1.tcss450.uw.edu.messageappgroup1.common.Common;
import group1.tcss450.uw.edu.messageappgroup1.model.WeatherForecastResult;
import group1.tcss450.uw.edu.messageappgroup1.retrofit.IOpenWeatherMap;
import group1.tcss450.uw.edu.messageappgroup1.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragement extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    TextView text_city_name, text_geo_coord;
    RecyclerView recycler_forecast;

    public ToggleSwitch getToggle_switch_forecast_temp() {
        return toggle_switch_forecast_temp;
    }

    public void setToggle_switch_forecast_temp(ToggleSwitch toggle_switch_forecast_temp) {
        this.toggle_switch_forecast_temp = toggle_switch_forecast_temp;
    }

    ToggleSwitch toggle_switch_forecast_temp;

    static ForecastFragement instance;

    public static ForecastFragement getInstance(){
        if(instance == null){
            instance = new ForecastFragement();
        }
        return instance;
    }
    public ForecastFragement() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_forecast, container,false);
        toggle_switch_forecast_temp = (ToggleSwitch)itemView.findViewById(R.id.toggle_switch_forecast_temp);
        text_city_name = (TextView)itemView.findViewById(R.id.text_city_name);
        text_geo_coord = (TextView)itemView.findViewById(R.id.text_geo_coord);

        recycler_forecast = (RecyclerView)itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        getForecastWeatherInformation();

        return itemView;
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", "" + throwable.getMessage());
                    }
                })
        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        text_city_name.setText(new StringBuilder(weatherForecastResult.city.name));
        text_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(),weatherForecastResult,toggle_switch_forecast_temp);
        recycler_forecast.setAdapter(adapter);

    }

}
