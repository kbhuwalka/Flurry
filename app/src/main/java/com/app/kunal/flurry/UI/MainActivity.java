package com.app.kunal.flurry.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kunal.flurry.R;
import com.app.kunal.flurry.Weather.CurrentData;
import com.app.kunal.flurry.Weather.DailyData;
import com.app.kunal.flurry.Weather.Forecast;
import com.app.kunal.flurry.Weather.HourlyData;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {


    //Getting all the views in the activity using Butter Knife
    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(){
        Intent intent = new Intent(this,HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });

        getForecast();

        //Log.d(TAG, "Main UI code is running!");

    }

    private void getForecast() {
        if(isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast=getForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception Caught: ", e);
                    }
                    catch (JSONException e){

                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Network Unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility()==View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        CurrentData currentData = mForecast.getCurrentData();
        mTemperatureLabel.setText(currentData.getTemperature()+"");
        mPrecipValue.setText(currentData.getPrecipChance()+"%");
        mHumidityValue.setText(currentData.getHumidity()+"");
        mTimeLabel.setText("At "+ currentData.getFormattedTime()+ " it will be");
        mSummaryLabel.setText(currentData.getSummary());
        Drawable drawable = getResources().getDrawable(currentData.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast getForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrentData(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        return forecast;
    }

    private HourlyData[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        HourlyData[] hours = new HourlyData[data.length()];
        for(int i=0; i<data.length();i++){
            JSONObject jsonHour = data.getJSONObject(i);
            HourlyData hour =new HourlyData();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTimezone(timezone);
            hour.setTime(jsonHour.getLong("time"));
            hour.setIcon(jsonHour.getString("icon"));
            hours[i]=hour;
        }
        return hours;
    }

    private DailyData[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        DailyData[] days = new DailyData[data.length()];
        for(int i=0; i<data.length();i++){
            JSONObject jsonDay = data.getJSONObject(i);
            DailyData day =new DailyData();

            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTimezone(timezone);
            day.setTime(jsonDay.getLong("time"));
            day.setIcon(jsonDay.getString("icon"));
            days[i]=day;
        }
        return days;
    }


    private CurrentData getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG,"JSON Timezone: "+timezone);
        JSONObject currently = forecast.getJSONObject("currently");
        CurrentData currentData = new CurrentData();
        currentData.setTime(currently.getLong("time"));
        currentData.setTemperature(currently.getDouble("temperature"));
        currentData.setHumidity(currently.getDouble("humidity"));
        currentData.setIcon(currently.getString("icon"));
        currentData.setPrecipChance(currently.getDouble("precipProbability"));
        currentData.setSummary(currently.getString("summary"));
        currentData.setTimezone(timezone);

        Log.d(TAG, "JSON formatted time: "+ currentData.getFormattedTime());

        return currentData;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected())
            isAvailable=true;
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }




    private String apiKey="c1f78f96f1eaa9914e06a8f47118e5a7";
    private double latitude=12.8268;
    private double longitude=80.050;
    private String forecastURL=
            "https://api.forecast.io/forecast/"+apiKey+"/"+latitude+","+longitude;
    private static final String TAG = MainActivity.class.getSimpleName().toString();
    public static  final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private Forecast mForecast;

}
