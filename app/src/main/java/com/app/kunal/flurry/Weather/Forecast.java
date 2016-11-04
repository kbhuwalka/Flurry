package com.app.kunal.flurry.Weather;

import com.app.kunal.flurry.R;

/**
 * Created by Kunal on 3/22/2015.
 */
public class Forecast {
    private CurrentData mCurrentData;
    private HourlyData[] mHourlyForecast;
    private DailyData[] mDailyForecast;

    public static int getIconId(String iconString){
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconId = R.drawable.clear_day;
        if(iconString.equals("clear-day"))
            iconId = R.drawable.clear_day;
        else if(iconString.equals("clear-night"))
            iconId= R.drawable.clear_night;
        else if(iconString.equals("rain"))
            iconId = R.drawable.rain;
        else if(iconString.equals("snow"))
            iconId = R.drawable.snow;
        else if(iconString.equals("sleet"))
            iconId = R.drawable.sleet;
        else if(iconString.equals("wind"))
            iconId = R.drawable.wind;
        else if(iconString.equals("fog"))
            iconId = R.drawable.fog;
        else if(iconString.equals("cloudy"))
            iconId = R.drawable.cloudy;
        else if(iconString.equals("partly-cloudy-day"))
            iconId = R.drawable.partly_cloudy;
        else if(iconString.equals("rain"))
            iconId = R.drawable.cloudy_night;

        return iconId;
    }

    //Member variables Getters and Setters
    public DailyData[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(DailyData[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public CurrentData getCurrentData() {
        return mCurrentData;
    }

    public void setCurrentData(CurrentData currentData) {
        mCurrentData = currentData;
    }

    public HourlyData[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(HourlyData[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }
}
