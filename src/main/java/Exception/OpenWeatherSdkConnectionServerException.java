package Exception;

import org.apache.http.client.HttpResponseException;

public class OpenWeatherSdkConnectionServerException extends RuntimeException
{
    public OpenWeatherSdkConnectionServerException(String message) {
        super(message);
    }
}
