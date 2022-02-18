package commands;

import commands.interfaces.Command;
import essentials.FuelType;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.HashSet;
import java.util.Stack;

/**
 * Класс команды вывода уникальных видов топлива.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class PrintUniqueFuelType implements Command {
    private final UserInteractor interactor;
    public PrintUniqueFuelType(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        interactor.broadcastMessage("Уникальные типы топлива:", true);
        HashSet<FuelType> hashSet = new HashSet<>();
        int num = 0;
        for (Vehicle vehicle : stack) {
            if (!hashSet.contains(vehicle.getFuelType())) {
                hashSet.add(vehicle.getFuelType());
                interactor.broadcastMessage(vehicle.getFuelType().toString(), true);
                num++;
            }
        }
        interactor.broadcastMessage("Всего: " + num + ".", true);
        return true;
    }
}
