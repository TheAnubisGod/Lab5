package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Comparator;
import java.util.Stack;

/**
 * Класс команды сортировки коллекции.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Sort implements Command {
    private final UserInteractor interactor;

    public Sort(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        stack.sort(Comparator.naturalOrder());
        interactor.broadcastMessage("Коллекция отсортирована.", true);
        return true;
    }
}
