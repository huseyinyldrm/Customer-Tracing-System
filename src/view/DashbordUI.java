package view;

import business.BasketController;
import business.CardController;
import business.CustomerController;
import business.ProductController;
import core.Helper;
import core.Item;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class DashbordUI extends JFrame {
    private JPanel container;
    private JLabel lbl_welcome;
    private JButton cikisYapButton;
    private JTabbedPane tab_menu;
    private JPanel pnl_customer;
    private JScrollPane scrl_customer;
    private JTable tbl_customer;
    private JPanel pnl_customerFilter;
    private JComboBox<Customer.TYPE> cmb_f_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JTextField lbl_filter_customer_name;
    private JLabel lbl_filter_customer_name2;
    private JLabel lbl_f_customer_type;
    private JPanel pnl_product;
    private JScrollPane scrl_product;
    private JTable tbl_product;
    private JPanel pnl_product_filter;
    private JTextField fld_f_product_name;
    private JTextField fld_f_product_code;
    private JComboBox<Item> cmb_f_product_stock;
    private JButton btn_product_filter;
    private JButton btn_product_filter_reset;
    private JButton btn_product_new;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JLabel lbl_f_product_stock;
    private JPanel pnl_basket;
    private JPanel pnl_basket_top;
    private JScrollPane scrl_basket;
    private JLabel lbl_basket_customer;
    private JComboBox<Item> cmb_basket_customer;
    private JButton btn_basket_reset;
    private JButton btn_basket_new;
    private JLabel lbl_basket_price;
    private JLabel lbl_basket_count;
    private JTable tbl_basket;
    private JScrollPane scrl_card;
    private JTable tbl_card;
    private JButton btn_card_clear;
    private User user;
    private DefaultTableModel tmdl_customer = new DefaultTableModel();
    private DefaultTableModel tmdl_product = new DefaultTableModel();
    private DefaultTableModel tmdl_basket = new DefaultTableModel();
    private DefaultTableModel tmdl_card = new DefaultTableModel();
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private CardController cardController;
    private JPopupMenu popup_customer =new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();
    private JPopupMenu popup_card  = new JPopupMenu();

    public DashbordUI(User user){
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();
        this.cardController = new CardController();

        if(user == null){
            Helper.showMsj("error");
            dispose();
        }

        this.add(container);
        this.setTitle("Müşteri Yönetim Sistemi");
        this.setSize(1000,500);
        this.setVisible(true);
        this.setLocationRelativeTo(null); // açılan pencere ekran ortasında olur.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //uygulamayı kapatır.

        this.lbl_welcome.setText("Hoşgeldin :"+this.user.getName());
        this.cikisYapButton.addActionListener(e -> {
            dispose();
            LoginUI loginUI = new LoginUI(); // tekrardan baslangic ekranina doner.
        });


        // CUSTOMER TABLE
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cmb_f_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_f_customer_type.setSelectedItem(null);

        //PRODUCT TABLE
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();
        this.cmb_f_product_stock.addItem(new Item(1,"Stokta Var."));
        this.cmb_f_product_stock.addItem(new Item(2,"Stokta Yok."));
        this.cmb_f_product_stock.setSelectedItem(null);

        //BASKET TABLE
        loadBasketTable();
        loadBasketButtonEvent();
        loadBasketCustomerCombo();

        //CARD TABLE
        loadCardTable();
        loadCardPopupMenu();
        loadCardButtonEvent();


    }
    private void loadCardButtonEvent(){
        this.btn_card_clear.addActionListener(e ->{

            if(Helper.confirm("sure")){
                if (this.cardController.clear()) {
                    Helper.showMsj("done");
                    loadCardTable();

                } else {
                    Helper.showMsj("error");
                }
            }
        });
    }

    private  void loadCardPopupMenu(){

        //seçilen ürünü tıklanınca seçme işlemi
        this.tbl_card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = tbl_card.rowAtPoint(e.getPoint());
                tbl_card.setRowSelectionInterval(selectRow, selectRow);
            }
        });

        popup_card.add("Sil").addActionListener(e -> {
            int selectId = (int) tbl_card.getValueAt(tbl_card.getSelectedRow(), 0);

            if (Helper.confirm("sure")) {
                if (this.cardController.delete(selectId)) {
                    Helper.showMsj("done");
                    loadProductTable(null);
                    loadBasketTable();
                    loadCardTable();
                } else {
                    Helper.showMsj("error");

                }
            }
        });

        this.tbl_card.setComponentPopupMenu(this.popup_card);


    }

    private void loadCardTable() {
        Object[] columnCard = {"ID", "Müşteri Adı", "Ürün Adı", "Fiyatı", "Sipariş Tarihi", "Not"};
        ArrayList<Card> cards = this.cardController.findAll();

        if (cards == null) {
            cards = this.cardController.findAll();
        }

        // tablo sıfırlama işlemi
        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_card.getModel();
        clearModel.setRowCount(0);
        this.tmdl_card.setColumnIdentifiers(columnCard);

        for (Card card : cards) {
            Object[] rowObject = {
                    card.getId(),
                    card.getCustomer().getName(),
                    card.getProduct().getName(),
                    card.getPrice(),
                    card.getDate(),
                    card.getNote()
            };
            this.tmdl_card.addRow(rowObject);
        }

        this.tbl_card.setModel(tmdl_card);
        this.tbl_card.getTableHeader().setReorderingAllowed(false);
        this.tbl_card.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_card.setEnabled(false);
    }

    private void loadBasketCustomerCombo(){
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cmb_basket_customer.removeAllItems();

        for (Customer customer : customers){
            int comboKey = customer.getId();
            String comboValue = customer.getName();
            this.cmb_basket_customer.addItem(new Item(comboKey,comboValue));
        }

        this.cmb_basket_customer.setSelectedItem(null);

    }

    public void loadBasketButtonEvent(){
        this.btn_basket_reset.addActionListener(e ->{
            if(this.basketController.clear()){
                Helper.showMsj("done");
                loadBasketTable();

            }else{
                Helper.showMsj("error");
            }
        });

        this.btn_basket_new.addActionListener(e ->{
            Item selectedCustomer = (Item) this.cmb_basket_customer.getSelectedItem();

            if(selectedCustomer == null){
                Helper.showMsj("Lütfen bir müşteri seçiniz:");
            }else{
                Customer customer = this.customerController.getById(selectedCustomer.getKey());
                ArrayList<Basket> baskets = this.basketController.findAll();


                if(customer.getId() == 0){
                    Helper.showMsj("Böyle bir müşteri bulunamadı.");
                }else if(baskets.size() == 0){
                    Helper.showMsj("Lütfen sepete ürün ekleyiniz.");
                }else{
                    CardUI cardUI = new CardUI(customer);
                    cardUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            loadBasketTable(); // güncelleme
                            loadProductTable(null);
                            loadCardTable();
                        }
                    });

                }
            }
        });

    }

    private void loadBasketTable() {
        Object[] columnBasket = {"ID", "Ürün Adı", "Ürün Kodu", "Fiyat", "Stok"};
        ArrayList<Basket> baskets = this.basketController.findAll();

        if (baskets == null) {
            baskets = this.basketController.findAll();
        }

        // tablo sıfırlama işlemi
        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_basket.getModel();
        clearModel.setRowCount(0);
        this.tmdl_basket.setColumnIdentifiers(columnBasket);

        int totalPrice = 0;
        for (Basket basket : baskets) {
            Object[] rowObject = {
                    basket.getId(),
                    basket.getProduct().getName(),
                    basket.getProduct().getCode(),
                    basket.getProduct().getPrice(),
                    basket.getProduct().getStock()
            };
            this.tmdl_basket.addRow(rowObject);
            totalPrice += basket.getProduct().getPrice();
        }
        this.lbl_basket_price.setText((totalPrice) + " TL");
        this.lbl_basket_count.setText((baskets.size()) + " Adet");

        this.tbl_basket.setModel(tmdl_basket);
        this.tbl_basket.getTableHeader().setReorderingAllowed(false);
        this.tbl_basket.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_basket.setEnabled(false);
    }

    private void loadProductPopupMenu() {

        //seçilen ürünü tıklanınca seçme işlemi
        this.tbl_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = tbl_product.rowAtPoint(e.getPoint());
                tbl_product.setRowSelectionInterval(selectRow, selectRow);
            }
        });

        this.popup_product.add("Sepete Ekle").addActionListener(e -> {
            int selectId = (int) tbl_product.getValueAt(tbl_product.getSelectedRow(), 0);
            Product basketProduct = this.productController.getById(selectId);
            if (basketProduct.getStock() <= 0) {
                Helper.showMsj("Bu ürün stokta yoktur !");
            } else {
                Basket basket = new Basket(basketProduct.getId());

                if (this.basketController.save(basket)) {
                    Helper.showMsj("done");
                    loadBasketTable();
                    loadCardTable();
                } else {
                    Helper.showMsj("error");
                }
            }
        });
        this.popup_product.add("Güncelle").addActionListener(e ->{
            int selectId = Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(),0).toString());
            ProductUI productUI = new ProductUI(this.productController.getById(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                    loadCardTable();
                }
            });
        });
        popup_product.add("Sil").addActionListener(e -> {
            int selectId = (int) tbl_product.getValueAt(tbl_product.getSelectedRow(), 0);

            if (Helper.confirm("sure")) {
                if (this.productController.delete(selectId)) {
                    Helper.showMsj("done");
                    loadProductTable(null);
                    loadBasketTable();
                    loadCardTable();
                } else {
                    Helper.showMsj("error");

                }
            }
        });

        this.tbl_product.setComponentPopupMenu(this.popup_product);
    }

    private void loadProductButtonEvent() {
        this.btn_product_new.addActionListener(e -> {
            ProductUI productUI = new ProductUI(new Product());

            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });

        this.btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filter(this.lbl_filter_customer_name.getText(), (Customer.TYPE) this.cmb_f_customer_type.getSelectedItem());

            loadCustomerTable(filteredCustomers);

        });

        btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
            loadCardTable();
            this.lbl_filter_customer_name.setText(null);
            this.cmb_f_customer_type.setSelectedItem(null);

        });

        this.btn_product_filter.addActionListener(e->{
            ArrayList<Product> filteredProducts = this.productController.filter(
                    this.fld_f_product_name.getText(),
                    this.fld_f_product_code.getText(),
                    (Item) this.cmb_f_product_stock.getSelectedItem()
            );
            loadProductTable(filteredProducts);
        });

        this.btn_product_filter_reset.addActionListener(e ->{
            this.fld_f_product_code.setText(null);
            this.fld_f_product_name.setText(null);
            this.cmb_f_product_stock.setSelectedItem(null);

            loadProductTable(null);
        });
    }

    private  void  loadProductTable(ArrayList<Product> products){
        Object[] columnProduct ={"ID","Ürün Adı","Ürün Kodu","Fiyat","Stok"};

        if(products == null){
            products=this.productController.findAll();
        }

        //tablo sifirlama işlemi
        DefaultTableModel clearModel =(DefaultTableModel) this.tbl_product.getModel();
        clearModel.setRowCount(0);
        this.tmdl_product.setColumnIdentifiers(columnProduct);
        for (Product product :products){
            Object[] rowObject ={
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock()
            };
            this.tmdl_product.addRow(rowObject); //buraya dikkat
        }
        this.tbl_product.setModel(tmdl_product);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_product.setEnabled(false);
    }

    private void loadCustomerButtonEvent() {
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());

            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();

                }
            });
        });

        //en zorlandigim yer duzeltmeye calisirken.
        this.btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filter(this.lbl_filter_customer_name.getText(), (Customer.TYPE) this.cmb_f_customer_type.getSelectedItem());

            loadCustomerTable(filteredCustomers);

        });

        btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
            this.lbl_filter_customer_name.setText(null);
            this.cmb_f_customer_type.setSelectedItem(null);

        });
    }

    private void loadCustomerPopupMenu() {

        //müşteri ekleme
        this.tbl_customer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = tbl_customer.rowAtPoint(e.getPoint());
                tbl_customer.setRowSelectionInterval(selectRow, selectRow);
            }
        });

        //müşteri güncelleme
        this.popup_customer.add("Güncelle").addActionListener(e -> {
            int selectId = (int) tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0);
            CustomerUI customerUI = new CustomerUI(this.customerController.getById(selectId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                    loadCardTable();
                }
            });
        });

        //müşteri silme
        this.popup_customer.add("Sil").addActionListener(e -> {
            int selectId = (int) tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0);
            if (Helper.confirm("sure")) {
                if (this.customerController.delete(selectId)) {
                    Helper.showMsj("done");
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();

                } else {
                    Helper.showMsj("error");
                }
            }
        });

        this.tbl_customer.setComponentPopupMenu(this.popup_customer);
    }

    private  void  loadCustomerTable(ArrayList<Customer> customers){
        Object[] columnCustomer ={"ID","Müşteri Adi","Tipi","Telefon","E-Posta","Adres"};

        if(customers == null){
            customers=this.customerController.findAll();
        }

        //tablo sifirlama işlemi
        DefaultTableModel clearModel =(DefaultTableModel) this.tbl_customer.getModel();
        clearModel.setRowCount(0);
        this.tmdl_customer.setColumnIdentifiers(columnCustomer);
        for (Customer customer :customers){
            Object[] rowObject ={
                    customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getMail(),
                    customer.getAddress()
            };
            this.tmdl_customer.addRow(rowObject);
        }
        this.tbl_customer.setModel(tmdl_customer);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_customer.setEnabled(false);
    }

}
