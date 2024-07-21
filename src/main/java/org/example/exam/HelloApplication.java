package org.example.exam;

import Controller.DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private DB db;

    @Override
    public void start(Stage primaryStage) {
        db = new DB();

        if (checkDatabaseConnection()) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Login");
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur de chargement", "Impossible de charger la vue : login.fxml", e.getMessage());
            }
        } else {
            showAlert("Erreur de connexion", "Impossible de se connecter à la base de données", "Vérifiez vos paramètres de connexion.");
        }
    }

    private boolean checkDatabaseConnection() {
        try {
            db.Getconnection();
            if (db.conn != null) {
                db.closeConnection();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
