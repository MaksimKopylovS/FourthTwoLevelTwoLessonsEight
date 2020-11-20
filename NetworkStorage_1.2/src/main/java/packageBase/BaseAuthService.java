package packageBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseAuthService implements AuthService {
    private static ResultSet resultSet;

    private String login;
    private String pass;
    private String path;
    private static int addBase;

    public static ResultSet getResultSet() {
        return resultSet;
    }


    @Override
    public void start() {
        System.out.println("Сервис Аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис Аутентификации остановлен");
    }

    @Override
    public String getLoginPass(String login, String pass) throws SQLException {
        return null;
    }

    @Override
    public void registrationBase(String login, String pass) throws SQLException {
            this.login = login;
            this.pass = pass;

            addBase = DB_Connect.getInstance()
                    .connection
                    .createStatement()
                    .executeUpdate(
                            "INSERT INTO" +
                                    " `net_storage`.`user`(`name`,`password`)" +
                                    " VALUE " +
                                    "('"+ this.login + "', '"+ this.pass + "');");
    }

    @Override
    public void renameBase(String nameFirst, String nameLast) throws SQLException {

    }

    @Override
    public String getNick(String name) throws SQLException {
        return null;
    }

    public static boolean busyLogin(String login) throws SQLException {
        resultSet = DB_Connect.getInstance().connection.createStatement().executeQuery("SELECT * FROM `user`;");
        String strName;
        while(resultSet.next()){
            strName = resultSet.getString("name");
            if(strName.equals(login)){
                return true;
            }
        }
        return false;
    }

    public static String authLoginPass(String login, String pass) throws SQLException {
        resultSet = DB_Connect.getInstance().connection.createStatement().executeQuery("SELECT * FROM `user`;");
        String strLogin;
        String strPass;
        while (resultSet.next()){
            strLogin = resultSet.getString("name");
            strPass = resultSet.getString("password");
            if(strLogin.equals(login) && strPass.equals(pass)){
                return strLogin;
            }
        }
        return null;
    }

    public static int getId(String user) throws SQLException {
        int id;
        resultSet = DB_Connect.getInstance().connection.createStatement().executeQuery("SELECT * FROM `user`");
        String strUser;
        while(resultSet.next()){
            strUser = resultSet.getString("name");
            id = resultSet.getInt("id");

            if (strUser.equals(user)){
                return id;
            }
        }
        return 0;
    }

    public static void savePathFileUesr(String user, String path){

    }

}
