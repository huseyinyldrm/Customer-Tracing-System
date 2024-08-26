import business.UserController;
import core.Database;
import core.Helper;
import entity.User;
import view.DashbordUI;
import view.LoginUI;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Helper.setTheme();
        LoginUI loginUI = new LoginUI();

/*
        UserController userController = new UserController();
        User user = userController.findByLogin("mustafa@patica.dev","123123");
        DashbordUI dashbordUI = new DashbordUI(user);

*/
    }
}