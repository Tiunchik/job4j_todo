package ru.hiber.start;

import org.hibernate.Session;
import org.junit.Test;
import ru.hiber.models.Car;
import ru.hiber.models.Driver;

import static org.junit.Assert.*;

public class CarStartTestWithGetTransaction {

    public void create() {
        Session session = CarStart.getFactory().openSession();
        session.beginTransaction();
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

    @Test
    public void whemWeDoAlThreeMethodThneDontReceiveExeption() {
        try (Session ses = CarStart.getFactory().openSession()) {
            CarStartTestWithGetTransaction test = new CarStartTestWithGetTransaction();
            test.create();
            assertNotNull(ses.load(Car.class, 1));
        }
    }
}