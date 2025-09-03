package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class CreateCard {
    /**
     * Se creeaza un card normal unui anumit cont
     * @param users
     * @param input
     */
    public void execute(final ArrayList<User> users, final CommandInput input) {
        Card card = new Card();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccount().equals(input.getAccount())) {
                    account.getCards().add(card);
                    Transaction transaction = new Transaction(account.getAccount(), card.getCardNumber(),
                            user.getEmail(), "New card created", input.getTimestamp());
                    transaction.setFrom("createCard");
                    account.getTransactions().add(transaction);
                    return;
                }
            }
        }
    }

}
