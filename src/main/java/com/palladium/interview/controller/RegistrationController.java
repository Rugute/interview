package com.palladium.interview.controller;

import com.palladium.interview.model.Counties;
import com.palladium.interview.service.CountiesService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/reg")
public class RegistrationController {

    @Autowired
    private CountiesService countiesService;

    @RequestMapping(value = "/counties", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity Getcounties() throws JSONException, ParseException {
        String output = "Rugute";

        List<Counties> countiesList = countiesService.getAllConties();

        int x = countiesList.size();
        System.out.println("Count Counties "+x);

        JSONArray jsonArray = new JSONArray();
        for(int y=0;y<x;y++){
            /*Map<String,Object> map = new HashMap<>();
            Counties counties = countiesList.get(y);
            map.put("code",counties.getCode());
            map.put("name",counties.getName());
            jsonArray.put(map);*/
            System.out.println("New Line "+y);

        }
        JSONObject json = new JSONObject(jsonArray);
        System.out.println(json);

       return ResponseEntity.ok(json.toString());
    }

    }
