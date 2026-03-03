package com.autowhouse.itemservice.repository;

import com.autowhouse.itemservice.model.CoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<CoreItem, UUID> {
}
