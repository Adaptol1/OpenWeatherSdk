package Exception;

import org.apache.http.client.HttpResponseException;

public class OpenWeatherSdkCityNotFoundException extends HttpResponseException {
    private final int statusCode;

    public OpenWeatherSdkCityNotFoundException(int statusCode, String message) {
        super(statusCode, message);
        this.statusCode = statusCode;
    }
}
