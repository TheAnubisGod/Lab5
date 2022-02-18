package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Класс команды фильтрации по имени.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class FilterStartsWithName implements Command {
    private final String argument;
    private final UserInteractor interactor;

    public FilterStartsWithName(UserInteractor interactor, String nameStart) {
        this.argument = nameStart;
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        interactor.broadcastMessage("Все элементы, чье название начинается с " + argument + ":", true);
        int num = 0;
        for (Vehicle vehicle : stack) {
            if (vehicle.getName().startsWith(argument)) {
                interactor.broadcastMessage(vehicle.toString(), true);
                num++;
            }
        }
        interactor.broadcastMessage("Всего: " + num + ".", true);
        return true;
    }
}
