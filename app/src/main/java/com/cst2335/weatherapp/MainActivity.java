package com.cst2335.weatherapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RefreshButtonFragment.OnRefreshButtonClickListener {

    private WeatherFragment weatherFragment;
    private int weatherDisplayMode = 0;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherFragment = new WeatherFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.weather_fragment_container, weatherFragment, "WeatherFragment")
                .commit();

        RefreshButtonFragment refreshButtonFragment = new RefreshButtonFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.refresh_button_fragment_container, refreshButtonFragment, "RefreshButtonFragment")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?lat=45.8168&lon=-77.1162&units=metric&appid=6c178b586e99daea7ae4d12d2cc31a9f";

        if (id == R.id.action_refresh) {
            fetchWeatherData(urlString, weatherDisplayMode);
            return true;
        } else if (id == R.id.action_current_weather) {
            weatherDisplayMode = 0;
            fetchWeatherData(urlString, weatherDisplayMode);
            return true;
        } else if (id == R.id.action_three_day_forecast) {
            weatherDisplayMode = 1;
            fetchWeatherData(urlString, weatherDisplayMode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefreshButtonClick() {
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?lat=45.8168&lon=-77.1162&units=metric&appid=6c178b586e99daea7ae4d12d2cc31a9f";
        fetchWeatherData(urlString, weatherDisplayMode);
    }

    public void fetchWeatherData(String urlString, int mode) {
        executorService.execute(() -> {
            try {
                String jsonResponse = NetworkUtils.fetchData(urlString);
                runOnUiThread(() -> {
                    if (jsonResponse != null) {
                        weatherFragment.updateWeatherData(jsonResponse, mode);
                    } else {
                        Toast.makeText(this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error fetching weather data", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
