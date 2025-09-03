package org.poo.bank.commands;

import org.poo.bank.userInfo.*;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class CreateOneTimeCard {
    /**
     * Se creeaza un card oneTime unui anumit cont
     * @param users
     * @param input
     */
    public void execute(final ArrayList<User> users, final CommandInput input) {
        OneTimeCard card = new OneTimeCard();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccount().equals(input.getAccount())) {
                    account.getCards().add(card);
                    Transaction transaction = new Transaction(account.getAccount(), card.getCardNumber(),
                            user.getEmail(), "New card created", input.getTimestamp());
                    transaction.setFrom("createOneTimeCard");
                    account.getTransactions().add(transaction);
                    return;
                }
            }
        }
    }

}
