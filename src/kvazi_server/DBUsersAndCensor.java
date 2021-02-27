package kvazi_server;

import com.sun.istack.internal.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBUsersAndCensor implements AuthAndCensorService{
    private final String ADDRESS = "192.168.0.11";
    private final String USER = "kvazimodo";
    private final String PASSWORD = "kvazimodo";
    private final String DATA_BASE = "kvazimodo_auth";
    private final String TABLE_USERS = "users";
    private final String TABLE_FILTER = "filter";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private void getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        String connectUrl = "jdbc:mysql://" + ADDRESS + "/" + DATA_BASE;
        connection = DriverManager.getConnection(connectUrl,USER,PASSWORD);
    }
    private void closeConnection() {
        try {
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(@NotNull String login, @NotNull String password) {
        if (login.length() * password.length() == 0)
            return null;
        String trueLogin = null;
        String truePassword = null;
        String nickname = null;
        try {
            getConnection();
            String preparedStatementString = "SELECT * FROM " + TABLE_USERS + " WHERE login = ?";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1,login);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trueLogin = resultSet.getString(2);
                truePassword = resultSet.getString(3);
                nickname = resultSet.getString(4);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        if (login.equals(trueLogin) && password.equals(truePassword))
            return nickname;
        return null;
    }

    @Override
    public boolean registration(@NotNull String login, @NotNull String password, @NotNull String nickname) {
        if (login.length() * password.length() * nickname.length() == 0)
            return false;
        String loginAtDB = null;
        try {
            getConnection();
            String preparedStatementString = "SELECT * FROM " + TABLE_USERS + " WHERE login = ?";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1,login);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                loginAtDB = resultSet.getString(2);
            if (loginAtDB != null)
                return false;
            preparedStatementString = "INSERT INTO users (uid, login, password, nickname) VALUES (NULL, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,nickname);
            resultSet = preparedStatement.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public boolean changeNickname(@NotNull String login, @NotNull String newNickname) {
        if (login.length() * newNickname.length() == 0)
            return false;
        try {
            getConnection();
            String preparedStatementString = "SELECT * FROM " + TABLE_USERS + " WHERE nickname = ?";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1,newNickname);
            resultSet = preparedStatement.executeQuery();
            String oldNickname = null;
            while (resultSet.next())
                 oldNickname = resultSet.getString(4);
            if (oldNickname != null)
                return false;
            preparedStatementString = "UPDATE users SET nickname = ? WHERE login = ?";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1,newNickname);
            preparedStatement.setString(2,login);
            preparedStatement.executeQuery();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return true;
    }

    @Override
    public Map<String, String> getFilter() {
        Map<String,String> filter = new HashMap<>();
        try {
            getConnection();
            String preparedStatementString = "SELECT * FROM " + TABLE_FILTER;
            preparedStatement = connection.prepareStatement(preparedStatementString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                filter.put(resultSet.getString(1),resultSet.getString(2));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } closeConnection();
        if (filter.isEmpty())
            return null;
        return filter;
    }
}
