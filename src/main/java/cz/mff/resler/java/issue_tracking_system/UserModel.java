package cz.mff.resler.java.issue_tracking_system;

import java.sql.*;

/**
 * Class for communicating with the database, handling users.
 */
public class UserModel {
    static Connection connection;

    public UserModel() {
        connection = SQLiteConnection.connector();
    }

    /**
     * Checks whether there exists a user with the given username in the database.
     *
     * @param username user's username.
     * @return true if there exists a user with given username in the database.
     */
    public boolean usernameExists(String username) {
        ResultSet resultSet;
        String query = "SELECT 1 FROM user WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the login credentials are valid.
     *
     * @param username user's username.
     * @param password user's password.
     * @return true if the credentials are correct.
     */
    public boolean loginExists(String username, String password) {
        ResultSet resultSet;
        String query = "SELECT 1 FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the id of the user with the given username.
     *
     * @param username user's username.
     * @return id of the user with this username.
     */
    public int getUserID(String username) {
        String query = "SELECT id FROM user WHERE username = ?;";
        ResultSet resultSet;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("id");
        } catch (SQLException e){
            return -1;
        }
    }

    /**
     * Returns username of the user with the given id.
     *
     * @param id id of the user.
     * @return username of the user with this id.
     */
    public String getUsername(int id) {
        String query = "SELECT username FROM user WHERE id = ?;";
        ResultSet resultSet;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            return resultSet.getString("username");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new user to the database.
     *
     * @param username new user's username.
     * @param password new user's password.
     * @param role new user's role value.
     */
    public void addNewUser(String username, String password, int role) {
        String query = "INSERT INTO user (username, password, role) VALUES( ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, role);

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the role value of the user with the given id.
     *
     * @param id user's id.
     * @return role value.
     */
    public int getUserRole(int id) {
        String query = "SELECT role FROM user WHERE id = ?;";
        ResultSet resultSet;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("role");
        } catch (SQLException e){
            return -1;
        }
    }
}
