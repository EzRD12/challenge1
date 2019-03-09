package com.opensource.controllers;

import com.opensource.Dao.AuthenticationDao;
import com.opensource.Dao.FileDao;
import com.opensource.enums.FileType;
import com.opensource.models.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelController {

    @Autowired
    private AuthenticationDao authenticationDao;

    @Autowired
    private FileDao fileDao;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public BasicOperationResult<AuthenticationResponse> AuthenticateUser(@RequestBody AuthenticateRequest request) {
        User user = authenticationDao.authenticateUser(request.Username, request.Password);
        if(user != null) {
            AuthenticationResponse response = new AuthenticationResponse(user.Username, user.FullName);
            return new BasicOperationResult<AuthenticationResponse>("", true, response);
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
        fileToDelete.delete();

        return fileDao.deleteFile(request.Id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User CreateUser(@RequestBody User request) {
        authenticationDao.createUser(request);

        return request;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public List<com.opensource.models.File> GetFiles() {
        List<com.opensource.models.File> files = fileDao.GetFiles();

        return files;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public boolean CreateExcelFile(@RequestBody FileRequest request) throws IOException {
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
    @RequestMapping(value = "/excel", method = RequestMethod.PUT)
    public boolean UpdateExcelFile(@RequestBody FileRequest request) throws IOException {
        String destXls = "files\\database\\database.xls";
        FileInputStream file = new FileInputStream(new File(destXls));
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        HSSFSheet sheet = workbook.getSheet("Files");
        HSSFRow row;

        for (int index = 1; index <= sheet.getLastRowNum(); index++) {
            String username = "";
            HSSFCell cell;
            final int nameCell = 0;
            final int ownerCell = 1;
            row = sheet.getRow(index);
            if(row != null){
                username = row.getCell(0).toString();
            }
            if(username.equals(request.Name)) {
                //////////Database File//////////////
                for(int i = 1; i <= row.getLastCellNum(); i++){
                    if(i == nameCell){
                        cell = row.getCell(i);
                        cell.setCellValue(request.Name);
                    } else if(i == ownerCell){
                        cell = row.getCell(i);
                        cell.setCellValue(request.UserOwner);
                    }
                }

                FileOutputStream fileOut = new FileOutputStream(new File(destXls));
                workbook.write(fileOut);
                fileOut.close();

                String fileToDeleteExtension;
                if(request.FileType == FileType.Excel){
                    fileToDeleteExtension = ".xls";
                } else if(request.FileType == FileType.Word){
                    fileToDeleteExtension = ".docx";
                } else {
                    fileToDeleteExtension = ".ppt";
                }

                File fileToUpdate = new File("files\\"+ request.FileType.toString().toLowerCase() +"\\"+ request.Name + fileToDeleteExtension);
                FileInputStream fsIP= new FileInputStream(fileToUpdate);
                HSSFWorkbook wb = new HSSFWorkbook(fsIP);
                HSSFSheet worksheet = wb.getSheetAt(0);
                HSSFCell updateCell;
                updateCell = worksheet.getRow(1).getCell(1);
                updateCell.setCellValue(request.FirstMessage);
                FileOutputStream output_file = new FileOutputStream(new File("C:\\Excel.xls"));
                wb.write(output_file);
                output_file.close();

                return true;
            }
        }
        return false;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/word", method = RequestMethod.POST)
    public boolean CreateWordFile(@RequestBody FileRequest request) throws IOException {
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
    public boolean CreatePowerPointFile(@RequestBody FileRequest request) throws IOException {
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

    private void CreateFileModel(FileRequest request, FileType fileType) throws IOException {
        com.opensource.models.File file = new com.opensource.models.File(request.Name, fileType, request.UserOwner);
        RegisterFile(file);
    }


    private void RegisterFile(com.opensource.models.File file) {
        fileDao.createFile(file);
    }
}
