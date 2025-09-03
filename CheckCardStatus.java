package org.poo.bank.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Card;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class CheckCardStatus {
    /**
     * Se verifica statusul unui anumit card
     * @param output
     * @param users
     * @param input
     */
    public void execute(final ArrayNode output, final ArrayList<User> users,
                        final CommandInput input) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        for (User user : users) {

            for (Account account : user.getAccounts()) {

                for (Card card : account.getCards()) {

                    if (card.getCardNumber().equals(input.getCardNumber())) {

                        if (account.getBalance() <= account.getMinBalance()
                                && card.getStatus().equals("active")) {
                            card.setStatus("frozen");
                            Transaction transaction = new Transaction();
                            transaction.setFrom("checkCardStatus");
                            transaction.setTimestamp(input.getTimestamp());
                            transaction.setDescription("You have reached the"
                                    + " minimum amount of funds, the card will be frozen");
                            account.getTransactions().add(transaction);
                            return;
                        }

                        if (card.checkUsed() && card.checkOneTimeCard()) {
                            card.setStatus("frozen");
                            Transaction transaction = new Transaction();
                            transaction.setFrom("checkCardStatus");
                            transaction.setTimestamp(input.getTimestamp());
                            transaction.setDescription("The card is frozen");
                            account.getTransactions().add(transaction);
                            return;
                        }
                        return;
                    }
                }
            }

        }
        node.put("command", "checkCardStatus");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", input.getTimestamp());
        outputNode.put("description", "Card not found");
        node.set("output", outputNode);
        node.put("timestamp", input.getTimestamp());
        output.add(node);
    }
}
