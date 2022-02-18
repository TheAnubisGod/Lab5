import commands.interfaces.Command;
import essentials.StackInfo;
import interact.ConsoleInteractor;
import interact.UserInteractor;
import main.CommandRouter;
import main.VehicleStackXmlParser;
import essentials.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Класс команды добавления элемента, если он минимальный.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public abstract class CollectionMaster {

    private static Stack<Vehicle> collection = new Stack<>();
    private static ZonedDateTime initDateTime;
    private static File file;
    private final static UserInteractor interactor = new ConsoleInteractor();

    public static void main(String[] args) {
        if (System.getenv("FILE_LOC") != null && !System.getenv("FILE_LOC").trim().isEmpty()) {
            file = new File(System.getenv("FILE_LOC"));

            if (!prepare()) {
                return;
            }

            runInteracting();
        } else {
            interactor.broadcastMessage("Не найдена переменная окружения FILE_LOC или не задано имя файла.", true);
        }

    }

    private static void uploadInfo() throws FileNotFoundException, NoSuchFieldException, IllegalAccessException {
        StackInfo stackInfo = VehicleStackXmlParser.parseFromXml(file);
        collection = Objects.requireNonNull(stackInfo).getStack();
        initDateTime = stackInfo.getCreationDate();
        Field field = Vehicle.class.getDeclaredField("maxId");
        field.setAccessible(true);
        field.setInt(null, stackInfo.getMaxId());
    }

    private static boolean prepare() {
        try {
            uploadInfo();
        } catch (FileNotFoundException | NoSuchFieldException | IllegalAccessException | NullPointerException ex) {
            if (ex instanceof NoSuchFieldException || ex instanceof IllegalAccessException || ex instanceof NullPointerException) {
                interactor.broadcastMessage("Возникли проблемы при обработке файла. Данные не считаны.", true);
            }
            initDateTime = ZonedDateTime.now();
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.close();
            } catch (IOException e) {
                interactor.broadcastMessage("Файл не может быть создан, недостаточно прав доступа или формат имени файла неверен.", true);
                interactor.broadcastMessage("Сообщение об ошибке: " + e.getMessage(), true);
                return false;
            }
        }
        return true;
    }

    private static void runInteracting() {
        interactor.broadcastMessage("Для просмотра списка команд введите 'help'.", true);
        boolean run = true;
        while (run) {
            interactor.broadcastMessage("\nВведите команду: ", false);
            String potentialCommand = interactor.getData();
            Command command = CommandRouter.getCommand(potentialCommand, false, interactor, file, initDateTime);
            if (command != null) {
                run = command.execute(collection);
            }
        }
    }

}
