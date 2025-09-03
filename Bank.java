package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.commands.*;
import org.poo.bank.userInfo.User;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;


@Getter
@Setter
public final class Bank {
    private ArrayList<User> users;
    private ArrayNode output;
    private ObjectInput objectInput;
    private CommandInput commandInput;

    /**
     * Se initializeaza un obiect de tip Bank
     * @param output
     * @param objectInput
     */
    public Bank(final ArrayNode output, final ObjectInput objectInput) {
        this.output = output;
        this.objectInput = objectInput;
        this.users = new ArrayList<>();
    }

    /**
     * Se incarca toti userii de la input
     */
    public void loadData() {
        for (UserInput userInput : objectInput.getUsers()) {
            User user = new User(userInput);
            users.add(user);
        }
    }

    /**
     * Se executa toate actiunile din input
     */
    public void actions() {
        loadData();
        for (CommandInput commandInput : this.objectInput.getCommands()) {
            switch (commandInput.getCommand()) {
                case "printUsers" -> {
                    PrintUsers printUsers = new PrintUsers();
                    printUsers.execute(output, users, commandInput.getTimestamp());
                }
                case "addAccount" -> {
                    AddAccount addAccount = new AddAccount();
                    addAccount.execute(users, commandInput);
                }
                case "addFunds" -> {
                    AddFunds printAccounts = new AddFunds();
                    printAccounts.execute(users, commandInput);
                }
                case "createCard" -> {
                    CreateCard createCard = new CreateCard();
                    createCard.execute(users, commandInput);
                }
                case "createOneTimeCard" -> {
                    CreateOneTimeCard createCard = new CreateOneTimeCard();
                    createCard.execute(users, commandInput);
                }
                case "deleteAccount" -> {
                    DeleteAccount deleteAccount = new DeleteAccount();
                    deleteAccount.execute(output, users, commandInput);
                }
                case "deleteCard" -> {
                    DeleteCard deleteCard = new DeleteCard();
                    deleteCard.execute(users, commandInput);
                }
                case "payOnline" -> {
                    PayOnline payOnline = PayOnline.getInstance();
                    ExchangeInput[] exchangeInputs = objectInput.getExchangeRates();
                    payOnline.execute(output, users, commandInput, exchangeInputs);

                }
                case "sendMoney" -> {
                    SendMoney sendMoney = SendMoney.getInstance();
                    ExchangeInput[] exchangeInputs = objectInput.getExchangeRates();
                    sendMoney.execute(users, commandInput, exchangeInputs);
                }
                case "printTransactions" -> {
                    PrintTransactions printTransactions = new PrintTransactions();
                    printTransactions.execute(output, users, commandInput);
                }
                case "setAlias" -> {
                    SetAlias setAlias = new SetAlias();
                    setAlias.execute(users, commandInput);
                }
                case "checkCardStatus" -> {
                    CheckCardStatus checkCardStatus = new CheckCardStatus();
                    checkCardStatus.execute(output, users, commandInput);
                }
                case "setMinimumBalance" -> {
                    SetMinBalance setMinimumBalance = new SetMinBalance();
                    setMinimumBalance.execute(users, commandInput);
                }
                case "splitPayment" -> {
                    SplitPayments splitPayments = new SplitPayments();
                    ExchangeInput[] exchangeInputs = objectInput.getExchangeRates();
                    splitPayments.execute(users, commandInput, exchangeInputs);
                }
                case "report" -> {
                    Report report = new Report();
                    report.execute(output, users, commandInput);
                }

                default -> {

                }

            }

        }
    }
}
