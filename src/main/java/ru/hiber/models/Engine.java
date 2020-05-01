/**
 * Package ru.hiber.car for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.models;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class Утпшту -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 06.04.2020
 */
@Entity
@Table(name = "engines")
public class Engine {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Engine{"
                + "id=" + id
                + ", type='" + type + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Engine engine = (Engine) o;
        return id == engine.id
                && Objects.equals(type, engine.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
