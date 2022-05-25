package cz.mff.resler.java.issue_tracking_system.controller;

import cz.mff.resler.java.issue_tracking_system.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class for handling the creation of a new account.
 */
public class CreateAccountController implements Initializable {
    private final static UserModel userModel = new UserModel();


    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField repeatPasswordTextField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    /**
     * Initializes the LoginController after loading its scene.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleChoiceBox.getItems().add("Project manager");
        roleChoiceBox.getItems().add("Team leader");
        roleChoiceBox.getItems().add("Programmer or tester");
        roleChoiceBox.getItems().add("User");
    }

    /**
     * Checks the given credentials from the create account scene and loads the dashboard scene.
     *
     * @param event source event.
     */
    public void createAccountButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String passwordRepeat = repeatPasswordTextField.getText();
        String role = roleChoiceBox.getValue();

//      Check correctness of the input values
        if (newAccountValuesAreCorrect(username, password, passwordRepeat, role)) {
            int roleInt = switch (role) {
                case "Project manager" -> 4;
                case "Team leader" -> 3;
                case "Programmer or tester" -> 2;
                case "User" -> 1;
                default -> throw new IllegalStateException("Unexpected value: " + role);
            };

            try {
                userModel.addNewUser(username, password, roleInt);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Account successfully created.", ButtonType.OK);
                alert.setTitle("Account created");
                alert.setHeaderText(null);
                alert.showAndWait();

//              Go to dashboard
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(Objects.requireNonNull(getClass().getResource("dashboard_scene.fxml")).openStream());
                DashboardController dashboardController = loader.getController();
//              Pass name of logged-in user
                dashboardController.getUsernameLabel().setText("Logged in as:  " + usernameTextField.getText());
                dashboardController.setLoggedInUsername(usernameTextField.getText());
                dashboardController.refreshOpenIssuesTable();
                dashboardController.refreshClosedIssuesTable();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks the given input whether it meets new account criteria.
     *
     * @param username       has to be unique.
     * @param password       no restrictions.
     * @param passwordRepeat has to be exactly the same as the first password.
     * @param role           cannot be null.
     * @return true if the given input values are valid.
     */
    public boolean newAccountValuesAreCorrect(String username, String password, String passwordRepeat, String role) {
//      Check for empty input fields
        if (username.equals("") || password.equals("") || passwordRepeat.equals("") || role == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields are mandatory.", ButtonType.OK);
            alert.setTitle("Empty field");
            alert.setHeaderText(null);
            alert.showAndWait();
            return false;
        }

//      Check whether the username is already existing in the database
        if (userModel.usernameExists(username)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "A user with this username already exists.", ButtonType.OK);
            alert.setTitle("Username already taken");
            alert.setHeaderText(null);
            alert.showAndWait();
            return false;
        }


//      Check whether the two passwords match
        if (!password.equals(passwordRepeat)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The given two passwords do not match.", ButtonType.OK);
            alert.setTitle("Passwords do not match");
            alert.setHeaderText(null);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Loads the login scene.
     *
     * @param event source event.
     */
    public void backButtonClicked(ActionEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("log_in_scene.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
