/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos.user;

import entities.Car;

/**
 *
 * @author mikke
 */
public class CarDTO {
    private Integer id;
    private String brand;
    private String model;
    private int year;
    private WorkShopDTO workshop;

    public CarDTO(Car car) {
        if(car.getId() != null){
            this.id = car.getId();
        }
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.year = car.getYear();
        this.workshop = new WorkShopDTO(car.getWorkshop());
    }
    
    
    
}
