/**
 * Package ru.hiber.logic for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.hiber.model.Item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    private <R> R execute(final Function<Session, R> run) {
        final Session session = FACTORY.openSession();
        final Transaction tr = session.getTransaction();
        tr.begin();
        try {
            R rsl = run.apply(session);
            tr.commit();
            return rsl;
        } catch (HibernateError e) {
            tr.rollback();
            LOG.error("hibernate error", e);
        } finally {
            session.close();
        }
        return null;
    }

    public void save(Item item) {
        LOGIC.execute(ses -> {
            ses.save(item);
            return null;
        });
    }

    public void update(Item item) {
        LOGIC.execute(ses -> {
            ses.update(item);
            return null;
        });
    }

    public Item get(Item item) {
        return LOGIC.execute((ses) -> ses.get(Item.class, item.getId()));
    }

    public void delete(Item item) {
        LOGIC.execute(ses -> {
            ses.delete(item);
            return null;
        });
    }

    public List<Item> getAll() {
        return LOGIC.execute((ses) -> {
            Query q = ses.createQuery("from Item");
            return q.list();
        });
    }

}
