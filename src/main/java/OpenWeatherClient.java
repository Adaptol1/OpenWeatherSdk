import Configuration.Configuration;
import Configuration.Mode;
import Controller.Controller;
import Model.City;
import Service.PollingService;
import com.google.gson.JsonObject;
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

    private static HashMap<String, OpenWeatherClient> sdkClientsPool = new HashMap<>();

    public static OpenWeatherClient newOpenWeatherClient (String key, Mode mode)
    {
        try
        {
            if (sdkClientsPool.containsKey(key))
                throw new OpenWeatherSdkIllegalKeyException("This key is already used");

            OpenWeatherClient openWeatherClient = new OpenWeatherClient(key, mode);
            sdkClientsPool.put(key, openWeatherClient);
            return openWeatherClient;
        }
        catch(RuntimeException e)
        {
            System.out.println(e.toString());
            return null;
        }
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

    public void removeCity(int index)
    {
        try {
            if(index < 0 | index > MAX_CITY_COUNT)
                throw new IndexOutOfBoundsException("City index is out of bound");

            switch (config.getMode())
            {
                case ON_DEMAND:
                    cities.remove(index);
                case POLLING:
                    cities = pollingService.getCities();
                    cities.remove(index);
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println(e.toString());
        }


    }

    public JsonObject getWeather (String cityName)
    {
        try
        {
        if (cities.size() >= MAX_CITY_COUNT)
            throw new OpenWeatherSdkCitiesLimitException("City limit exceeded");
        }
        catch (RuntimeException e)
        {
            System.out.println(e.toString());
            return null;
        }

        if(config.getMode() == Mode.ON_DEMAND)
            return getWeatherOnDemand(cityName);

        return getWeatherPolling(cityName);
    }

    private JsonObject getWeatherOnDemand(String cityName)
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

    private JsonObject getWeatherPolling(String cityName)
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
