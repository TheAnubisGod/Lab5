package commands;

import commands.interfaces.Command;
import essentials.StackInfo;
import interact.UserInteractor;
import essentials.Vehicle;
import main.VehicleStackXmlParser;

import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.util.Stack;

/**
 * Класс команды сохранения коллекции.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Save implements Command {
    private final File file;
    private final ZonedDateTime zonedDateTime;
    private final UserInteractor interactor;

    public Save(UserInteractor interactor, File file, ZonedDateTime zonedDateTime){
        this.file = file;
        this.zonedDateTime = zonedDateTime;
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(VehicleStackXmlParser.stackToXml(new StackInfo(stack, Vehicle.getMaxId(), zonedDateTime)));
            fileWriter.flush();
        } catch (Exception e) {
            interactor.broadcastMessage("Возникла ошибка при сохранении в файл: " + e.getMessage(), true);
            return true;
        }
        interactor.broadcastMessage("Файл успешно сохранен.", true);
        return true;
    }
}
