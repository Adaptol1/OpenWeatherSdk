package Exception;

import org.apache.http.client.HttpResponseException;

public class OpenWeatherSdkInvalidKeyException extends HttpResponseException
{
    private final int statusCode;

    public OpenWeatherSdkInvalidKeyException(int statusCode, String message) {
        super(statusCode, message);
        this.statusCode = statusCode;
    }
}
