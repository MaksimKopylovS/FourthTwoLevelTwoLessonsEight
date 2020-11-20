package packageBase;

import java.sql.SQLException;

public interface AuthService {

    void start();
    void stop();

    String getLoginPass(String login, String pass) throws SQLException;
    void registrationBase(String login, String pass) throws SQLException;
    void renameBase(String nameFirst, String nameLast) throws SQLException;
    String getNick(String name) throws SQLException;
}
