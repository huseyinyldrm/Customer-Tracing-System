package dao;
import business.ProductController;
import core.Database;
import entity.Card;
import entity.Customer;

import java.sql.Date; // sql ile tarih uyumu sağlandi.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CardDao {
    private Connection connection;
    private ProductDao productDao;
    private CustomerDao customerDao;


    public CardDao(){
        this.connection = Database.getInstance();
        this.productDao = new ProductDao();
        this.customerDao = new CustomerDao();
    }

    public ArrayList<Card> findAll() {
        ArrayList<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM card";

        try (PreparedStatement pr = this.connection.prepareStatement(query);
             ResultSet rs = pr.executeQuery()) {
            while (rs.next()) {
                cards.add(this.match(rs)); // Her bir müşteri kaydını Customer nesnesine dönüştürüp listeye ekler
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Hata durumunda bir RuntimeException fırlatır
        }
        return cards;
    }

    public Card match(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId(rs.getInt("id"));
        card.setPrice(rs.getInt("price"));
        card.setCustomerId(rs.getInt("customer_id"));
        card.setProductId(rs.getInt("product_id"));
        card.setNote(rs.getString("note"));
        card.setDate(LocalDate.parse(rs.getString("date")));
        card.setCustomer(this.customerDao.getById(card.getCustomerId()));
        card.setProduct(this.productDao.getById(card.getProductId()));

        return card;
    }
    public boolean save(Card card) {
        String query = "INSERT INTO card (customer_id,product_id,price,date,note) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            // Parametreleri sorguya yerleştirir
            pr.setInt(1, card.getCustomerId());
            pr.setInt(2, card.getProductId());
            pr.setInt(3, card.getPrice());
            pr.setDate(4, Date.valueOf(card.getDate())); // Çok onemli bir yer.
            pr.setString(5, card.getNote());

            return !(pr.executeUpdate() == -1); // Başarılıysa true döner
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döner
        }
    }

    public Card  getById(int id) {
        Card card = null;
        String query = "SELECT * FROM card WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setInt(1, id); // Sorguya id parametresini ekler
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                card = this.match(rs); // Kayıt bulunduysa Customer nesnesine dönüştürür
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public boolean delete(int id) {
        String query = "DELETE FROM card WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setInt(1, id); // Sorguya id parametresini ekler
            return pr.executeUpdate() != -1; // Başarılıysa true döner
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döner
        }
    }


    public boolean clear() {
        String query = "DELETE FROM card";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }






}
