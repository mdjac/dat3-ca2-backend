/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.user.UserDTO;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.GenericExceptionMapper;
import facades.UserFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author christianrosenbaek
 */
@Path("user")
public class UserResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @Context
    private UriInfo context;
    
    @Context
    SecurityContext securityContext;

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(String jsonString) throws API_Exception, Exception {
        UserDTO userDTO = gson.fromJson(jsonString, UserDTO.class);
        UserDTO newUserDTO = USER_FACADE.createUser(userDTO);
        return Response.ok().entity(gson.toJson(newUserDTO)).build();
    }
    
    @GET
    @RolesAllowed({"user","admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser() throws API_Exception, Exception {
        String username = securityContext.getUserPrincipal().getName();
        User user = USER_FACADE.getUser(username);
        UserDTO newUserDTO = new UserDTO(user);
        return Response.ok().entity(gson.toJson(newUserDTO)).build();
    }
    
    @PUT
    @RolesAllowed({"user","admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response editUser(String jsonString) throws API_Exception, Exception {
        UserDTO userDTO = gson.fromJson(jsonString, UserDTO.class);
        String username = securityContext.getUserPrincipal().getName();
        if(!(username.equals(userDTO.getUsername()))){
            throw new API_Exception("You cant edit another username than your own");
        }
        UserDTO newUserDTO = USER_FACADE.updateUser(userDTO);
        return Response.ok().entity(gson.toJson(newUserDTO)).build();
    }
}
