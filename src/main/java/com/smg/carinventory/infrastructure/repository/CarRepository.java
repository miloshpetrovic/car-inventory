package com.smg.carinventory.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smg.carinventory.domain.model.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

	public List<Car> findByModelManufacturerNameIgnoreCase(String manufacturer);
	
	public List<Car> findByModelManufacturerNameIgnoreCaseAndModelNameIgnoreCase(String manufacturer, String model);

}
