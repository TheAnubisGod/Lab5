package commands;

import commands.interfaces.Command;
import interact.ScriptInteractor;
import interact.UserInteractor;
import main.CommandRouter;
import essentials.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Класс команды выполнения скрипта.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class ExecuteScript implements Command {
    private final UserInteractor interactor;
    private final String argument;
    private final File file;
    private final ZonedDateTime zonedDateTime;

    public ExecuteScript(UserInteractor interactor, ArrayList<String> args, File file, ZonedDateTime zonedDateTime) {
        this.interactor = interactor;
        this.argument = args.get(0);
        this.file = file;
        this.zonedDateTime = zonedDateTime;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        File f = new File(argument);

        Scanner fileScanner;
        try {
            fileScanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            interactor.broadcastMessage("Такого файла не существует.", true);
            return true;
        }
        int line_num = 1;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                Command command = CommandRouter.getCommand(line, true, new ScriptInteractor(fileScanner), file, zonedDateTime);
                if (command == null){
                    continue;
                }
                command.execute(stack);
            } catch (Exception e) {
                interactor.broadcastMessage("Возникла ошибка при выполнении " + line_num + " строки:\n" + line, true);
                break;
            }
            line_num++;
        }
        return true;
    }
}
