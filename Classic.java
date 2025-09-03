package org.poo.bank.userInfo.accountType;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class Classic extends Account {

    /**
     * Cont clasic
     * @param commandInput
     */
    public Classic(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Constructor cont clasic
     * @param account
     * @param balance
     * @param currency
     * @param accountType
     * @param cards
     * @param minBalance
     * @param alias
     * @param transactions
     */
    public Classic(final String account, final double balance, final String currency,
                   final String accountType, final ArrayList<Card> cards, final double minBalance,
                   final String alias, final ArrayList<Transaction> transactions) {
        super(account, balance, currency, accountType, cards, minBalance, alias, transactions);
    }

    /**
     * este facut pentru deepcopy, returneaza un cont clasic
     * @return
     */
    @Override
    public Account deepCopy() {
        return new Classic(this.getAccount(), this.getBalance(), this.getCurrency(),
                this.getAccountType(), this.getCards(), this.getMinBalance(), this.getAlias(),
                this.getTransactions());
    }

}
