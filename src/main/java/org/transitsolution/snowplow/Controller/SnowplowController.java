package org.transitsolution.snowplow.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.transitsolution.snowplow.Service.SnowPlowService;


@Controller
public class SnowplowController {

    @Autowired
    private SnowPlowService snowPlowService;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @ResponseBody
    @PostMapping(value = "/searchSnowPlow")
    public Map<String, Object> searchSnowPlow(@RequestParam("stDate") String stDate, @RequestParam("edDate") String edDate) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Parse the response if needed and put data into the result map
            result.put("data", snowPlowService.getBusList(stDate, edDate));
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/searchRoute")
    public Map<String, Object> getBusRoute(@RequestParam("stDate") String stDate, @RequestParam("busId") String busId) throws IOException, ParseException, org.json.simple.parser.ParseException {
        Map<String, Object> result = new HashMap<>();
        result.put("data", snowPlowService.getBusGpsList(stDate, busId));
        return result;
    }
}

