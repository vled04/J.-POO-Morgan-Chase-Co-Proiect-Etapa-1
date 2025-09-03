package org.poo.bank.commands;

import org.poo.bank.commands.factoryDesign.AccountFactory;
import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;


public final class AddAccount {
    /**
     * Adauga un cont la un anumit user
     * @param users
     * @param commandInput
     */
    public void execute(final ArrayList<User> users, final CommandInput commandInput) {
        Transaction transaction = new Transaction();
        AccountFactory accountFactory = AccountFactory.getInstance();
        Account account = accountFactory.createAccount(commandInput);
        account.getTransactions().add(transaction);
        transaction.setDescription("Account created");
        transaction.setFrom("addAccount");
        transaction.setTimestamp(commandInput.getTimestamp());
        for (User user : users) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                user.getAccounts().add(account);
            }
        }
    }
}

