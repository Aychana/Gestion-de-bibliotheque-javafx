package org.example.exam;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import Model.Admin;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private Admin admin;

    public LoginController() {
        // Initialiser l'admin avec des valeurs par défaut ou récupérer depuis une source de données
        this.admin = new Admin("aicha@gmail.com", "passer123");
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur de connexion", "Veuillez remplir tous les champs.");
            return;
        }

        if (email.equals(admin.getEmail()) && password.equals(admin.getMotDePasse())) {
            showAlert(AlertType.INFORMATION, "Connexion réussie", "Bienvenue, " + email + "!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) emailField.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                // Obtenir une instance du MainController et lui passer la référence à la fenêtre principale
                MainController mainController = loader.getController();
                mainController.setPrimaryStage(stage);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erreur de chargement", "Impossible de charger la vue : mainView.fxml");
            }
        } else {
            showAlert(AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
