package Configuration;

/**
 * Enum defining the modes of two types of behavior for OpenWeatherClient.
 */
public enum Mode
{
    /**
     * In on-demand mode the
     * SDK updates the weather information only on request.
     */
    ON_DEMAND,

    /**
     * In polling mode SDK requests new
     * weather information for all stored locations and updates it automatically every 10 minutes
     */
    POLLING
}
