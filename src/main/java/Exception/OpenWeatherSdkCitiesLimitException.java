package Exception;

public class OpenWeatherSdkCitiesLimitException extends RuntimeException
{
    public OpenWeatherSdkCitiesLimitException(String message) {
        super(message);
    }
}