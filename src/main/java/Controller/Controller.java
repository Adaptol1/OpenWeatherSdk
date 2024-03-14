package Controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import Exception.OpenWeatherSdkCityNotFoundException;
import Exception.OpenWeatherSdkInvalidKeyException;
import Exception.OpenWeatherSdkConnectionServerException;
import Exception.OpenWeatherSdkRequestsLimitException;
import org.apache.http.util.EntityUtils;

/**
 * Class designed to send requests to OpenWeatherApi
 * Json object with actual information about weather
 */
public class Controller
{
    private String defaultUrl = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public JsonObject requestWeather(String cityName, String apiKey) throws IOException, RuntimeException
    {
        JsonObject weather = null;
        String url = String.format(defaultUrl, URLEncoder.encode(cityName, "UTF-8"), apiKey);
        Request request = Request.Get(url);
        weather = processResponse(request);
        return weather;
    }

    private JsonObject processResponse(Request request) throws IOException, RuntimeException
    {
        JsonObject weather = null;
        HttpResponse response = request.execute().returnResponse();
        int statusCode = response.getStatusLine().getStatusCode();

        if(statusCode == 200)
        {
            byte[] entityBytes = EntityUtils.toByteArray(response.getEntity());
            weather = new Gson().fromJson(new String(entityBytes), JsonObject.class);
            weather = editJson(weather);
        }
        else
        {
            switch(statusCode)
            {
                case 404:
                    throw new OpenWeatherSdkCityNotFoundException(statusCode, "City with this name was not found");
                case 401:
                    throw new OpenWeatherSdkInvalidKeyException(statusCode, "Unauthorized: invalid api key");
                case 429:
                    throw new OpenWeatherSdkRequestsLimitException(statusCode, "Limit of requests exceeded");
                default:
                    throw new OpenWeatherSdkConnectionServerException("Server connection failed");
            }
        }

        return weather;
    }

    private JsonObject editJson(JsonObject json)
    {
        Map<String,Object> orderedJson =  new LinkedHashMap<>();
        JsonObject weather = json.getAsJsonArray("weather").get(0).getAsJsonObject();
        weather.remove("id");
        weather.remove("icon");
        orderedJson.put("weather", weather);
        JsonObject temperature = json.getAsJsonObject("main");
        temperature.remove("temp_min");
        temperature.remove("temp_max");
        temperature.remove("pressure");
        temperature.remove("humidity");
        temperature.remove("sea_level");
        temperature.remove("grnd_level");
        orderedJson.put("temperature", temperature);
        JsonElement visibility = json.get("visibility");
        orderedJson.put("visibility", visibility);
        JsonObject wind = json.getAsJsonObject("wind");
        wind.remove("deg");
        wind.remove("gust");
        orderedJson.put("wind", wind);
        JsonElement dateTime = json.get("dt");
        orderedJson.put("datetime", dateTime);
        JsonObject sys = json.getAsJsonObject("sys");
        sys.remove("type");
        sys.remove("id");
        sys.remove("country");
        orderedJson.put("sys", sys);
        JsonElement timeZone = json.get("timezone");
        orderedJson.put("timezone", timeZone);
        JsonElement name = json.get("name");
        orderedJson.put("name", name);
        Gson gson = new Gson();
        String newJson = gson.toJson(orderedJson, LinkedHashMap.class);
        return new Gson().fromJson(newJson, JsonObject.class);
    }

}
