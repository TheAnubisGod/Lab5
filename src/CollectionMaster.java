import essentials.FuelType;
import essentials.StackInfo;
import essentials.VehicleType;
import main.VehicleStackXmlParser;
import main.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Класс управления коллекцией с помощью командной строки.
 *
 * @author Владислав Дюжев
 * @version 1.0
 */
public class CollectionMaster {

    private static Stack<Vehicle> collection = new Stack<>();
    private static ZonedDateTime initDateTime;
    private static boolean run = true;
    private static File file;

    public static void main(String[] args) {
        if (System.getenv("FILE_LOC") != null && !System.getenv("FILE_LOC").trim().isEmpty()) {
            file = new File(System.getenv("FILE_LOC"));

            try {
                Scanner fileScanner = new Scanner(file);
                StringBuilder xml = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    xml.append(fileScanner.nextLine());
                }

                StackInfo stackInfo = VehicleStackXmlParser.parseFromXml(xml.toString());
                collection = Objects.requireNonNull(stackInfo).getStack();
                initDateTime = stackInfo.getCreationDate();
                Field field = Vehicle.class.getDeclaredField("maxId");
                field.setAccessible(true);
                field.setInt(null, stackInfo.getMaxId());
            } catch (FileNotFoundException | NoSuchFieldException | IllegalAccessException | NullPointerException ex) {
                if (ex instanceof NoSuchFieldException || ex instanceof IllegalAccessException || ex instanceof NullPointerException) {
                    System.out.println("Возникли проблемы при обработке файла. Данные не считаны.");
                }
                initDateTime = ZonedDateTime.now();
                FileWriter fileWriter;
                try {
                    fileWriter = new FileWriter(file);
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Файл не может быть создан, недостаточно прав доступа или формат имени файла неверен.");
                    System.out.println("Сообщение об ошибке: " + e.getMessage());
                    return;
                }
            }

            Scanner commandScanner = new Scanner(System.in);
            System.out.println("Для просмотра списка команд введите 'help'.");

            while (run) {
                System.out.print("\nВведите команду: ");
                String potentialCommand = commandScanner.nextLine();
                proceedCommand(potentialCommand, false, new Scanner(System.in));
            }
        } else {
            System.out.println("Не найдена переменная окружения FILE_LOC или не задано имя файла.");
            run = false;
        }

    }

    private static void proceedCommand(String potentialCommand, boolean from_script, Scanner argumentScanner) {
        potentialCommand = potentialCommand.trim();
        String[] commandParts = potentialCommand.split("\\s+");
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
                Commander.help();
                break;
            case "info":
                Commander.info();
                break;
            case "show":
                Commander.show();
                break;
            case "add":
                Commander.add(from_script, argumentScanner);
                break;
            case "update":
                if (Args.size() == 0) {
                    System.out.println("Отсутствуют необходимые параметры.");
                    return;
                }
                Commander.update(Args.get(0), from_script, argumentScanner);
                break;
            case "remove_by_id":
                if (Args.size() == 0) {
                    System.out.println("Отсутствуют необходимые параметры.");
                    return;
                }
                Commander.removeById(Args.get(0));
                break;
            case "clear":
                Commander.clear();
                break;
            case "save":
                Commander.save();
                break;
            case "execute_script":
                if (Args.size() == 0) {
                    System.out.println("Отсутствуют необходимые параметры.");
                    return;
                }
                Commander.script(Args.get(0));
                break;
            case "exit":
                Commander.exit();
                break;
            case "remove_first":
                Commander.remove_first();
                break;
            case "add_if_min":
                Commander.addIfMin(from_script, argumentScanner);
                break;
            case "reorder":
                Commander.reorder();
                break;
            case "group_counting_by_id":
                Commander.groupCountingById();
                break;
            case "filter_starts_with_name":
                if (Args.size() == 0) {
                    System.out.println("Отсутствуют необходимые параметры.");
                    return;
                }
                String nameStart = Args.get(0);
                Commander.filterStartsWithName(nameStart);
                break;
            case "print_unique_fuel_type":
                Commander.printUniqueFuelType();
                break;
            case "sort":
                Commander.sort();
                break;

            case "info_by_id":
                if (Args.size() == 0) {
                    System.out.println("Отсутствуют необходимые параметры.");
                    return;
                }
                Commander.infoById(Args.get(0));
                break;
            default:
                System.out.println("Команды '" + command + "' не существует. " +
                        "Воспользуйтесь 'help' для получения списка команд.");
        }
    }

    /**
     * Класс взаимодействия с пользователем посредством командной строки.
     *
     * @author Владислав Дюжев
     * @version 1.0
     */
    static abstract class Commander {

        private static int getIndexById(int id) throws Exception {
            for (Vehicle vehicle : collection) {
                if (vehicle.getId() == id) {
                    return collection.indexOf(vehicle);
                }
            }
            throw new Exception("Элемента с таким id не существует в коллекции.");
        }

        private static void chooseVehicleType(Vehicle vehicle, boolean from_script, Scanner argumentScanner) {
            boolean res = false;
            VehicleType vehicleType;
            if (!from_script) {
                System.out.print("Доступные типы транспорта: ");

                for (int i = 0; i < VehicleType.values().length; i++) {
                    System.out.print(VehicleType.values()[i].toString() + " ");
                }
                System.out.println();
            }

            while (!res) {
                if (!from_script) {
                    System.out.print("Выберете тип транспорта (поле type): ");
                }
                String vehicleTypeName = argumentScanner.nextLine();
                vehicleType = VehicleType.getByName(vehicleTypeName);
                try {
                    vehicle.setType(vehicleType);
                } catch (Exception e) {
                    System.out.println("Такого типа транспорта не существует.");
                    continue;
                }
                res = true;
            }
        }

        private static void chooseFuelType(Vehicle vehicle, boolean from_script, Scanner argumentScanner) {
            boolean res = false;
            FuelType fuelType;
            if (!from_script) {
                System.out.print("Доступные типы топлива: ");

                for (int i = 0; i < FuelType.values().length; i++) {
                    System.out.print(FuelType.values()[i].toString() + " ");
                }
                System.out.println();
            }

            while (!res) {
                if (!from_script) {
                    System.out.print("Выберете тип топлива (поле fuelType): ");
                }
                String fuelTypeName = argumentScanner.nextLine();
                if (fuelTypeName.isEmpty()) {
                    vehicle.setFuelType(null);
                    return;
                }
                fuelType = FuelType.getByName(fuelTypeName);
                if (fuelType != null) {
                    vehicle.setFuelType(fuelType);
                } else {
                    System.out.println("Такого типа топлива не существует.");
                    continue;
                }
                res = true;
            }
        }

        private static void chooseName(Vehicle vehicle, boolean from_script, Scanner argumentScanner) {
            boolean res = false;
            while (!res) {
                if (!from_script) {
                    System.out.print("Дайте название транспорту: ");
                }
                try {
                    vehicle.setName(argumentScanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Название не должно являться пустой строкой.");
                    continue;
                }

                res = true;
            }
        }

        private static void chooseEnginePower(Vehicle vehicle, boolean from_script, Scanner argumentScanner) {
            boolean res = false;
            while (!res) {
                if (!from_script) {
                    System.out.print("Введите мощность двигателя: ");
                }
                try {
                    vehicle.setEnginePower(argumentScanner.nextFloat());
                } catch (InputMismatchException e) {
                    System.out.println("Неверный формат ввода");
                    continue;
                } catch (Exception e) {
                    System.out.println("Мощность двигателя не может быть отрицательной.");
                    continue;
                }

                res = true;
            }
        }

        private static void chooseCoordinates(Vehicle vehicle, boolean from_script, Scanner argumentScanner) {
            boolean res = false;
            argumentScanner.nextLine();
            while (!res) {
                if (!from_script) {
                    System.out.print("Введите начальные координаты через пробел (х у): ");
                }
                String potentialCords = argumentScanner.nextLine();
                potentialCords = potentialCords.trim();
                String[] cords = potentialCords.split("\\s+");
                ArrayList<String> Cords = new ArrayList<>();
                for (String cord : cords) {
                    String arg = cord.replaceAll("\\s+", "");
                    if (!arg.isEmpty()) {
                        Cords.add(arg);
                    }
                }
                if (Cords.size() != 2) {
                    System.out.println("Неверный формат ввода.");
                    continue;
                }

                long X;
                double Y;

                try {
                    X = Long.parseLong(Cords.get(0));
                    Y = Double.parseDouble(Cords.get(1));
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат ввода.");
                    continue;
                }

                try {
                    vehicle.setCoordinates(X, Y);
                } catch (Exception e) {
                    System.out.println("Координаты введены в неверном формате. Максимальное значение у 849.");
                    continue;
                }

                res = true;
            }
        }

        private static Vehicle createVehicle(boolean from_script, Scanner argumentScanner) {
            Vehicle vehicle = new Vehicle();

            chooseVehicleType(vehicle, from_script, argumentScanner);
            chooseFuelType(vehicle, from_script, argumentScanner);
            chooseName(vehicle, from_script, argumentScanner);
            chooseEnginePower(vehicle, from_script, argumentScanner);
            chooseCoordinates(vehicle, from_script, argumentScanner);

            vehicle.generateId();

            return vehicle;
        }

        private static int idArgToIndex(String argument) {
            int id;
            try {
                id = Integer.parseInt(argument);
            } catch (NumberFormatException e) {
                System.out.println("Неверный аргумент. Ожидается число (id).");
                return -1;
            }

            int index;

            try {
                index = getIndexById(id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return -1;
            }

            return index;
        }

        public static void help() {
            System.out.println("Справка по доступным командам:\n" +
                    "\n" +
                    "help : вывести справку по доступным командам\n" +
                    "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
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
                    "print_unique_fuel_type : вывести уникальные значения поля fuelType всех элементов в коллекции");
        }

        public static void info() {
            System.out.println("Важная информация о коллекции:\n" +
                    "\n" +
                    "Тип: " + Vehicle.class.getName() + "\n" +
                    "Дата инициализации: " + initDateTime.toString() + "\n" +
                    "Максимальный id: " + Vehicle.getMaxId() + "\n" +
                    "Количество элементов: " + collection.size());

        }

        public static void show() {
            if (collection.size() == 0) {
                System.out.println("В коллекции нет элементов.");
            } else {
                System.out.println("Все элементы коллекции:");
            }
            for (Vehicle vehicle : collection) {
                System.out.println(vehicle.toString());
            }
            System.out.println("Всего: " + collection.size() + ".");
        }

        public static void add(boolean from_script, Scanner argumentScanner) {
            System.out.println("Добавление элемента в коллекцию.");
            collection.add(createVehicle(from_script, argumentScanner));
            System.out.println("Элемент успешно добавлен.");
        }

        public static void update(String argument, boolean from_script, Scanner argumentScanner) {
            int index = idArgToIndex(argument);
            if (index == -1) {
                return;
            }
            collection.remove(index);
            collection.add(index, createVehicle(from_script, argumentScanner));
            System.out.println("Элемент успешно обновлен.");
        }

        public static void removeById(String argument) {
            int index = idArgToIndex(argument);
            if (index == -1) {
                return;
            }

            collection.remove(index);
            System.out.println("Элемент успешно удален.");
        }

        public static void clear() {
            collection.clear();
            System.out.println("Коллекция очищена.");
        }

        public static void save() {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(VehicleStackXmlParser.stackToXml(new StackInfo(collection, Vehicle.getMaxId(), initDateTime)));
                fileWriter.flush();
            } catch (Exception e) {
                System.out.println("Возникла ошибка при сохранении в файл: " + e.getMessage());
                return;
            }
            System.out.println("Файл успешно сохранен.");
        }

        public static void script(String argument) {
            File f = new File(argument);

            Scanner fileScanner;
            try {
                fileScanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                System.out.println("Такого файла не существует.");
                return;
            }
            int line_num = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                try {
                    proceedCommand(line, true, fileScanner);
                } catch (Exception e) {
                    System.out.println("Возникла ошибка при выполнении " + line_num + " строки:\n" + line);
                    break;
                }
                line_num++;
            }
        }

        public static void exit() {
            run = false;
        }

        public static void remove_first() {
            if (collection.size() == 0) {
                System.out.println("В коллекции нет элементов.");
                return;
            }
            collection.remove(0);
            System.out.println("Элемент успешно удален.");
        }

        public static void addIfMin(boolean from_script, Scanner argumentScanner) {
            Vehicle vehicle = createVehicle(from_script, argumentScanner);
            if (collection.isEmpty() || vehicle.compareTo(Collections.min(collection)) < 0) {
                collection.add(vehicle);
                System.out.println("Элемент успешно добавлен.");
            } else {
                System.out.println("Элемент не минимальный.");
            }
        }

        public static void reorder() {
            Collections.reverse(collection);
            System.out.println("Порядок коллекции инвертирован.");
        }

        public static void groupCountingById() {
            for (Vehicle vehicle : collection) {
                System.out.println("1 элемент со значением id=" + vehicle.getId() + ".");
            }
        }

        public static void filterStartsWithName(String nameStart) {
            System.out.println("Все элементы, чье название начинается с " + nameStart + ":");
            int num = 0;
            for (Vehicle vehicle : collection) {
                if (vehicle.getName().startsWith(nameStart)) {
                    System.out.println(vehicle);
                    num++;
                }
            }
            System.out.println("Всего: " + num + ".");
        }

        public static void printUniqueFuelType() {
            System.out.println("Уникальные типы топлива:");
            HashSet<FuelType> hashSet = new HashSet<>();
            int num = 0;
            for (Vehicle vehicle : collection) {
                if (!hashSet.contains(vehicle.getFuelType())) {
                    hashSet.add(vehicle.getFuelType());
                    System.out.println(vehicle.getFuelType().toString());
                    num++;
                }
            }
            System.out.println("Всего: " + num + ".");
        }

        public static void sort() {
            collection.sort(Comparator.naturalOrder());
            System.out.println("Коллекция отсортирована.");
        }

        public static void infoById(String argument) {
            int index = idArgToIndex(argument);
            if (index == -1) {
                return;
            }
            Vehicle vehicle = collection.get(index);
            String info = String.format("id: %d \n" +
                            "Название: %s \n" +
                            "Тип: %s \n" +
                            "Дата создания: %s \n" +
                            "Мощность: %s \n" +
                            "Тип топлива: %s \n" +
                            "Координаты: %s", vehicle.getId(), vehicle.getName(), vehicle.getType(),
                    vehicle.getCreationDate(), vehicle.getEnginePower(), vehicle.getFuelType(),
                    vehicle.getCoordinates());
            System.out.println(info);
        }
    }


}
