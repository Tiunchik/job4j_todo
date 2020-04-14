/**
 * Package ru.hiber.prog for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.prog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.hiber.model.*;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class HiberDB - class for work with hibernate api
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 07.04.2020
 */
public enum HiberDB {
    HIBER_DB;

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(HiberDB.class.getName());

    /**
     * error message for saving operations
     */
    private static final String LOAD_ERRORS = "Error to save into DB";

    /**
     * info message for query requests to DB
     */
    private static final String NO_ENTITY_FOUND_FOR_QUERY = "No entity found for query";

    /**
     * Hibernate Session Factory
     */
    private static final SessionFactory FACTORY = createFactory();

    /**
     * Create session factory during programm uploading
     *
     * @return SessionFactory
     */
    private static SessionFactory createFactory() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("herokuhibernate.cfg.xml") // or herokuhibernate.cfg.xml
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Comment.class)
                .addAnnotatedClass(Advertisement.class)
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(RightGroup.class)
                .addAnnotatedClass(Engine.class)
                .addAnnotatedClass(Company.class)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();
    }

    /**
     * return Enum HIBER_DB
     *
     * @return Enum HIBER_DB
     */
    public static HiberDB getBD() {
        return HIBER_DB;
    }

    /**
     * return created SessionFactory
     *
     * @return SessionFactory
     */
    public SessionFactory getFactory() {
        return FACTORY;
    }

    /**
     * method for functional construction another methods without receiving information
     *
     * @param action Session method
     */
    public void baseAction(Consumer<Session> action) {
        Transaction tran = null;
        try (Session session = HIBER_DB.getFactory().openSession()) {
            tran = session.getTransaction();
            tran.begin();
            action.accept(session);
            tran.commit();
        } catch (Exception e) {
            if (tran != null) {
                tran.rollback();
            }
            LOG.error(LOAD_ERRORS, e);
        }
    }

    /**
     * method for functional construction another methods with receiving information
     *
     * @param action Session method and type of returning value
     */
    public <E> E baseQuaery(Function<Session, E> action) {
        E answer = null;
        try (Session session = HIBER_DB.getFactory().openSession()) {
            answer = action.apply(session);
        } catch (NoResultException e) {
            LOG.info(NO_ENTITY_FOUND_FOR_QUERY);
        }
        return answer;
    }

    public void addCompany(Company comp) {
        baseAction(session -> session.persist(comp));
    }

    public List<Company> getCompanyList() {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Company");
            return (List<Company>) query.getResultList();
        });
    }

    public Company getCompany(Company comp) {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Company AS a WHERE a.name= :name");
            query.setParameter("name", comp.getName());
            return (Company) query.getSingleResult();
        });
    }

    public List<String> getModels(Company comp) {
        return baseQuaery(session -> session
                .get(Company.class, comp.getId())
                .getModels()
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    public void addNewUser(User user) {
        baseAction(session -> session.persist(user));
    }

    public User getUser(User user) {
        return baseQuaery(session -> session.get(User.class, user.getId()));
    }

    public void updateUser(User user) {
        baseAction(session -> session.update(user));
    }

    public User getUserByLogin(User user) {
        return (User) baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.User AS u WHERE u.login= :login");
            query.setParameter("login", user.getLogin());
            return query.getSingleResult();
        });
    }

    public void addNewAd(Advertisement adv) {
        baseAction(session -> session.persist(adv));
    }

    public List<Advertisement> getAdList(User user) {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Advertisement AS a WHERE a.creator.id= :id");
            query.setParameter("id", user.getId());
            return (List<Advertisement>) query.getResultList();
        });
    }

    public List<Advertisement> getAllList() {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.Advertisement");
            return (List<Advertisement>) query.getResultList();
        });
    }

    public void updateAdv(Advertisement adv) {
        baseAction(session -> session.update(adv));
    }

    public void addNewCar(Car car) {
        baseAction(session -> session.persist(car));
    }

    public void updateCar(Car car) {
        baseAction(session -> session.update(car));
    }

    public void addNewEng(Engine eng) {
        baseAction(session -> session.persist(eng));
    }

    public void updateEng(Engine eng) {
        baseAction(session -> session.update(eng));
    }

    public Advertisement getAdd(Advertisement adv) {
        return baseQuaery(session -> session.get(Advertisement.class, adv.getId()));
    }

    public Car getCar(Car car) {
        return baseQuaery(session -> session.get(Car.class, car.getVin()));
    }

    public RightGroup gerRights(String nameGroup) {
        return baseQuaery(session -> {
            Query query = session.createQuery("FROM ru.hiber.model.RightGroup AS r WHERE r.groupName= :group");
            query.setParameter("group", nameGroup);
            return (RightGroup) query.getSingleResult();
        });
    }

    public List<Advertisement> getfilteredAdd(HashMap<String, String> keys) {
        StringBuilder builder = new StringBuilder("from Advertisement as adv where ");
        keys.keySet().forEach(e -> {
            builder.append("adv.").append(e);
            if (e.equalsIgnoreCase("created")) {
                builder.append(" > ");
            } else {
                builder.append(" = ");
            }
            builder.append(keys.get(e)).append(" and ");
        });
        String requestLine = builder.toString().substring(0, builder.toString().length() - 4);
        return (List<Advertisement>) baseQuaery(session -> session.createQuery(requestLine).list());
    }

}
