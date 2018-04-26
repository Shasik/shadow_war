package database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

public class Database {
    private static Connection conn;
    private static int count = 0;


    public static Connection getConnection() {
        count++;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.enterprise.naming.SerialInitContextFactory");

        try {
            InitialContext ic = new InitialContext(env);
            DataSource ds = (DataSource) ic.lookup("jdbc/shadow_war");
            if (conn == null) {
                conn = ds.getConnection();
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static int getCount() {
        int temp = count;
        count = 0;
        return temp;
    }
}
