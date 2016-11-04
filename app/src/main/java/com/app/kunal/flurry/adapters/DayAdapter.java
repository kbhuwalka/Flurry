package com.app.kunal.flurry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.kunal.flurry.R;
import com.app.kunal.flurry.Weather.DailyData;

/**
 * Created by Kunal on 3/23/2015.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private DailyData[] mDays;

    public DayAdapter(Context context, DailyData[] days){
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // We aren't using this
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            //Create a brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView= (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        DailyData day = mDays[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax()+"");
        if(position==0){
            holder.dayLabel.setText("Today");
        }else{
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
