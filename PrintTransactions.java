package org.poo.bank.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.Transaction;
import org.poo.bank.userInfo.User;
import org.poo.bank.userInfo.FormatPrintTransactions;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class PrintTransactions {
    /**
     * Afiseaza tranzactiile unui anumit user
     * @param output
     * @param users
     * @param commandInput
     */
    public void execute(final ArrayNode output, final ArrayList<User> users,
                        final CommandInput commandInput) {
        ObjectMapper mapper = new ObjectMapper();
        FormatPrintTransactions formatter = new FormatPrintTransactions();

        for (User user : users) {
            if (user.getEmail().equals(commandInput.getEmail())) {
                for (Account account : user.getAccounts()) {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("command", "printTransactions");
                    ArrayNode myTransactions = mapper.createArrayNode();

                    for (Transaction transaction : account.getTransactions()) {
                        Transaction deepCopy = new Transaction(transaction);
                        ObjectNode transactionNode = formatter.formatTransaction(deepCopy, mapper);
                        myTransactions.add(transactionNode);
                    }

                    node.set("output", myTransactions);
                    node.put("timestamp", commandInput.getTimestamp());
                    output.add(node);
                }
            }
        }
    }
}
