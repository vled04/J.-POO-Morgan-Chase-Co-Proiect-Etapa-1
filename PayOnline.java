package org.poo.bank.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;

public final class PayOnline {
    private static PayOnline instance;

    private PayOnline() {

    }

    /**
     * Singleton
     * @return
     */
    public static PayOnline getInstance() {
        if (instance == null) {
            instance = new PayOnline();
        }
        return instance;
    }

    /**
     * Comanda de plata online
     * @param output
     * @param users
     * @param input
     * @param exchangeInput
     */
    public void execute(final ArrayNode output, final ArrayList<User> users,
                        final CommandInput input, final ExchangeInput[] exchangeInput) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(input.getCardNumber()) && user.getEmail().equals
                            (input.getEmail())) {
                        if (card.getStatus().equals("frozen")) {
                            Transaction transaction = new Transaction();
                            transaction.setFrom("checkCardStatus");
                            transaction.setTimestamp(input.getTimestamp());
                            transaction.setDescription("The card is frozen");
                            account.getTransactions().add(transaction);
                            return;
                        }
                        if (account.getCurrency().equals(input.getCurrency())
                                && card.getStatus().equals("active")) {
                            if (0 >= account.getBalance() - input.getAmount()) {
                                Transaction transaction = new Transaction("Insufficient"
                                        + " funds", input.getTimestamp());
                                account.getTransactions().add(transaction);
                                transaction.setFrom("insufficientFunds");
                                return;
                            }
                            if (0 <= account.getBalance() - input.getAmount()) {
                                account.setBalance(account.getBalance() - input.getAmount());
                                Transaction transaction = new Transaction(input.getDescription(),
                                        input.getTimestamp(),
                                        account.getAccount(), input.getCommerciant(),
                                        input.getAmount(), "sent", input.getCommerciant());
                                account.getTransactions().add(transaction);
                                transaction.setFrom("payOnline");
                                if (card.checkOneTimeCard()) {
                                    card.setUsed(true);
                                }
                                return;
                            }
                        } else {
                            for (ExchangeInput exchange : exchangeInput) {
                                if (account.getCurrency().equals(exchange.getFrom())
                                        && input.getCurrency().equals(exchange.getTo())
                                        && card.getStatus().equals("active")) {
                                    if (0 >= account.getBalance() - input.getAmount()
                                            / exchange.getRate()) {
                                        Transaction transaction = new Transaction
                                                ("Insufficient funds",
                                                        input.getTimestamp());
                                        account.getTransactions().add(transaction);
                                        transaction.setFrom("insufficientFunds");
                                        return;
                                    }
                                    if (0 <= account.getBalance() - input.getAmount()
                                            / exchange.getRate()) {
                                        account.setBalance(account.getBalance() - input.getAmount()
                                                / exchange.getRate());
                                        Transaction transaction = new Transaction("Card"
                                                + " payment", input.getTimestamp(),
                                                account.getAccount(), input.getCommerciant(),
                                                        input.getAmount() /
                                                                exchange.getRate(),
                                                        "sent", input.getCommerciant());
                                        account.getTransactions().add(transaction);
                                        transaction.setFrom("payOnline");
                                        if (card.checkOneTimeCard()) {
                                            card.setUsed(true);
                                        }
                                        return;
                                    }
                                } else if (account.getCurrency().equals(exchange.getTo())
                                        && input.getCurrency().equals(exchange.getFrom())
                                        && card.getStatus().equals("active")) {
                                    if (0 >= account.getBalance() - input.getAmount()
                                            / exchange.getRate()) {
                                        Transaction transaction = new Transaction("Insufficient"
                                                + " funds",
                                                        input.getTimestamp());
                                        account.getTransactions().add(transaction);
                                        transaction.setFrom("insufficientFunds");
                                        return;
                                    }
                                    if (0 <= account.getBalance() - input.getAmount()
                                            / exchange.getRate()) {
                                        account.setBalance(account.getBalance()
                                                - input.getAmount() * exchange.getRate());
                                        Transaction transaction = new Transaction("Card"
                                                + " payment", input.getTimestamp(),
                                                account.getAccount(), input.getCommerciant(),
                                                        input.getAmount()
                                                                * exchange.getRate(),
                                                        "sent", input.getCommerciant());
                                        account.getTransactions().add(transaction);
                                        transaction.setFrom("payOnline");
                                        if (card.checkOneTimeCard()) {
                                            card.setUsed(true);
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                        if (card.getStatus().equals("active")) {
                            ChangeCurrencyForOnlinePayment changeCurrencyForOnlinePayment
                                    = new ChangeCurrencyForOnlinePayment();
                            changeCurrencyForOnlinePayment.execute(account, exchangeInput,
                                    input, card);
                            return;
                        }
                        return;
                    }
                }
            }
        }
        node.put("command", "payOnline");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", input.getTimestamp());
        node.set("output", outputNode);
        node.put("timestamp", input.getTimestamp());
        output.add(node);
    }
}
