package commands;

import interact.UserInteractor;
import essentials.Vehicle;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Класс команды обновления элемента коллекции по id.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Update extends Add {
    private final String argument;

    public Update(UserInteractor interactor, boolean from_script, ArrayList<String> args) {
        super(interactor, from_script);
        this.argument = args.get(0);
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        int index = idArgToIndex(argument,stack);
        if (index == -1) {
            return true;
        }
        stack.remove(index);
        stack.add(index, createVehicle(fromScript, interactor));
        interactor.broadcastMessage("Элемент успешно обновлен.", true);
        return true;
    }

    private int getIndexById(int id, Stack<Vehicle> stack) throws Exception {
        for (Vehicle vehicle : stack) {
            if (vehicle.getId() == id) {
                return stack.indexOf(vehicle);
            }
        }
        throw new Exception("Элемента с таким id не существует в коллекции.");
    }

    private int idArgToIndex(String argument, Stack<Vehicle> stack) {
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
