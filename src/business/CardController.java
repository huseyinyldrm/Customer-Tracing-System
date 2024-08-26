package business;

import core.Helper;
import dao.CardDao;
import entity.Card;
import entity.Customer;

import java.util.ArrayList;

public class CardController {
    private final CardDao cardDao = new CardDao();

    public boolean save(Card card){
        return this.cardDao.save(card);
    }

    public ArrayList<Card> findAll(){
        return this.cardDao.findAll();
    }

    public Card getById(int id) {
        return this.cardDao.getById(id);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMsj(id + " ID Kayıtlı Müşteri Bulunamadı!");
            return false;
        }

        return this.cardDao.delete(id);
    }

    public  boolean clear(){
        return this.cardDao.clear();
    }



}
