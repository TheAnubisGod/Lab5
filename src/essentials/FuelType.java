package essentials;

import java.util.Objects;


/**
 * Enum описывающий виды топлива.
 *
 * @version 1.0
 * @author Владислав Дюжев
 */
public enum FuelType {
    GASOLINE("GASOLINE"),
    KEROSENE("KEROSENE"),
    DIESEL("DIESEL"),
    MANPOWER("MANPOWER"),
    PLASMA("PLASMA");

    private String name;

    private FuelType(String name) {
        this.name = name;
    }

    public static FuelType getByName(String name) {
        for (FuelType fuelType : FuelType.values()) {
            if (Objects.equals(fuelType.name, name)) {
                return fuelType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
