package com.sathvik;

import jakarta.persistence.*;

@Entity //allows a class to be marked as a JPA entity, so it can be added to the database
public class Customer {
    @Id //specifies the primary key of the entity. allows for crud database operations

    @SequenceGenerator(name = "customer_id_sequence", sequenceName = "customer_id_sequence",
    allocationSize = 1)

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_sequence")

    private Integer id;
    private String name;
    private int age;

    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Customer() {
        this("Default", 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
