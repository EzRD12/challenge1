package com.opensource.controllers;

import com.opensource.enums.FileType;
import com.opensource.models.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelController {

//    @CrossOrigin(origins = "http://localhost:4200")
//    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
//    public BasicOperationResult<AuthenticationResponse> AuthenticateUser(@RequestBody AuthenticateRequest request) throws IOException {
//        FileInputStream file = new FileInputStream("files\\ExcelDB\\Files.xls");
//        HSSFWorkbook workbook = new HSSFWorkbook(file);
//
//        HSSFSheet sheet = workbook.getSheet("Users");
//        HSSFRow row;
//
//        for (int index = 1; index <= sheet.getLastRowNum(); index++) {
//            row = sheet.getRow(index);
//            String username = row.getCell(0).toString();
//            if(username.equals(request.Username)) {
//                String password = row.getCell(1).toString();
//                if(password.equals(request.Password)) {
//                    return new BasicOperationResult<AuthenticationResponse>("", true, new AuthenticationResponse(username, password));
//                }
//            }
//        }
//
//        return new BasicOperationResult<AuthenticationResponse>("InvalidCredentials", false, null);
//    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @RequestMapping(value = "/user", method = RequestMethod.POST)
//    public User CreateUser(@RequestBody User request) throws IOException {
//        FileInputStream file = new FileInputStream("files\\ExcelDB\\Files.xls");
//
//        HSSFWorkbook workbook = new HSSFWorkbook(file);
//
//
//        HSSFSheet sheet = workbook.getSheet("Users");
//        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
//
//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue(request.Username);
//
//        HSSFCell cell1 = row.createCell(1);
//        cell1.setCellValue(request.Password);
//
//        HSSFCell cell2 = row.createCell(2);
//        cell2.setCellValue(request.Email);
//
//        FileOutputStream out = new FileOutputStream("files\\ExcelDB\\Files.xls");
//
//        workbook.write(out);
//        out.flush();
//        out.close();
//
//        return request;
//    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/createStudent", method = RequestMethod.POST)
    public static void CreateStudent(@RequestBody CreateFileRequest request) throws IOException {

        FileInputStream file = new FileInputStream("files\\ExcelDB\\Files.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        HSSFSheet sheet = workbook.getSheet("Files");

        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0)
                .setCellValue(request.Name);
        row.createCell(1)
                .setCellValue(request.LastName);
        row.createCell(2)
                .setCellValue(request.UniqueId);
        row.createCell(3)
                .setCellValue(request.Age);
        row.createCell(4)
                .setCellValue(request.Session);

        FileOutputStream fileOut = new FileOutputStream("files\\ExcelDB\\Files.xls");
        workbook.write(fileOut);
        fileOut.close();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public List<com.opensource.models.File> GetFiles() throws IOException {
        FileInputStream file = new FileInputStream("files\\ExcelDB\\Files.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        HSSFSheet sheet = workbook.getSheet("Files");
        HSSFRow row;
        List<com.opensource.models.File> files = new ArrayList<com.opensource.models.File>();

        for (int index = 1; index <= sheet.getLastRowNum(); index++) {
            row = sheet.getRow(index);
            com.opensource.models.File students = new com.opensource.models.File();
            students.Name = row.getCell(0).toString();
            students.LastName = row.getCell(1).toString();
            students.UniqueId = row.getCell(2).toString();
            students.Age = (row.getCell(3).toString());
            students.Session = row.getCell(4).toString();
            files.add(students);
        }

        return files;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/deleteStudent/{id}", method = RequestMethod.DELETE)
    public static void DeleteStudent(@PathVariable("id") int index) throws IOException {
        FileInputStream file = new FileInputStream("files\\ExcelDB\\Files.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheet("Files");
        HSSFRow removingRow = sheet.getRow(index);
        sheet.removeRow(removingRow);

        FileOutputStream fileOut = new FileOutputStream("files\\ExcelDB\\Files.xls");
        workbook.write(fileOut);
        fileOut.close();
    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @RequestMapping(value = "/excel", method = RequestMethod.POST)
//    public boolean CreateExcelFile(@RequestBody CreateFileRequest request) throws IOException {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("FirstSheet");
//        HSSFRow row = sheet.createRow(0);
//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue(request.Name);
//        FileOutputStream file = new FileOutputStream("files\\excel\\" +request.Name + ".xls");
//
//        try {
//            workbook.write(file);
//            file.close();
//            CreateFileModel(request);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//        return true;
//    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/word", method = RequestMethod.POST)
    public boolean CreateWordFile(@RequestBody Word request) throws IOException {
        try{

            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(request.Message);
            run.setFontSize(18);

            File file = new File("files\\Reportes\\" +request.Name + ".docx");
            FileOutputStream out = new FileOutputStream(file);

            document.write(out);
            out.close();

        }catch (Exception e ){
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/powerPoint", method = RequestMethod.POST)
    public static void CreatePowerPointFile(@RequestBody Word request) throws IOException {

        File file = new File("files\\Presentation\\" +request.Name + ".ppt");
        XMLSlideShow ppt = new XMLSlideShow();
        ppt.createSlide();
        XSLFSlide slide = ppt.createSlide();
        XSLFTextBox text = slide.createTextBox();
        XSLFTextParagraph paragraph = text.addNewTextParagraph();
        XSLFTextRun run = paragraph.addNewTextRun();
        run.setFontColor(Color.BLUE);
        run.setFontSize(24);
        run.setText(request.Message);

        FileOutputStream outputStream = new FileOutputStream(file);

        ppt.write(outputStream);
        outputStream.close();
    }

//    private void CreateFileModel(CreateFileRequest request) throws IOException {
//        com.opensource.models.File file = new com.opensource.models.File(request.Name,request.LastName,request.UniqueId);
//        RegisterFile(file);
//    }

//    private void CreateDatabase() throws IOException  {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Users");
//
//        HSSFRow row = sheet.createRow(0);
//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue("Username");
//        HSSFCell cell2 = row.createCell(1);
//        cell2.setCellValue("Password");
//        HSSFCell cell3 = row.createCell(2);
//        cell3.setCellValue("FullName");
//
//        HSSFSheet sheet2 = workbook.createSheet("Files");
//
//        HSSFRow rowSheet2 = sheet2.createRow(0);
//        HSSFCell cellSheet2 = rowSheet2.createCell(0);
//        cellSheet2.setCellValue("Name");
//        HSSFCell cell2Sheet2 = rowSheet2.createCell(1);
//        cell2Sheet2.setCellValue("Owner");
//        HSSFCell cell3Sheet2 = rowSheet2.createCell(2);
//        cell3Sheet2.setCellValue("Type");
//        HSSFCell cell4Sheet2 = rowSheet2.createCell(3);
//        cell4Sheet2.setCellValue("CreationDate");
//        HSSFCell cell5Sheet2 = rowSheet2.createCell(4);
//        cell5Sheet2.setCellValue("UpdateDate");
//
//        FileOutputStream file = new FileOutputStream("files\\ExcelDB\\Files.xls");
//
//        try {
//            workbook.write(file);
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

//    private void RegisterFile(com.opensource.models.File file) throws IOException {
//        FileInputStream input = new FileInputStream("files\\ExcelDB\\Files.xls");
//        HSSFWorkbook workbook = new HSSFWorkbook(input);
//
//        HSSFSheet sheet = workbook.getSheet("Files");
//
//        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
//
//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue(file.Name);
//
//        HSSFCell cell1 = row.createCell(1);
//        cell1.setCellValue(file.LastName);
//
//        HSSFCell cell2 = row.createCell(2);
//        cell2.setCellValue(file.UniqueId);
//
//        HSSFCell cell3 = row.createCell(3);
//        cell3.setCellValue(file.Age);
//
//        HSSFCell cell4 = row.createCell(4);
//        cell4.setCellValue(file.Session);
//
//        FileOutputStream out = new FileOutputStream("files\\ExcelDB\\Files.xls");
//
//        workbook.write(out);
//        out.flush();
//        out.close();
//    }
}
