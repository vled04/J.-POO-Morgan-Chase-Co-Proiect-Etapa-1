package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class DeleteCard {
    /**
     * Sterge un card anume
     * @param users
     * @param input
     */
    public void execute(final ArrayList<User> users, final CommandInput input) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(input.getCardNumber())) {
                        Transaction transaction = new Transaction(account.getAccount(),
                                card.getCardNumber(), user.getEmail(),
                                "Card deleted", input.getTimestamp());
                        transaction.setFrom("deleteCard");
                        account.getCards().remove(card);
                        account.getTransactions().add(transaction);
                        return;
                    }
                }
            }
        }
    }
}
