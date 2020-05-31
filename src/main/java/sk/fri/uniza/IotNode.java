package sk.fri.uniza;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sk.fri.uniza.api.WeatherStationService;
import sk.fri.uniza.model.WeatherData;

import java.io.IOException;
import java.util.List;

public class IotNode {

    private final Retrofit retrofit;
    private final WeatherStationService weatherStationService;

    public IotNode(){
        retrofit = new Retrofit.Builder()

                .baseUrl("http://ip172-18-0-36-br9tncaosm4g00a0vi9g-9000.direct.labs.play-with-docker.com/")//http://localhost:9000/
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        weatherStationService = retrofit.create(WeatherStationService.class);
    }

    public WeatherStationService getWeatherStationService(){
        return weatherStationService;
    }
    double getAverageTemperature(String station,String from, String to){

        double temperature = 0.0;

        Call<List<WeatherData>> historyWeatherPojo = getWeatherStationService()
                .getHistoryWeather(station,from,to,List.of("airTemperature"));
        try{
            Response<List<WeatherData>> response = historyWeatherPojo.execute();
            if(response.isSuccessful()){
                List<WeatherData> body = response.body();
                int sizeOfBody = body.size();
                System.out.println("velkost body je " + sizeOfBody);
                for(int i = 0; i< sizeOfBody;i++){
                    temperature = temperature + body.get(i).getAirTemperature();
                }
                temperature = temperature / sizeOfBody;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return temperature;
    }
}
