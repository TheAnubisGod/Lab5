package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Класс команды показа элементов.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Show implements Command {
    private final UserInteractor interactor;

    public Show(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        if (stack.size() == 0) {
            interactor.broadcastMessage("В коллекции нет элементов.", true);
        } else {
            interactor.broadcastMessage("Все элементы коллекции:", true);
        }
        for (Vehicle vehicle : stack) {
            interactor.broadcastMessage(vehicle.toString(), true);
        }
        interactor.broadcastMessage("Всего: " + stack.size() + ".", true);
        return true;
    }
}
