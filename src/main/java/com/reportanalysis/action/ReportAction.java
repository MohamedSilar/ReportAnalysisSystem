package com.reportanalysis.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.rest.RestActionSupport;

import com.reportanalysis.service.ReportService;
import com.reportanalysis.service.impl.ReportServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ReportAction extends RestActionSupport {

    private Long id; 
    private InputStream fileInputStream;
    private String fileName;
    private final ReportService reportService = new ReportServiceImpl();
    
    public String show() {

        try {
            String filePath = reportService.exportUserReport(id);
            File file = new File(filePath);
            fileName = file.getName();
            fileInputStream = new FileInputStream(file);
            return SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }
    
    public String index() {
     try {
         String filePath = reportService.exportAllUsersFullReportWithErrorSheet();
         File file = new File(filePath);
         fileName = file.getName();
         fileInputStream = new FileInputStream(file);
         return SUCCESS;

     } catch (Exception e) {
         e.printStackTrace();
         return ERROR;
     }
 }


    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
