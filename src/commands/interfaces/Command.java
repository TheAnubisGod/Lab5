package commands.interfaces;

import essentials.Vehicle;

import java.util.Stack;

/**
 * Общий интерфейс для команд.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public interface Command {
    boolean execute(Stack<Vehicle> stack);
}
