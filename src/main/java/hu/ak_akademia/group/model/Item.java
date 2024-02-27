package hu.ak_akademia.group.model;

import jakarta.persistence.*;

@Entity
public class Item {
    // Entititások, adattároló osztály

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // @Column(name = "good_name")
    private String name;
    private Double quantity;
    private String qtyUnit;
    @ManyToOne
    private Shop shop;

    public Item() {}

    public Item(String name, Double quantity, String qtyUnit) {
        this.name = name;
        this.quantity = quantity;
        this.qtyUnit = qtyUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
