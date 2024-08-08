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

    public String tracking(String stDate, String edDate, Model model) throws IOException, ParseException {
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

        // Use org.json to parse JSON
        JSONObject obj = new JSONObject(result.toString());
        JSONObject obj2 = obj.getJSONObject("MsgHeader");
        JSONObject obj3 = obj2.getJSONObject("resultMessage");
        JSONArray arr = obj3.getJSONArray("BusList");

        model.addAttribute("stDate", stDate);
        model.addAttribute("edDate", edDate);
        model.addAttribute("list", arr);

        br.close();
        return result.toString();
    }
}

