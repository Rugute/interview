package com.palladium.interview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/reg")
public class RegController {
    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public ModelAndView EmployeesProfile() {
            Date nowDate = new Date();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("register");
            return modelAndView;

    }

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ModelAndView newclient() {
        Date nowDate = new Date();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("newregister");
        return modelAndView;
    }

}
