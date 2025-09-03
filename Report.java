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

public final class Report {
    /**
     * Afiseaza raportul unui anumit cont
     * @param output
     * @param users
     * @param commandInput
     */
    public void execute(final ArrayNode output, final ArrayList<User> users,
                        final CommandInput commandInput) {
        Account accountReported = null;
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccount().equals(commandInput.getAccount())) {
                    accountReported = account;
                }
            }
        }
        if (accountReported == null || commandInput.getTimestamp() < 0) {
            return;
        }
        for (Transaction transaction : accountReported.getTransactions()) {
            if (transaction.getTimestamp() >= commandInput.getStartTimestamp()
                    && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                transactions.add(transaction);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        FormatPrintTransactions formatter = new FormatPrintTransactions();

        ObjectNode reportNode = mapper.createObjectNode();
        reportNode.put("command", "report");
        reportNode.put("timestamp", commandInput.getTimestamp());

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("balance", accountReported.getBalance());
        outputNode.put("currency", accountReported.getCurrency());
        outputNode.put("IBAN", accountReported.getAccount());

        ArrayNode transactionsArray = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            ObjectNode transactionNode = formatter.formatTransaction(transaction, mapper);
            transactionsArray.add(transactionNode);
        }

        outputNode.set("transactions", transactionsArray);
        reportNode.set("output", outputNode);
        output.add(reportNode);
    }
}
