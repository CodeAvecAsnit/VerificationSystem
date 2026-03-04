package com.autowhouse.orderservice.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "test_table")
public class TestClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "test_name",length = 50)
    private String testName;
}
