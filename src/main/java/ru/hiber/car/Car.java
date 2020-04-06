/**
 * Package ru.hiber.car for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.car;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class Car -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 06.04.2020
 */
public class Car {

    private int id;

    private String model;

    private Engine engine;

    private Set<Driver> owners = new HashSet<>();

    public Car() {
    }

    public Car(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Set<Driver> getOwners() {
        return owners;
    }

    public void setOwners(Set<Driver> owners) {
        this.owners = owners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return id == car.id
                && Objects.equals(model, car.model)
                && Objects.equals(engine, car.engine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, engine);
    }

    @Override
    public String toString() {
        return "Car{"
                + "id=" + id
                + ", model='" + model + '\''
                + ", engine=" + engine
                + '}';
    }
}
