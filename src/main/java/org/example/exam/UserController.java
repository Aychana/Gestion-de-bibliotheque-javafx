package org.example.exam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import Model.User;
import Controller.DB;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class UserController {

    private Stage primaryStage;

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numeroDeTelephoneField;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> nomColumn;
    @FXML
    private TableColumn<User, String> prenomColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> numeroDeTelephoneColumn;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private DB db = new DB();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().getPrenomProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
        numeroDeTelephoneColumn.setCellValueFactory(cellData -> cellData.getValue().getNumeroDeTelephoneProperty());

        userTable.setItems(userList);
        loadUsersFromDatabase();

        userTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                handleRowSelect();
            }
        });
    }

    private void loadUsersFromDatabase() {
        String sql = "SELECT * FROM user";
        try {
            db.initPrepare(sql);
            ResultSet rs = db.execeutSelect();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getString("numeroDeTelephone"));
                userList.add(user);
            }
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Correction du bouton "Ajouter utilisateur"
    @FXML
    private void handleAddUser() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String numeroDeTelephone = numeroDeTelephoneField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numeroDeTelephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Générer un ID unique pour l'utilisateur (par exemple, en utilisant la taille de la liste)
        int id = userList.size() + 1;
        User user = new User(id, nom, prenom, email, numeroDeTelephone);
        userList.add(user);

        // Ajouter l'utilisateur à la base de données
        db.ajouterUtilisateur(nom, prenom, email, numeroDeTelephone);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès.");

        // Effacer les champs après l'ajout
        clearFields();
    }
    // Ajout de la méthode handleClear
    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        numeroDeTelephoneField.clear();
    }
    // Ajout de la méthode handleBackToMain
//    @FXML
//    private void handleBackToMain() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainView.fxml"));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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



    @FXML
    private void handleUpdateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setNom(nomField.getText());
            selectedUser.setPrenom(prenomField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setNumeroDeTelephone(numeroDeTelephoneField.getText());

            // Mettre à jour l'utilisateur dans la base de données
            db.modifierUtilisateur(selectedUser.getId(), selectedUser.getNom(), selectedUser.getPrenom(), selectedUser.getEmail(), selectedUser.getNumeroDeTelephone());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur modifié avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    @FXML
    private void handleDeleteUser() {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = userTable.getItems().remove(selectedIndex);

            // Supprimer l'utilisateur de la base de données
            db.supprimerUtilisateur(selectedUser.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private void handleRowSelect() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            nomField.setText(selectedUser.getNom());
            prenomField.setText(selectedUser.getPrenom());
            emailField.setText(selectedUser.getEmail());
            numeroDeTelephoneField.setText(selectedUser.getNumeroDeTelephone());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Méthodes pour la navigation entre les vues
    @FXML
    private void handleBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("livreView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            LivreController livreController = loader.getController();
            livreController.setPrimaryStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoans() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("empruntView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            EmpruntController empruntController = loader.getController();
            empruntController.setPrimaryStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
