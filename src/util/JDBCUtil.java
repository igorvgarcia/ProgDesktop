// JDBCUtil.java
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/meubanco";
    private static final String USER = "igor";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String getDatabaseName(Connection connection) throws SQLException {
        return connection.getCatalog();
    }
   
}
