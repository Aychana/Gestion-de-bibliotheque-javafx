package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Livre {

    private final IntegerProperty id;
    private final StringProperty titre;
    private final StringProperty auteur;
    private final StringProperty isbn;
    private final StringProperty genre;

    public Livre(int id, String titre, String auteur, String isbn, String genre) {
        this.id = new SimpleIntegerProperty(id);
        this.titre = new SimpleStringProperty(titre);
        this.auteur = new SimpleStringProperty(auteur);
        this.isbn = new SimpleStringProperty(isbn);
        this.genre = new SimpleStringProperty(genre);
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public StringProperty getTitreProperty() {
        return titre;
    }

    public StringProperty getAuteurProperty() {
        return auteur;
    }

    public StringProperty getIsbnProperty() {
        return isbn;
    }

    public StringProperty getGenreProperty() {
        return genre;
    }

    public int getId() {
        return id.get();
    }

    public String getTitre() {
        return titre.get();
    }

    public String getAuteur() {
        return auteur.get();
    }

    public String getIsbn() {
        return isbn.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public void setAuteur(String auteur) {
        this.auteur.set(auteur);
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }
}
