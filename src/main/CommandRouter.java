package main;

import commands.*;
import commands.interfaces.Command;
import interact.UserInteractor;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Класс обращения к командам.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public abstract class CommandRouter {
    public static Command getCommand(String input, boolean from_script, UserInteractor interactor, File file, ZonedDateTime zonedDateTime) {
        input = input.trim();
        String[] commandParts = input.split("\\s+");
        String command = commandParts[0];
        ArrayList<String> Args = new ArrayList<>();
        for (int i = 1; i < commandParts.length; i++) {
            String arg = commandParts[i].replaceAll("\\s+", "");
            if (!arg.isEmpty()) {
                Args.add(arg);
            }
        }


        switch (command) {
            case "help":
                return new Help(interactor);
            case "info":
                return new Info(interactor, zonedDateTime);
            case "show":
                return new Show(interactor);
            case "add":
                return new Add(interactor, from_script);
            case "update":
                if (Args.size() == 0) {
                    interactor.broadcastMessage("Отсутствуют необходимые параметры.", true);
                    return null;
                }
                return new Update(interactor, from_script, Args);
            case "remove_by_id":
                if (Args.size() == 0) {
                    interactor.broadcastMessage("Отсутствуют необходимые параметры.", true);
                    return null;
                }
                return new RemoveById(interactor, Args);
            case "clear":
                return new Clear(interactor);
            case "save":
                return new Save(interactor, file, zonedDateTime);
            case "execute_script":
                if (from_script) {
                    interactor.broadcastMessage("Запрещено выполнять скрипт из другого скрипта.", true);
                    return null;
                }
                if (Args.size() == 0) {
                    interactor.broadcastMessage("Отсутствуют необходимые параметры.", true);
                    return null;
                }
                return new ExecuteScript(interactor, Args, file, zonedDateTime);
            case "exit":
                return new Exit();
            case "remove_first":
                return new RemoveFirst(interactor);
            case "add_if_min":
                return new AddIfMin(interactor, from_script);
            case "reorder":
                return new Reorder(interactor);
            case "group_counting_by_id":
                return new GroupCountingById(interactor);
            case "filter_starts_with_name":
                if (Args.size() == 0) {
                    interactor.broadcastMessage("Отсутствуют необходимые параметры.", true);
                    return null;
                }
                String nameStart = Args.get(0);
                return new FilterStartsWithName(interactor, nameStart);
            case "print_unique_fuel_type":
                return new PrintUniqueFuelType(interactor);
            case "sort":
                return new Sort(interactor);

            case "info_by_id":
                if (Args.size() == 0) {
                    interactor.broadcastMessage("Отсутствуют необходимые параметры.", true);
                    return null;
                }
                return new InfoById(interactor, Args);
            default:
                interactor.broadcastMessage("Команды '" + command + "' не существует. " +
                        "Воспользуйтесь 'help' для получения списка команд.", true);
                return null;
        }

    }
}
