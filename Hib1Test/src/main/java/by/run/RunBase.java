package by.run;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunBase {

    private static final Logger log = LoggerFactory.getLogger(RunBase.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    /**
     * Main Run
     */
    public static void main(String[] args) {
        log.info("_________ HIBERNATE START _________");
        setUpSessionFactory();
        sessionWork(false);
        closeSessionFactory();
        destroyRegistry();
        log.info( "_________ FINISHED _________" );
    }

    public static void doTxWork(Session session) {

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
                // можно конкретизировать, на какие исключения делать откат
                log.error("Transaction Exception, it will be ROLLBACKED !!", e);
                if (tx != null) tx.rollback();
            } finally {
                // т.к. при getCurrentSession - она закрывается автоматически
                // после завершения транзакции.
                // иначе, если вручную открывали сессию, то вручную должы
                // её же и закрыть.
                if (openSession) {
                    session.close();
                }
            }
        } else{
            log.error("___________ !!! sessionFactory is NULL !!! ___________");
        }
    }

    private static void setUpSessionFactory() {
        if (sessionFactory == null) {
            registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                destroyRegistry();
            }
        }
    }

    private static void closeSessionFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (HibernateException e) {
                log.error("Error while closing SessionFactory !!", e);
            }
        }
    }

    private static void destroyRegistry() {
        StandardServiceRegistryBuilder.destroy( registry );
    }
}
