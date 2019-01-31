package com.opensource.controllers;

import com.opensource.models.AuthenticateRequest;
import com.opensource.models.CreateFileRequest;
import com.opensource.models.User;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class ExcelController {

    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public boolean AuthenticateUser(@RequestBody AuthenticateRequest request) throws IOException {
        FileInputStream file = new FileInputStream("files\\database\\database.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        HSSFSheet sheet = workbook.getSheet("Users");
        HSSFRow row;

        for (int index = 1; index <= sheet.getLastRowNum(); index++) {
            row = sheet.getRow(index);
            String username = row.getCell(0).toString();
            if(username.equals(request.Username)) {
                String password = row.getCell(1).toString();
                if(password.equals(request.Password)) {
                    return true;
                }
            }
        }

        return false;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User CreateUser(@RequestBody User request) throws IOException {
        File database = new File("files\\database\\database.xls");

        if(!database.exists()) {
            CreateDatabase();
        }

        FileInputStream file = new FileInputStream("files\\database\\database.xls");

        HSSFWorkbook workbook = new HSSFWorkbook(file);

        HSSFSheet sheet = workbook.getSheet("Users");
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue(request.Username);

        HSSFCell cell1 = row.createCell(1);
        cell1.setCellValue(request.Password);

        HSSFCell cell2 = row.createCell(2);
        cell2.setCellValue(request.FullName);

        FileOutputStream out = new FileOutputStream("files\\database\\database.xls");

        workbook.write(out);
        out.flush();
        out.close();

        return request;
    }

    public void CreateDatabase() throws IOException  {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Users");

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Username");
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("Password");
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellValue("FullName");

        FileOutputStream file = new FileOutputStream("files\\database\\database.xls");

        try {
            workbook.write(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public boolean CreateExcelFile(@RequestBody CreateFileRequest request) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(request.FirstMessage);
        FileOutputStream file = new FileOutputStream("files\\excel\\" +request.Name + ".xls");

        try {
            workbook.write(file);
            file.close();
            CreateDatabase();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @RequestMapping(value = "/word", method = RequestMethod.POST)
    public boolean CreateWordFile(@RequestBody CreateFileRequest request) throws IOException {
        try{
            XWPFDocument document = new XWPFDocument();
            File file = new File("files\\\\word\\" +request.Name + ".docx");
            FileOutputStream out = new FileOutputStream(file);

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(request.FirstMessage);
            document.write(out);

            out.close();
        }catch (Exception e ){
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @RequestMapping(value = "/power-point", method = RequestMethod.POST)
    public boolean CreatePowerPointFile(@RequestBody CreateFileRequest request) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow();
        FileOutputStream file = new FileOutputStream("files\\power point\\" + request.Name + ".ppt");

        XSLFSlide slide = ppt.createSlide();
        XSLFTextBox text = slide.createTextBox();
        text.setText(request.FirstMessage);
        ppt.write(file);

        file.close();

        return true;
    }
}
