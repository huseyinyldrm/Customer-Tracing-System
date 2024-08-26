package core;

import javax.swing.*;

public class Helper {
    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals(("Nimbus"))) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fields) {
        for (JTextField field : fields) {
            if (isFieldEmpty(field)) return true;
        }
        return false;
    }

    public static boolean isEmailValid(String mail) {

        if (mail == null || mail.trim().isEmpty()) return false;

        if (!mail.contains("@")) return false;

        String[] parts = mail.split("@");
        if (parts.length != 2) return false;

        if (parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) return false;

        if (!parts[1].contains(".")) return false;

        return true;
    }

    public static void optioPanelDialogTr() {
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }

    public static void showMsj(String message) {
        String msg;
        String title = switch (message) {
            case "fill" -> {
                msg = "Lutfen tum alanlari doldurunuz.";
                yield "HATA!";
            }
            case "done" -> {
                msg = "Islem basarili";
                yield "Sonuc";
            }
            case "error" -> {
                msg = "Bir hata olustu.";
                yield "HATA!";
            }
            default -> {
                msg = message;
                yield "mesaj";
            }
        };
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        optioPanelDialogTr();
        String msg;

        if (str.equals("sure")) {
            msg = "Bu işlemi Gerçekleştirmek İstediğinize Emin Misiniz?";
        } else {
            msg = str;
        }
        return JOptionPane.showConfirmDialog(null, msg, "Emin Misin ?", JOptionPane.YES_NO_OPTION) == 0;
    }
}
