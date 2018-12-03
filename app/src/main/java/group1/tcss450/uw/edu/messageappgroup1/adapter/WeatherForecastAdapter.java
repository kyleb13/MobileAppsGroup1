package group1.tcss450.uw.edu.messageappgroup1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;
import com.squareup.picasso.Picasso;

import group1.tcss450.uw.edu.messageappgroup1.R;
import group1.tcss450.uw.edu.messageappgroup1.common.Common;
import group1.tcss450.uw.edu.messageappgroup1.model.WeatherForecastResult;
import group1.tcss450.uw.edu.messageappgroup1.weather.ForecastFragement;


public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForecastResult weatherForecastResult;
    MyViewHolder myViewH;
    ForecastFragement forecastFragement;
    ToggleSwitch myToggleSwitch;
    int position;
    double temp;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult, ToggleSwitch toggleSwitch) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
        this.myToggleSwitch = toggleSwitch;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewH = myViewHolder;
        position = i;
        //Load image
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(myViewHolder.img_weather);


        myViewH.text_date_time.setText(new StringBuilder(Common.convertUnixToDate(
                weatherForecastResult.list.get(position).dt)));
        myViewH.text_description.setText(new StringBuilder(weatherForecastResult.
                list.get(position).weather.get(0).getDescription()));


//        myViewH.text_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.
//                list.get(position).main.getTemp())).append("°C").toString());

        myViewH.text_temperature.setText(new StringBuilder(String.valueOf(Math.round(weatherForecastResult.
                list.get(position).main.getTemp()*9/5+32))).append("°F").toString());
//        myViewH.text_temperature.setText(new StringBuilder(String.valueOf(temp)).append("°C").toString());
//        myToggleSwitch.setOnChangeListener(new ToggleSwitch.OnChangeListener() {
//            @Override
//            public void onToggleSwitchChanged(int toggle_position) {
//                if (toggle_position == 0) {
//                    myViewH.text_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°C").toString());
//                } else {
//                    myViewH.text_temperature.setText(new StringBuilder(String.valueOf(Math.round(weatherForecastResult.list.get(position).main.getTemp()*9/5+32)))
//                            .append("°F").toString());
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_date_time, text_description, text_temperature;
        ImageView img_weather;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
            text_date_time = (TextView) itemView.findViewById(R.id.text_date);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            text_temperature = (TextView) itemView.findViewById(R.id.text_temperature);
        }
    }
}
