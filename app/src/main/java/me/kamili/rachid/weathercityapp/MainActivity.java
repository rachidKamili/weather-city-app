package me.kamili.rachid.weathercityapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.kamili.rachid.weathercityapp.model.City;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "ccb67a7bbec4c82b2b835fd65d0c0ca1";
    private final String LAST_CITY_SEARCHED = "last_city_searched";
    private final String ICON_LINK = "http://openweathermap.org/img/w/";
    private final String ICON_FORMAT = ".png";

    @BindView(R.id.etCitySearch)
    EditText etCitySearch;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTemp)
    TextView tvTemp;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvWind)
    TextView tvWind;
    @BindView(R.id.tvHum)
    TextView tvHum;
    @BindView(R.id.tvSunUp)
    TextView tvSunUp;
    @BindView(R.id.tvSunDown)
    TextView tvSunDown;
    @BindView(R.id.cardView)
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String city = sharedPref.getString(LAST_CITY_SEARCHED, "");
        etCitySearch.setText(city);

        if (!city.isEmpty())
            getData(city);
    }

    private void getData(final String city) {
        APIManager.getApiService().getWeatherInfo(
                city,
                API_KEY)
                .enqueue(new Callback<City>() {
                    @Override
                    public void onResponse(Call<City> call, Response<City> response) {
                        City weather = response.body();

                        if (weather!=null){
                            String city = weather.getName()+", "+ weather.getSys().getCountry();
                            tvCity.setText(city);
                            etCitySearch.setText(city);
                            tvTime.setText("as of "+getDate(weather.getDt()));
                            tvTemp.setText((int)weather.getMain().getTemp()+"°");
                            tvDesc.setText(weather.getWeather().get(0).getDescription().toUpperCase());
                            tvWind.setText(weather.getWind().getSpeed() + " mph");
                            tvHum.setText(weather.getMain().getHumidity()+ "%");
                            tvSunUp.setText(getDate(weather.getSys().getSunrise())+" ");
                            tvSunDown.setText(getDate(weather.getSys().getSunset())+" ");
                            Glide.with(MainActivity.this)
                                    .load(ICON_LINK+weather.getWeather().get(0).getIcon() + ICON_FORMAT)
                                    .apply(new RequestOptions().override(50,50))
                                    .into(ivIcon);

                            cardView.setVisibility(View.VISIBLE);

                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(LAST_CITY_SEARCHED, city);
                            editor.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<City> call, Throwable t) {

                    }
                });
    }


    public void onSearch(View view) {
        String city = etCitySearch.getText().toString();
        if (!city.isEmpty()) {
            getData(city);
        }
    }

    private String getDate(long timeStamp){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "NONE";
        }
    }
}
