package com.app.kunal.flurry.Weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Kunal on 3/22/2015.
 */
public class DailyData implements Parcelable{
    private String mSummary;
    private String mTimezone;
    private long mTime;
    private double mTemperatureMax;
    private String mIcon;

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime*1000);

        return formatter.format(dateTime);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeString(mTimezone);
        dest.writeString(mIcon);
        dest.writeDouble(mTemperatureMax);
    }

    public  DailyData(){}

    private DailyData(Parcel in){
        mTime = in.readLong();
        mSummary = in.readString();
        mTimezone = in.readString();
        mIcon = in.readString();
        mTemperatureMax = in.readDouble();
    }

    public static final Creator<DailyData> CREATOR = new Creator<DailyData>() {
        @Override
        public DailyData createFromParcel(Parcel source) {
            return new DailyData(source);
        }

        @Override
        public DailyData[] newArray(int size) {
            return new DailyData[size];
        }
    };

    //Getters and Setters for all member variables
    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }


}
