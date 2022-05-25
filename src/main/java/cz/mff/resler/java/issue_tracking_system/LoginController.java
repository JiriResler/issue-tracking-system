package cz.mff.resler.java.issue_tracking_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class for handling login input.
 */
public class LoginController implements Initializable {
    private final static UserModel userModel = new UserModel();

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    /**
     * Initializes the LoginController after loading its scene.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//      Check if connection was established with the database
        if (UserModel.connection == null) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Connection to the database could not established.",
                    ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR");
            alert.showAndWait();
            System.exit(1);
        }
    }

    /**
     * Checks the given credentials from the login scene and loads the dashboard scene.
     *
     * @param event source event.
     */
    public void logInButtonClicked(ActionEvent event) {
        try {
            if (userModel.loginExists(loginUsername.getText(), loginPassword.getText())) {
//              Go to dashboard
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(Objects.requireNonNull(getClass().getResource("dashboard_scene.fxml")).openStream());
                DashboardController dashboardController = loader.getController();

//              Pass name of logged-in user
                dashboardController.loggedInUsername = loginUsername.getText();
                dashboardController.usernameLabel.setText("Logged in as:  " + loginUsername.getText());
                dashboardController.refreshOpenIssuesTable();
                dashboardController.refreshClosedIssuesTable();

                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "The given credentials are wrong.", ButtonType.CLOSE);
                alert.setTitle("Wrong credentials");
                alert.setHeaderText(null);
                alert.showAndWait();
                loginPassword.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the create account scene.
     *
     * @param event source event.
     */
    public void signUpButtonClicked(ActionEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("create_account_scene.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
