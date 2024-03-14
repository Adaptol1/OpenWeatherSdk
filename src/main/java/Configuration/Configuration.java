package Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * Configuration class for OpenWeatherClient,
 * that initializes with OpenWeatherClient object and
 * stores OpenWeatherApi key and mode for current object
 */
@AllArgsConstructor
@Getter
public class Configuration
{
    final private String key;
    private Mode mode;

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
