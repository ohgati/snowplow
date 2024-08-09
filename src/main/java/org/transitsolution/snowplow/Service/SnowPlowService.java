package org.transitsolution.snowplow.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SnowPlowService {

    static String appKey = "&key=b0000001";

    public Map<String, Object> getBusList(String stDate, String edDate) throws IOException, ParseException {
        Map<String, Object> busList = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();

        if (stDate == null || stDate.isEmpty()) {
            stDate = sdf.format(now);
        }

        if (edDate == null || edDate.isEmpty()) {
            edDate = sdf.format(now);
        }

        // 버스 정보
        String testUrl = "http://api.busrang.com/spc_getListBus?stDate=" + stDate + "&edDate=" + edDate + appKey;
        URL url = new URL(testUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        String result = br.readLine();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject)parser.parse(result);
        JSONObject obj2 = (JSONObject)obj.get("MsgHeader");
        JSONObject obj3 = (JSONObject)obj2.get("resultMessage");
        JSONArray List = (JSONArray)obj3.get("BusList");


        // 버스 운행 시간
        String testUrl2 = "http://api.busrang.com/spc_getListBusDay?stDate=" + stDate + "&edDate=" + edDate + appKey;
        URL url2 = new URL(testUrl2);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

        String result2 = br2.readLine();
        JSONParser parser2 = new JSONParser();
        JSONObject obj4 = (JSONObject)parser2.parse(result2);
        JSONObject obj5 = (JSONObject)obj4.get("MsgHeader");
        JSONObject obj6 = (JSONObject)obj5.get("resultMessage");
        JSONArray List2 = (JSONArray)obj6.get("BusList");
        busList.put("BusInfo", List);
        busList.put("BusTime", List2);

        return busList;
    }

    public JSONArray getBusGpsList(String stDate, String busId) throws UnsupportedEncodingException, IOException, ParseException {
        if (stDate == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date now = new Date();
            stDate = sdf.format(now);
        }
        String term = "5";

        String testUrl = "http://api.busrang.com/spc_getBusGpsList?stDate=" + stDate + "&busId=" + busId +"&term=" + term + appKey;
        URL url = new URL(testUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String result = br.readLine();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject)parser.parse(result);
        JSONObject obj2 = (JSONObject)obj.get("MsgHeader");
        JSONObject obj3 = (JSONObject)obj2.get("resultMessage");
        JSONArray List = (JSONArray)obj3.get("BusList");
        return List;
    }
}

