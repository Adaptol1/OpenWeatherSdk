package Controller;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import Exception.OpenWeatherSdkCityNotFoundException;
import Exception.OpenWeatherSdkInvalidKeyException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class ControllerTest
{
    private String key = "key";

    private String cityName = "Ufa";

    @Test
    void requestWeather_success() throws IOException
    {
        Controller controller = new Controller();
        JsonObject weather = controller.requestWeather(cityName, key);
        JsonObject weatherTest = controller.requestWeather(cityName, key);
        Controller controllerMock = Mockito.mock(Controller.class);
        Mockito.when(controllerMock.requestWeather(cityName, key))
                    .thenReturn(weatherTest);
        weatherTest = controllerMock.requestWeather(cityName, key);
        Mockito.verify(controllerMock, times(1)).requestWeather(cityName, key);
        Assertions.assertEquals(weather, weatherTest);
    }

    @Test
    void requestWeather_throwsOpenWeatherSdkCityNotFoundException()
    {
        Controller controller = new Controller();
        assertThrows(OpenWeatherSdkCityNotFoundException.class, () ->
                controller.requestWeather("wrong_city", "key"));
    }

    @Test
    void requestWeather_throwsOpenWeatherSdkInvalidKeyException()
    {
        Controller controller = new Controller();
        assertThrows(OpenWeatherSdkInvalidKeyException.class, () ->
                controller.requestWeather("Ufa", "wrong_key"));
    }
}