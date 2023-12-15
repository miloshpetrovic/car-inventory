package com.smg.carinventory.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smg.carinventory.domain.model.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
	
	public Optional<Manufacturer> findByNameIgnoreCase(String name);
	
}
