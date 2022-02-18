package commands;

import commands.interfaces.Command;
import commands.interfaces.IdCommand;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Класс команды получения информации об элементе по id.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class InfoById implements Command, IdCommand {
    private final UserInteractor interactor;
    private final String argument;

    public InfoById(UserInteractor interactor, ArrayList<String> args) {
        this.argument = args.get(0);
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        int index = idArgToIndex(argument, stack, interactor);
        if (index == -1) {
            return true;
        }
        Vehicle vehicle = stack.get(index);
        String info = String.format("id: %d \n" +
                        "Название: %s \n" +
                        "Тип: %s \n" +
                        "Дата создания: %s \n" +
                        "Мощность: %s \n" +
                        "Тип топлива: %s \n" +
                        "Координаты: %s", vehicle.getId(), vehicle.getName(), vehicle.getType(),
                vehicle.getCreationDate(), vehicle.getEnginePower(), vehicle.getFuelType(),
                vehicle.getCoordinates());
        interactor.broadcastMessage(info, true);
        return true;
    }
}
