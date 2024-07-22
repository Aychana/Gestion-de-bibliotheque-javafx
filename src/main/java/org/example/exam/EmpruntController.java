package org.example.exam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import Model.Emprunt;
import Model.User;
import Model.Livre;
import Controller.DB;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.time.LocalDate;

public class EmpruntController {

    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<Livre> livreComboBox;
    @FXML
    private DatePicker dateEmpruntPicker;
    @FXML
    private DatePicker dateRetourPicker;
    @FXML
    private TableView<Emprunt> empruntTable;
    @FXML
    private TableColumn<Emprunt, Integer> idColumn;
    @FXML
    private TableColumn<Emprunt, Integer> idLivreColumn;
    @FXML
    private TableColumn<Emprunt, Integer> idUtilisateurColumn;
    @FXML
    private TableColumn<Emprunt, LocalDate> dateEmpruntColumn;
    @FXML
    private TableColumn<Emprunt, LocalDate> dateRetourColumn;

    private Stage primaryStage;
    private ObservableList<Emprunt> empruntList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Livre> livreList = FXCollections.observableArrayList();
    private DB db = new DB();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        idLivreColumn.setCellValueFactory(cellData -> cellData.getValue().getIdLivreProperty().asObject());
        idUtilisateurColumn.setCellValueFactory(cellData -> cellData.getValue().getIdUtilisateurProperty().asObject());
        // Personnaliser l'affichage des colonnes
        idUtilisateurColumn.setCellFactory(column -> new TableCell<Emprunt, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    User user = userList.stream().filter(u -> u.getId() == item).findFirst().orElse(null);
                    setText(user != null ? user.getNom() + " " + user.getPrenom() : "Inconnu");
                }
            }
        });

        idLivreColumn.setCellFactory(column -> new TableCell<Emprunt, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Livre livre = livreList.stream().filter(l -> l.getId() == item).findFirst().orElse(null);
                    setText(livre != null ? livre.getTitre() : "Inconnu");
                }
            }
        });
        // Personnaliser l'affichage des utilisateurs
        userComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getNom() + " " + user.getPrenom());
                }
            }
        });
        userComboBox.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getNom() + " " + user.getPrenom());
                }
            }
        });

// Personnaliser l'affichage des livres
        livreComboBox.setCellFactory(lv -> new ListCell<Livre>() {
            @Override
            protected void updateItem(Livre livre, boolean empty) {
                super.updateItem(livre, empty);
                if (empty || livre == null) {
                    setText(null);
                } else {
                    setText(livre.getTitre());
                }
            }
        });
        livreComboBox.setButtonCell(new ListCell<Livre>() {
            @Override
            protected void updateItem(Livre livre, boolean empty) {
                super.updateItem(livre, empty);
                if (empty || livre == null) {
                    setText(null);
                } else {
                    setText(livre.getTitre());
                }
            }
        });

        dateEmpruntColumn.setCellValueFactory(cellData -> cellData.getValue().getDateEmpruntProperty());
        dateRetourColumn.setCellValueFactory(cellData -> cellData.getValue().getDateRetourProperty());

        empruntTable.setItems(empruntList);
        loadUsersFromDatabase();
        loadLivresFromDatabase();
        loadEmpruntsFromDatabase();

        empruntTable.setOnMouseClicked((MouseEvent event) -> {
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
            userComboBox.setItems(userList);
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
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
            livreComboBox.setItems(livreList);
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void loadEmpruntsFromDatabase() {
        String sql = "SELECT * FROM emprunt";
        try {
            db.initPrepare(sql);
            ResultSet rs = db.execeutSelect();
            while (rs.next()) {
                Emprunt emprunt = new Emprunt(rs.getInt("id"), rs.getInt("id_livre"), rs.getInt("id_user"), rs.getDate("date_emprunt").toLocalDate(), rs.getDate("date_retour").toLocalDate());
                empruntList.add(emprunt);
            }
            db.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void handleAddEmprunt() {
        User selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        Livre selectedLivre = livreComboBox.getSelectionModel().getSelectedItem();
        LocalDate dateEmprunt = dateEmpruntPicker.getValue();
        LocalDate dateRetour = dateRetourPicker.getValue();

        if (selectedUser == null || selectedLivre == null || dateEmprunt == null || dateRetour == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (dateRetour.isBefore(dateEmprunt)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de retour ne peut pas être antérieure à la date d'emprunt.");
            return;
        }

        if (dateRetour.isAfter(dateEmprunt.plusDays(15))) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de retour doit être au maximum 15 jours après la date d'emprunt.");
            return;
        }

        // Vérifier si le livre est disponible à la date d'emprunt souhaitée
        if (!db.isLivreDisponible(selectedLivre.getId(), dateEmprunt)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Ce livre est déjà emprunté à cette date.");
            return;
        }

        // Générer un ID unique pour l'emprunt (par exemple, en utilisant la taille de la liste)
        int id = empruntList.size() + 1;
        Emprunt emprunt = new Emprunt(id, selectedLivre.getId(), selectedUser.getId(), dateEmprunt, dateRetour);
        empruntList.add(emprunt);

        // Ajouter l'emprunt à la base de données
        db.ajouterEmprunt(selectedLivre.getId(), selectedUser.getId(), dateEmprunt, dateRetour);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Emprunt ajouté avec succès.");

        // Effacer les champs après l'ajout
        clearFields();
    }




    @FXML
    private void handleUpdateEmprunt() {
        Emprunt selectedEmprunt = empruntTable.getSelectionModel().getSelectedItem();
        if (selectedEmprunt != null) {
            selectedEmprunt.setIdLivre(livreComboBox.getSelectionModel().getSelectedItem().getId());
            selectedEmprunt.setIdUtilisateur(userComboBox.getSelectionModel().getSelectedItem().getId());
            selectedEmprunt.setDateEmprunt(dateEmpruntPicker.getValue());
            selectedEmprunt.setDateRetour(dateRetourPicker.getValue());

            // Mettre à jour l'emprunt dans la base de données
            db.modifierEmprunt(selectedEmprunt.getId(), selectedEmprunt.getIdLivre(), selectedEmprunt.getIdUtilisateur(), selectedEmprunt.getDateEmprunt(), selectedEmprunt.getDateRetour());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Emprunt modifié avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un emprunt à modifier.");
        }
    }

    @FXML
    private void handleDeleteEmprunt() {
        int selectedIndex = empruntTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Emprunt selectedEmprunt = empruntTable.getItems().remove(selectedIndex);

            // Supprimer l'emprunt de la base de données
            db.supprimerEmprunt(selectedEmprunt.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Emprunt supprimé avec succès.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un emprunt à supprimer.");
        }
    }

    private void handleRowSelect() {
        Emprunt selectedEmprunt = empruntTable.getSelectionModel().getSelectedItem();
        if (selectedEmprunt != null) {
            userComboBox.getSelectionModel().select(getUserById(selectedEmprunt.getIdUtilisateur()));
            livreComboBox.getSelectionModel().select(getLivreById(selectedEmprunt.getIdLivre()));
            dateEmpruntPicker.setValue(selectedEmprunt.getDateEmprunt());
            dateRetourPicker.setValue(selectedEmprunt.getDateRetour());
        }
    }

    private User getUserById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    private Livre getLivreById(int id) {
        for (Livre livre : livreList) {
            if (livre.getId() == id) {
                return livre;
            }
        }
        return null;
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

    // Ajout de la méthode handleClear
    @FXML
    private void handleClear() {
        clearFields();
    }

    private void clearFields() {
        userComboBox.getSelectionModel().clearSelection();
        livreComboBox.getSelectionModel().clearSelection();
        dateEmpruntPicker.setValue(null);
        dateRetourPicker.setValue(null);
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

}
