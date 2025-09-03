package org.poo.bank.userInfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter
@Setter
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts = new ArrayList<>();

    /**
     * Constructor
     * @param userInput
     */
    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
    }

    /**
     * Constructor
     * @param other
     */
    public User(final User other) {
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.accounts = new ArrayList<>();
        for (Account account : other.accounts) {
            this.accounts.add(account.deepCopy());
        }
    }

    /**
     * Facut pentru deepcopy, returneaza un user
     * @return
     */
    public User deepCopy() {
        return new User(this);
    }
}
