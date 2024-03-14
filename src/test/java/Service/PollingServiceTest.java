package Service;

import Controller.Controller;
import Model.City;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

class PollingServiceTest
{
    static private PollingService pollingService;

    static private String key = "key";

    static private CopyOnWriteArrayList <City> cities = new CopyOnWriteArrayList<>();

    static private Controller controller;

    @BeforeAll
    static void setUp() throws Exception
    {
        controller = Mockito.mock(Controller.class);
        City firstCity = new City("Ufa", null, new Date(1710112268));
        City secondCity = new City("Moscow", null, new Date(1710112268));
        cities.add(firstCity);
        cities.add(secondCity);
        pollingService = new PollingService(key, controller, cities);
        pollingService.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        pollingService.interrupt();
    }

    @Test
    void run() throws Exception
    {
        TimeUnit.MINUTES.sleep(1);
        Assertions.assertTrue(cities.get(0).isActualWeather());
        Assertions.assertTrue(cities.get(1).isActualWeather());
    }
}