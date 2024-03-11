import Configuration.Configuration;
import Configuration.Mode;
import Controller.Controller;
import Model.City;
import Service.PollingService;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenWeatherClient
{
    private Configuration config;

    private List<City> cities = new ArrayList<>();

    private Controller controller = new Controller();

    private Thread pollingService;

    public OpenWeatherClient (String key, Mode mode)
    {
        config = new Configuration(key, mode);

        if(mode == Mode.POLLING)
        {
            pollingService = new PollingService(config.getKey(), controller, cities);
            pollingService.start();
        }
    }

    public JsonObject getWeather (String cityName)
    {
        if(config.getMode() == Mode.ON_DEMAND)
            return getWeatherOnDemand(cityName);

        return getWeatherPolling(cityName);
    }

    private JsonObject getWeatherOnDemand(String cityName)
    {
        for(int i = 0; i < cities.size(); i++)
        {
            City city = cities.get(i);

            if(city.getName().equals(cityName) && city.isActualWeather())
                return city.getWeather();
        }

        JsonObject weather = controller.requestWeather(cityName, config.getKey());
        Date requestTime = new Date();
        cities.add(new City(cityName, weather, requestTime));
        return weather;
    }

    private JsonObject getWeatherPolling(String cityName)
    {
       for(int i = 0; i < cities.size(); i++)
       {
           City city = cities.get(i);

           if(city.getName().equals(cityName))
               return city.getWeather();
       }
       JsonObject weather = controller.requestWeather(cityName, config.getKey());
       Date requestTime = new Date();
       cities.add(new City(cityName, weather, requestTime));
       return weather;
    }

}
