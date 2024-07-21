package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {
    private final StringProperty email;
    private final StringProperty motDePasse;

    public Admin(String email, String motDePasse) {
        this.email = new SimpleStringProperty(email);
        this.motDePasse = new SimpleStringProperty(motDePasse);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse.get();
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse.set(motDePasse);
    }

    public StringProperty motDePasseProperty() {
        return motDePasse;
    }
}

