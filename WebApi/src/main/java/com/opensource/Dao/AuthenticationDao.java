package com.opensource.Dao;

import com.opensource.models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationDao {

    public boolean createUser(User user) {
        User productToCheck;
        productToCheck = getUserByName(user.Username);
        if (productToCheck != null) {
            return false;
        } else {
            String query = "INSERT INTO Users VALUES ('" + user.Username + "','"
                    + user.FullName + "',"
                    + user.Password + ")";
            return executeCommand(query);
        }
    }

    public User authenticateUser(String userName, String password) {
        String query = "Select * from Users WHERE username =" + userName + " AND password =" + password;
        return getUser(query);
    }

    private User getUserByName(String name) {
        String query = "Select * from Users WHERE name =" + name;
        return getUser(query);
    }

    private User getUser(String query) {
        try {
            User product = new User();
            Connection con = SqlServerConnector.getConnection();
            Statement state;
            state = con.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                User newProduct = new User(
                        rs.getString("username"),
                        rs.getString("fullName"),
                        rs.getString("password")
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
