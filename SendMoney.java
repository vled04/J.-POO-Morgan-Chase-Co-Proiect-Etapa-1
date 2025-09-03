package org.poo.bank.commands;

import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;

public final class SendMoney {
    private static SendMoney instance;

    /**
     *
     */
    private SendMoney() {

    }

    /**
     * Singleton
     * @return
     */
    public static SendMoney getInstance() {
        if (instance == null) {
            instance = new SendMoney();
        }
        return instance;
    }

    /**
     * Comanda de trimitere bani
     * @param users
     * @param commandInput
     * @param exchangeInputs
     */
    public void execute(final ArrayList<User> users, final CommandInput commandInput,
                        final ExchangeInput[] exchangeInputs) {
        Account sender = null;
        Account receiver = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {

                if (account.getAccount().equals(commandInput.getAccount())
                        && user.getEmail().equals(commandInput.getEmail())) {
                    sender = account;
                }
                if (account.getAlias().equals(commandInput.getAccount())
                        && user.getEmail().equals(commandInput.getEmail())) {
                    return;
                }

                if (account.getAccount().equals(commandInput.getReceiver())) {
                    receiver = account;
                }
                if (receiver == null && account.getAlias().equals(commandInput.getReceiver())) {
                    receiver = account;
                }

            }
        }
        if (sender == null || receiver == null) {
            return;
        }

        if (0 >= sender.getBalance() - commandInput.getAmount()) {
            Transaction transaction = new Transaction("Insufficient funds",
                    commandInput.getTimestamp());
            sender.getTransactions().add(transaction);
            transaction.setFrom("insufficientFunds");
            return;
        }
        if (sender.getCurrency().equals(receiver.getCurrency())) {
            if (0 <= sender.getBalance() - commandInput.getAmount()) {
                sender.setBalance(sender.getBalance() - commandInput.getAmount());
                receiver.setBalance(receiver.getBalance() + commandInput.getAmount());
                Transaction transaction1 = new Transaction(commandInput.getDescription(),
                        commandInput.getTimestamp(),
                        sender.getAccount(), receiver.getAccount(), commandInput.getAmount(),
                        "sent", commandInput.getCommerciant());
                transaction1.setCurrency(sender.getCurrency());
                sender.getTransactions().add(transaction1);
                transaction1.setFrom("sendMoney");
                for (Card card : sender.getCards()) {
                    card.setUsed(true);
                }
                return;
            }
        }
        for (ExchangeInput exchangeInput : exchangeInputs) {
            if (sender.getCurrency().equals(exchangeInput.getFrom())
                    && receiver.getCurrency().equals(exchangeInput.getTo())) {
                if (0 <= sender.getBalance() - commandInput.getAmount()) {
                    sender.setBalance(sender.getBalance() - commandInput.getAmount());
                    receiver.setBalance(receiver.getBalance() + commandInput.getAmount()
                            * exchangeInput.getRate());
                    Transaction transaction1 = new Transaction(commandInput.getDescription(),
                            commandInput.getTimestamp(),
                            sender.getAccount(), receiver.getAccount(), commandInput.getAmount(),
                            "sent", commandInput.getCommerciant());
                    transaction1.setCurrency(sender.getCurrency());
                    sender.getTransactions().add(transaction1);
                    Transaction transaction2 = new Transaction(commandInput.getDescription(),
                            commandInput.getTimestamp(),
                            receiver.getAccount(), commandInput.getReceiver(),
                            commandInput.getAmount() * exchangeInput.getRate(),
                            "received", commandInput.getCommerciant());
                    receiver.getTransactions().add(transaction2);
                    transaction1.setFrom("sendMoney");
                    transaction2.setFrom("sendMoney");
                    for (Card card : sender.getCards()) {
                        card.setUsed(true);
                    }
                    return;
                }
            } else if (sender.getCurrency().equals(exchangeInput.getTo())
                    && receiver.getCurrency().equals(exchangeInput.getFrom())) {
                if (0 <= sender.getBalance() - commandInput.getAmount()) {
                    sender.setBalance(sender.getBalance() - commandInput.getAmount());
                    receiver.setBalance(receiver.getBalance() + commandInput.getAmount()
                            / exchangeInput.getRate());
                    Transaction transaction1 = new Transaction(commandInput.getDescription(),
                            commandInput.getTimestamp(),
                            sender.getAccount(), receiver.getAccount(), commandInput.getAmount(),
                            "sent", commandInput.getCommerciant());
                    transaction1.setCurrency(sender.getCurrency());
                    sender.getTransactions().add(transaction1);
                    transaction1.setFrom("sendMoney");
                    return;
                }
            }
        }
        ChangeCurrencyForMoneyTransfer changeCurrencyForMoneyTransfer
                = new ChangeCurrencyForMoneyTransfer();
        changeCurrencyForMoneyTransfer.execute(sender, receiver, exchangeInputs, commandInput);
    }
}
