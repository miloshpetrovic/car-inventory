package com.smg.carinventory.presentation.controller;

import static com.smg.carinventory.presentation.controller.EntityModelMapper.entityModelOf;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.smg.carinventory.application.service.ManufacturerService;
import com.smg.carinventory.domain.exception.ManufacturerNameIsEmptyException;
import com.smg.carinventory.domain.exception.ManufacturerNotFoundException;
import com.smg.carinventory.presentation.dto.ManufacturerDTO;


@RestController
@RequestMapping("/smg")
public class ManufacturerController {
	
	private static final String MANUFACTURER_CARS_URI_TEMPLATE = "/smg/manufacturers/{name}/cars";
	private static final String MANUFACTURER_MODELS_URI_TEMPLATE = "/smg/manufacturers/{name}/models";
	private static final String MANUFACTURER_URI_TEMPLATE = "/smg/manufacturers/{name}";
	
	private ManufacturerService manufacturerService;
	
	public ManufacturerController(ManufacturerService manufacturerService) {
		this.manufacturerService = manufacturerService;
	}

	@GetMapping("/manufacturers")
	public Collection<EntityModel<ManufacturerDTO>> getAllManufacturers(){
		var manufacturers = manufacturerService.findAll();
		var selfLink =  Link.of(MANUFACTURER_URI_TEMPLATE).withRel("self");
		var modelsLink =  Link.of(MANUFACTURER_MODELS_URI_TEMPLATE).withRel("models");
		var carsLink =  Link.of(MANUFACTURER_CARS_URI_TEMPLATE).withRel("cars");
		
		return manufacturers.stream().map(manufacturer -> 
			entityModelOf(manufacturer, ManufacturerDTO.class)
				.add(selfLink.expand(manufacturer.getName().toLowerCase()))
				.add(modelsLink.expand(manufacturer.getName().toLowerCase()))
				.add(carsLink.expand(manufacturer.getName().toLowerCase()))).toList();
		
	}
	
	@GetMapping("/manufacturers/{name}")
	public EntityModel<ManufacturerDTO> getManufacturerByName(@PathVariable String name){
		try {
			var manufacturer = manufacturerService.findByName(name);
			var modelsLink =  Link.of(MANUFACTURER_MODELS_URI_TEMPLATE).withRel("models");
			var carsLink =  Link.of(MANUFACTURER_CARS_URI_TEMPLATE).withRel("cars");

			return entityModelOf(manufacturer, ManufacturerDTO.class)
					.add(modelsLink.expand(manufacturer.getName().toLowerCase()))
					.add(carsLink.expand(manufacturer.getName().toLowerCase()));
		} catch (ManufacturerNameIsEmptyException ne) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ne.getMessage());
		} catch (ManufacturerNotFoundException mnfe) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, mnfe.getMessage());
		}
	}
	
	

}
