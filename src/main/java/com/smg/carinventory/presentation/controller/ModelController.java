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

import com.smg.carinventory.application.service.ModelService;
import com.smg.carinventory.domain.exception.ManufacturerNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNotFoundException;
import com.smg.carinventory.presentation.dto.ModelDTO;

@RestController
@RequestMapping("/smg")
public class ModelController {
	
	private static final String MANUFACTURER_MODEL_CARS_URI_TEMPLATE = "/smg/manufacturers/{manufacturer}/models/{model}/cars";
	private static final String MANUFACTURER_URI_TEMPLATE = "/smg/manufacturers/{name}";
	private static final String MANUFACTURER_MODEL_URI_TEMPLATE = "/smg/manufacturers/{name}/models/{model}";
	
	private ModelService modelService;
	
	public ModelController(ModelService modelService) {
		this.modelService = modelService;
	}

	@GetMapping("/manufacturers/{manufacturer}/models")
	public Collection<EntityModel<ModelDTO>> getAllManufacturerModels(@PathVariable String manufacturer){
		try {
			
			var models = modelService.findByManufacturerName(manufacturer);
			
			var selfLink = Link.of(MANUFACTURER_MODEL_URI_TEMPLATE).withRel("self");
			var manufacturerLink =  Link.of(MANUFACTURER_URI_TEMPLATE).withRel("manufacturer");
			var carsLink =  Link.of(MANUFACTURER_MODEL_CARS_URI_TEMPLATE).withRel("cars");
			
			return models.stream().map(model -> 
				entityModelOf(model, ModelDTO.class)
					.add(selfLink.expand(manufacturer, model.getName().toLowerCase()))
					.add(manufacturerLink.expand(manufacturer))
					.add(carsLink.expand(manufacturer, model.getName().toLowerCase()))).toList();
			
		} catch (ManufacturerNameIsEmptyException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/manufacturers/{manufacturer}/models/{name}")
	public EntityModel<ModelDTO> getManufacturerModel(@PathVariable String manufacturer, 
			@PathVariable String name){
		try {
			
			var model = modelService.findByManufacturerNameAndModelName(manufacturer, name);
			
			var manufacturerLink =  Link.of(MANUFACTURER_URI_TEMPLATE).withRel("manufacturer");
			var carsLink =  Link.of(MANUFACTURER_MODEL_CARS_URI_TEMPLATE).withRel("cars");
			
			return entityModelOf(model, ModelDTO.class)
					.add(manufacturerLink.expand(manufacturer))
					.add(carsLink.expand(manufacturer, model.getName().toLowerCase()));
			
		} catch (ManufacturerNameIsEmptyException ne) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ne.getMessage());
		} catch (ModelNameIsEmptyException mne) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mne.getMessage());
		} catch (ModelNotFoundException mnfe) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, mnfe.getMessage());
		}
	}
}
