package org.poo.bank.userInfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;


@Getter
@Setter
public class Card {
    private String cardNumber;
    private String status;

    /**
     * Se genereaza card
     */
    public Card() {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
    }

    /**
     * Constructor
     * @param other
     */
    public Card(final Card other) {
        this.cardNumber = other.cardNumber;
        this.status = other.status;
    }

    /**
     * Facut pentru deepcopy
     * @return
     */
    public Card deepCopy() {
        return new Card(this);
    }

    /**
     * Se verifica daca cardul este folosit (nu, pentru ca asta e doar la OneTime
     * @return
     */
    public boolean checkUsed() {
        return false;
    }

    /**
     * Se verifica daca este card de tip OneTime
     * @return
     */
    public boolean checkOneTimeCard() {
        return false;
    }

    /**
     * Se seteaza statusul cardului (la acest tip de card nu conteaza pentru ca nu e OneTime)
     * @param used
     */
    public void setUsed(boolean used) {

    }
}

