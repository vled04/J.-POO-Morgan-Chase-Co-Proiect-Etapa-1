package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class SetMinBalance {
    /**
     * Se seteaza balanta minima
     * @param users
     * @param input
     */
    public void execute(final ArrayList<User> users, final CommandInput input) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccount().equals(input.getAccount())) {
                    account.setMinBalance(input.getAmount());
                }
            }
        }
    }
}
