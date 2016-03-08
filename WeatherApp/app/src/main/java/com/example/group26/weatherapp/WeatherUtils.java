package com.example.group26.weatherapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos on 3/8/2016.
 */
public class WeatherUtils {

    static public class WeatherPullParser {

        static List<Weather> parseWeather(InputStream in) throws IOException, XmlPullParserException{


            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            Weather weather = null;
            List<Weather> weatherList = new ArrayList<Weather>();

            boolean areWeInFCTTIMEElement = false;
            boolean areWeInTemperatureElement = false;
            boolean areWeInDewPointElement = false;
            boolean areWeInWindSpeedElement = false;
            boolean areWeInWindDirectionElement = false;
            boolean areWeInFeelsLikeElement = false;
            boolean areWeInPressureTag = false;

            int event = parser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT){

                switch (event){
                    case XmlPullParser.START_TAG:


                        if(parser.getName().equals("FCTTIME")){
                            areWeInFCTTIMEElement = true;
                        }

                        if(parser.getName().equals("civil")){
                            if(areWeInFCTTIMEElement){
                                String time = parser.nextText();
                                Log.d("xml", "time: " + time);
                                weather.setTime(time);
                            }
                        }

                        if(parser.getName().equals("weekday_name_abbrev")){
                            if(areWeInFCTTIMEElement){
                                String dayAbbreviation = parser.nextText();
                                Log.d("xml", "day: " + dayAbbreviation);
                                weather.setDay(dayAbbreviation);
                            }
                        }

                        if(parser.getName().equals("forecast")){
                            weather = new Weather();
                        }
                        else if(parser.getName().equals("temp")){
                            areWeInTemperatureElement = true;
                        }
                        else if(parser.getName().equals("english")){
                            if(areWeInTemperatureElement){
                                String temperature = parser.nextText();
                                Log.d("xml", "current temperature: " + temperature);
                                weather.setTemperature(temperature);
                            }
                            else if(areWeInDewPointElement){
                                String dewpoint = parser.nextText();
                                Log.d("xml", "dewpoint: " + dewpoint);
                                weather.setDewpoint(dewpoint);
                            }
                            else if(areWeInWindSpeedElement){
                                String windSpeed = parser.nextText();
                                Log.d("xml", "wind speed: " + windSpeed);
                                weather.setWindSpeed(windSpeed);
                            }
                            else if(areWeInFeelsLikeElement){
                                String feelsLike = parser.nextText();
                                Log.d("xml", "feels like: " + feelsLike);
                                weather.setFeelsLike(feelsLike);
                            }
                        }
                        else if(parser.getName().equals("dewpoint")){
                            areWeInDewPointElement = true;
                        }
                        else if(parser.getName().equals("condition")){
                            String clouds = parser.nextText();
                            Log.d("xml", "clouds: " + clouds);
                            weather.setClouds(clouds);
                        }
                        else if(parser.getName().equals("icon_url")){
                            String iconURL = parser.nextText();
                            Log.d("xml", "iconURL: " + iconURL);
                            weather.setIconURL(iconURL);
                        }
                        else if(parser.getName().equals("wspd")){
                            areWeInWindSpeedElement = true;
                        }
                        else if(parser.getName().equals("wdir")){
                            areWeInWindDirectionElement = true;
                        }
                        else if(parser.getName().equals("dir")){
                            if(areWeInWindDirectionElement){
                                String windDirection = parser.nextText();
                                Log.d("xml", "wind speed direction: " + windDirection);
                                weather.setWindDirection(windDirection);
                            }
                        }
                        else if(parser.getName().equals("degrees")){
                            if(areWeInWindDirectionElement){
                                String windDegrees = parser.nextText();
                                Log.d("xml", "wind degrees: " + windDegrees);
                                weather.setWindDegrees(windDegrees);
                            }
                        }
                        else if(parser.getName().equals("wx")){
                            String climateType = parser.nextText();
                            Log.d("xml", "climate type: " + climateType);
                            weather.setClimateType(climateType);
                        }
                        else if(parser.getName().equals("humidity")){
                            String humidity = parser.nextText();
                            Log.d("xml", "humidity: " + humidity);
                            weather.setHumidity(humidity);
                        }
                        else if(parser.getName().equals("feelslike")){
                            areWeInFeelsLikeElement = true;
                        }
                        else if(parser.getName().equals("mslp")){
                            areWeInPressureTag = true;
                        }
                        else if(parser.getName().equals("metric")){
                            if(areWeInPressureTag){
                                String pressure = parser.nextText();
                                Log.d("xml", "pressure: " + pressure);
                                weather.setPressure(pressure);
                            }
                        }

                        break;

                    case XmlPullParser.END_TAG:

                        if(parser.getName().equals("forecast")){
                            weatherList.add(weather);
                            weather = null;
                        } else if(parser.getName().equals("FCTTIME")){
                            areWeInFCTTIMEElement = false;
                        } else if(parser.getName().equals("temp")){
                            areWeInTemperatureElement = false;
                        } else if(parser.getName().equals("dewpoint")){
                            areWeInDewPointElement = false;
                        } else if(parser.getName().equals("wspd")){
                            areWeInWindSpeedElement = false;
                        } else if(parser.getName().equals("wdir")){
                            areWeInWindDirectionElement = false;
                        } else if(parser.getName().equals("feelslike")){
                            areWeInFeelsLikeElement = false;
                        } else if(parser.getName().equals("mslp")){
                            areWeInPressureTag = false;
                        }

                        break;
                }
                event = parser.next();

            }

            List<Weather> deepCopyWeatherList = new ArrayList<Weather>();
            for(Weather w: weatherList){
                deepCopyWeatherList.add(w);
            }

            Collections.sort(deepCopyWeatherList);
            String maxTemp = deepCopyWeatherList.get(0).getTemperature();
            String minTemp = deepCopyWeatherList.get(deepCopyWeatherList.size() - 1).getTemperature();

            Log.d("testing2", "Max Temp: " + maxTemp);
            Log.d("testing2", "Min Temp: " + minTemp);

            for(Weather w: weatherList){
                w.setMaximumTemp(maxTemp);
                w.setMinimumTemp(minTemp);
            }

            return weatherList;
        }
    }
}
