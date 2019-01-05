package com.navigation.edgescreen.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.RemoteFetch;
import com.navigation.edgescreen.data.CityPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private TextView cityField, currentTemperatureField, weatherIcon, tvHumidity, tvPressure, tvDetail, tvTime, tvWind;
    private Handler handler;
    private int mDay, mMount, mYear;
    private LocationManager locationManager;

    private Activity activity;
    private String provider;
    public WeatherFragment() {
        handler = new Handler();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        activity = getActivity();
        cityField = view.findViewById(R.id.city_field);
        currentTemperatureField = view.findViewById(R.id.current_temperature_field);
        weatherIcon = view.findViewById(R.id.weather_icon);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvPressure = view.findViewById(R.id.tvPressure);
        tvDetail = view.findViewById(R.id.detail);
        tvTime = view.findViewById(R.id.tvTime);
        tvWind = view.findViewById(R.id.tvWind);
        weatherIcon.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/weathericons-regular-webfont.ttf"));
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            getCityName(location);
        }

        Thread t = new Thread() {


            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        if (activity == null)
                            return;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public void getCityName(final Location location) {
        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String city = address.get(0).getAdminArea().toUpperCase(Locale.US);
            String country = address.get(0).getCountryCode();
            String fnialAddress = city + ", " + country;
            updateWeatherData(fnialAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void updateTime() {
        Calendar cal = Calendar.getInstance();
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mMount = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);
        tvTime.setText(mYear + "-" + (mMount + 1) + "-" + mDay);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void renderWeather(JSONObject json) {
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            tvDetail.setText(details.getString("description").toUpperCase(Locale.US));
            tvHumidity.setText(main.getString("humidity") + "%");
            tvPressure.setText(main.getString("pressure") + " hPa");

            currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " ℃");
            JSONObject wind = json.getJSONObject("wind");
            tvWind.setText(wind.getDouble("speed") + " m/s");
            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json;
                json = RemoteFetch.getJSON(activity, city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(activity,
                                    activity.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }


            }
        }.start();
    }


    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = activity.getString(R.string.weather_sunny);
            } else {
                icon = activity.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = activity.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = activity.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = activity.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = activity.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = activity.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = activity.getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(Html.fromHtml(icon));
    }

}
