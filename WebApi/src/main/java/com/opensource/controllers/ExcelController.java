package com.opensource.controllers;

import com.opensource.enums.FileType;
import com.opensource.models.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public BasicOperationResult<AuthenticationResponse> AuthenticateUser(@RequestBody AuthenticateRequest request) throws IOException {
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
                    return new BasicOperationResult<AuthenticationResponse>("", true, new AuthenticationResponse(username, password));
                }
            }
        }

        return new BasicOperationResult<AuthenticationResponse>("InvalidCredentials", false, null);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/files/delete", method = RequestMethod.POST)
    public boolean DeleteFile(@RequestBody DeleteFileModel request) throws IOException {
        String destXls = "files\\database\\database.xls";
        FileInputStream file = new FileInputStream(new File(destXls));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        String fileToDeleteExtension;
        if(request.FileType == FileType.Excel){
            fileToDeleteExtension = ".xls";
        } else if(request.FileType == FileType.Word){
            fileToDeleteExtension = ".docx";
        } else {
            fileToDeleteExtension = ".ppt";
        }
        File fileToDelete = new File("files\\"+ request.FileType.toString().toLowerCase() +"\\"+ request.Name + fileToDeleteExtension);

        HSSFSheet sheet = workbook.getSheet("Files");
        HSSFRow row;

        for (int index = 1; index <= sheet.getLastRowNum(); index++) {
            String username = "";
            row = sheet.getRow(index);
            if(row != null){
                username = row.getCell(0).toString();
            }
            if(username.equals(request.Name)) {
                sheet.removeRow(row);
                FileOutputStream fileOut = new FileOutputStream(new File(destXls));
                workbook.write(fileOut);
                fileOut.close();

                fileToDelete.delete();
                return true;
            }
        }

        return false;
    }

    @CrossOrigin(origins = "http://localhost:4200")
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

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public List<com.opensource.models.File> GetFiles() throws IOException, ParseException {
        FileInputStream file = new FileInputStream("files\\database\\database.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        HSSFSheet sheet = workbook.getSheet("Files");
        HSSFRow row;
        List<com.opensource.models.File> files = new ArrayList<com.opensource.models.File>();

        for (int index = 1; index <= sheet.getLastRowNum(); index++) {
            row = sheet.getRow(index);
            com.opensource.models.File fi = new com.opensource.models.File();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            if(row != null){
                fi.Name = row.getCell(0).toString();
                fi.Owner = row.getCell(1).toString();
                fi.Type = FileType.values()[(int)row.getCell(2).getNumericCellValue()];
                fi.CreationDate = format.parse(row.getCell(3).toString());
                fi.UpdateDate = format.parse(row.getCell(4).toString());
                files.add(fi);
            }
        }

        return files;
    }

    @CrossOrigin(origins = "http://localhost:4200")
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
            CreateFileModel(request, FileType.Excel);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @CrossOrigin(origins = "http://localhost:4200")
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
            CreateFileModel(request, FileType.Word);

            out.close();
        }catch (Exception e ){
            e.printStackTrace();
            throw e;
        }

        return true;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/power-point", method = RequestMethod.POST)
    public boolean CreatePowerPointFile(@RequestBody CreateFileRequest request) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow();
        FileOutputStream outputStream = new FileOutputStream("files\\powerpoint\\" + request.Name + ".ppt");

        XSLFSlide slide = ppt.createSlide();
        XSLFTextBox text = slide.createTextBox();
        text.setText(request.FirstMessage);
        ppt.write(outputStream);
        CreateFileModel(request, FileType.PowerPoint);

        outputStream.close();

        return true;
    }

    private void CreateFileModel(CreateFileRequest request, FileType fileType) throws IOException {
        com.opensource.models.File file = new com.opensource.models.File(request.Name, fileType, request.UserOwner);
        RegisterFile(file);
    }

    private void CreateDatabase() throws IOException  {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Users");

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Username");
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("Password");
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellValue("FullName");

        HSSFSheet sheet2 = workbook.createSheet("Files");

        HSSFRow rowSheet2 = sheet2.createRow(0);
        HSSFCell cellSheet2 = rowSheet2.createCell(0);
        cellSheet2.setCellValue("Name");
        HSSFCell cell2Sheet2 = rowSheet2.createCell(1);
        cell2Sheet2.setCellValue("Owner");
        HSSFCell cell3Sheet2 = rowSheet2.createCell(2);
        cell3Sheet2.setCellValue("Type");
        HSSFCell cell4Sheet2 = rowSheet2.createCell(3);
        cell4Sheet2.setCellValue("CreationDate");
        HSSFCell cell5Sheet2 = rowSheet2.createCell(4);
        cell5Sheet2.setCellValue("UpdateDate");

        FileOutputStream file = new FileOutputStream("files\\database\\database.xls");

        try {
            workbook.write(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void RegisterFile(com.opensource.models.File file) throws IOException {
        FileInputStream input = new FileInputStream("files\\database\\database.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(input);

        HSSFSheet sheet = workbook.getSheet("Files");

        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue(file.Name);

        HSSFCell cell1 = row.createCell(1);
        cell1.setCellValue(file.Owner);

        HSSFCell cell2 = row.createCell(2);
        cell2.setCellValue(file.Type.ordinal());

        HSSFCell cell3 = row.createCell(3);
        cell3.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(file.CreationDate));

        HSSFCell cell4 = row.createCell(4);
        cell4.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(file.UpdateDate));

        FileOutputStream out = new FileOutputStream("files\\database\\database.xls");

        workbook.write(out);
        out.flush();
        out.close();
    }
}
