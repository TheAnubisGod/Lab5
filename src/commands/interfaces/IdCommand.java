package commands.interfaces;

import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Команды, принимающие в качестве аргумента id.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public interface IdCommand {
     default int getIndexById(int id, Stack<Vehicle> stack) throws Exception {
        for (Vehicle vehicle : stack) {
            if (vehicle.getId() == id) {
                return stack.indexOf(vehicle);
            }
        }
        throw new Exception("Элемента с таким id не существует в коллекции.");
    }

    default int idArgToIndex(String argument, Stack<Vehicle> stack, UserInteractor interactor) {
        int id;
        try {
            id = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            interactor.broadcastMessage("Неверный аргумент. Ожидается число (id).", true);
            return -1;
        }

        int index;

        try {
            index = getIndexById(id, stack);
        } catch (Exception e) {
            interactor.broadcastMessage(e.getMessage(), true);
            return -1;
        }

        return index;
    }
}
