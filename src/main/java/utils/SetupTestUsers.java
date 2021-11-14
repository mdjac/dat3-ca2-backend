package utils;

import entities.Car;
import entities.Role;
import entities.User;
import entities.Workshop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

    public static void main(String[] args) {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
        User user = new User("user", "testUser");
        User admin = new User("admin", "testAdmin");
        User both = new User("user_admin", "testBoth");

        Car c1 = new Car("Toyota", "Aygo", 2021);
        Car c2 = new Car("Mercedes", "A200d", 2021);
        Car c3 = new Car("Audi", "A8", 2021);

        Workshop w1 = new Workshop("Allans biler", "Hellerup");
        Workshop w2 = new Workshop("Mehmeds biler", "Taastrup");

        if (admin.getUserPass().equals("test") || user.getUserPass().equals("test") || both.getUserPass().equals("test")) {
            throw new UnsupportedOperationException("You have not changed the passwords");
        }

        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
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

        em.persist(c1);
        em.persist(c2);
        em.persist(c3);

        em.persist(w1);
        em.persist(w2);

        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.persist(both);
        em.getTransaction().commit();
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

    }

}
