package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.time.ZonedDateTime;
import java.util.Stack;

/**
 * Класс команды получения информации о коллекции.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Info implements Command {
    private final UserInteractor interactor;
    private final ZonedDateTime zonedDateTime;

    public Info(UserInteractor interactor, ZonedDateTime zonedDateTime) {
        this.interactor = interactor;
        this.zonedDateTime = zonedDateTime;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        interactor.broadcastMessage("Важная информация о коллекции:\n" +
                "\n" +
                "Тип: " + Vehicle.class.getName() + "\n" +
                "Дата инициализации: " + zonedDateTime.toString() + "\n" +
                "Максимальный id: " + Vehicle.getMaxId() + "\n" +
                "Количество элементов: " + stack.size(), true);
        return true;
    }
}
