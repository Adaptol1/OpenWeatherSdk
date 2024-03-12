package Exception;

import org.apache.http.client.HttpResponseException;

public class OpenWeatherSdkRequestsLimitException extends HttpResponseException {
    private final int statusCode;

    public OpenWeatherSdkRequestsLimitException(int statusCode, String message) {
        super(statusCode, message);
        this.statusCode = statusCode;
    }
}
