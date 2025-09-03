package org.poo.bank.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.userInfo.User;

import java.util.ArrayList;

public final class PrintUsers {
    /**
     * Afiseaza toti userii
     * @param output
     * @param users
     * @param timestamp
     */
    public void execute(final ArrayNode output, final ArrayList<User> users, final int timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ArrayList<User> deepCopy = new ArrayList<>();
        for (User user : users) {
            deepCopy.add(new User(user));
        }
        node.put("command", "printUsers")
                .putPOJO("output", deepCopy)
                .put("timestamp", timestamp);
        output.add(node);
    }
}