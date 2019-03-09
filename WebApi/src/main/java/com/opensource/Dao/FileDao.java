package com.opensource.Dao;

import com.opensource.enums.FileType;
import com.opensource.models.File;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class FileDao {


    public List<File> GetFiles() {
        String query = "Select * from Files";
        return GetFiles(query);
    }

    public List<File> GetFilesByOwner(String owner) {
        String query = "Select * from WHERE owner =" + owner;
        return GetFiles(query);
    }

    private List<File> GetFiles(String query) {
        Connection connection = SqlServerConnector.getConnection();
        List<File> files = new ArrayList<File>();

        if (connection == null) {
            return null;
        } else {
            Statement state;
            try {
                state = connection.createStatement();
                ResultSet rs = state.executeQuery(query);

                while (rs.next()) {
                    File file = new File(
                            rs.getString("name"),
                            FileType.valueOf(rs.getString("type")),
                            rs.getString("owner"),
                            rs.getDate("CreationDate"),
                            rs.getDate("UpdateDate")
                    );
                    file.Id = (rs.getInt("id"));
                    files.add(file);
                }
            } catch (SQLException ex) {
                return null;
            } catch (Exception e) {
                return null;
            }
            return files;
        }
    }

    public boolean createFile(File file) {
        File productToCheck;
        productToCheck = getFileByName(file.Name);
        if (productToCheck != null) {
            return false;
        } else {
            String query = "INSERT INTO files VALUES ('" + file.Name + "','"
                    + file.Type + "','"
                    + file.Owner + "','"
                    + new java.sql.Timestamp(new Date().getTime())+ "','"
                    + new java.sql.Timestamp(new Date().getTime()) + "')";
            return executeCommand(query);
        }
    }

    private File getFileByName(String name) {
        String query = "Select * from Files WHERE name =" + name;
        return getFile(query);
    }

    private File getFile(String query) {
        try {
            File product = new File();
            Connection con = SqlServerConnector.getConnection();
            Statement state;
            state = con.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                File newProduct = new File(
                        rs.getString("name"),
                        FileType.values()[rs.getInt("type")],
                        rs.getString("owner"),
                        rs.getDate("creation_date"),
                        rs.getDate("update_date")
                );
                newProduct.Id = (rs.getInt("id"));
                product = newProduct;
            }
            return product;
        } catch (SQLException ex) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateFile(File file) {
        File productToCheck;
        productToCheck = getFilesById(file.Id);
        if (file != productToCheck) {
            String query = "UPDATE files SET name = '" + file.Name
                    + "', type = '" + file.Type.ordinal()
                    + "', owner =" + file.Owner
                    + ", update_date= " + new java.sql.Timestamp(new Date().getTime()) + " WHERE id =" + file.Id;
            return executeCommand(query);
        } else {
            return true;
        }
    }

    public boolean deleteFile(String name) {
        File productToCheck;
        productToCheck = getFilesByName(name);
        if (name != productToCheck.Name) {
            String query = "DELETE files SET WHERE id =" + name;
            return executeCommand(query);
        } else {
            return true;
        }
    }

    private File getFilesById(int id) {
        String query = "Select * from files WHERE id =" + id;
        return getFile(query);
    }

    private File getFilesByName(String name) {
        String query = "Select * from Files WHERE Name ='" + name +"'";
        return getFile(query);
    }

    private boolean executeCommand(String query) {
        try {
            Connection con = SqlServerConnector.getConnection();
            Statement state;
            state = con.createStatement();
            int result = state.executeUpdate(query);

            if (result >= 1) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
