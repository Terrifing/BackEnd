package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.enterprise.inject.Default;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "Products")
public class Element extends PanacheEntity{

    @Column(name  = "id_product")
    private int ID;

    @Column(name = "name_product")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "manufacturer")
    private String manufacturer;

    public Element() {};

    public Element(int id, String elem, int price, String manufacturer){
        this.ID = id;
        this.name = elem;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
