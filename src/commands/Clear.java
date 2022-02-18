package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Класс команды очистки коллекции.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Clear implements Command {
    private final UserInteractor interactor;
    public Clear(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        stack.clear();
        interactor.broadcastMessage("Коллекция очищена.", true);
        return true;
    }
}
