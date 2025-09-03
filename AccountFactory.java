package org.poo.bank.commands.factoryDesign;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.accountType.Classic;
import org.poo.bank.userInfo.accountType.Savings;
import org.poo.fileio.CommandInput;

public final class AccountFactory implements Factory<Account> {
    private static AccountFactory instance;

    private AccountFactory() {
    }

    ;

    public static AccountFactory getInstance() {
        if (instance == null) {
            return new AccountFactory();
        }
        return instance;
    }

    /**
     *
     * @param commandInput
     * @return
     */
    @Override
    public Account createAccount(final CommandInput commandInput) {
        if (commandInput.getAccountType().equals(AccountTypes.Classic.toString())) {
            return new Classic(commandInput);
        }
        return new Savings(commandInput);
    }
}
