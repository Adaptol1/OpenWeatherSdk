package Model;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
/**
 * Dto object, that stores city name, last requested weather
 * and time of last weather request
 */
@AllArgsConstructor
@Setter
@Getter
public class City
{
    private String name;

    private JsonObject weather;
    private Date requestTime;

    /**
     * Checks time since last weather request and returns true,
     * if this time is less than 10 minutes
     */
    public Boolean isActualWeather()
    {
        Date currentDate = new Date();
        long timeDifference = requestTime.toInstant().until(currentDate.toInstant(), ChronoUnit.MINUTES);

        if (timeDifference > 10)
            return false;

        return true;
    }
}
