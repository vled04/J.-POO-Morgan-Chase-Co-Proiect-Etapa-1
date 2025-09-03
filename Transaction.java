package org.poo.bank.userInfo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Transaction {
    private String description;
    private int timestamp;
    private String senderIban;
    private String receiverIban;
    private double amount;
    private String transferType;
    private String cardHolder;
    @JsonProperty("card")
    private String card;
    private String from;
    private String receiver;
    private List<String> splittingAccounts = new ArrayList<>();
    private String currency;

    /**
     * Constructor
     */
    public Transaction() {
    }

    /**
     * Constructor
     * @param timestamp
     * @param description
     * @param currency
     * @param amount
     * @param splittingAccounts
     */
    public Transaction(final int timestamp, final String description, final String currency,
                       final double amount, final List<String> splittingAccounts) {
        this.description = description;
        this.timestamp = timestamp;
        this.amount = amount;
        this.currency = currency;
        this.splittingAccounts.addAll(splittingAccounts);
    }

    /**
     * Constructor
     * @param description
     * @param timestamp
     * @param senderIban
     * @param receiverIban
     * @param amount
     * @param transferType
     * @param receiver
     */
    public Transaction(final String description, final int timestamp, final String senderIban,
                       final String receiverIban, final double amount, final String transferType,
                       final String receiver) {
        this.description = description;
        this.timestamp = timestamp;
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.transferType = transferType;
        this.receiver = receiver;
    }

    /**
     * Constructor
     * @param iban
     * @param card
     * @param cardHolder
     * @param description
     * @param timestamp
     */
    public Transaction(final String iban, final String card, final String cardHolder,
                       final String description, final int timestamp) {
        this.senderIban = iban;
        this.card = card;
        this.cardHolder = cardHolder;
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     * Constructor
     * @param commandInput
     */
    public Transaction(final CommandInput commandInput) {
        this.description = commandInput.getDescription();
        this.amount = commandInput.getAmount();
        this.receiverIban = commandInput.getCommerciant();
        this.senderIban = commandInput.getAccount();
        this.timestamp = commandInput.getTimestamp();
    }

    /**
     * Constructor
     * @param transaction
     */
    public Transaction(final Transaction transaction) {
        this.description = transaction.description;
        this.timestamp = transaction.timestamp;
        this.senderIban = transaction.senderIban;
        this.receiverIban = transaction.receiverIban;
        this.amount = transaction.amount;
        this.transferType = transaction.transferType;
        this.cardHolder = transaction.cardHolder;
        this.card = transaction.card;
        this.from = transaction.from;
        this.receiver = transaction.receiver;
        this.splittingAccounts.addAll(transaction.splittingAccounts);
        this.currency = transaction.currency;
    }

    /**
     * Constructor
     * @param description
     * @param timestamp
     */
    public Transaction(final String description, final int timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     * Facut pentru deepcopy
     * @return
     */
    public Transaction deepCopy() {
        return new Transaction(this);
    }


}
