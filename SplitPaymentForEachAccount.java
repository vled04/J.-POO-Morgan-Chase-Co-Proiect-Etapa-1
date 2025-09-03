package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

import java.util.List;

public final class SplitPaymentForEachAccount {
    /**
     * Din splitPayments se ia fiecare cont si se executa comanda
     * @param account
     * @param currency
     * @param exchangeInputs
     * @param splittingAccounts
     * @param input
     * @param amount
     */
    public void execute(final Account account, final String currency,
                        final ExchangeInput[] exchangeInputs, final List<String> splittingAccounts,
                        final CommandInput input, final double amount) {
        if (account.getCurrency().equals(currency)) {
            if (0 >= account.getBalance() - amount) {
                return;
            }
            if (0 <= account.getBalance() - amount) {
                account.setBalance(account.getBalance() - amount);

                String formattedAmount = String.format("%.2f", input.getAmount());
                Transaction transaction = new Transaction(
                        input.getTimestamp(),
                        "Split payment of " + formattedAmount + " " + input.getCurrency(),
                        input.getCurrency(),
                        amount,
                        splittingAccounts
                );
                transaction.setFrom("splitPayments");
                account.getTransactions().add(transaction);
                return;
            }
        }

        for (ExchangeInput exchange : exchangeInputs) {
            if (account.getCurrency().equals(exchange.getFrom())
                    && currency.equals(exchange.getTo())) {
                if (0 >= account.getBalance() - amount / exchange.getRate()) {
                    return;
                }
                if (0 <= account.getBalance() - amount / exchange.getRate()) {
                    account.setBalance(account.getBalance() - amount / exchange.getRate());

                    String formattedAmount = String.format("%.2f", input.getAmount());
                    Transaction transaction = new Transaction(
                            input.getTimestamp(),
                            "Split payment of " + formattedAmount + " "
                                    + input.getCurrency(), input.getCurrency(), amount,
                            splittingAccounts);
                    transaction.setFrom("splitPayments");
                    account.getTransactions().add(transaction);
                    return;
                }
            }
            if (account.getCurrency().equals(exchange.getTo())
                    && currency.equals(exchange.getFrom())) {
                if (0 >= account.getBalance() - amount * exchange.getRate()) {
                    return;
                }

                if (0 <= account.getBalance() - amount * exchange.getRate()) {
                    account.setBalance(account.getBalance() - amount * exchange.getRate());
                    String formattedAmount = String.format("%.2f", input.getAmount());
                    Transaction transaction = new Transaction(
                            input.getTimestamp(),
                            "Split payment of " + formattedAmount + " "
                                    + input.getCurrency(),
                            input.getCurrency(),
                            amount,
                            input.getAccounts());
                    transaction.setFrom("splitPayments");
                    account.getTransactions().add(transaction);
                    return;
                }
            }
        }
        ChangeCurrencyForSplitPayments changeCurrencyForSplitPayments =
                new ChangeCurrencyForSplitPayments();
        changeCurrencyForSplitPayments.execute(account, input, exchangeInputs, splittingAccounts);
    }
}
