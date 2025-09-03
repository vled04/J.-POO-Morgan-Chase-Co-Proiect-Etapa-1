package org.poo.bank.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;

public final class SplitPayments {
    /**
     * Imparte o suma de bani in functie de numarul de conturi si fiecare cont trebuie dupa
     *    sa plateasca
     * @param users
     * @param input
     * @param exchangeInputs
     */
    public void execute(final ArrayList<User> users, final CommandInput input,
                        final ExchangeInput[] exchangeInputs) {
        double numberOfAccounts = input.getAccounts().size();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (String accountInput : input.getAccounts()) {
                    if (account.getAccount().equals(accountInput)) {
                        SplitPaymentForEachAccount splitPaymentForEachAccount
                                = new SplitPaymentForEachAccount();
                        splitPaymentForEachAccount.
                                execute(account, input.getCurrency(), exchangeInputs,
                                        input.getAccounts(), input,
                                        input.getAmount() / numberOfAccounts);
                        break;
                    }
                }
            }
        }
    }
}
