package sk.fri.uniza;

import retrofit2.Call;
import java.util.List;
import java.util.Map;
import retrofit2.Response;
import java.io.IOException;

import retrofit2.http.Path;
import retrofit2.http.Query;
import sk.fri.uniza.model.Location;
import sk.fri.uniza.model.WeatherData;
/**
 * Hello IoT!
 */

public class App {

    public static void main(String[] args) {
        // vytvorenie poziadavky na ziskanie udajov o aktualnom pocasi z meteo stance s ID: station 1
        IotNode iotNode = new IotNode();

        Call<Map<String, String>> currentWeather = iotNode.getWeatherStationService()
                .getCurrentWeatherAsMap("station_1",
                        List.of("time", "date", "airTemperature"));

        try {
            //odosielanie poziadavky na server pomocou resst rozhrania
            Response<Map<String, String>> response = currentWeather.execute();

            if (response.isSuccessful()) { // dotaz na serrver bol uspesny
                // yiskanie udajov vo forme mapy
                Map<String, String> body = response.body();
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Vytvorenie poziadavky na ziskanie udajov o vsetkych meteo staniciach
        Call<List<Location>> stationLocation =
                iotNode.getWeatherStationService().getStationLocations();
        try {
            Response<List<Location>> response = stationLocation.execute();
            if (response.isSuccessful()) {
                List<Location> body = response.body();
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // vytvorenie poziadavky na ziskanie udajov o aktualnom pocasi
        // z meteostanice s id: station_1
        Call<WeatherData> currentWeatherPojo = iotNode.getWeatherStationService()
                .getCurrentWeather("station_1");

        try {
            Response<WeatherData> response = currentWeatherPojo.execute();
            if (response.isSuccessful()) {
                WeatherData body = response.body();
                System.out.println(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //vytvorenie poziadavky na ziskanie historickych udajov o pocasi
        // z meteostanice s id: station_1
        Call<List<WeatherData>> historyWeatherPojo = iotNode.getWeatherStationService()
                    .getHistoryWeather("station_1","20/01/2020 15:00","21/01/2020 15:00",List.of("station_name","date","time","airTemperature"));
        try{
            Response<List<WeatherData>> response = historyWeatherPojo.execute();
            if(response.isSuccessful()){
                List<WeatherData> body = response.body();
                System.out.println(body);
                double averageTemperature=iotNode.getAverageTemperature("station_1","20/01/2020 15:00","21/01/2020 15:00");
                System.out.println(averageTemperature);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
