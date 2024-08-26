package dao;

import core.Database;
import entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao {
    private Connection connection;

    // Constructor - Veritabanı bağlantısını alır
    public CustomerDao() {
        this.connection = Database.getInstance();
    }

    // Tüm müşterileri veritabanından çeken metod
    public ArrayList<Customer> findAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";

        try (PreparedStatement pr = this.connection.prepareStatement(query);
             ResultSet rs = pr.executeQuery()) {
            while (rs.next()) {
                customers.add(this.match(rs)); // Her bir müşteri kaydını Customer nesnesine dönüştürüp listeye ekler
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Hata durumunda bir RuntimeException fırlatır
        }
        return customers;
    }

    // Yeni bir müşteri kaydını veritabanına ekleyen metod
    public boolean save(Customer customer) {
        String query = "INSERT INTO customer (name, type, phone, mail, address) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            // Parametreleri sorguya yerleştirir
            pr.setString(1, customer.getName());
            pr.setString(2, customer.getType().toString());
            pr.setString(3, customer.getPhone());
            pr.setString(4, customer.getMail());
            pr.setString(5, customer.getAddress());

            return !(pr.executeUpdate() == -1); // Başarılıysa true döner
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döner
        }
    }

    // Belirli bir id'ye sahip müşteriyi bulan metod
    public Customer getById(int id) {
        Customer customer = null;
        String query = "SELECT * FROM customer WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setInt(1, id); // Sorguya id parametresini ekler
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                customer = this.match(rs); // Kayıt bulunduysa Customer nesnesine dönüştürür
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    // Mevcut bir müşteri kaydını güncelleyen metod
    public boolean update(Customer customer) {
        String query = "UPDATE customer SET name = ?, type = ?, phone = ?, mail = ?, address = ? WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            // Parametreleri sorguya yerleştirir
            pr.setString(1, customer.getName());
            pr.setString(2, customer.getType().toString());
            pr.setString(3, customer.getPhone());
            pr.setString(4, customer.getMail());
            pr.setString(5, customer.getAddress());
            pr.setInt(6, customer.getId());

            return pr.executeUpdate() != -1; // Başarılıysa true döner
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döner
        }
    }

    // Belirli bir id'ye sahip müşteriyi silen metod
    public boolean delete(int id) {
        String query = "DELETE FROM customer WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setInt(1, id); // Sorguya id parametresini ekler
            return pr.executeUpdate() != -1; // Başarılıysa true döner
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döner
        }
    }

    // Özelleştirilmiş bir sorgu ile müşteri listesini döndüren metod
    public ArrayList<Customer> query(String query) {
        ArrayList<Customer> customers = new ArrayList<>();

        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while (rs.next()){
                customers.add(this.match(rs));
            }
        }catch (SQLException exception){
            exception.printStackTrace();
        }

        return customers;
    }

    // ResultSet'ten Customer nesnesine dönüşüm yapan yardımcı metod
    public Customer match(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name")); // getString metodu kullanılır
        customer.setType(Customer.TYPE.valueOf(rs.getString("type"))); // String türünü enum'a çevirir
        customer.setPhone(rs.getString("phone"));
        customer.setMail(rs.getString("mail"));
        customer.setAddress(rs.getString("address"));

        return customer;
    }
}
//Eğer id ile ilgili bir sıkıntı cikarsa bu kodu mysql -> customer'de yaz ve kaydet.

//ALTER TABLE customer MODIFY COLUMN id INT AUTO_INCREMENT;

