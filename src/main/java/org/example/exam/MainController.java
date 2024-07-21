package org.example.exam;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import Controller.DB;
import javafx.stage.Stage;

import java.sql.ResultSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainController {
    private Stage primaryStage;

    @FXML
    private Label userCountLabel;
    @FXML
    private Label bookCountLabel;
    @FXML
    private Label loanCountLabel;

    private DB db = new DB();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    private void initialize() {
        updateStatistics();
    }

    private void updateStatistics() {
        userCountLabel.setText(String.valueOf(getCount("user")));
        bookCountLabel.setText(String.valueOf(getCount("livre")));
        loanCountLabel.setText(String.valueOf(getCount("emprunt")));
    }

    private int getCount(String tableName) {
        String sql = "SELECT COUNT(*) AS count FROM " + tableName;
        int count = 0;
        try {
            db.initPrepare(sql);
            ResultSet rs = db.execeutSelect();
            if (rs.next()) {
                count = rs.getInt("count");
            }
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return count;
    }



    @FXML
    private void handleUsers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBooks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("livreView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoans(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empruntView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBackToMain(ActionEvent event) {
        try {
            // Fermer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Charger et afficher la nouvelle scène
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainView.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et initialiser la fenêtre principale
            MainController mainController = loader.getController();
            mainController.setPrimaryStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

        // Affichez les détails de l'exception dans la console
        if (alertType == Alert.AlertType.ERROR) {
            System.err.println(content);
        }
    }
}
