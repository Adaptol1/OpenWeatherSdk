import Configuration.Configuration;
import Configuration.Mode;
import Controller.Controller;
import Model.City;
import Service.PollingService;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import Exception.OpenWeatherSdkIllegalKeyException;
import Exception.OpenWeatherSdkCitiesLimitException;

public class OpenWeatherClient
{
    private final Configuration config;

    private final byte MAX_CITY_COUNT = 10;

    private List<City> cities;

    private Controller controller = new Controller();

    private PollingService pollingService;

    public static HashMap<String, OpenWeatherClient> sdkClientsPool = new HashMap<>();

    public static OpenWeatherClient newOpenWeatherClient (String key, Mode mode) throws InterruptedException, RuntimeException
    {
        if (sdkClientsPool.containsKey(key))
            throw new OpenWeatherSdkIllegalKeyException("This key is already used");

        OpenWeatherClient openWeatherClient = new OpenWeatherClient(key, mode);
        sdkClientsPool.put(key, openWeatherClient);
        return openWeatherClient;
    }

    private OpenWeatherClient (String key, Mode mode)
    {
        config = new Configuration(key, mode);

        if(mode == Mode.ON_DEMAND)
            cities = new ArrayList<>();

        if(mode == Mode.POLLING)
        {
            cities = new CopyOnWriteArrayList();
            pollingService = new PollingService(config.getKey(), controller, cities);
            pollingService.start();
        }
    }

    public void remove()
    {
        sdkClientsPool.remove(config.getKey());

        if (config.getMode() == Mode.POLLING)
            pollingService.interrupt();
    }

    public void removeCity(int index) throws IndexOutOfBoundsException
    {
        if(index < 0 | index > MAX_CITY_COUNT)
            throw new IndexOutOfBoundsException("City index is out of bound");

        if(config.getMode() == Mode.POLLING)
        {
            cities = pollingService.getCities();
            cities.remove(index);
        }

        cities.remove(index);
    }

    public JsonObject getWeather (String cityName) throws IOException, RuntimeException, InterruptedException
    {

        if (cities.size() >= MAX_CITY_COUNT)
            throw new OpenWeatherSdkCitiesLimitException("City limit exceeded");

        if(config.getMode() == Mode.ON_DEMAND)
            return getWeatherOnDemand(cityName);

        return getWeatherPolling(cityName);
    }

    private JsonObject getWeatherOnDemand(String cityName) throws IOException, RuntimeException
    {
        for(int i = 0; i < cities.size(); i++)
        {
            City city = cities.get(i);

            if(city.getName().equals(cityName))
            {
                if(city.isActualWeather())
                    return city.getWeather();

                JsonObject weather = controller.requestWeather(cityName, config.getKey());
                Date requestTime = new Date();
                city.setRequestTime(requestTime);
                return weather;
            }
        }
        JsonObject weather = controller.requestWeather(cityName, config.getKey());
        Date requestTime = new Date();
        cities.add(new City(cityName, weather, requestTime));
        return weather;
    }

    private JsonObject getWeatherPolling(String cityName) throws IOException, RuntimeException
    {
        cities = pollingService.getCities();

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
