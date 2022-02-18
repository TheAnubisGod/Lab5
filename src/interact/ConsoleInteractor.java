package interact;

import java.util.Scanner;

/**
 * Класс получения данных из консоли.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class ConsoleInteractor implements UserInteractor {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getData() {
        return scanner.nextLine();
    }

    @Override
    public void broadcastMessage(String msg, boolean newLine) {
        if (newLine) {
            System.out.println(msg);
        } else {
            System.out.print(msg);
        }
    }

}
