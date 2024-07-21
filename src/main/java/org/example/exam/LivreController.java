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
import Model.Livre;
import Controller.DB;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class LivreController {

    private Stage primaryStage;

    @FXML
    private TextField titreField;
    @FXML
    private TextField auteurField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField genreField;
    @FXML
    private TableView<Livre> livreTable;
    @FXML
    private TableColumn<Livre, Integer> idColumn;
    @FXML
    private TableColumn<Livre, String> titreColumn;
    @FXML
    private TableColumn<Livre, String> auteurColumn;
    @FXML
    private TableColumn<Livre, String> isbnColumn;
    @FXML
    private TableColumn<Livre, String> genreColumn;

    private ObservableList<Livre> livreList = FXCollections.observableArrayList();
    private DB db = new DB();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        titreColumn.setCellValueFactory(cellData -> cellData.getValue().getTitreProperty());
        auteurColumn.setCellValueFactory(cellData -> cellData.getValue().getAuteurProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().getIsbnProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().getGenreProperty());

        livreTable.setItems(livreList);
        loadLivresFromDatabase();

        livreTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                handleRowSelect();
            }
        });
    }

    private void loadLivresFromDatabase() {
        String sql = "SELECT * FROM livre";
        try {
            db.initPrepare(sql);
            ResultSet rs = db.execeutSelect();
            while (rs.next()) {
                Livre livre = new Livre(rs.getInt("id"), rs.getString("titre"), rs.getString("auteur"), rs.getString("isbn"), rs.getString("genre"));
                livreList.add(livre);
            }
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void handleAddLivre() {
        String titre = titreField.getText();
        String auteur = auteurField.getText();
        String isbn = isbnField.getText();
        String genre = genreField.getText();

        if (titre.isEmpty() || auteur.isEmpty() || isbn.isEmpty() || genre.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Générer un ID unique pour le livre (par exemple, en utilisant la taille de la liste)
        int id = livreList.size() + 1;
        Livre livre = new Livre(id, titre, auteur, isbn, genre);
        livreList.add(livre);

        // Ajouter le livre à la base de données
        db.ajouterLivre(titre, auteur, isbn, genre);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Livre ajouté avec succès.");

        // Effacer les champs après l'ajout
        clearFields();
    }

    @FXML
    private void handleUpdateLivre() {
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();
        if (selectedLivre != null) {
            selectedLivre.setTitre(titreField.getText());
            selectedLivre.setAuteur(auteurField.getText());
            selectedLivre.setIsbn(isbnField.getText());
            selectedLivre.setGenre(genreField.getText());

            // Mettre à jour le livre dans la base de données
            db.modifierLivre(selectedLivre.getId(), selectedLivre.getTitre(), selectedLivre.getAuteur(), selectedLivre.getIsbn(), selectedLivre.getGenre());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Livre modifié avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un livre à modifier.");
        }
    }

    @FXML
    private void handleDeleteLivre() {
        int selectedIndex = livreTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Livre selectedLivre = livreTable.getItems().remove(selectedIndex);

            // Supprimer le livre de la base de données
            db.supprimerLivre(selectedLivre.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Livre supprimé avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un livre à supprimer.");
        }
    }

    private void handleRowSelect() {
        Livre selectedLivre = livreTable.getSelectionModel().getSelectedItem();
        if (selectedLivre != null) {
            titreField.setText(selectedLivre.getTitre());
            auteurField.setText(selectedLivre.getAuteur());
            isbnField.setText(selectedLivre.getIsbn());
            genreField.setText(selectedLivre.getGenre());
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
    private void handleUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            UserController userController = loader.getController();
            userController.setPrimaryStage(primaryStage);
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


    // Ajout de la méthode handleClear
    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        titreField.clear();
        auteurField.clear();
        isbnField.clear();
        genreField.clear();
    }

}

