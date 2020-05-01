/**
 * Package ru.hiber.car for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class Driver -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 06.04.2020
 */
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "history", joinColumns = {
            @JoinColumn(name = "car_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "driver_id",
                    nullable = false, updatable = false) })
    private Set<Car> cars = new HashSet<>();

    public Driver() {
    }

    public Driver(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Driver driver = (Driver) o;
        return id == driver.id
                && Objects.equals(name, driver.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Driver{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
