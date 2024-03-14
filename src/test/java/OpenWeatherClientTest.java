import Configuration.Configuration;
import Configuration.Mode;
import Model.City;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

class OpenWeatherClientTest
{
    private String key = "key";

    private String cityName = "Ufa";

    @Test
    void newOpenWeatherClient() throws Exception
    {
        OpenWeatherClient sdkCleint = OpenWeatherClient.newOpenWeatherClient(key, Mode.ON_DEMAND);
        OpenWeatherClient sdkMock = Mockito.mock(OpenWeatherClient.class);
        Configuration testConfig = new Configuration(key, Mode.ON_DEMAND);
        Mockito.when(sdkMock.getConfig()).thenReturn(testConfig);
        Configuration clientConfig = sdkCleint.getConfig();
        Assertions.assertEquals(testConfig.getKey(), clientConfig.getKey());
        Assertions.assertEquals(testConfig.getMode(), clientConfig.getMode());
    }

    @Test
    void remove()throws Exception
    {
        Assertions.assertEquals(OpenWeatherClient.sdkClientsPool.size(), 0);
        OpenWeatherClient sdkCleint = OpenWeatherClient.newOpenWeatherClient(key, Mode.POLLING);
        Assertions.assertEquals(OpenWeatherClient.sdkClientsPool.size(), 1);
        Assertions.assertFalse(sdkCleint.getPollingService().isInterrupted());
        sdkCleint.remove();
        Assertions.assertEquals(OpenWeatherClient.sdkClientsPool.size(), 0);
        Assertions.assertTrue(sdkCleint.getPollingService().isInterrupted());
    }

    @Test
    void removeCity() throws Exception
    {
        OpenWeatherClient sdkClient = OpenWeatherClient.newOpenWeatherClient(key, Mode.ON_DEMAND);
        City city = Mockito.mock(City.class);
        int citiesCount = sdkClient.getCities().size();
        Assertions.assertEquals(sdkClient.getCities().size(), 0);
        sdkClient.getCities().add(city);
        Assertions.assertEquals(sdkClient.getCities().size(), 1);
        sdkClient.removeCity(0);
        Assertions.assertEquals(sdkClient.getCities().size(), 0);
    }

    @Test
    void getWeather() throws Exception
    {
        OpenWeatherClient sdkClient = OpenWeatherClient.
                newOpenWeatherClient(key, Mode.ON_DEMAND);
        OpenWeatherClient sdkMock = Mockito.mock(OpenWeatherClient.class);
        JsonObject weather = sdkClient.getWeather(cityName);
        JsonObject weatherTest = sdkClient.getWeather(cityName);
        Mockito.when(sdkMock.getWeather(cityName)).thenReturn(weatherTest);
        weatherTest = sdkMock.getWeather(cityName);
        verify(sdkMock, times(1)).getWeather(cityName);
        Assertions.assertEquals(weather, weatherTest);
    }
}