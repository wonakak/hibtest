package by.run;

import by.model.Department;
import by.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class App
{
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    private static void setUp() {
        // A SessionFactory is set up once for an application!
        registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
    }

    private static void destroy() {
            StandardServiceRegistryBuilder.destroy( registry );
    }

    public static void main( String[] args )
    {
        log.info("Hello World!");
        setUp();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        //////////////////////////////////////////////////////////

        Criteria criteria = session.createCriteria(User.class);
        print(criteria.list());
        criteria = session.createCriteria(Department.class);
        print(criteria.list());

        //////////////////////////////////////////////////////////
        session.getTransaction().commit();
        destroy();
        log.info( "FINISHED" );
    }

    public static void print(List list) {
        for (Object obj : list) {
            log.info("\n\n-----------> {}\n", obj);
        }
    }

}
