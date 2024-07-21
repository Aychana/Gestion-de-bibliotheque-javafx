package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class User {

    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final StringProperty email;
    private final StringProperty numeroDeTelephone;

    public User(int id, String nom, String prenom, String email, String numeroDeTelephone) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.email = new SimpleStringProperty(email);
        this.numeroDeTelephone = new SimpleStringProperty(numeroDeTelephone);
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public StringProperty getNomProperty() {
        return nom;
    }

    public StringProperty getPrenomProperty() {
        return prenom;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public StringProperty getNumeroDeTelephoneProperty() {
        return numeroDeTelephone;
    }

    public int getId() {
        return id.get();
    }

    public String getNom() {
        return nom.get();
    }

    public String getPrenom() {
        return prenom.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getNumeroDeTelephone() {
        return numeroDeTelephone.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setNumeroDeTelephone(String numeroDeTelephone) {
        this.numeroDeTelephone.set(numeroDeTelephone);
    }
}
