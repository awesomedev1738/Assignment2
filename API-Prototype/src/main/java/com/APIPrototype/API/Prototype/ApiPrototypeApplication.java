package com.APIPrototype.API.Prototype;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;
import java.io.*;

@SpringBootApplication
public class ApiPrototypeApplication {       
    public static void main(String[] args) {
        String apiKey = "rkEgKf4ZRzYGzYwNpLovJ5BFSnhmwHyX";

        String locationKey = "329822";

        try {
            String weatherUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + locationKey + "?apikey=" + apiKey;

            URL url = new URL(weatherUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                StringBuilder response;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                String jsonData = response.toString();

                JSONObject jsonObject = new JSONObject(jsonData);

                JSONArray dailyForecasts = jsonObject.getJSONArray("DailyForecasts");

                for (int i = 0; i < dailyForecasts.length(); i++) {
                    JSONObject dailyForecast = dailyForecasts.getJSONObject(i);
                    String date = dailyForecast.getString("Date");
                    JSONObject temperature = dailyForecast.getJSONObject("Temperature");
                    int minTemperature = temperature.getJSONObject("Minimum").getInt("Value");
                    int maxTemperature = temperature.getJSONObject("Maximum").getInt("Value");
                    JSONObject night = dailyForecast.getJSONObject("Night");
                    String nightIconPhrase = night.getString("IconPhrase");
                    boolean hasPrecipitation = night.getBoolean("HasPrecipitation");

                    System.out.println("Date: " + date);
                    System.out.println("Min Temperature: " + minTemperature);
                    System.out.println("Max Temperature: " + maxTemperature);
                    System.out.println("Night Icon Phrase: " + nightIconPhrase);
                    System.out.println("Has Precipitation: " + hasPrecipitation);
                }
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }
    } 
}