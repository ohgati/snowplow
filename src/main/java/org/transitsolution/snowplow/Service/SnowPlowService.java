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
import java.util.Date;

@Service
public class SnowPlowService {

    static String appKey = "&key=b0000001";

    public JSONArray getBusList(String stDate, String edDate) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();

        if (stDate == null || stDate.isEmpty()) {
            stDate = sdf.format(now);
        }

        if (edDate == null || edDate.isEmpty()) {
            edDate = sdf.format(now);
        }

        String testUrl = "http://api.busrang.com/spc_getListBusDay?stDate=" + stDate + "&edDate=" + edDate + appKey;
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

