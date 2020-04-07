package ru.hiber.start;

import org.hibernate.Session;
import org.junit.Test;
import ru.hiber.car.Car;
import ru.hiber.car.Driver;

import static org.junit.Assert.*;

public class CarStartTest {

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

    public void update() {
        Session session = CarStart.getFactory().openSession();
        session.getTransaction().begin();
        Driver driver1 = session.get(Driver.class, 1);
        driver1.setName("Volvo");
        Car car1 = session.get(Car.class, 1);
        car1.getOwners().add(driver1);
        session.getTransaction().commit();
    }

    public void delete() {
        Session session = CarStart.getFactory().openSession();
        session.getTransaction().begin();
        Driver driver1 = session.get(Driver.class, 1);
        session.delete(driver1);
        session.getTransaction().commit();
    }

    @Test
    public void whemWeDoAlThreeMethodThneDontReceiveExeption() {
        try (Session ses = CarStart.getFactory().openSession()) {
            CarStartTest test = new CarStartTest();
            test.create();
            assertNotNull(ses.load(Car.class, 1));
            test.update();
            assertEquals("Volvo", ses.load(Driver.class, 1).getName());
            test.delete();
            ses.clear();
            assertNull(ses.get(Driver.class, 1));
        }
    }
}