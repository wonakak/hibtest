package by.run;

import by.model.shop.*;
import org.hibernate.*;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RunShop {

    private static final Logger log = LoggerFactory.getLogger(RunShop.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    public static void main(String[] args) {
        log.info("_________ HIBERNATE START RUN_SHOP_________");
        setUpSessionFactory();
        sessionWork(false);
        closeSessionFactory();
        destroyRegistry();
        log.info( "_________ FINISHED RUN_SHOP_________" );
    }

    public static void doTxWork(Session session) {
//        populateShop_PC_Lap(session);
//        populateShop_Person_User(session);
//        makeOrder(session);
//        saveOrderItems(session);
//        makeOrderPCSidorX2(session);
//        sqlQueryProdJoinCat(session);
//        sqlQueryUpdateProductSQL(session);
//        sqlQueryAllProducts(session);
//        hqlQueryAllProducts(session);
//        hqlQueryProdNCat(session);
//        hqlQueryJoinWhere(session);
//        criteria1AllProducts(session);
//        criteria2ProdNCat(session);
//        criteria3Category(session);
        validateProduct(session);
    }

    public static void validateProduct(Session session) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Product product = new Product();
        product.setTitle("ABC");
        product.setPrice(new BigDecimal(150));
//        product.setDescription("simple.description@mail"); // @Email
//        product.setDescription("simple description"); // @Pattern
        product.setDescription(" "); // @NotEmpty
        /*
            Для дат ис-ся @Past или @Future, для проверки, что даты
            нах-ся в прошлом или в будщем
         */
        Set<ConstraintViolation<Product>> validate = validator.validate(product);
        for (ConstraintViolation<Product> constr : validate) {
            log.info("\n___val=["+constr.getInvalidValue()+"]"
                    + "\n___msg=["+constr.getMessage()+"]"
                    + "\n___prop=["+constr.getPropertyPath()+"]");
        }
    }

    public static void criteria3Category(Session session) {
        Criteria c = session.createCriteria(Category.class, "cat");
        c.add(Restrictions.like("cat.title", "%Lap%"));
        c.createCriteria("cat.products", "p");
        c.add(Restrictions.ge("p.id", 3L));
        c.setProjection(Projections.distinct(
                Projections.property("products")
        ));
        for (Object obj : c.list()) {
            log.info("_____ CRITERIA 3 ______ >>>\n" + obj+"\n");
        }
    }

    public static void criteria2ProdNCat(Session session) {
        Criteria c = session.createCriteria(Product.class, "p");
        c.createCriteria("p.productCategory", "cat");
        c.add(Restrictions.eq("cat.title", "Printer"));
//        c.setProjection(Projections.distinct(
//            Projections.property("price")
//        ));
//        c.setResultTransformer(Criteria. DISTINCT_ROOT_ENTITY);
//        c.setFirstResult(5); // выбрать начиная с 5-й строки резалта
//        c.setMaxResults(20); // отсчитать 20 строк оттуда.

        for (Object obj : c.list()) {
            log.info("________ CRITERIA _______ >>>\n" + obj+"\n"
//                    + ((Product)obj).getProductCategory()
            );
        }
    }
    public static void criteria1AllProducts(Session session) {
        Long[] ids = {2L, 3L, 4L};
        Criteria c = session.createCriteria(Product.class, "aliasP");
//         select * from product as aliasP where id not in (2, 3, 4)
//        c.add(Restrictions.eq("title", "Compaq Tower"));
//        c.add(Restrictions.in("id", ids));
        c.add(Restrictions.between("price", new BigDecimal(200), new BigDecimal(900)));
//        c.add(Restrictions.like());
        c.add(Restrictions.or(
                Restrictions.not(
                        Restrictions.in("id", ids)
                )
        ));
//        c.add(Restrictions.sqlRestriction("date > 2015-01-01"));
        c.addOrder(Order.asc("price"));

//        log.info("________ CRITERIA ___uniqueResult____ >>>\n" + c.uniqueResult());
        for (Object obj : c.list()) {
            log.info("________ CRITERIA _______ >>>\n" + obj+"\n");
        }
    }


    public static void hqlQueryJoinWhere(Session session) {
        Query q = session.createQuery(
                "from Product p "
                        + "inner join fetch p.productCategory pc "
//                        + "where p.id = 1 "
//                        + "and pc.title like '%PC%'"
                        + "where p.id > ? "
//                        + "and p.productCategory.title like '%Lap%'"
                        + "and p.productCategory.title like :catTitle"
        );
        q.setParameter(0, 1L);
        q.setParameter("catTitle", "%PC%");
        log.info("________ HQL ___uniqueResult____ >>>\n" + q.uniqueResult());

        List<Product> list = q.list();
        for (Product obj : list) {
            log.info("________ HQL _______ >>>\n" + obj + "\n" + obj.getProductCategory());
        }
    }

    public static void hqlQueryAllProducts(Session session) {
        Query q = session.createQuery("from Product ");
        List<Product> ps = q.list();
        for (Product p : ps) {
            log.info("________HQL PRODUCT_____="+p);
        }
        /*
                from Prouduct
                from Prouduct as p
                from Prouduct p
                from Prouduct p, Category c

         */

    }

    public static void hqlQueryProdNCat(Session session) {
        //SELECT * FROM test.product p
        // INNER JOIN test.category pc
        // ON p.product_category_id = pc.id"
        Query q = session.createQuery(
                "from Product p "
//                       + "inner join fetch p.productCategory pc "
                        + "inner join p.productCategory pc "
//                        + "with pc.id = 3"
        );
        /*
            МОЖНО КОМБИНИРОВАТЬ FETCH И МНОГО РАЗНЫХ JOIN-ОВ
                from Product p
                inner join fetch p.productCategory
                inner join fetch p.orderItem
                inner join p.manufacturer
                inner join ...
         */
        // БЕЗ FETCH ВОЗВРАЩАЕТ МАССИВ ОБЪЕКТОВ
        List<Object[]> ps = q.list();
        for (Object[] o : ps) {
            log.info("________HQL_____="+Arrays.toString(o));
        }
//        List<Product> ps = q.list();
//        for (Product o : ps) {
//            log.info("________HQL_____="+o + "\n" + o.getProductCategory());
//        }
    }

    public static void sqlQueryUpdateProductSQL(Session session) {
        SQLQuery q = session.createSQLQuery(
//                "UPDATE test.product p SET title = 'MyDesctop' WHERE p.id = 1");
                "UPDATE test.product p SET title = :newTitle WHERE p.id = :pid");
        q.setParameter("newTitle", "Desctop_Intel");
        q.setParameter("pid", 1);
//        q.setParameter(0, "Desctop_Intel");
//        q.setParameter(1, 1);
        q.executeUpdate();
    }

    public static void sqlQueryProdJoinCat(Session session) {
        SQLQuery q1 = session.createSQLQuery(
                "SELECT * FROM test.product p " +
                        "INNER JOIN test.category pc " +
                        "ON p.product_category_id = pc.id");
        q1.addEntity("p", Product.class);
        q1.addJoin("pc", "p.productCategory");
        List<Object []> l = q1.list();
        for (Object[] obj : l) {
            log.info("__________Prod & Cat____="+Arrays.toString(obj));
        }

        SQLQuery sqlQuery = session.createSQLQuery(
                "SELECT pc.title category, p.title, p.price, p.description " +
                        "FROM test.product p " +
                        "INNER JOIN test.category pc " +
                        "ON p.product_category_id = pc.id");
        List<Object []> list = sqlQuery.list();
        for (Object []obj : list) {
//            for (int i = 0; i < obj.length; i++) {
            log.info("__________ProdJoinCat="+ Arrays.toString(obj));
//            }
        }

        // Тут с ошибками, т.к. совпадают имена колонок.
//        SQLQuery q2 = session.createSQLQuery(
//                "SELECT p.*, pc.* FROM test.product p " +
//                        "INNER JOIN test.category pc " +
//                        "ON p.product_category_id = pc.id");
//        sqlQuery.addEntity("p", Product.class);
//        sqlQuery.addJoin("pc", "p.productCategory");
//        for (Object o : q2.list()) {
//            log.info("__________ProdJoinCat___CAST__="+ o);
//        }
    }

    public static void sqlQueryAllProducts(Session session) {
        SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM test.product p");
        // Для установки параметров
//        sqlQuery.setEntity("p", Product.class);

        // Для установки alias
        sqlQuery.addEntity("p", Product.class);

        List<Product> list = sqlQuery.list();
        for (Product p : list) {
            log.info("__________Product="+p);
        }
    }

    public static void saveOrderItems(Session session) {
        ShopOrder o1 = session.get(ShopOrder.class, 1L);
        ShopOrder o2 = session.get(ShopOrder.class, 2L);
        OrderItem oi1 = new OrderItem();
        OrderItem oi2 = new OrderItem();
        Product p1 = session.get(Product.class, 1L);
        Product p2 = session.get(Product.class, 2L);

        oi1.setShopOrder(o1);
        oi1.setProduct(p1);
        oi1.setSellPrice(p1.getPrice());
        oi1.setCount(1);

        oi2.setShopOrder(o2);
        oi2.setProduct(p2);
        oi2.setSellPrice(p2.getPrice());
        oi2.setCount(2);

        session.save(oi1);
        session.save(oi2);
    }

    public static void makeOrderPCSidorX2(Session session) {
        makeOrderBase(new ShopOrder(),
                session.get(User.class, 1L),
                session.get(Product.class, 1L),
                2, true, session);
    }

    public static void makeOrderBase(ShopOrder o, User u, Product p , int c, boolean s, Session session) {
        o.setUser(u);
        o.setStatus(s);
        OrderItem oi = new OrderItem();
        oi.setShopOrder(o);
        oi.setProduct(p);
        oi.setSellPrice(p.getPrice());
        oi.setCount(c);

        session.save(oi);
        session.save(o);
    }

    public static void makeOrder2(Session session) {
        ShopOrder o1 = new ShopOrder();
        ShopOrder o2 = new ShopOrder();
        OrderItem oi1 = new OrderItem();
        OrderItem oi2 = new OrderItem();
        User u1 = session.get(User.class, 1L);
        User u2 = session.get(User.class, 2L);
        Product p1 = session.get(Product.class, 1L);
        Product p2 = session.get(Product.class, 2L);
        o1.setUser(u1);
        o1.setStatus(false);
        oi1.setShopOrder(o1);
        oi1.setProduct(p1);
        oi1.setSellPrice(p1.getPrice());
        oi1.setCount(1);

        o2.setUser(u2);
        o2.setStatus(true);
        oi2.setShopOrder(o2);
        oi2.setProduct(p2);
        oi2.setSellPrice(p2.getPrice());
        oi2.setCount(2);

        session.save(oi1);
        session.save(oi2);
        session.save(o1);
        session.save(o2);
    }

    public static void populateShop_Person_User(Session session) {
        Person p = new Person("Ivan", "Petrov", 30, new BigDecimal(123000));
        User u = new User();
        u.setPerson(p);
        Person p2 = new Person("Sidor", "Kruglov", 45, new BigDecimal(45600));
        User u2 = new User();
        u2.setPerson(p2);
        session.save(u);
        session.save(u2);
    }

    public static void populateShop_PC_Lap(Session session) {
        Category catRoot = new Category();
        catRoot.setTitle("ROOT");
        Category catElectro = new Category();
        catElectro.setTitle("Electronics");
        Category catPC = new Category();
        catPC.setTitle("PC");
        Category catLaptop = new Category();
        catLaptop.setTitle("Laptop");
        //****************************************
        catElectro.setParentCategory(catRoot);
        catPC.setParentCategory(catElectro);
        catLaptop.setParentCategory(catElectro);
        Product pc1 = new Product("Desctop_Old", "Old computer from 2009",
                new BigDecimal(200), catPC);
        Product lap1 = new Product("Dell Laptop", "Inspiron i5558",
                new BigDecimal(450), catLaptop);
        session.save(pc1);
        session.save(lap1);

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

    private static void setUpSessionFactory() {
        log.info("setUpSessionFactory 1 start");
        if (sessionFactory == null) {
            registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            log.info("setUpSessionFactory 2 registry="+registry);
            try {
                MetadataSources metadataSources = new MetadataSources( registry );
                log.info("setUpSessionFactory 3 metadataSources="+metadataSources);
                Metadata metadata = metadataSources.buildMetadata();
                log.info("setUpSessionFactory 4 metadata="+metadata);
                sessionFactory =  metadata.buildSessionFactory();
                log.info("setUpSessionFactory 5 sessionFactory="+sessionFactory);
            }
            catch (Exception e) {
                log.error("Cannot create SessionFactory !!!", e);
                log.info("setUpSessionFactory 6 destroy");
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
