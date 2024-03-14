package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Date;

class CityTest
{
    private City city;

    @Test
    void isActualWeather_returnTrue()
    {
        city = new City("Ufa", null, new Date());
        boolean isActual = city.isActualWeather();
        Assertions.assertTrue(isActual);
    }

    @Test
    void isActualWeather_returnFalse()
    {
        city = new City("Ufa", null, new Date(1710265928));
        boolean isActual = city.isActualWeather();
        Assertions.assertFalse(isActual);
    }

}