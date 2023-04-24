package com.cod;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * java调用中央气象局天气预报接口
 * @author Administrator
 */
public class WeatherUtil {

    /**
     * get city weather info
     */
    public static Map<String, Object> getTodayWeather(String Cityid) throws IOException, NullPointerException {
        // connected API
        URL windDirectionHumidityUrl = new URL("http://www.weather.com.cn/data/sk/" + Cityid + ".html");
        URL cityInfoUrl = new URL("http://www.weather.com.cn/data/cityinfo/" + Cityid + ".html");
        URLConnection windDirectionHumidityConnectionData = windDirectionHumidityUrl.openConnection();
        URLConnection cityInfoConnectionData = cityInfoUrl.openConnection();
        windDirectionHumidityConnectionData.setConnectTimeout(1000);
        cityInfoConnectionData.setConnectTimeout(1000);

        // info
        Map<String, Object> mapInfo = new HashMap<>();
        try {
            BufferedReader windDirectionHumidityBufferedReader = new BufferedReader(new InputStreamReader(windDirectionHumidityConnectionData.getInputStream(), "UTF-8"));
            BufferedReader cityInfoBufferedReader = new BufferedReader(new InputStreamReader(cityInfoConnectionData.getInputStream(), "UTF-8"));
            StringBuilder windDirectionHumidityStringBuilder = new StringBuilder();
            StringBuilder cityInfoStringBuilder = new StringBuilder();

            String windDirectionHumidityInputString = null;
            while ((windDirectionHumidityInputString = windDirectionHumidityBufferedReader.readLine()) != null) {
                windDirectionHumidityStringBuilder.append(windDirectionHumidityInputString);
            }
            String cityInfoConnectionInputString = null;
            while ((cityInfoConnectionInputString = cityInfoBufferedReader.readLine()) != null) {
                cityInfoStringBuilder.append(cityInfoConnectionInputString);
            }
            String windDirectionHumidityDataInfo = windDirectionHumidityStringBuilder.toString();
            String cityInfoConnectionDataInfo = cityInfoStringBuilder.toString();
            JSONObject windDirectionHumidityJsonData = JSONObject.fromObject(windDirectionHumidityDataInfo);
            JSONObject jsonData1 = JSONObject.fromObject(cityInfoConnectionDataInfo);

            JSONObject info = windDirectionHumidityJsonData.getJSONObject("weatherinfo");
            mapInfo.put("city", info.getString("city").toString());
            mapInfo.put("temperature", info.getString("temp").toString());
            mapInfo.put("wind direction", info.getString("WD").toString());
            mapInfo.put("wind strength", info.getString("WS").toString());
            mapInfo.put("humidity", info.getString("SD").toString());
            mapInfo.put("time", info.getString("time").toString());

            JSONObject info1 = jsonData1.getJSONObject("weatherinfo");
            mapInfo.put("lowest temperature", info1.getString("temp1").toString());
            mapInfo.put("maximum temperature", info1.getString("temp2").toString());
            mapInfo.put("weather", info1.getString("weather").toString());
            mapInfo.put("ptime", info1.getString("ptime").toString());
        } catch (SocketTimeoutException e) {
            System.out.println("connection time out");
        } catch (FileNotFoundException e) {
            System.out.println("connection failure");
        }
        return mapInfo;
    }

    /**
     *
     */
    public static void parseTodayWeather(Map<String, Object> map) {
        System.out.println("地区:" + map.get("city"));
        System.out.println("天气:" + map.get("weather"));
        System.out.println("温度:" + map.get("temperature"));
        System.out.println("最低温度:" + map.get("lowest temperature"));
        System.out.println("最高温度:" + map.get("maximum temperature"));
        System.out.println("风向:" + map.get("wind direction"));
        System.out.println("风力:" + map.get("wind strength"));
        System.out.println("湿度:" + map.get("humidity"));
    }

    public static void main(String[] args) {
        try {
            Map<String, Object> map = getTodayWeather("101270101");
            parseTodayWeather(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}