package commands;

import commands.interfaces.Command;
import interact.UserInteractor;
import essentials.Vehicle;

import java.util.Stack;

/**
 * Класс команды вывода сводки.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class Help implements Command {
    private final UserInteractor interactor;

    public Help(UserInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public boolean execute(Stack<Vehicle> stack) {

        interactor.broadcastMessage("Справка по доступным командам:\n" +
                "\n" +
                "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "info_by_id id : вывести в стандартный поток вывода информацию об элементе коллекции\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_first : удалить первый элемент из коллекции\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "reorder : отсортировать коллекцию в порядке, обратном нынешнему\n" +
                "group_counting_by_id : сгруппировать элементы коллекции по значению поля id, вывести количество элементов в каждой группе\n" +
                "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                "sort : сортировать коллекцию, используя сортировку по умолчанию\n" +
                "print_unique_fuel_type : вывести уникальные значения поля fuelType всех элементов в коллекции", true);

        return true;

    }
}
