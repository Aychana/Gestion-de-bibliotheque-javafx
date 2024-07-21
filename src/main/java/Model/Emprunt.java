package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import java.time.LocalDate;

public class Emprunt {

    private final IntegerProperty id;
    private final IntegerProperty idLivre;
    private final IntegerProperty idUtilisateur;
    private final ObjectProperty<LocalDate> dateEmprunt;
    private final ObjectProperty<LocalDate> dateRetour;

    public Emprunt(int id, int idLivre, int idUtilisateur, LocalDate dateEmprunt, LocalDate dateRetour) {
        this.id = new SimpleIntegerProperty(id);
        this.idLivre = new SimpleIntegerProperty(idLivre);
        this.idUtilisateur = new SimpleIntegerProperty(idUtilisateur);
        this.dateEmprunt = new SimpleObjectProperty<>(dateEmprunt);
        this.dateRetour = new SimpleObjectProperty<>(dateRetour);
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public IntegerProperty getIdLivreProperty() {
        return idLivre;
    }

    public IntegerProperty getIdUtilisateurProperty() {
        return idUtilisateur;
    }

    public ObjectProperty<LocalDate> getDateEmpruntProperty() {
        return dateEmprunt;
    }

    public ObjectProperty<LocalDate> getDateRetourProperty() {
        return dateRetour;
    }

    public int getId() {
        return id.get();
    }

    public int getIdLivre() {
        return idLivre.get();
    }

    public int getIdUtilisateur() {
        return idUtilisateur.get();
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt.get();
    }

    public LocalDate getDateRetour() {
        return dateRetour.get();
    }

    // Ajout des setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setIdLivre(int idLivre) {
        this.idLivre.set(idLivre);
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur.set(idUtilisateur);
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt.set(dateEmprunt);
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour.set(dateRetour);
    }
}
