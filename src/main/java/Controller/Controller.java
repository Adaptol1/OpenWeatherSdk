package Controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller
{
    private String defaultUrl = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public JsonObject requestWeather(String cityName, String apiKey)
    {
        String url = String.format(defaultUrl, cityName, apiKey);
        try {
            Content content = Request.Get(url)
                            .execute().returnContent();
            JsonObject weather = new Gson().fromJson(content.toString(), JsonObject.class);
            return editJson(weather);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new Gson().fromJson(e.getMessage(), JsonObject.class);
        }
    }

    private JsonObject editJson(JsonObject json)
    {
        try
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
        catch (Exception e)
        {
            e.printStackTrace();
            return new Gson().fromJson(e.getMessage(), JsonObject.class);
        }

    }

}
