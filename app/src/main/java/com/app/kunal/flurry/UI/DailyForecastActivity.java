package com.app.kunal.flurry.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.kunal.flurry.R;
import com.app.kunal.flurry.Weather.DailyData;
import com.app.kunal.flurry.adapters.DayAdapter;

import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {

    private DailyData[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelabels = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelabels, parcelabels.length, DailyData[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String dayOfWeek = mDays[position].getDayOfTheWeek();
        String conditions = mDays[position].getSummary();
        String highTemp = mDays[position].getTemperatureMax()+"";
        String message = String.format("on %s the high will be %s and it will be %s",
                dayOfWeek,
                highTemp,
                conditions);
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
