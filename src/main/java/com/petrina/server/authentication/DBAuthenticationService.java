package com.petrina.server.authentication;

import java.sql.*;

public class DBAuthenticationService implements AuthenticationService{

    private static Connection connection;
    private static Statement statement;

    @Override
    public void startAuthentication() throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
        statement = connection.createStatement();

    }
    @Override
    public String getUsernameByLoginAndPassword(String login, String password) throws SQLException, ClassNotFoundException {
        startAuthentication();
        String sql = String.format("SELECT nickname FROM main where login = '%s' and password = '%s'", login, password);

        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                String str = rs.getString(1);
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void endAuthentication() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
