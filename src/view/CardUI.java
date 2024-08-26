package view;

import business.BasketController;
import business.CardController;
import business.ProductController;
import core.Helper;
import entity.Basket;
import entity.Card;
import entity.Customer;
import entity.Product;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CardUI extends JFrame{
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_customer_name;
    private JTextArea tarea_card_note;
    private JButton btn_card;
    private JLabel lbl_card_date;
    private JTextField fld_card_date;
    private JLabel lbl_card_note;
    private Customer customer;
    private BasketController basketController;
    private CardController cardController;
    private ProductController productController;
    public CardUI(Customer customer){
        this.customer = customer;
        this.basketController = new BasketController();
        this.cardController = new CardController();
        this.productController = new ProductController();

        this.add(container);
        this.setTitle("Sipariş Oluştur");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null); // açılan pencere ekran ortasında olur.
        this.setVisible(true);

        if(customer.getId() == 0){
            Helper.showMsj("Lütfen geçerli bir müşteri giriniz.");
            dispose();
        }

        ArrayList<Basket> baskets = this.basketController.findAll();
        if(baskets.size() == 0){
            Helper.showMsj("Lütfen sepete ürün ekleyiniz.");
            dispose();
        }

        this.lbl_customer_name.setText("Müşteri : "+ this.customer.getName());

        btn_card.addActionListener(e ->{
            if(Helper.isFieldEmpty(this.fld_card_date)){
                Helper.showMsj("fill");
            }else{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for(Basket basket : baskets){

                    if(basket.getProduct().getStock()<= 0) continue;

                    Card card =new Card();
                    card.setCustomerId(this.customer.getId());
                    card.setProductId(basket.getProductId());
                    card.setPrice(basket.getProduct().getPrice());
                    card.setDate(LocalDate.parse(this.fld_card_date.getText(), formatter));                    card.setNote(this.tarea_card_note.getText());
                    this.cardController.save(card);

                    //burasi onemli
                    Product unStockProduct = basket.getProduct();
                    unStockProduct.setStock(unStockProduct.getStock()-1);
                    this.productController.update(unStockProduct);
                }
                this.basketController.clear();
                Helper.showMsj("done");
                dispose();
                System.out.println("Sipariş oluşturuldu.");
            }
        });

    }

    private void createUIComponents() throws ParseException {
        this.fld_card_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fld_card_date.setText(formatter.format(LocalDate.now()));
    }
}
