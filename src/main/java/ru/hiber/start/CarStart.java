/**
 * Package ru.hiber.start for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.start;

import com.sun.xml.bind.v2.runtime.reflect.Lister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.hiber.car.Car;
import ru.hiber.car.Driver;
import ru.hiber.car.Engine;

import java.util.Set;

/**
 * Class CarStart -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 06.04.2020
 */
public class CarStart {
    private static final Logger LOG = LogManager.getLogger(CarStart.class.getName());

    public static SessionFactory createFacrory() {
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Car.class);
            configuration.addAnnotatedClass(Engine.class);
            configuration.addAnnotatedClass(Driver.class);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sesfactory = configuration.buildSessionFactory(builder.build());
            return sesfactory;

        } catch (Exception e) {
            LOG.error("Make factory error", e);
        }
        return null;
    }

    private static SessionFactory sesfactory = null;

    public static SessionFactory getFactory() {
        if (sesfactory == null) {
            sesfactory = createFacrory();
        }
        return sesfactory;
    }

    /**
     * Test of creating and adding objects to hibarDB via hibarnate API
     */
    public void create() {
        Session session = CarStart.getFactory().openSession();
        session.getTransaction().begin();
        Car car1 = new Car(1);
        Car car2 = new Car(1);
        Driver driver1 = new Driver(1);
        Driver driver2 = new Driver(2);
        session.save(car1);
        session.save(car2);
        session.save(driver1);
        session.save(driver2);
        car1.getOwners().add(driver1);
        car2.getOwners().add(driver1);
        car2.getOwners().add(driver2);
        session.getTransaction().commit();
    }

    /**
     * Test of updating objects in hibarDB via hibarnate API
     */
    public void update() {
        Session session = CarStart.getFactory().openSession();
        session.getTransaction().begin();
        Driver driver1 = session.get(Driver.class, 1);
        driver1.setName("Volvo");
        Car car1 = session.get(Car.class, 1);
        car1.getOwners().add(driver1);
        session.getTransaction().commit();
    }

    /**
     * Test of deleting objects from hibarDB via hibarnate API
     */
    public void delete() {
        Session session = CarStart.getFactory().openSession();
        session.getTransaction().begin();
        Driver driver1 = session.get(Driver.class, 1);
        session.delete(driver1);
        session.getTransaction().commit();
    }

    public static void main(String[] args) {
        CarStart car = new CarStart();
        car.create();
        car.update();
        car.delete();

    }
}
