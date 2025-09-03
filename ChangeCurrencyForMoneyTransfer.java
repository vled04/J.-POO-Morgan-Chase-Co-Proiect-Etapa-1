package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

public final class ChangeCurrencyForMoneyTransfer {
    /**
     * Daca nu este un schimb valutar direct in comanda sendMoney se schimba valuta aici
     * @param sender
     * @param receiver
     * @param exchangeInputs
     * @param input
     */
    public void execute(final Account sender, final Account receiver,
                        final ExchangeInput[] exchangeInputs, final CommandInput input) {
        String badCurrency = receiver.getCurrency();
        String start = sender.getCurrency();
        double moneyToTransfer = input.getAmount();
        for (ExchangeInput exchangeInput : exchangeInputs) {
            if (exchangeInput.getFrom().equals(badCurrency)) {
                badCurrency = exchangeInput.getTo();
                moneyToTransfer /= exchangeInput.getRate();
            } else if (exchangeInput.getTo().equals(badCurrency)) {
                badCurrency = exchangeInput.getFrom();
                moneyToTransfer *= exchangeInput.getRate();
            }
        }
        if (!start.equals(badCurrency)) {
            for (ExchangeInput exchangeInput : exchangeInputs) {
                if (start.equals(exchangeInput.getFrom()) && badCurrency.equals(exchangeInput.getTo())) {
                    badCurrency = exchangeInput.getFrom();
                    moneyToTransfer /= exchangeInput.getRate();
                } else if (start.equals(exchangeInput.getTo())
                        && badCurrency.equals(exchangeInput.getFrom())) {
                    badCurrency = exchangeInput.getTo();
                    moneyToTransfer *= exchangeInput.getRate();
                }
            }
        }
        if (start.equals(badCurrency)) {
            if (0 <= sender.getBalance() - input.getAmount()) {
                sender.setBalance(sender.getBalance() - input.getAmount());
                receiver.setBalance(receiver.getBalance() + moneyToTransfer);
                Transaction transaction1 = new Transaction(input.getDescription(),
                        input.getTimestamp(),
                        sender.getAccount(), receiver.getAccount(), input.getAmount(),
                        "sent", input.getCommerciant());
                transaction1.setCurrency(sender.getCurrency());
                sender.getTransactions().add(transaction1);
                transaction1.setFrom("sendMoney");

            }
        }
    }
}