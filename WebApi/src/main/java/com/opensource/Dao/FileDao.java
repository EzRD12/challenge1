package com.opensource.Dao;

import com.opensource.enums.FileType;
import com.opensource.models.File;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileDao {

    public List<File> GetFiles() {
        Connection connection = SqlServerConnector.getConnection();
        List<File> files = new ArrayList<File>();

        if (connection == null) {
            return null;
        } else {
            Statement state;
            String query = "Select * from Files";
            try {
                state = connection.createStatement();
                ResultSet rs = state.executeQuery(query);

                while (rs.next()) {
                    File file = new File(
                            rs.getString("name"),
                            FileType.values()[rs.getInt("type")],
                            rs.getString("owner"),
                            rs.getDate("creation_date"),
                            rs.getDate("update_date")
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
            String query = "INSERT INTO products VALUES ('" + file.Name + "','"
                    + file.Type + "',"
                    + file.Owner + ","
                    + file.CreationDate + ","
                    + file.UpdateDate + ")";
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
        productToCheck = getProductById(file.Id);
        if (file != productToCheck) {
            String query = "UPDATE files SET name = '" + file.Name
                    + "', type = '" + file.Type.ordinal()
                    + "', owner =" + file.Owner
                    + ", update_date= " + new Date().toString() + " WHERE id =" + file.Id;
            return executeCommand(query);
        } else {
            return true;
        }
    }

    public boolean deleteFile(int id) {
        File productToCheck;
        productToCheck = getProductById(id);
        if (id != productToCheck.Id) {
            String query = "DELETE files SET WHERE id =" + id;
            return executeCommand(query);
        } else {
            return true;
        }
    }

    private File getProductById(int id) {
        String query = "Select * from products WHERE id =" + id;
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
