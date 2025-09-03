package org.poo.bank.userInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class OneTimeCard extends Card {
    @JsonIgnore
    private boolean used;

    /**
     * Se genereaza card
     */
    public OneTimeCard() {
        super();
        this.used = false;
    }

    /**
     * Constructor
     * @param other
     */
    public OneTimeCard(final OneTimeCard other) {
        super(other);
        this.used = other.used;
    }

    /**
     * Facut pentru deepcopy
     * @return
     */
    public OneTimeCard deepCopy() {
        return new OneTimeCard(this);
    }

    /**
     * Se verifica daca cardul este folosit
     * @return
     */
    @Override
    public boolean checkUsed() {
        return this.used;
    }

    /**
     * Se verifica daca este card de tip OneTime
     * @return
     */
    @Override
    public boolean checkOneTimeCard() {
        return true;
    }

    /**
     * Se seteaza statusul cardului
     * @param used
     */
    @Override
    public void setUsed(final boolean used) {
        this.used = used;
    }
}
