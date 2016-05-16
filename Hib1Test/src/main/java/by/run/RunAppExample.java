package by.run;

import by.model.office.Department;
import by.model.shop.Person;
import by.model.shop.Role;
import by.model.shop.User;
import org.hibernate.*;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RunAppExample
{
    private static final Logger log = LoggerFactory.getLogger(RunAppExample.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    public static void main( String[] args ) {
        log.info("_________ HIBERNATE START _________");
        setUp();
        sessionWork(false);
//        testEqualsHashcode();
        destroy();
        log.info( "_________ FINISHED _________" );
    }

    /**
     * Create work in transaction
     * @param session
     */
    public static void doTxWork(Session session) {
//        readSameUser(session);
//        dynamicModels(session);
//        getRefByID(session);
//        getByID(session);
//        printTables(session);
        //**********************************
//            createNewPerson(session);
//            assignRole2Person(session);
//        createNewUser121Person(session);
    }

    public static void createNewUser121Person(Session session) {
        User uu = new User();
//        uu.setSalary(45600);
        Person p1 = session.get(Person.class, 1L);
        uu.setPerson(p1);
        session.save(uu);
    }

    public static void assignRole2Person(Session session) {
//        Person p1 = session.get(Person.class, 2L);
        Role role = session.get(Role.class, 4L);

        Person p1 = new Person("Admin", "Admin", 999);

//        Role role = new Role("ADMIN");
//        session.save(role);
//        role = new Role("Discount");
//        session.save(role);
//        role = new Role("Basic");
//        session.save(role);
//        role = new Role("Advanced");
//        session.save(role);
//        p1.setRole(role);
        session.save(p1);
    }

    public static void createNewPerson(Session session) {
        Person p1 = new Person("Ivan", "Petrov", 24);
        Person p2 = new Person("Sidor", "Kruglov", 33);
        session.save(p1);
        session.save(p2);
    }

    public static void getByID(Session session) {
        User user = session.get(User.class, 1L);
//        user.setName("Denis");
//        user.setLastName("Malyarevich");
//        session.flush();
    }

    public static void getRefByID(Session session) {
        IdentifierLoadAccess<User> loadAccess = session.byId(User.class);
        User uref = loadAccess.getReference(3L);
        log.info("User1 Ref={}", uref);
        User aUser = (User) session.byNaturalId( User.class )
                .using( "name", "Den" )
                .using( "lastName", "Maliar" )
                .getReference();
        log.info("User2 Ref={}", aUser);
        SimpleNaturalIdLoadAccess<Department> naturalIDLoadAccess = session.bySimpleNaturalId(Department.class);
        Department dep = naturalIDLoadAccess.getReference("home");
        log.info("Department Ref={}", dep);
    }

    public static void dynamicModels(Session session) {
//        Hibernate Domain Model Mapping
//        The mapping of dynamic models

        // Create a customer entity
        Map david = new HashMap();
        david.put("name", "David");

// Create an organization entity
        Map foobar = new HashMap();
        foobar.put("name", "Foobar Inc.");

// Link both
        david.put("deps", foobar);

// Save both
        session.save("deps", foobar);
        session.save("users", david);
    }

    public static void readSameUser(Session session) {
        User u1 = session.get(User.class, 1L);
        log.info("_________ u1={}", u1);
        User u2 = session.get(User.class, 1L);
        log.info("_________ u2={}", u2);
        log.info("_________ u1 eq u2 ={}", u1.equals(u2));
        log.info("_________ hash u1 eq u2 ={}", u1.hashCode() == u2.hashCode());

    }
    public static void printTables(Session session) {
        log.info( "_________ printTables _________" );

        printList(session.createCriteria(User.class).list());
        printList(session.createCriteria(Department.class).list());

        printList(session.createQuery("from User ").list());
//        printList(session.createQuery("from Department ").list());
    }

    public static void printList(List list) {
        for (Object obj : list) {
            log.info("\n\n-----------> {}\n", obj);
        }
    }

    public static void sessionWork(boolean openSession) {
        if (sessionFactory != null) {
            Session session;
            if (openSession) {
                session = sessionFactory.openSession();
            } else {
                session = sessionFactory.getCurrentSession();
            }
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                doTxWork(session);

                tx.commit();
            }
            catch (Exception e) {
                if (tx!=null) tx.rollback();
                e.printStackTrace();
            } finally {
                if (openSession) {
                    session.close();
                }
            }

        } else {
            log.error("___________ !!! sessionFactory is NULL !!! ___________");
        }
    }
    public static void testEqualsHashcode() {
        if (sessionFactory != null) {
            Session session = sessionFactory.getCurrentSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                User u1 = session.get(User.class, 1L);
                User u2 = session.get(User.class, 2L);
//                User u1 = new User("user1");
//                User u2 = new User("user2");
                Department d1 = session.get(Department.class, 1L);
                log.info("_____in tx____ u2={}", u2);
                log.info("_____in tx____ u1={}", u1);
                log.info("_____in tx____ d1={}", d1);
                log.info("_____in tx____ d1 users={}",d1.getUsers());
                d1.getUsers().add(u1);
                d1.getUsers().add(u2);
                tx.commit();

                log.info("__after tx__session_____ isOpen={}", session.isOpen());
                log.info("__after tx__session_____ isConnected={}", session.isConnected());

                log.info("____after tx_____ u2={}", u2);
                log.info("____after tx_____ u1={}", u1);
                log.info("____after tx_____ d1={}", d1);
                boolean contains = d1.getUsers().contains(u2);
                log.info("____after tx_____d1 contains u2={}", contains);
                contains = d1.getUsers().contains(u1);
                log.info("____after tx_____d1 contains u1={}", contains);
                log.info("____after tx____ d1 users={}",d1.getUsers());

            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            }
        }
    }

    private static void setUpJPA() {

    }

    private static void setUp2() {
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure( "org/hibernate/example/MyCfg.xml" )
                .build();

        Metadata metadata = new MetadataSources( standardRegistry )
                .addAnnotatedClass( User.class )
                .addAnnotatedClassName( "by.model.office.Department" )
                .addResource( "org/hibernate/example/ShopOrder.hbm.xml" )
                .addResource( "org/hibernate/example/Product.orm.xml" )
                .getMetadataBuilder()
                .applyImplicitNamingStrategy( ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
                .build();

        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
//                .applyBeanManager( getBeanManagerFromSomewhere() )
                .build();
    }

    private static void setUp() {
        //http://docs.jboss.org/hibernate/orm/5.0/quickstart/html/

        if (sessionFactory == null) {
            // A SessionFactory is set up once for an application!
            registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                // The registry would be destroyed by the SessionFactory,
                // but we had trouble building the SessionFactory
                // so destroy it manually.
                StandardServiceRegistryBuilder.destroy( registry );
            }

//        Configuration configuration = new Configuration().configure("hibernate_sp.cfg.xml");
//
//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties()).build();
//        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

//        StandardServiceRegistryBuilder serviceRegistry = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties());
//        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry.build());
        }
    }

    private static void destroy() {
        StandardServiceRegistryBuilder.destroy( registry );
    }

}
