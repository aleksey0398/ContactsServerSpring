package Authorization;

import java.sql.*;
import java.util.UUID;

public class AuthorizationSQLHelper {

    Connection connection = null;
    long halfYearInMills = 15_724_800_000l;

    public AuthorizationSQLHelper() {

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SamsungContacts",
                    "samsung", "samsung");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("Connection success!");
        } else {
            System.out.println("FAIL connection");
        }
    }

    public AuthorizationAnswer registerNewUser(String login, String password) throws SQLException {
        AuthorizationAnswer answer = new AuthorizationAnswer();

        Timestamp dateCreate = new Timestamp(System.currentTimeMillis());

        PreparedStatement ps = connection.
                prepareStatement("INSERT INTO users_table (login,password,date_create,date_last_login) VALUES(?,?,?,?)");
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setTimestamp(3, dateCreate);
        ps.setTimestamp(4, dateCreate);
        ps.executeUpdate();
        ps.close();
        close();
        return answer.setResult(true).setCause("Новый пользователь успешно зарегистрирован");
    }

    public AuthorizationAnswer login(String login, String password) throws SQLException {

        Timestamp lastLogin = new Timestamp(System.currentTimeMillis());
        AuthorizationAnswer answer = new AuthorizationAnswer();
        PreparedStatement ps = connection.
                prepareStatement("select password from users_table where login=?");

        ps.setString(1, login);

        ResultSet result = ps.executeQuery();
        String passwordFromDB = "";
        while (result.next()) {
            passwordFromDB = result.getString(1);
        }

        if (!passwordFromDB.equals(password)) {
            answer.setResult(false);
            answer.setCause("Неправльный логин или пароль");
            ps.close();
            close();
            return answer;
        } else {
            answer.setResult(true);
            answer.setCause("Данные верны");
            PreparedStatement psUpdate = connection.
                    prepareStatement("UPDATE users_table SET date_last_login = ? where login=?");
            psUpdate.setTimestamp(1, lastLogin);
            psUpdate.setString(2, login);
            psUpdate.executeUpdate();
            psUpdate.close();

            psUpdate = connection.prepareStatement("" +
                    "insert into users_session (session_key, login, date_create, date_end_rental) VALUES (?,?,?,?)");
            String sessionKey = UUID.randomUUID().toString();
            Timestamp dateCreate = new Timestamp(System.currentTimeMillis());
            Timestamp dateEndRental = new Timestamp(System.currentTimeMillis() + halfYearInMills);
            psUpdate.setString(1, sessionKey);
            psUpdate.setString(2, login);
            psUpdate.setTimestamp(3, dateCreate);
            psUpdate.setTimestamp(4, dateEndRental);
            psUpdate.executeUpdate();
            psUpdate.close();

            answer.setSessionKey(sessionKey);

            close();
            return answer;
        }
    }


    public AuthorizationAnswer checkLoginEmployment(String login) throws SQLException {
        PreparedStatement psCheck = connection.prepareStatement("select login from users_table where login=?");
        psCheck.setString(1, login);
        ResultSet result = psCheck.executeQuery();

        while (result.next()) {
            if (login.equals(result.getString(1))) {
                close();
                return new AuthorizationAnswer().setResult(false).setCause("Логин занят");
            }
        }
        close();
        return new AuthorizationAnswer().setResult(true).setCause("Логин не занят");
    }


    public AuthorizationAnswer checkSession(String session, String login) throws SQLException {
        AuthorizationAnswer answer = new AuthorizationAnswer();
        return answer;
    }

    void close() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
