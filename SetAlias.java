package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class SetAlias {
    /**
     * Seteaza un alias unui anumit cont
     * @param users
     * @param commandInput
     */
    public void execute(final ArrayList<User> users, final CommandInput commandInput) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccount().equals(commandInput.getAccount())
                        && user.getEmail().equals(commandInput.getEmail())) {
                    account.setAlias(commandInput.getAlias());
                }
            }
        }
    }
}
