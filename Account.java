package org.poo.bank.userInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Setter
@Getter
public abstract class Account {
    private double balance;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private String alias;
    private String currency;
    @JsonProperty("type")
    private String accountType;
    @JsonProperty("IBAN")
    private String account;
    private ArrayList<Card> cards;
    @JsonIgnore
    private ArrayList<Transaction> transactions;

    /**
     * Constructor cont, insa nu avem niciun cont de tip Account, doar classic sau savings
     * @param commandInput
     */
    public Account(final CommandInput commandInput) {
        this.account = Utils.generateIBAN();
        this.balance = 0;
        this.currency = commandInput.getCurrency();
        this.accountType = commandInput.getAccountType();
        this.cards = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.minBalance = 0;
        this.alias = "/0";
    }

    /**
     * Constructor
     * @param account
     * @param balance
     * @param currency
     * @param accountType
     * @param cards
     * @param minBalance
     * @param alias
     * @param transactions
     */
    public Account(final String account, final double balance, final String currency,
                   final String accountType, final ArrayList<Card> cards, final double minBalance,
                   final String alias, final ArrayList<Transaction> transactions) {
        this.account = account;
        this.balance = balance;
        this.currency = currency;
        this.accountType = accountType;
        this.minBalance = minBalance;
        this.cards = new ArrayList<>();
        this.alias = alias;
        for (Card card : cards) {
            this.cards.add(card.deepCopy());
        }
        this.transactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            this.transactions.add(transaction.deepCopy());
        }
    }

    /**
     * Facut pentru deepcopy, se face override
     * @return
     */
    public abstract Account deepCopy();

}
