package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Класс команды удаления первого элемента.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class RemoveFirst implements Command {
    private final UserInteractor interactor;

    public RemoveFirst(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        if (stack.size() == 0) {
            interactor.broadcastMessage("В коллекции нет элементов.", true);
            return true;
        }
        stack.remove(0);
        interactor.broadcastMessage("Элемент успешно удален.", true);
        return true;
    }
}
