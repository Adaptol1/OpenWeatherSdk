# OpenWeatherSdk

## Introduction
OpenWeatherSDK provides access to weather data via a JSON requested from Open Weather API. It allows developers to create applications using this data very easy.

## Contents
- [Installation](#installation)
- [Getting started](#getting-started)
- [Configuration](#configuration)
- [Usage Example](#usage-example)
- [Errors](#errors)

## Installation
To install SDK you need to save OpenWeatherSdk.jar from this repository
and import it to your Java project.

## Getting started
You need to signup and then you can find your API key under your account, and start using SDK.
The SDK have two types of behavior: on-demand and polling mode. 
In on-demand mode the SDK updates the weather information only on requests. 
In polling mode SDK requests new weather information for all stored locations to 
have zero-latency response for requests.

## Configuration
To start using SDK you need to initialize OpenWeatherClient object. Use static method newOpenWeatherClient 
and pass your API key and mode in method parameters:

```java
OpenWeatherClient sdkClient = OpenWeatherClient.newOpenWeatherClient("api_key", Mode.ON_DEMAND);
```


## Usage Example
```java
// Create the object of OpenWeatherClient
OpenWeatherClient sdkClient = OpenWeatherClient.newOpenWeatherClient("api_key", Mode.ON_DEMAND);

// Request weather data for the city
JsonObject weatherInUfa = sdkClient.getWeather("Ufa");
JsonObject temperatureInUfa =  weatherInUfa.getAsJsonObject("temperature");
System.out.println("Temperature in Ufa today is " + temperatureInUfa.get("temp").toString()
+ "K, but is feels like " + temperatureInUfa.get("feels_like").toString() + "K");

// Remove city from list
onDemandClient.removeCity(0);

// An attempt to create a second object with the same key will cause an exception
OpenWeatherClient pollingClient = OpenWeatherClient.newOpenWeatherClient("api_key", Mode.POLLING);

// Remove OpenWeatherClient object
onDemandClient.remove();

// After removing first object you can use this key to create a new object
OpenWeatherClient pollingClient = OpenWeatherClient.newOpenWeatherClient("api_key", Mode.POLLING);
```

Calling of getWeather() method will return JsonObject with information about the weather in requested city:

```json
{
  "weather": {
    "main": "Clouds",
    "description": "overcast clouds"
  },
  "temperature": {
    "temp": 264.23,
    "feels_like": 261.57
  },
  "visibility": 832,
  "wind": {
    "speed": 1.34
  },
  "datetime": 1710112268,
  "sys": {
    "sunrise": 1710124836,
    "sunset": 1710166275
  },
  "timezone": 18000,
  "name": "Ufa"
}
```
## Errors

| Error type                              | Error Description                          |
|-----------------------------------------|--------------------------------------------|
| OpenWeatherSdkCitiesLimitException      | Thrown when limit of cities is exceeded    |
| OpenWeatherSdkCityNotFoundException     | Thrown when API cannot find requested city |
| OpenWeatherSdkConnectionServerException | Bad connection with server                 |
| OpenWeatherSdkIllegalKeyException       | Entered API key is already using           |
| OpenWeatherSdkInvalidKeyException       | Entered wrong API key                      |
| OpenWeatherSdkRequestsLimitException    | Thrown when limit of requests is exceeded  |
