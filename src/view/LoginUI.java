package view;
import entity.User;
import business.UserController;
import core.Helper;

import javax.swing.*;

public class LoginUI extends JFrame {
    private JPanel container;
    private JPanel panel_top;
    private JLabel label_title;
    private JTextField Fld_eposta;
    private JTextField Fld_password;
    private JButton girişYapButton;
    private JLabel label_eposta;
    private JLabel label_password;
    private UserController userController;

    public LoginUI(){
        this.userController = new UserController();
        this.add(container);
        this.setTitle("Müşteri Yönetim Sistemi");
        this.setSize(400,400);
        this.setVisible(true);
        this.setLocationRelativeTo(null); // açılan pencere ekran ortasında olur.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //uygulamayı kapatır.

        this.girişYapButton.addActionListener(e ->  {
            JTextField[] checkList ={this.Fld_eposta , this.Fld_password};
            if(!Helper.isEmailValid(this.Fld_eposta.getText())){
                Helper.showMsj("Gecerli bir eposta adresi giriniz.");
            }

            else if(Helper.isFieldListEmpty(checkList)){
                Helper.showMsj("fill");
            }
            else{
                User user = this.userController.findByLogin(this.Fld_eposta.getText(),this.Fld_password.getText());

                if(user == null){
                    Helper.showMsj(
                            "Girdiginiz bilgilere gore kullanici bulunamadi."
                    );
                }else{
                    this.dispose();
                    DashbordUI dashbordUI = new DashbordUI(user);

                }

            }
        });
    }
}
