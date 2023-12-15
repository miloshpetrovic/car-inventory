package com.smg.carinventory.infrastructure.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smg.carinventory.domain.model.Model;

public interface ModelRepository extends JpaRepository<Model, Integer> {

	public Collection<Model> findByManufacturerNameIgnoreCase(String name);

	public Optional<Model> findByManufacturerNameIgnoreCaseAndNameIgnoreCase(String manufacturer, String model);
	
}
