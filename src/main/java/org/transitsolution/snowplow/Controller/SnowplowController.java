package org.transitsolution.snowplow.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.transitsolution.snowplow.Service.SnowPlowService;


@Controller
public class SnowplowController {

    private SnowPlowService service;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @PostMapping(value = "/searchSnowPlow")
    public Map<String, Object> searchSnowPlow(@RequestParam String stDate, @RequestParam String edDate) {
        Map<String, Object> result = new HashMap<>();
        try {
            String response = service.tracking(stDate, edDate);
            // Parse the response if needed and put data into the result map
            result.put("data", response);
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        return result;
    }
}

