package entity;
import java.time.LocalDate;

public class Card {
    private int id;

    private  int customerId;
    private int productId;
    private int price;
    private String note;
    private LocalDate date;
    private Customer customer;
    private Product product;

    public  Card(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int pricce) {
        this.price = pricce;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", price=" + price +
                ", note='" + note + '\'' +
                ", date=" + date +
                ", customer=" + customer +
                ", product=" + product +
                '}';
    }
}
