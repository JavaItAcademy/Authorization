package com.company;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector {
    public final String url = "jdbc:postgresql://localhost:5432/postgres";
    public final String user = "postgres";
    public final String password = "123";

    public Connection connect() {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, user, password);
            //System.out.println("Successfully connected to the Database");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean register (User user) {
        String SQL = "insert into users (login, email, password, date_of_registration) values (?,?,?,NOW());";

        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setString(1, user.login);
            stmt.setString(2, user.email);
            stmt.setString(3, user.password);
            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean authorize (User user) {
        String SQL = "select id from users where login = ?";
        int id = -1;
        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setString(1, user.login);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                id = rs.getInt("id");

        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        if (id == -1) {
            return false;
        }
        // check password
        return checkLoginAndPassword(user, id);
    }

    private String decrypt (String password) {
        return "";
    }

    //asd => 123
    //asd => 12345
    public boolean checkLoginAndPassword(User user, int userId) {
        String SQL = "select count(*) as cnt from users where login = ? and password = ?";
        int count = 0;
        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setString(1, user.login);
            stmt.setString(2, user.password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                count = rs.getInt("cnt");
            }

        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            // Write Fail to Log
            return false;
        }
        if (count == 0) {
            // Write Fail to Log
            writeToLog(userId, false);
            return false;
        }

        // Write OK to Log
        writeToLog(userId, true);
        return true;
    }

    public void writeToLog(int id, boolean result) {
        String SQL = "insert into user_logs (user_id, login_date, is_successful) values (?, NOW(), ?)";

        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id);
            stmt.setBoolean(2, result);
            stmt.executeUpdate();

        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (result) cleanLogs(id);
        else checkLogins(id);

    }
    //Обнулить логи при успешной авторизации
    public void cleanLogs(int id){
        String SQL = "delete from user_logs where user_id = ?";

        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    //Проверить количество неправильных логинов пользователя
    public void checkLogins(int id){
        String SQL = "select count(*) as cnt from user_logs where user_id = ? and is_successful = false";
        int count = 0;
        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                count = rs.getInt("cnt");
            }

        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        if (count >= 3) {
            blockOrUnblockUser(id, true);
        }
    }
    // Заблокировать пользователя
    public void blockOrUnblockUser(int id, boolean block){
        String SQL = "update users set is_blocked = ? where id = ?";

        try(Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(SQL)
        ) {
            stmt.setInt(2, id);
            stmt.setBoolean(1, block);
            stmt.executeUpdate();
            if (block) System.out.println("User is blocked");
            else System.out.println("User is unblocked");
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}