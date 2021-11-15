package facades;

import dtos.user.UserDTO;
import entities.Car;
import entities.User;
import entities.Role;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    public User getUser(String username) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new API_Exception("User not found");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    
    
    public UserDTO createUser(UserDTO userDTO) throws Exception {
       User user = userDTO.getEntity();
       EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            //Throws error if username exists
            if(em.find(User.class, user.getUserName()) != null){
                throw new API_Exception("Username already exists",404);
            }
            //Makes sure roles is managed objects and checks that it exist
            for (int i = 0; i < user.getRoleList().size(); i++) {
                Role role = user.getRoleList().get(i);
                role = em.find(Role.class, role.getRoleName());
                if(role == null){
                    throw new NotFoundException("Role doesn't exist");
                }
            }
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }
    
    public UserDTO updateUser (UserDTO userDto) throws API_Exception {
        User user = userDto.getEntity();
        if (user.getUserName() == null)
            throw new API_Exception("No User can be updated when id is missing");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
            
            //Used to make sure that if receiving a DTO with empty password that we do not override the users current password with null
            if(userDto.getPassword() == null){
                User newUser = em.find(User.class, userDto.getUsername());
                user.setUserPass(newUser.getUserPass());
            }
            
        user.getCars().forEach(car->{
          if(car.getId() != null){
            Car tmpCar = em.find(Car.class, car.getId());
            car.setUsers(tmpCar.getUsers());
            car.addUser(user);
          }
          em.merge(car);
        });
        User updatedUser = em.merge(user);
        
        em.getTransaction().commit();
        return new UserDTO(updatedUser);
    }

}
