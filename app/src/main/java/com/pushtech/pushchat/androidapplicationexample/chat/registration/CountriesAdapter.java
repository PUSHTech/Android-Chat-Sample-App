package com.pushtech.pushchat.androidapplicationexample.chat.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pushtech.pushchat.androidapplicationexample.R;
import com.pushtech.sdk.chat.model.Country;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by goda87 on 26/08/14.
 */
public class CountriesAdapter extends BaseAdapter {

    private ArrayList<Country> countries;
    private LayoutInflater layoutInflater;

    public CountriesAdapter(Context context, Collection<Country> countries) {
        this.countries = new ArrayList<Country>(countries);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countries != null ? countries.size() : 0;
    }

    @Override
    public Country getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        ViewHolder viewHolder = null;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            rowView = layoutInflater.inflate(R.layout.item_spinner_registration_countries, null);
            viewHolder.countryNameTV = (TextView)rowView.findViewById(R.id.countryNameTV);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final Country country = getItem(position);
        if (country != null) {
            viewHolder.countryNameTV.setText(country.getName());
        }
        return rowView;
    }

    static class ViewHolder {
        TextView countryNameTV;
    }
}
