package com.cst2335.weatherapp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class WeatherFragment extends Fragment {

    private LinearLayout weatherLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherLinearLayout = view.findViewById(R.id.linearLayout_weather);
        return view;
    }

    public void updateWeatherData(String data, int displayMode) {
        if (getActivity() == null || data == null) {
            Log.e("WeatherFragment", "Activity is null or data is null");
            return;
        }

        Log.d("WeatherFragment", "JSON data: " + data);

        getActivity().runOnUiThread(() -> {
            weatherLinearLayout.removeAllViews();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray listArray = jsonObject.getJSONArray("list");
                Calendar today = Calendar.getInstance();
                SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String todayDate = apiDateFormat.format(today.getTime());
                Set<String> processedDates = new HashSet<>();

                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject listObject = listArray.getJSONObject(i);
                    if (listObject == null) continue;

                    long dateTimeInMillis = listObject.getLong("dt") * 1000;
                    Date date = new Date(dateTimeInMillis);
                    String dateText = apiDateFormat.format(date);

                    if (displayMode == 0 && !dateText.equals(todayDate)) {
                        continue;
                    }
                    if (displayMode == 1 && processedDates.size() >= 3) {
                        break;
                    }
                    if (dateText.equals(todayDate) || displayMode == 1) {
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a", Locale.getDefault());
                        String formattedDate = dateFormatter.format(date);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                        int backgroundColor;

                        if (hourOfDay >= 6 && hourOfDay < 9) {
                            backgroundColor = Color.parseColor("#FFD54F");
                        } else if (hourOfDay >= 9 && hourOfDay < 17) {
                            backgroundColor = Color.parseColor("#FFEB5B");
                        } else if (hourOfDay >= 17 && hourOfDay < 20) {
                            backgroundColor = Color.parseColor("#FF7943");
                        } else {
                            backgroundColor = Color.parseColor("#2196F3");
                        }

                        LinearLayout entryContainer = new LinearLayout(getContext());
                        entryContainer.setOrientation(LinearLayout.VERTICAL);
                        entryContainer.setPadding(20, 20, 20, 20);

                        GradientDrawable background = new GradientDrawable();
                        background.setColor(backgroundColor);
                        entryContainer.setBackground(background);

                        TextView dateTextView = new TextView(getContext());
                        dateTextView.setText(formattedDate);
                        entryContainer.addView(dateTextView);

                        JSONObject mainObject = listObject.getJSONObject("main");
                        if (mainObject == null) continue;

                        double temperature = mainObject.getDouble("temp");

                        TextView temperatureTextView = new TextView(getContext());
                        temperatureTextView.setText("Temperature: " + temperature + " °C");
                        entryContainer.addView(temperatureTextView);

                        JSONArray weatherArray = listObject.getJSONArray("weather");
                        if (weatherArray.length() > 0) {
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String description = weatherObject.getString("description");

                            TextView descriptionTextView = new TextView(getContext());
                            descriptionTextView.setText("Description: " + description);
                            entryContainer.addView(descriptionTextView);

                            String iconId = weatherObject.getString("icon");
                            String iconUrl = "http://openweathermap.org/img/w/" + iconId + ".png";

                            ImageView weatherIcon = new ImageView(getContext());
                            weatherIcon.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                            Glide.with(getContext()).load(iconUrl).into(weatherIcon);
                            entryContainer.addView(weatherIcon);
                        }

                        weatherLinearLayout.addView(entryContainer);
                        processedDates.add(dateText);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}


