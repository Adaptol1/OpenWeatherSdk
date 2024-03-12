package Service;

import Controller.Controller;
import Model.City;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollingService extends Thread
{
    private String key;

    private Controller controller;

    private List<City> cities;

    public PollingService(String key, Controller controller, List<City> cities)
    {
        this.key = key;
        this.controller = controller;
        this.cities = cities;
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < cities.size(); i++) {
                    City city = cities.get(i);
                    if (!city.isActualWeather()) {
                        JsonObject updatedWeather = controller.requestWeather(city.getName(), key);
                        city.setWeather(updatedWeather);
                    }
                }
                TimeUnit.MINUTES.sleep(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}