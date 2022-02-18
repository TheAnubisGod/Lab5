package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Класс команды подсчета групп по id.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class GroupCountingById implements Command {
    private final UserInteractor interactor;
    public GroupCountingById(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        for (Vehicle vehicle : stack) {
            interactor.broadcastMessage("1 элемент со значением id=" + vehicle.getId() + ".", true);
        }
        return true;
    }
}
