package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;

import javax.swing.*;

public class CustomerUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_name;
    private JTextField fld_customerName;
    private JLabel lbl_type;
    private JComboBox<Customer.TYPE> cmb_customer_type;
    private JLabel lbl_customer_phone;
    private JTextField fld_customer_phone;
    private JLabel lbl_customer_eposta;
    private JTextField fld_customer_eposta;
    private JLabel lbl_customer_address;
    private JTextArea txtArea_customer_address;
    private JButton btn_customer_save;
    private Customer customer;
    private CustomerController customerController;

    public CustomerUI(Customer customer) {
        this.customer = customer;
        this.customerController = new CustomerController();
        this.add(container);
        this.setTitle("Müşteri Ekle/Düzenle");
        this.setSize(500, 500);
        this.setVisible(true);
        this.setLocationRelativeTo(null); // açılan pencere ekran ortasında olur.

        this.cmb_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));

        if (this.customer.getId() == 0) {
            this.lbl_title.setText("Müşteri Ekle");
        } else {
            this.lbl_title.setText("Müşteri Düzenle");
            this.fld_customerName.setText(this.customer.getName());
            this.fld_customer_eposta.setText(this.customer.getMail());
            this.fld_customer_phone.setText(this.customer.getPhone());
            this.txtArea_customer_address.setText(this.customer.getAddress());
            this.cmb_customer_type.getModel().setSelectedItem(this.customer.getType());
        }

        this.btn_customer_save.addActionListener(e -> {
            JTextField[] checkList = {this.fld_customerName, this.fld_customer_phone};
            if (Helper.isFieldListEmpty(checkList)) {
                Helper.showMsj("fill");
            } else if (!Helper.isFieldEmpty(this.fld_customer_eposta) && !Helper.isEmailValid(this.fld_customer_eposta.getText())) {
                Helper.showMsj("Lütfen geçerli bir e-posta adresi giriniz.");
            } else {
                boolean result = false;
                this.customer.setName(this.fld_customerName.getText());
                this.customer.setPhone(this.fld_customer_phone.getText());
                this.customer.setMail(this.fld_customer_eposta.getText());
                this.customer.setAddress(this.txtArea_customer_address.getText()); // Düzeltildi
                this.customer.setType((Customer.TYPE) this.cmb_customer_type.getSelectedItem());

                if (this.customer.getId() == 0){
                    result = this.customerController.save(this.customer);
                }
                else{
                    result = this.customerController.update(this.customer);
                }

                if (result) {
                    Helper.showMsj("done");
                    dispose();
                } else {
                    Helper.showMsj("error");
                }
            }
        });
    }
}
