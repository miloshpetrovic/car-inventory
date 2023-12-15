package com.smg.carinventory.application.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.smg.carinventory.domain.exception.ManufacturerNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNotFoundException;
import com.smg.carinventory.domain.model.Model;
import com.smg.carinventory.infrastructure.repository.ModelRepository;

@Service
public class ModelService {
	
	private ModelRepository modelRepository;
	
	public ModelService(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	public Collection<Model> findByManufacturerName(String name){
		if(name == null || name.isEmpty()) {
			throw new ManufacturerNameIsEmptyException();
		}
		
		return modelRepository.findByManufacturerNameIgnoreCase(name);
	}

	public Model findByManufacturerNameAndModelName(String manufacturer, String name){
		if(manufacturer == null || manufacturer.isEmpty()) {
			throw new ManufacturerNameIsEmptyException();
		}
		
		if(name == null || name.isEmpty()) {
			throw new ModelNameIsEmptyException();
		}
		
		var model =modelRepository
				.findByManufacturerNameIgnoreCaseAndNameIgnoreCase(manufacturer, name); 
		
		return model.orElseThrow(ModelNotFoundException::new);
	}

}
