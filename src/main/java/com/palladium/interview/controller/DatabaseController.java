package com.palladium.interview.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.palladium.interview.model.Branch;
import com.palladium.interview.model.DatabaseInfo;
import com.palladium.interview.service.BranchService;
import com.palladium.interview.service.DatabaseinfoService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    @Value("${spring.datasource.username}")
    public String username;
    @Value("${spring.datasource.password}")
    public String password;
    @Value("${spring.datasource.url}")
    public String dburl;

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
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        String fpath = dbpath;
        String realPathtoUploads = dbpath + fileName;

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);

        File file = new File(realPathtoUploads);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length()) //
                .body(resource);
    }
    @GetMapping("/restore/{fileName:.+}")
    @ResponseBody
    public String restoreFileFromLocal(@PathVariable String fileName) throws IOException, InterruptedException {
        String fpath = dbpath;
        String smg = "";
        String realPathtoUploads = dbpath + fileName;
        String nfilename = fileName.substring(0, fileName.length() - 4);

        try {

            Connection connection = DriverManager.getConnection(dburl, username, password);
            System.out.println("New file name is " + nfilename+" "+ realPathtoUploads);
            String sql = "CREATE DATABASE " + nfilename;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            String dbname = nfilename+".sql";


            new Thread(new Runnable() {
                @Override
                public void run() {
                    DatabaseInfo databaseInfo = databaseinfoService.getByDbname(dbname);
                    databaseInfo.setStatus("Restoring");
                    databaseinfoService.save(databaseInfo);

                    restorebd(nfilename, realPathtoUploads);

                    DatabaseInfo databaseInfo1 = databaseinfoService.getByDbname(dbname);
                    databaseInfo1.setStatus("Restored");
                    databaseinfoService.save(databaseInfo1);
                }
            }).start();
            smg = "Database created successfully";
        } catch (SQLException e) {
            String dbname = nfilename+".sql";
            System.out.println("New file name is " + nfilename+" "+ realPathtoUploads);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DatabaseInfo databaseInfo = databaseinfoService.getByDbname(dbname);
                    databaseInfo.setStatus("Restoring");
                    databaseinfoService.save(databaseInfo);

                    restorebd(nfilename, realPathtoUploads);

                    DatabaseInfo databaseInfo1 = databaseinfoService.getByDbname(dbname);
                    databaseInfo1.setStatus("Restored");
                    databaseInfo1.setReuploaded("Yes");
                    databaseinfoService.save(databaseInfo1);
                }
            }).start();

            smg = e.getMessage()+ "Mysql database already exist";


        }
        return smg;
    }

    public String restorebd(String nfilename, String realPathtoUploads ){
        String smg = "";

        String[] executeCmd = new String[]{mysqlpath, "--user=" + username, "--password=" + password, nfilename,"-e", " source " + realPathtoUploads};

        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                smg = "Backup restored successfully";
            } else {
                smg = "Could not restore the backup";

            }
            return smg;//"Restored Successfully";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return smg;//"Restored Successfully";
    }



}
