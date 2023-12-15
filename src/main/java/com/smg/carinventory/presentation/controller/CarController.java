package com.smg.carinventory.presentation.controller;

import static com.smg.carinventory.presentation.controller.EntityModelMapper.entityModelOf;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.smg.carinventory.application.service.CarService;
import com.smg.carinventory.domain.exception.CarMileageIsNotDefinedException;
import com.smg.carinventory.domain.exception.CarNotFoundException;
import com.smg.carinventory.domain.exception.CarProductionYearIsNotDefinedException;
import com.smg.carinventory.domain.exception.CarSellingPriceIsNotDefinedException;
import com.smg.carinventory.domain.exception.CarVinIsNotDefinedException;
import com.smg.carinventory.domain.exception.ManufacturerNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNotFoundException;
import com.smg.carinventory.presentation.dto.CarDTO;
import com.smg.carinventory.presentation.dto.CarDataDTO;

@RestController
@RequestMapping("/smg")
public class CarController {
	
	public static final String MANUFACTURER_MODEL_URI_TEMPLATE = "/smg/manufacturers/{manufacturer}/models/{model}";
	public static final String CAR_URI_TEMPLATE = "/smg/cars/{id}";
	
	private CarService carService;
	
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@GetMapping("/cars")
	public List<EntityModel<CarDataDTO>> getAllCars(){
		var cars = carService.findAll();
		
		var selfLink =  Link.of(CAR_URI_TEMPLATE)
				.withRel(LinkRelation.of("self"));
		var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
				.withRel(LinkRelation.of("model"));
		
		return cars.stream().map(car -> 
			entityModelOf(car, CarDataDTO.class)
				.add(selfLink.expand(car.getId()))
				.add(modelLink.expand(car.manufacturerName().toLowerCase(), 
						car.modelName().toLowerCase()))).toList();
	}
	
	@GetMapping("/manufacturers/{manufacturer}/cars")
	public List<EntityModel<CarDataDTO>> getAllCarsOfManufacturer(@PathVariable String manufacturer){
		try {
			var cars = carService.findByManufacturer(manufacturer);
			
			var selfLink =  Link.of(CAR_URI_TEMPLATE)
					.withRel(LinkRelation.of("self"));
			var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
					.withRel(LinkRelation.of("model"));
			
			return cars.stream().map(car -> 
				entityModelOf(car, CarDataDTO.class)
					.add(selfLink.expand(car.getId()))
					.add(modelLink.expand(car.manufacturerName().toLowerCase(), 
							car.modelName().toLowerCase()))).toList();
		} catch (ManufacturerNameIsEmptyException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/manufacturers/{manufacturer}/models/{model}/cars")
	public List<EntityModel<CarDataDTO>> getAllCarsOfManufacturerModel(@PathVariable String manufacturer, 
			@PathVariable String model){
		try {
			
			var cars = carService.findByManufacturerModel(manufacturer, model);
			
			var selfLink =  Link.of(CAR_URI_TEMPLATE)
					.withRel(LinkRelation.of("self"));
			var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
					.withRel(LinkRelation.of("model"));
			
			return cars.stream().map(car -> 
				entityModelOf(car, CarDataDTO.class)
					.add(selfLink.expand(car.getId()))
					.add(modelLink.expand(car.manufacturerName().toLowerCase(), 
							car.modelName().toLowerCase()))).toList();
			
		} catch ( ManufacturerNameIsEmptyException
				| ModelNameIsEmptyException mne) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mne.getMessage());
		}
	}
	
	@GetMapping("/cars/{id}")
	public EntityModel<CarDataDTO> getCar(@PathVariable Integer id){
		var car = carService.findById(id);
		try {
			var model = car.getModel();
			var manufacturer = model.getManufacturer();
			var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
					.withRel(LinkRelation.of("model"));
			
			return entityModelOf(car, CarDataDTO.class)
					.add(modelLink.expand(manufacturer.getName().toLowerCase(), 
							model.getName().toLowerCase()));
		} catch (CarNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/cars")
	public EntityModel<CarDataDTO> createCar(@RequestBody CarDTO car){
		try {
			
			var savedCar = carService.save(car.getManufacturer(), car.getModel(), 
					car.getMileage(), car.getYear(), car.getPrice(), car.getVin());		
			var model = savedCar.getModel();
			var manufacturer = model.getManufacturer();
			var selfLink =  Link.of(CAR_URI_TEMPLATE).withRel(LinkRelation.of("self"));
			var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
					.withRel(LinkRelation.of("model"));

			return entityModelOf(savedCar, CarDataDTO.class)
					.add(selfLink.expand(savedCar.getId()))
					.add(modelLink.expand(manufacturer.getName().toLowerCase(), 
							model.getName().toLowerCase()));
			
		} catch ( ManufacturerNameIsEmptyException 
				| ModelNameIsEmptyException 
				| ModelNotFoundException 
				| CarMileageIsNotDefinedException 
				| CarProductionYearIsNotDefinedException 
				| CarSellingPriceIsNotDefinedException 
				| CarVinIsNotDefinedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PostMapping("/manufacturers/{manufacturer}/models/{model}/cars")
	public EntityModel<CarDataDTO> createCarForConcreteManufacturerModel(@RequestBody CarDataDTO car, 
			@PathVariable String manufacturer, @PathVariable String model){
		try {
			
			var savedCar = carService.save(manufacturer, model, 
					car.getMileage(), car.getYear(), car.getPrice(), car.getVin());		
			var carModel = savedCar.getModel();
			var modelManufacturer = carModel.getManufacturer();
			
			var selfLink =  Link.of(CAR_URI_TEMPLATE).withRel(LinkRelation.of("self"));
			var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
					.withRel(LinkRelation.of("model"));
			
			return entityModelOf(savedCar, CarDataDTO.class)
					.add(selfLink.expand(savedCar.getId()))
					.add(modelLink.expand(modelManufacturer.getName().toLowerCase(), 
							carModel.getName().toLowerCase()));
			
		} catch ( ManufacturerNameIsEmptyException 
				| ModelNameIsEmptyException 
				| ModelNotFoundException 
				| CarMileageIsNotDefinedException 
				| CarProductionYearIsNotDefinedException 
				| CarSellingPriceIsNotDefinedException 
				| CarVinIsNotDefinedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/cars/{id}")
	public EntityModel<CarDataDTO> updateCar(@RequestBody CarDTO car, @PathVariable Integer id){
		try {
			
			var updatedCar = carService.update(id, 
					car.getManufacturer(), car.getModel(), 
					car.getMileage(), car.getYear(), 
					car.getPrice(), car.getVin());
			var carModel = updatedCar.getModel();
			var modelManufacturer = carModel.getManufacturer();
			var modelLink =  Link.of(MANUFACTURER_MODEL_URI_TEMPLATE)
					.withRel(LinkRelation.of("model"));
			
			return entityModelOf(car, CarDataDTO.class)
					.add(modelLink.expand(modelManufacturer.getName().toLowerCase(), 
							carModel.getName().toLowerCase()));
			
		} catch ( ManufacturerNameIsEmptyException 
				| ModelNameIsEmptyException 
				| ModelNotFoundException 
				| CarMileageIsNotDefinedException 
				| CarProductionYearIsNotDefinedException 
				| CarSellingPriceIsNotDefinedException 
				| CarVinIsNotDefinedException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/cars/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		carService.deteleById(id);
		return ResponseEntity.noContent().build();
	}
	

}
