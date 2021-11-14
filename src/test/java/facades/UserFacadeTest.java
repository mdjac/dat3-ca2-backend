package facades;

import entities.Car;
import utils.EMF_Creator;
import entities.RenameMe;
import entities.Role;
import entities.User;
import entities.Workshop;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static User user,admin,both;
    private static Role userRole,adminRole;
    private static Car c1,c2,c3;
    private static Workshop w1,w2;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = UserFacade.getUserFacade(emf);
    }

    
    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            user = new User("user", "testUser");
            admin = new User("admin", "testAdmin");
            both = new User("user_admin", "testBoth");
            
            userRole = new Role("user");
            adminRole = new Role("admin");
            
            c1 = new Car("Toyota","Aygo",2021);
            c2 = new Car("Mercedes","A200d",2021);
            c3 = new Car("Audi","A8",2021);
            
            w1 = new Workshop("Allans biler","Hellerup");
            w2 = new Workshop("Mehmeds biler","Taastrup");

    
            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            
            user.addCar(c1);
            user.addCar(c2);
            admin.addCar(c1);
            
            c1.setWorkshop(w1);
            w2.addCar(c2);
            c3.setWorkshop(w1);
            
            em.persist(userRole);
            em.persist(adminRole);
            
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            
            em.persist(w1);
            em.persist(w2);
            
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
    }


    // TODO: Delete or change this method 
    @Test
    public void testTrue() throws Exception {
        assertEquals(true, true);
    }

}
