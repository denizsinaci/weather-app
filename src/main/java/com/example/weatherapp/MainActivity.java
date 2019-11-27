package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    String CITY = "istanbul,tr";
    String API = "15961c53eccc411abc8e53d714a7cc10";
    JSONObject jsonObj;

    TextView cityText, updateTimeText, weatherTypeText, degreeText, tempMinText, tempMaxText, sunriseText, sunsetText, sunriseTimeText, sunsetTimeText,
            windText, windDegreeText, pressureText, pressureDegreeText,humidityText, humidityDegreeText, createdByText, denizsinaciText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = findViewById(R.id.cityText);
        updateTimeText = findViewById(R.id.updateTimeText);
        weatherTypeText = findViewById(R.id.weatherTypeText);
        degreeText = findViewById(R.id.degreeText);
        tempMinText = findViewById(R.id.tempMinText);
        tempMaxText = findViewById(R.id.tempMaxText);
        sunriseText = findViewById(R.id.sunriseText);
        sunsetText = findViewById(R.id.sunsetText);
        sunriseTimeText = findViewById(R.id.sunriseTimeText);
        sunsetTimeText = findViewById(R.id.sunsetTimeText);
        windText = findViewById(R.id.windText);
        windDegreeText = findViewById(R.id.windDegreeText);
        pressureText = findViewById(R.id.pressureText);
        pressureDegreeText = findViewById(R.id.pressureDegreeText);
        humidityText = findViewById(R.id.humidityText);
        humidityDegreeText = findViewById(R.id.humidityDegreeText);
        createdByText = findViewById(R.id.createdByText);
        denizsinaciText = findViewById(R.id.denizSinaciText);

        weatherTask weatherTask = new weatherTask();
        weatherTask.execute();

    }


    class weatherTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        protected String doInBackground(String... args){
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            Log.i("OK",response);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updateTime = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");

                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                cityText.setText(address);
                updateTimeText.setText(updateTime);
                weatherTypeText.setText(weatherDescription);
                degreeText.setText(temp);
                tempMinText.setText(tempMin);
                tempMaxText.setText(tempMax);
                sunriseTimeText.setText(new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTimeText.setText(new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windDegreeText.setText(windSpeed);
                pressureDegreeText.setText(pressure);
                humidityDegreeText.setText(humidity);

            } catch (JSONException e) {
                degreeText.setText("error");
                Log.i("ex",e.toString());
            }
        }
    }
}
