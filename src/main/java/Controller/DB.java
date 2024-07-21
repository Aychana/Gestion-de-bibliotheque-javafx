package Controller;

import java.sql.*;
import java.time.LocalDate;

public class DB {
    // pour la connexion
    public Connection conn = null;
    // pour les resultats des requetes de type Select
    private ResultSet rs;
    // pour les requetes preparees
    private PreparedStatement pstm;
    // pour les resultats de type mise a jour(INSERT,UPDATE,DELETE)
    private int ok;

    public void Getconnection() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/dbexam";
            String user = "root";
            String password = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("connexion réussie");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void initPrepare(String sql) {
        try {
            Getconnection();
            pstm = conn.prepareStatement(sql);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public ResultSet execeutSelect() {
        rs = null;
        try {
            rs = pstm.executeQuery();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return rs;
    }

    public int executeMaj() {
        try {
            ok = pstm.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ok;
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public PreparedStatement getPstm() {
        return pstm;
    }

    public void setPstm(PreparedStatement pstm) {
        this.pstm = pstm;
    }

    // Méthode pour ajouter un utilisateur
    public void ajouterUtilisateur(String nom, String prenom, String email, String numeroDeTelephone) {
        String sql = "INSERT INTO user (nom, prenom, email, numeroDeTelephone) VALUES (?, ?, ?, ?)";
        try {
            initPrepare(sql);
            pstm.setString(1, nom);
            pstm.setString(2, prenom);
            pstm.setString(3, email);
            pstm.setString(4, numeroDeTelephone);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour modifier un utilisateur
    public void modifierUtilisateur(int id, String nom, String prenom, String email, String numeroDeTelephone) {
        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, numeroDeTelephone = ? WHERE id = ?";
        try {
            initPrepare(sql);
            pstm.setString(1, nom);
            pstm.setString(2, prenom);
            pstm.setString(3, email);
            pstm.setString(4, numeroDeTelephone);
            pstm.setInt(5, id);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour supprimer un utilisateur
    public void supprimerUtilisateur(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try {
            initPrepare(sql);
            pstm.setInt(1, id);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour ajouter un livre
    public void ajouterLivre(String titre, String auteur, String isbn, String genre) {
        String sql = "INSERT INTO livre (titre, auteur, isbn, genre) VALUES (?, ?, ?, ?)";
        try {
            initPrepare(sql);
            pstm.setString(1, titre);
            pstm.setString(2, auteur);
            pstm.setString(3, isbn);
            pstm.setString(4, genre);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour modifier un livre
    public void modifierLivre(int id, String titre, String auteur, String isbn, String genre) {
        String sql = "UPDATE livre SET titre = ?, auteur = ?, isbn = ?, genre = ? WHERE id = ?";
        try {
            initPrepare(sql);
            pstm.setString(1, titre);
            pstm.setString(2, auteur);
            pstm.setString(3, isbn);
            pstm.setString(4, genre);
            pstm.setInt(5, id);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour supprimer un livre
    public void supprimerLivre(int id) {
        String sql = "DELETE FROM livre WHERE id = ?";
        try {
            initPrepare(sql);
            pstm.setInt(1, id);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour ajouter un emprunt
    public void ajouterEmprunt(int idLivre, int idUtilisateur, LocalDate dateEmprunt, LocalDate dateRetour) {
        String sql = "INSERT INTO emprunt (id_livre, id_user, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
        try {
            initPrepare(sql);
            pstm.setInt(1, idLivre);
            pstm.setInt(2, idUtilisateur);
            pstm.setDate(3, Date.valueOf(dateEmprunt));
            pstm.setDate(4, Date.valueOf(dateRetour));
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour modifier un emprunt
    public void modifierEmprunt(int id, int idLivre, int idUtilisateur, LocalDate dateEmprunt, LocalDate dateRetour) {
        String sql = "UPDATE emprunt SET id_livre = ?, id_user = ?, date_emprunt = ?, date_retour = ? WHERE id = ?";
        try {
            initPrepare(sql);
            pstm.setInt(1, idLivre);
            pstm.setInt(2, idUtilisateur);
            pstm.setDate(3, Date.valueOf(dateEmprunt));
            pstm.setDate(4, Date.valueOf(dateRetour));
            pstm.setInt(5, id);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Méthode pour supprimer un emprunt
    public void supprimerEmprunt(int id) {
        String sql = "DELETE FROM emprunt WHERE id = ?";
        try {
            initPrepare(sql);
            pstm.setInt(1, id);
            executeMaj();
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public boolean isLivreDisponible(int livreId, LocalDate dateEmprunt) {
        String sql = "SELECT date_retour FROM emprunt WHERE id_livre = ?";
        try {
            initPrepare(sql);
            getPstm().setInt(1, livreId);
            ResultSet rs = execeutSelect();
            while (rs.next()) {
                LocalDate dateRetour = rs.getDate("date_retour").toLocalDate();
                if (dateEmprunt.isBefore(dateRetour)) {
                    return false; // Le livre n'est pas disponible si la date d'emprunt est avant la date de retour
                }
            }
            closeConnection();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true; // Le livre est disponible si aucune date de retour n'est postérieure à la date d'emprunt
    }



}
