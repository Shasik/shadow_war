package database;

import model.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private static long timeExecFull = 0;
    private static long timeExec = 0;

    public static Player getUserByNickname(String login) {
        timeExec = System.nanoTime();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE login = '" + login + "';");
            if (rs.next()) {
                return createPlayer(rs);
            }
        } catch (SQLException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        timeExec = Math.abs(System.nanoTime() - timeExec);
        timeExecFull += timeExec;
        return null;
    }

    /** Метод возвращает 1, если такой логин существует и пароль совпадает
     *  Метод возвращает 0, если такой логин не существует
     *  Метод возвращает -1, если такой логин существует и пароль не совпадает*/
    public static int checkLogin(String login, String password) {
        timeExec = System.nanoTime();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM users WHERE login = '" + login + "' AND password = '" + password + "';");
            if (rs1.next()) {
                timeExec = Math.abs(System.nanoTime() - timeExec);
                timeExecFull += timeExec;
                return 1;
            } else {
                ResultSet rs2 = stmt.executeQuery("SELECT * FROM users WHERE login = '" + login + "';");
                if (rs2.next()) {
                    timeExec = Math.abs(System.nanoTime() - timeExec);
                    timeExecFull += timeExec;
                    return -1;
                } else {
                    timeExec = Math.abs(System.nanoTime() - timeExec);
                    timeExecFull += timeExec;
                    return 0;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        timeExec = Math.abs(System.nanoTime() - timeExec);
        timeExecFull += timeExec;
        return 100;
    }

    public static void registrationUser(String login, String password) {
        timeExec = System.nanoTime();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO users(login, password, health_points, damage_points, rating_points) VALUE ('" + login + "', '" + password + "', 100, 10, 50);");
        } catch (SQLException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        timeExec = Math.abs(System.nanoTime() - timeExec);
        timeExecFull += timeExec;
    }

    public static void updateCharactersAfterFight(Player player, boolean winner) {
        timeExec = System.nanoTime();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("update users set health_points = health_points + 1 where login = '" + player.getNickname() + "';");
            stmt.executeUpdate("update users set damage_points = damage_points + 1 where login = '" + player.getNickname() + "';");
            if (winner) {
                stmt.executeUpdate("update users set rating_points = rating_points + 1 where login = '" + player.getNickname() + "';");
            } else {
                stmt.executeUpdate("update users set rating_points = rating_points - 1 where login = '" + player.getNickname() + "';");
            }
        } catch (SQLException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        timeExec = Math.abs(System.nanoTime() - timeExec);
        timeExecFull += timeExec;
    }

    public static int getRatePosition(String nickname) {
        timeExec = System.nanoTime();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT num from (SELECT (@row_number := @row_number + 1) AS num, login FROM users, (SELECT @row_number := 0) AS t order by rating_points desc) s where login = '" + nickname + "';");
            rs.next();
            timeExec = Math.abs(System.nanoTime() - timeExec);
            timeExecFull += timeExec;
            return rs.getInt("num");
        } catch (SQLException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        timeExec = Math.abs(System.nanoTime() - timeExec);
        timeExecFull += timeExec;
        return -1;
    }

    private static Player createPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setNickname(rs.getString("login"));
        System.out.println(rs.getString("login"));
        player.setHp(rs.getInt("health_points"));
        System.out.println(rs.getInt("health_points"));
        player.setDamage(rs.getInt("damage_points"));
        System.out.println(rs.getInt("damage_points"));
        player.setRating(rs.getInt("rating_points"));
        System.out.println(rs.getInt("rating_points"));
        return player;
    }

    public static double getTimeExecFullInMillis() {
        double temp = timeExecFull / 1000000.0;
        timeExecFull = 0;
        return temp;
    }
}
