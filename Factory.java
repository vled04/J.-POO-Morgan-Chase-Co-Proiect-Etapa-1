package org.poo.bank.commands.factoryDesign;

import org.poo.fileio.CommandInput;

public interface Factory<E> {
    E createAccount(CommandInput commandInput);
}
