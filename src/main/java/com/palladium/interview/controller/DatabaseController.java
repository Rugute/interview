package com.palladium.interview.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.palladium.interview.model.Branch;
import com.palladium.interview.model.DatabaseInfo;
import com.palladium.interview.service.BranchService;
import com.palladium.interview.service.DatabaseinfoService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Controller
@Transactional
@RequestMapping("/databasemanager")
public class DatabaseController {
    @Value("${app.dbpath}")
    public String dbpath;
    @Value("${app.mysqlpath}")
    public String mysqlpath;

    @Autowired
    public DatabaseinfoService databaseinfoService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    public BranchService branchService;


    @RequestMapping(value = "/restoredb", method = RequestMethod.GET)
    public ModelAndView restoredb() throws IOException, JSONException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("branch", branchService.getAllBraches());//.searchByFtypeLike("F"));
        modelAndView.setViewName("restoredb");
        return modelAndView;

    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("facility") String facility
    ) throws IOException, InvalidFormatException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String progres = "";
        long size = file.getSize();
        System.out.println("File size "+ size);
        Date nowDate = new Date();
        int month= nowDate.getMonth();
        int year = nowDate.getYear();
        String nfilename = "insurance" + facility + month + year + ".sql";
        String fpath = dbpath + nfilename;

        try {
            file.transferTo(new File(dbpath + nfilename));
            long bytes = file.getSize();
            long gbc = (1024*3);
            long gb = bytes/gbc;

            long kilobytes = (bytes / 1024);
            long megabytes = (kilobytes / 1024);
            long gigabytes = (megabytes / 1024);

            DatabaseInfo databaseInfo = new DatabaseInfo();
            Branch facilities = branchService.getBranchByCode(facility);
            databaseInfo.setName(facilities.getBranchname());
            databaseInfo.setMflcode(facility);
            databaseInfo.setReuploaded("No");
            databaseInfo.setCreated_by(1);
            databaseInfo.setUrl(fpath);
            databaseInfo.setStatus("Pending restore");
            databaseInfo.setDbsize(String.valueOf(megabytes+" MB"));
            databaseInfo.setDbname(nfilename);
            databaseInfo.setCreated_on(nowDate);
            databaseinfoService.save(databaseInfo);

        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR.toString();
        }
        return "File uploaded successfully.";
    }


    @RequestMapping(value = "/branches", method = RequestMethod.GET)
    public ModelAndView branches() throws IOException, JSONException {
        //  System.out.println("Imefika Hapa");

            ModelAndView modelAndView = new ModelAndView();
            List<Branch> facilitiesList = branchService.getAllBraches();
            modelAndView.addObject("branches", branchService.getAllBraches());
            modelAndView.addObject("countfacilities", branchService.getAllBraches().size());
            modelAndView.setViewName("masterbranches");
            return modelAndView;

    }
    @RequestMapping(value = "/masterdatabases", method = RequestMethod.GET)
    public ModelAndView masterdatabases() throws IOException, JSONException {
            ModelAndView modelAndView = new ModelAndView();
           List<DatabaseInfo> databaseInfos = databaseinfoService.getAllDataset();

            modelAndView.addObject("dbs", databaseInfos);
            modelAndView.addObject("branch", branchService.getAllBraches());//.searchByFtypeLike("F"));
            modelAndView.addObject("countfacilities",  branchService.getAllBraches().size());
            modelAndView.setViewName("restoredb");
            return modelAndView;

    }

}
