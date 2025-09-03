package org.poo.bank.userInfo.accountType;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class Savings extends Account {
    private double interestRate;

    /**
     * Cont de economii care are in plus dobanda
     * @param commandInput
     */
    public Savings(final CommandInput commandInput) {
        super(commandInput);
        this.interestRate = commandInput.getInterestRate();
    }

    /**
     * Constructor
     * @param account
     * @param balance
     * @param currency
     * @param accountType
     * @param cards
     * @param interestRate
     * @param minBalance
     * @param alias
     * @param transactions
     */
    public Savings(final String account, final double balance, final String currency,
                   final String accountType, final ArrayList<Card> cards,
                   final double interestRate, final double minBalance, final String alias,
                   final ArrayList<Transaction> transactions) {
        super(account, balance, currency, accountType, cards, minBalance, alias, transactions);
        this.interestRate = interestRate;
    }

    /**
     * este facut pentru deepcopy, returneaza un cont de economii
     * @return
     */
    @Override
    public Account deepCopy() {
        return new Savings(this.getAccount(), this.getBalance(), this.getCurrency(),
                this.getAccountType(),
                this.getCards(), this.interestRate, this.getMinBalance(),
                this.getAlias(), this.getTransactions());
    }

}
