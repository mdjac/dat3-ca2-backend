/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos.user;

import entities.Workshop;

/**
 *
 * @author mikke
 */
public class WorkShopDTO {
    private Integer id;
    private String name;
    private String city;

    public WorkShopDTO(Workshop workshop) {
        if(workshop.getId() != null){
            this.id = workshop.getId();
        }
        this.name = workshop.getName();
        this.city = workshop.getCity();
    }
    
    
    
}
