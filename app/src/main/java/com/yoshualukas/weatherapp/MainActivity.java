package com.yoshualukas.weatherapp;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.yoshualukas.weatherapp.adapter.DetailForecastAdapter;
import com.yoshualukas.weatherapp.adapter.DetailCurrentAdapter;
import com.yoshualukas.weatherapp.adapter.ForecastAdapter;
import com.yoshualukas.weatherapp.model.AirQuality;
import com.yoshualukas.weatherapp.model.Forecast;
import com.yoshualukas.weatherapp.model.Forecastday;
import com.yoshualukas.weatherapp.model.History;
import com.yoshualukas.weatherapp.model.Hour;
import com.yoshualukas.weatherapp.model.MainModel;
import com.yoshualukas.weatherapp.retrofit.ApiService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DetailCurrentAdapter detailCurrentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static MainActivity mainActivity;

    TextView tv_loc, tv_wind, tv_pressure, tv_precip, tv_humidity, tv_cloud, tv_gust, tv_condition, tv_temp, tv_lastup, tv_localtime ;
    ImageView img;
    TextView tv_co, tv_no2, tv_so2, tv_o3, tv_pm2_5, tv_pm10, tv_epa;
    TextView tv_hist_date, tv_hist_avgtemp, tv_hist_condition;
    TextView tv_fc_date, tv_fc_avgtemp, tv_fc_condition;
    TextView tv_today_date, tv_today_avgtemp, tv_today_condition;
    ImageView history_img, tv_icon, today_img, today_img1;

    SwipeRefreshLayout swipeLayout;
    LinearLayout linearLayout;

    ArrayList<Forecastday> listForecast = new ArrayList<>();
    ArrayList<Forecastday> listHistory = new ArrayList<>();
    ArrayList<Hour> listDetailCurrent = new ArrayList<>();
    private static final String TAG="MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize swipe refresh layout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        linearLayout = (LinearLayout) findViewById(R.id.swiperefresh);
        //refresh color
        int color1 = getResources().getColor(R.color.Purple);
        int color2 = getResources().getColor(R.color.Paradise);
        swipeLayout.setColorSchemeColors(color1,color2);


        tv_loc = findViewById(R.id.tv_location);
        tv_wind = findViewById(R.id.tv_wind);
        tv_pressure = findViewById(R.id.tv_pressure);
        tv_precip = findViewById(R.id.tv_precip);
        tv_humidity = findViewById(R.id.tv_humidity);
        tv_cloud = findViewById(R.id.tv_cloud);
        tv_gust = findViewById(R.id.tv_gust);
        tv_condition = findViewById(R.id.tv_condition);
        tv_temp = findViewById(R.id.tv_temp);
        tv_lastup = findViewById(R.id.tv_lastup);
        tv_localtime = findViewById(R.id.tv_localtime);
        img = findViewById(R.id.img_weather);

        tv_epa = findViewById(R.id.btn_aqi);

        tv_today_avgtemp = findViewById(R.id.tv_today_avgtemp);
        tv_today_condition = findViewById(R.id.tv_today_condition);
        today_img = findViewById(R.id.img_today);

        tv_fc_avgtemp = findViewById(R.id.tv_forecast_avgtemp);
        tv_fc_condition = findViewById(R.id.tv_forecast_condition);
        tv_icon = findViewById(R.id.img_forecast);

        tv_hist_avgtemp = findViewById(R.id.tv_history_avgtemp);
        tv_hist_condition = findViewById(R.id.tv_history_condition);
        history_img = findViewById(R.id.img_history);

        recyclerView = findViewById(R.id.list_current);
        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailCurrentAdapter);
        mainActivity = this;

        getCurrentDataFromApi();
        getForecastDataFromApi();
        getHistoryDataFromApi();

        //Refresh
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void getCurrentDataFromApi (){
        ApiService.endpoint().getCurrentData().enqueue(new Callback<MainModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                String hari = response.body().getCurrent().getLast_updated();
                SimpleDateFormat fromlastup = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat Formatter = new SimpleDateFormat("EEEE");
                SimpleDateFormat Formatter1 = new SimpleDateFormat("HH:mm");
                String dateFormat = null;
                String dateFormat1 = null;
                try {
                    dateFormat = Formatter.format(fromlastup.parse(hari));
                    dateFormat1 = Formatter1.format(fromlastup.parse(hari));
                } catch (ParseException e) {
                    e.printStackTrace();

                }
                tv_localtime.setText(dateFormat);
                tv_lastup.setText("Last Updated : " + dateFormat1);
                tv_loc.setText(response.body().getLocation().getName());
                tv_wind.setText(Double.toString(response.body().getCurrent().getWind_kph()) + " kph");
                tv_pressure.setText(Double.toString(response.body().getCurrent().getPressure_mb() )+ " mb");
                tv_precip.setText(Double.toString(response.body().getCurrent().getPrecip_mm())+ " mm");
                tv_humidity.setText(Integer.toString(response.body().getCurrent().getHumidity()) + " %");
                tv_cloud.setText(Integer.toString(response.body().getCurrent().getCloud()) + " %");
                tv_gust.setText(Double.toString(response.body().getCurrent().getGust_kph()) + " kph");
                tv_condition.setText(response.body().getCurrent().getCondition().getText());
                tv_temp.setText(Double.toString(response.body().getCurrent().getTemp_c()) + " \u2103");
                tv_epa.setText("AQI " + response.body().getCurrent().getAir_quality().getUsepaindex());
                Glide.with(MainActivity.this).load("https:" + response.body().getCurrent().getCondition().getIcon()).into(img);

            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {

            }
        });
    }


    private void getForecastDataFromApi() {
        ApiService.endpoint().getForecastData().enqueue(new Callback<MainModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {

                listForecast = response.body().getForecast().getForecastday();

                tv_today_avgtemp.setText(Double.toString(listForecast.get(0).getDay().getAvgtemp_c()) + " °C");
                tv_today_condition.setText(listForecast.get(0).getDay().getCondition().getText());
                Glide.with(MainActivity.this).load("https:" + listForecast.get(0).getDay().getCondition().getIcon()).into(today_img);


                tv_fc_avgtemp.setText(Double.toString(listForecast.get(1).getDay().getAvgtemp_c()) + " °C");
                tv_fc_condition.setText(listForecast.get(1).getDay().getCondition().getText());
                Glide.with(MainActivity.this).load("https:" + listForecast.get(1).getDay().getCondition().getIcon()).into(tv_icon);

                listDetailCurrent = listForecast.get(0).getHour();
                DetailCurrentAdapter detailCurrentAdapter = new DetailCurrentAdapter(listDetailCurrent);
                recyclerView.setAdapter(detailCurrentAdapter);
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getHistoryDataFromApi() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        String history = dateFormat.format(cal.getTime());

        Log.d(TAG, String.valueOf(dateFormat.format(cal.getTime())));

        //History -1
        ApiService.endpoint().getHistoryData("history.json?key=56c5cbdb9c8c471c8cc84631210806&q=Jakarta&dt=" + history).enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                listHistory = response.body().getForecast().getForecastday();

                tv_hist_avgtemp.setText(Double.toString(listHistory.get(0).getDay().getAvgtemp_c())+ " °C");
                tv_hist_condition.setText(listHistory.get(0).getDay().getCondition().getText());
                Glide.with(MainActivity.this).load("https:" + listHistory.get(0).getDay().getCondition().getIcon()).into(history_img);

            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {

            }
        });

    }

    public void aqibutton(View v){
        Intent intent = new Intent(this, AirQualityActivity.class);
        startActivity (intent);
    }
    public void forecastButton(View v){
        Intent intent = new Intent(this, ForecastActivity.class);
        startActivity (intent);
    }
}