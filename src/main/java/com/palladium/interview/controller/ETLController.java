package com.palladium.interview.controller;

import com.palladium.interview.repository.ETLRepository;
import com.palladium.interview.service.BranchService;
import com.palladium.interview.service.ETLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/etl")
public class ETLController {
    @Autowired
    public BranchService branchService;
    @Value("${app.dbpath}")
    public String dbpath;
    @Value("${spring.datasource.username}")
    public String username;
    @Value("${spring.datasource.password}")
    public String password;
    @Value("${spring.datasource.url}")
    public String dburl;
    @Value("${app.mysqlpath}")
    public String mysqlpath;

    @Autowired
    ETLRepository etlRepository;

    @Autowired
    ETLService etlService;

    @GetMapping("refresh")
    String test() {
        return etlRepository.testSp().toString();
    }
    /*//@ResponseBody
    public ModelAndView UserDetails(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User userdetails = (User) session.getAttribute("user");
            Date nowDate = new Date();
            ModelAndView modelAndView = new ModelAndView();
          //  List<User> userList = userService.getAllUsers();
           // modelAndView.addObject("userslist",userList);
            modelAndView.addObject("smg", session.getAttribute("smg"));
            // modelAndView.setViewName("users");
            modelAndView.setViewName("pm/users");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }
    }*/
    @RequestMapping(value = "/admin")
    public ModelAndView Period(HttpSession session) {
            Date nowDate = new Date();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("etl",etlService.getAllDataset());
            modelAndView.setViewName("etl");
            return modelAndView;

    }
}

