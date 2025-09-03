package org.poo.bank.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.userInfo.Account;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public final class DeleteAccount {
    /**
     * Sterge un cont unui anumit user
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
                if (account.getAccount().equals(input.getAccount())) {
                    if (account.getBalance() == 0) {
                        user.getAccounts().remove(account);
                        node.put("command", input.getCommand());

                        ObjectNode outputNode = mapper.createObjectNode();
                        outputNode.put("success", "Account deleted");
                        outputNode.put("timestamp", input.getTimestamp());
                        node.set("output", outputNode);

                    } else {
                        node.put("command", input.getCommand());

                        ObjectNode outputNode = mapper.createObjectNode();
                        outputNode.put("error", "Account couldn't be deleted" +
                                " - see org.poo.transactions for details");
                        outputNode.put("timestamp", input.getTimestamp());
                        node.set("output", outputNode);
                    }
                    node.put("timestamp", input.getTimestamp());
                    output.add(node);
                    return;
                }
            }
        }
    }
}
