package org.transitsolution.snowplow.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SnowPlowService {

    static String appKey = "&key=b0000001";

    public String tracking(String stDate, String edDate) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();

        if (stDate == null || stDate.isEmpty()) {
            stDate = sdf.format(now);
        }

        if (edDate == null || edDate.isEmpty()) {
            edDate = sdf.format(now);
        }

        String testUri = "http://api.busrang.com/spc_getListBusDay?stDate=" + stDate;
        URL url = new URL(testUri + appKey);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        br.close();
        return result.toString();
    }
}

//    @ResponseBody
//    @RequestMapping(
//            value = {"/tracking/getBusGpsList.do"},
//            method = {RequestMethod.GET},
//            produces = {"application/json;charset=utf-8"}
//    )
//    public JSONArray getBusGpsList(Model model, HttpServletRequest request) throws UnsupportedEncodingException, IOException, ParseException {
//        String busId = request.getParameter("busId");
//        String stDate = request.getParameter("stDate");
//        String term = "5";
//        String stTime = "";
//        String edTime = "";
//        if (stDate == null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            Date now = new Date();
//            stDate = sdf.format(now);
//        }
//
//        String testUri = "http://api.busrang.com/spc_getBusGpsList?stDate=" + stDate + "&busId=" + busId + "&stTime=" + stTime + "&edTime=" + edTime + "&term=" + term;
//        return this.getData(testUri);
//    }
//
//    public JSONArray getData(String uri) throws UnsupportedEncodingException, IOException, ParseException {
//        URL url = new URL(uri + appKey);
//        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//        String result = br.readLine();
//        JSONParser parser = new JSONParser();
//        JSONObject obj = (JSONObject)parser.parse(result);
//        JSONObject obj2 = (JSONObject)obj.get("MsgHeader");
//        JSONObject obj3 = (JSONObject)obj2.get("resultMessage");
//        JSONArray arr = (JSONArray)obj3.get("BusList");
//        return arr;
//    }
