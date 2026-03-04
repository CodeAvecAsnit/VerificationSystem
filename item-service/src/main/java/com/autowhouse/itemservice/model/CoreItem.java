package com.autowhouse.itemservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "item_table")
@AllArgsConstructor
@NoArgsConstructor
public class CoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;

    @Column(name = "item_name",length = 30)
    private String itemName;

    @Column(name = "price")
    private float itemPrice;
}
