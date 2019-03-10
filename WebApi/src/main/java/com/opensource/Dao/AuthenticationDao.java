package com.opensource.Dao;

import com.opensource.models.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class AuthenticationDao {

    public boolean createUser(User user) {
        User productToCheck;
        productToCheck = getUserByName(user.Username);
        if (productToCheck != null) {
            return false;
        } else {
            String query = "INSERT INTO Users VALUES ('" + user.Username + "','"
                    + user.Password + "','"
                    + user.FullName + "')";
            return executeCommand(query);
        }
    }

    public User authenticateUser(String userName, String password) {
        String query = "Select * from Users WHERE Username = '" + userName + "' AND Password = '" + password + "'";
        return getUser(query);
    }

    private User getUserByName(String name) {
        String query = "Select * from Users WHERE Username = " + name + "";
        return getUser(query);
    }

    private User getUser(String query) {
        try {
            Connection con = SqlServerConnector.getConnection();
            Statement state;
            state = con.createStatement();
            ResultSet rs = state.executeQuery(query);
            rs.next();
            User newProduct = new User();
            newProduct.setFullName(rs.getString("FullName"));
            newProduct.setId(rs.getInt("Id"));
            newProduct.setUsername(rs.getString("Username"));
            newProduct.setPassword(rs.getString("Password"));

            return newProduct;
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
