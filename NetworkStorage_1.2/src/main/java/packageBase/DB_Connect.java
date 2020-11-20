package packageBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connect {
    private static DB_Connect db_connect;
    Connection connection;
    public DB_Connect() throws SQLException {

        String hostName = "127.0.0.1";
        String dbName = "net_storage";
        String user = "root";
        String password = "Zz123456";

        String jdbcURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?serverTimezone=UTC";
        connection = DriverManager.getConnection(jdbcURL, user, password);
    }

    public static DB_Connect getInstance(){
        if(db_connect ==null){
            try {
                db_connect = new DB_Connect();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return db_connect;
    }
}
