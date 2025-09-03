package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

import java.util.List;

public final class ChangeCurrencyForSplitPayments {
    /**
     * Daca nu este un schimb valutar direct in comanda splitPayment se schimba valuta aici
     * @param account
     * @param input
     * @param exchangeInputs
     * @param splittingAccounts
     */
    public void execute(final Account account, final CommandInput input,
                        final ExchangeInput[] exchangeInputs, final List<String> splittingAccounts) {
        String badCurrency = input.getCurrency();
        String start = account.getCurrency();
        double amount = input.getAmount() / input.getAccounts().size();
        double amountCopy = amount;
        for (ExchangeInput exchangeInput : exchangeInputs) {
            if (exchangeInput.getFrom().equals(badCurrency)) {
                badCurrency = exchangeInput.getTo();
                amount = amount * exchangeInput.getRate();
            } else if (exchangeInput.getTo().equals(badCurrency)) {
                badCurrency = exchangeInput.getFrom();
                amount = amount / exchangeInput.getRate();
            }
        }

        if (!start.equals(badCurrency)) {
            for (ExchangeInput exchangeInput : exchangeInputs) {
                if (start.equals(exchangeInput.getFrom()) && badCurrency.equals(exchangeInput.getTo())) {
                    badCurrency = exchangeInput.getFrom();
                    amount = amount / exchangeInput.getRate();
                } else if (start.equals(exchangeInput.getTo()) && badCurrency.equals(exchangeInput.getFrom())) {
                    badCurrency = exchangeInput.getTo();
                    amount = amount * exchangeInput.getRate();
                }
            }
        }
        if (start.equals(badCurrency)) {
            if (0 >= account.getBalance() - input.getAmount()) {
                return;
            }
            if (0 <= account.getBalance() - amount) {
                account.setBalance(account.getBalance() - input.getAmount());
                String formattedAmount = String.format("%.2f", input.getAmount());
                Transaction transaction = new Transaction(
                        input.getTimestamp(),
                        "Split payment of " + formattedAmount + " " + input.getCurrency(),
                        input.getCurrency(),
                        amountCopy,
                        splittingAccounts);
                transaction.setFrom("splitPayments");
                account.getTransactions().add(transaction);
            }
        }
    }
}
