package com.smg.carinventory.application.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.smg.carinventory.domain.exception.ManufacturerNameIsEmptyException;
import com.smg.carinventory.domain.exception.ManufacturerNotFoundException;
import com.smg.carinventory.domain.model.Manufacturer;
import com.smg.carinventory.infrastructure.repository.ManufacturerRepository;

@Service
public class ManufacturerService {

	private ManufacturerRepository manufacturerRepository;

	public ManufacturerService(ManufacturerRepository manufacturerRepository) {
		this.manufacturerRepository = manufacturerRepository;
	}

	public Collection<Manufacturer> findAll() {
		return manufacturerRepository.findAll();
	}

	public Manufacturer findByName(String name) {
		if(name == null || name.isEmpty()) {
			throw new ManufacturerNameIsEmptyException();
		}
		
		var manufacturer = manufacturerRepository.findByNameIgnoreCase(name);
		
		return manufacturer.orElseThrow(ManufacturerNotFoundException::new);
	}

}
