package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

public final class ChangeCurrencyForOnlinePayment {

    /**
     * Daca nu este un schimb valutar direct in comanda payOnline se schimba valuta aici
     * @param account        The account to process the payment.
     * @param exchangeInputs The exchange rates available.
     * @param input          The command input details for the payment.
     * @param card           The card used for the payment.
     */
    public void execute(final Account account, final ExchangeInput[] exchangeInputs,
                        final CommandInput input, final Card card) {

        double amount = input.getAmount();
        String targetCurrency = input.getCurrency();
        String initialCurrency = account.getCurrency();
        double moneyAmount = input.getAmount();

        for (ExchangeInput exchangeInput : exchangeInputs) {
            if (exchangeInput.getFrom().equals(targetCurrency)) {
                targetCurrency = exchangeInput.getTo();
                amount *= exchangeInput.getRate();
                moneyAmount *= exchangeInput.getRate();
            } else if (exchangeInput.getTo().equals(targetCurrency)) {
                targetCurrency = exchangeInput.getFrom();
                amount /= exchangeInput.getRate();
                moneyAmount /= exchangeInput.getRate();
            }
        }

        if (!initialCurrency.equals(targetCurrency)) {
            for (ExchangeInput exchangeInput : exchangeInputs) {
                if (initialCurrency.equals(exchangeInput.getFrom())
                        && targetCurrency.equals(exchangeInput.getTo())) {
                    targetCurrency = exchangeInput.getFrom();
                    amount /= exchangeInput.getRate();
                    moneyAmount /= exchangeInput.getRate();
                } else if (initialCurrency.equals(exchangeInput.getTo())
                        && targetCurrency.equals(exchangeInput.getFrom())) {
                    targetCurrency = exchangeInput.getTo();
                    amount *= exchangeInput.getRate();
                    moneyAmount *= exchangeInput.getRate();
                }
            }
        }

        if (initialCurrency.equals(targetCurrency)) {
            double balance = account.getBalance();
            double newBalance = balance - amount;

            if (newBalance < 0) {
                Transaction transaction = new Transaction("Insufficient funds",
                        input.getTimestamp());
                account.getTransactions().add(transaction);
                transaction.setFrom("insufficientFunds");
                return;
            }

            account.setBalance(newBalance);
            Transaction transaction = new Transaction("Card payment",
                    input.getTimestamp(),
                    account.getAccount(), input.getCommerciant(), amount,
                    "sent", input.getCommerciant());
            account.getTransactions().add(transaction);
            transaction.setFrom("payOnline");

            if (card.checkOneTimeCard()) {
                card.setUsed(true);
            }
        }
    }
}
