package cz.mff.resler.java.issue_tracking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Class for communicating with the database, handling issues.
 */
public class IssueModel {
    private final static UserModel userModel = new UserModel();

    private final Connection connection;

    public IssueModel() {
        this.connection = SQLiteConnection.connector();
    }

    /**
     * Adds a new issue in the database.
     *
     * @param createdBy user id.
     * @param priority priority value.
     * @param dateCreated date of creation of the issue.
     * @param summary text representing summary.
     * @param description text representing description.
     */
    public void addNewIssue(int createdBy, int priority, String dateCreated, String summary, String description) {
        String query =
                """
                INSERT INTO open_issue
                (created_by, priority, date_created, summary, description)
                VALUES( ?, ?, ?, ?, ?);
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, createdBy);
            preparedStatement.setInt(2, priority);
            preparedStatement.setString(3, dateCreated);
            preparedStatement.setString(4, summary);
            preparedStatement.setString(5, description);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all open issues from the database.
     *
     * @return all open issues.
     */
    public LinkedList<Issue> getAllOpenIssues() {
        String query = "SELECT * FROM open_issue";
        ResultSet resultSet;
        LinkedList<Issue> resultList = new LinkedList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String createdByUserString = userModel.getUsername(resultSet.getInt("created_by"));

                String priorityString =
                        switch (resultSet.getInt("priority")) {
                            case 1 -> "Lowest";
                            case 2 -> "Low";
                            case 3 -> "Medium";
                            case 4 -> "High";
                            case 5 -> "Highest";
                            default -> throw new IllegalStateException("Unexpected value: " + resultSet.getInt("priority"));
                        };

                resultList.add(
                        new Issue(
                                resultSet.getInt("id"),
                                createdByUserString,
                                resultSet.getInt("created_by"),
                                priorityString,
                                resultSet.getInt("priority"),
                                resultSet.getString("date_created"),
                                resultSet.getString("summary"),
                                resultSet.getString("description"),
                                -1, "-1", ""
                        ));
            }

            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all closed issues from the database.
     *
     * @return all closed issues.
     */
    public LinkedList<Issue> getAllClosedIssues() {
        String query = "SELECT * FROM closed_issue";
        ResultSet resultSet;
        LinkedList<Issue> resultList = new LinkedList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String createdByUserString = userModel.getUsername(resultSet.getInt("created_by"));

                String priorityString =
                        switch (resultSet.getInt("priority")) {
                            case 1 -> "Lowest";
                            case 2 -> "Low";
                            case 3 -> "Medium";
                            case 4 -> "High";
                            case 5 -> "Highest";
                            default -> throw new IllegalStateException("Unexpected value: " + resultSet.getInt("priority"));
                        };

                String closedByString = userModel.getUsername(resultSet.getInt("closed_by"));

//                (int id, String createdByUserString, int createdByUserId,
//                String priorityString, int priorityInt, String dateCreated,
//                        String summary, String description, int closedBy, String closedByString, String dateClosed)

                resultList.add(
                        new Issue(
                                resultSet.getInt("id"),
                                createdByUserString,
                                resultSet.getInt("created_by"),
                                priorityString,
                                resultSet.getInt("priority"),
                                resultSet.getString("date_created"),
                                resultSet.getString("summary"),
                                resultSet.getString("description"),
                                resultSet.getInt("closed_by"),
                                closedByString,
                                resultSet.getString("date_closed")
                        ));
            }

            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates an issue in the database also with the date of creation.
     *
     * @param id id of the issue.
     * @param priority priority value.
     * @param dateCreated date of creation.
     * @param summary text representing summary.
     * @param description text representing description.
     */
    public void updateIssueWithDate(int id, int priority, String dateCreated, String summary, String description) {
        String query =
                """
                UPDATE open_issue
                SET priority = ?,
                    date_created = ?,
                    summary = ?,
                    description = ?
                WHERE
                    id = ?;
                 """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, priority);
            preparedStatement.setString(2, dateCreated);
            preparedStatement.setString(3, summary);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an issue in the database, does not update the date of creation.
     *
     * @param id id of the issue.
     * @param priority priority value.
     * @param summary text representing summary.
     * @param description text representing description.
     */
    public void updateIssueWithoutDate(int id, int priority, String summary, String description) {
        String query =
                """
                UPDATE open_issue
                SET priority = ?,
                    summary = ?,
                    description = ?
                WHERE
                    id = ?;
                 """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, priority);
            preparedStatement.setString(2, summary);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves an issue from the open issues table to the closed issues table.
     * @param issue issue to be closed.
     */
    public void closeIssue(Issue issue) {
//      Remove the issue from the open issues table
        String queryDelete = "DELETE FROM open_issue WHERE id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDelete)) {
            preparedStatement.setInt(1, issue.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//      Add the issue to the closed issues table
        String queryAdd =
                """
                INSERT INTO closed_issue
                (id, created_by, priority, date_created, summary, description, closed_by, date_closed)
                VALUES( ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryAdd)) {
            preparedStatement.setInt(1, issue.getId());
            preparedStatement.setInt(2, issue.getCreatedByUserId());
            preparedStatement.setInt(3, issue.getPriorityInt());
            preparedStatement.setString(4, issue.getDateCreated());
            preparedStatement.setString(5, issue.getSummary());
            preparedStatement.setString(6, issue.getDescription());
            preparedStatement.setInt(7, issue.getClosedByUserId());
            preparedStatement.setString(8, issue.getDateClosed());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
