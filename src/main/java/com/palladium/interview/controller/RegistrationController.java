package com.palladium.interview.controller;

import com.palladium.interview.model.Counties;
import com.palladium.interview.model.SubCounties;
import com.palladium.interview.model.Wards;
import com.palladium.interview.service.CountiesService;
import com.palladium.interview.service.SubCountiesService;
import com.palladium.interview.service.WardsService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/reg")
public class RegistrationController {

    @Autowired
    private CountiesService countiesService;
    @Autowired
    private SubCountiesService subCountiesService;
    @Autowired
    private WardsService wardsService;

    @RequestMapping(value = "/counties", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity Getcounties() throws JSONException, ParseException {
        String output = "Rugute";
       List<Counties> countiesList = countiesService.getAllConties();
        int x = countiesList.size();
        System.out.println("Count Counties "+x);
        JSONArray jsonArray = new JSONArray();
        for(int y=0;y<x;y++){
            JSONObject countieobj = new JSONObject();
            Counties counties = countiesList.get(y);
           countieobj.put("code",counties.getCode());
            countieobj.put("name",counties.getName());
            jsonArray.put(countieobj);
        }
       return ResponseEntity.ok(jsonArray.toString());
    }
    @RequestMapping(value = "/subcounties/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity Getsubcounties(@PathVariable(name = "id") int id) throws JSONException, ParseException {
        List<SubCounties> subCountiesList = subCountiesService.getSubCountiesByCountyID(id);
        int x = subCountiesList.size();
        JSONArray jsonArray = new JSONArray();
        for(int y=0;y<x;y++){
            JSONObject subcountieobj = new JSONObject();
            SubCounties subcounties = subCountiesList.get(y);
            subcountieobj.put("code",subcounties.getCode());
            subcountieobj.put("name",subcounties.getName());
            jsonArray.put(subcountieobj);
        }
        return ResponseEntity.ok(jsonArray.toString());
    }
    @RequestMapping(value = "/wards/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity Getwards(@PathVariable(name = "id") int id) throws JSONException, ParseException {
        List<Wards> wardsList = wardsService.getWradsBySubCountyID(id);
        int x = wardsList.size();
        JSONArray jsonArray = new JSONArray();
        for(int y=0;y<x;y++){
            JSONObject wardsobj = new JSONObject();
            Wards wards = wardsList.get(y);
            wardsobj.put("code",wards.getCode());
            wardsobj.put("name",wards.getName());
            jsonArray.put(wardsobj);
        }
        return ResponseEntity.ok(jsonArray.toString());
    }

    }
