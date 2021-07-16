package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@FilterDef(name = "Id_Better", parameters = {
        @ParamDef(name = "id", type = "integer")
})
@FilterDef(name = "Id_Low", parameters = {
        @ParamDef(name = "id", type = "integer")
})
@FilterDef(name = "Id_Equal", parameters = {
        @ParamDef(name = "id", type = "integer")
})
@FilterDef(name = "Id_Other", parameters = {
        @ParamDef(name = "id", type = "integer")
})

@FilterDef(name = "Name", parameters = {
        @ParamDef(name = "name", type = "string")
})

@FilterDef(name = "Price_Better", parameters = {
        @ParamDef(name = "price", type = "integer")
})
@FilterDef(name = "Price_Low", parameters = {
        @ParamDef(name = "price", type = "integer")
})
@FilterDef(name = "Price_Equal", parameters = {
        @ParamDef(name = "price", type = "integer")
})
@FilterDef(name = "Price_Other", parameters = {
        @ParamDef(name = "price", type = "integer")
})

@FilterDef(name = "Manufacturer", parameters = {
        @ParamDef(name = "manufacturer", type = "string")
})

@Filters({
        @Filter(name = "Id_Better", condition = ":id < id_product"),
        @Filter(name = "Id_Low", condition = ":id > id_product"),
        @Filter(name = "Id_Equal", condition = ":id = id_product"),
        @Filter(name = "Id_Other", condition = ":id <= id_product"),

        @Filter(name = "Price_Better", condition = ":price < price"),
        @Filter(name = "Price_Low", condition = ":price > price"),
        @Filter(name = "Price_Equal", condition = ":price = price"),
        @Filter(name = "Price_Other", condition = ":price <= price"),

        @Filter(name = "Name", condition = "name_product LIKE :name"),
        @Filter(name = "Manufacturer", condition = "manufacturer LIKE :manufacturer")
})

@Entity
@Table(name = "Products")
public class Element extends PanacheEntity{

    @Column(name  = "ID_product")
    private int idprod;

    @Column(name = "name_product")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "manufacturer")
    private String manufacturer;

    public Element() {};

    public Element(int idprod, String name, int price, String manufacturer){
        this.idprod = idprod;
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public int getIdprod() {
        return idprod;
    }

    public void setIdprod(int idprod) {
        this.idprod = idprod;
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
