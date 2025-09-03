package org.poo.bank.userInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class FormatPrintTransactions {
    /**
     * Clasa facuta pentru a formata tranzactiile si nu a avea cod duplicat
     * Metoda asta apeleaza metodele de formatare pentru fiecare tip de tranzactie
     * @param transaction
     * @param mapper
     * @return
     */
    public ObjectNode formatTransaction(final Transaction transaction, final ObjectMapper mapper) {
        ObjectNode transactionNode = mapper.createObjectNode();

        switch (transaction.getFrom()) {
            case "addAccount":
                formatAddAccount(transaction, transactionNode);
                break;
            case "sendMoney":
                formatSendMoney(transaction, transactionNode);
                break;
            case "payOnline":
                formatPayOnline(transaction, transactionNode);
                break;
            case "createCard":
            case "createOneTimeCard":
                formatCreateCard(transaction, transactionNode);
                break;
            case "insufficientFunds":
                formatInsufficientFunds(transaction, transactionNode);
                break;
            case "deleteCard":
                formatDeleteCard(transaction, transactionNode);
                break;
            case "splitPayments":
                formatSplitPayments(transaction, transactionNode, mapper);
                break;
            default:
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
        }

        return transactionNode;
    }

    /**
     * Formateaza tranzactia de tip sendMoney
     * @param transaction
     * @param transactionNode
     */
    private void formatSendMoney(final Transaction transaction, final ObjectNode transactionNode) {
        transactionNode.put("description", transaction.getDescription());
        transactionNode.put("timestamp", transaction.getTimestamp());
        transactionNode.put("senderIBAN", transaction.getSenderIban());
        transactionNode.put("receiverIBAN", transaction.getReceiverIban());
        transactionNode.put("amount", transaction.getAmount() + " " + transaction.getCurrency());
        transactionNode.put("transferType", transaction.getTransferType());
    }

    /**
     * Formateaza tranzactia de tip payOnline
     * @param transaction
     * @param transactionNode
     */
    private void formatPayOnline(final Transaction transaction, final ObjectNode transactionNode) {
        transactionNode.put("amount", transaction.getAmount());
        transactionNode.put("commerciant", transaction.getReceiver());
        transactionNode.put("description", "Card payment");
        transactionNode.put("timestamp", transaction.getTimestamp());
    }

    /**
     * Formateaza tranzactia de tip addAccount
     * @param transaction
     * @param transactionNode
     */
    private void formatAddAccount(final Transaction transaction, final ObjectNode transactionNode) {
        transactionNode.put("description", "New account created");
        transactionNode.put("timestamp", transaction.getTimestamp());
    }

    /**
     * Formateaza tranzactia de tip createCard / createOneTimeCard
     * @param transaction
     * @param transactionNode
     */
    private void formatCreateCard(final Transaction transaction, final ObjectNode transactionNode) {
        transactionNode.put("account", transaction.getSenderIban());
        transactionNode.put("card", transaction.getCard());
        transactionNode.put("cardHolder", transaction.getCardHolder());
        transactionNode.put("description", transaction.getDescription());
        transactionNode.put("timestamp", transaction.getTimestamp());
    }

    /**
     * Formateaza tranzactia de tip insufficientFunds
     * @param transaction
     * @param transactionNode
     */
    private void formatInsufficientFunds(final Transaction transaction, final ObjectNode transactionNode) {
        transactionNode.put("description", "Insufficient funds");
        transactionNode.put("timestamp", transaction.getTimestamp());
    }

    /**
     * Formateaza tranzactia de tip deleteCard
     * @param transaction
     * @param transactionNode
     */
    private void formatDeleteCard(final Transaction transaction, final ObjectNode transactionNode) {
        transactionNode.put("account", transaction.getSenderIban());
        transactionNode.put("card", transaction.getCard());
        transactionNode.put("cardHolder", transaction.getCardHolder());
        transactionNode.put("description", "The card has been destroyed");
        transactionNode.put("timestamp", transaction.getTimestamp());
    }

    /**
     * Formateaza tranzactia de tip splitPayments
     * @param transaction
     * @param transactionNode
     * @param mapper
     */
    private void formatSplitPayments(final Transaction transaction, final ObjectNode transactionNode,
                                     final ObjectMapper mapper) {
        transactionNode.put("timestamp", transaction.getTimestamp());
        transactionNode.put("description", transaction.getDescription());
        transactionNode.put("currency", transaction.getCurrency());
        transactionNode.put("amount", transaction.getAmount());
        transactionNode.set("involvedAccounts", mapper.valueToTree(transaction.getSplittingAccounts()));
    }

}
