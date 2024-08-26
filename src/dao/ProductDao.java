package dao;
import core.Database;
import entity.Customer;
import entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDao {
    private Connection connection;

    public ProductDao(){
        this.connection = Database.getInstance();
    }

    public ArrayList<Product> findAll() {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";

        try (PreparedStatement pr = this.connection.prepareStatement(query);
             ResultSet rs = pr.executeQuery()) {
            while (rs.next()) {
                products.add(this.match(rs)); // Her bir müşteri kaydını Customer nesnesine dönüştürüp listeye ekler
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Hata durumunda bir RuntimeException fırlatır
        }
        return products;
    }

    public boolean save(Product product) {
        String query = "INSERT INTO product (name,code,price,stock) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);

            pr.setString(1, product.getName());
            pr.setString(2, product.getCode());
            pr.setInt(3, product.getPrice());
            pr.setInt(4, product.getStock());

            return !(pr.executeUpdate()== -1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<Product> query(String query) {
        ArrayList<Product> products = new ArrayList<>();

        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while (rs.next()){
                products.add(this.match(rs));
            }
        }catch (SQLException exception){
            exception.printStackTrace();
        }

        return products;
    }

    public Product match(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setCode(rs.getString("code"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));

        return product;
    }

    public boolean update(Product product) {
        String query = "UPDATE product SET " + "name = ? , " + "code = ? ," + "price = ? ," + "stock = ? " + "WHERE id = ?";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);

            pr.setString(1, product.getName());
            pr.setString(2, product.getCode());
            pr.setInt(3, product.getPrice());
            pr.setInt(4, product.getStock());
            pr.setInt(5, product.getId());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }


    public Product getById(int id) {
        Product product= null;
        String query = "SELECT * FROM product WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setInt(1, id); // Sorguya id parametresini ekler
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                product = this.match(rs); // Kayıt bulunduysa Customer nesnesine dönüştürür
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public boolean delete(int id) {
        String query = "DELETE FROM product WHERE id = ?";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setInt(1, id); // Sorguya id parametresini ekler
            return pr.executeUpdate() != -1; // Başarılıysa true döner
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Hata durumunda false döner
        }
    }



}
