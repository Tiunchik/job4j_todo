/**
 * Package ru.hiber.logic for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.hiber.model.Item;

import java.util.List;

/**
 * Class ToDoLogic -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 05.04.2020
 */
public enum ToDoLogic {
    LOGIC;

    private static final Logger LOG = LogManager.getLogger(ToDoLogic.class.getName());

    private static final SessionFactory FACTORY = ToDoLogic.getFactory();

    public static ToDoLogic getInstance() {
        return LOGIC;
    }

    private static SessionFactory getFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            LOG.error("Make factory error", e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return null;
    }

    public void save(Item item) {
        try (Session session = FACTORY.openSession()) {
            session.getTransaction().begin();
            session.save(item);
            session.getTransaction().commit();
        }
    }

    public void update(Item item) {
        try (Session session = FACTORY.openSession()) {
            session.getTransaction().begin();
            session.saveOrUpdate(item);
            session.getTransaction().commit();
        }
    }

    public Item get(Item item) {
        Item answer = null;
        try (Session session = FACTORY.openSession()) {
            session.getTransaction().begin();
            answer = session.get(Item.class, item.getId());
            session.getTransaction().commit();
        }
        return answer;
    }

    public void delete(Item item) {
        try (Session session = FACTORY.openSession()) {
            session.getTransaction().begin();
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    public List<Item> getAll() {
        List<Item> asnwer = null;
        try (Session session = FACTORY.openSession()) {
            session.getTransaction().begin();
            asnwer = session.createQuery("from Item").list();
            session.getTransaction().commit();
        }
        return asnwer;
    }

}
