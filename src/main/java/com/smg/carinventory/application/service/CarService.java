package com.smg.carinventory.application.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smg.carinventory.domain.exception.CarMileageIsNotDefinedException;
import com.smg.carinventory.domain.exception.CarNotFoundException;
import com.smg.carinventory.domain.exception.CarProductionYearIsNotDefinedException;
import com.smg.carinventory.domain.exception.CarSellingPriceIsNotDefinedException;
import com.smg.carinventory.domain.exception.CarVinIsNotDefinedException;
import com.smg.carinventory.domain.exception.ManufacturerNameIsEmptyException;
import com.smg.carinventory.domain.exception.ModelNameIsEmptyException;
import com.smg.carinventory.domain.model.Car;
import com.smg.carinventory.domain.model.CarEvent;
import com.smg.carinventory.infrastructure.repository.CarRepository;

@Retryable
@Service
public class CarService {

	Logger logger = LoggerFactory.getLogger(CarService.class);
	
	private CarRepository carRepository;
	private ModelService modelService;
	private CarEventPublisherService publisherService;
	
	public CarService(CarRepository carRepository, ModelService modelService,
			CarEventPublisherService publisherService) {
		this.carRepository = carRepository;
		this.modelService = modelService;
		this.publisherService = publisherService;
	}

	public List<Car> findAll(){
		return carRepository.findAll();
	}
	
	public List<Car> findByManufacturer(String manufacturer){
		if (manufacturer == null || manufacturer.isEmpty()) {
			throw new ManufacturerNameIsEmptyException();
		}
		
		return carRepository.findByModelManufacturerNameIgnoreCase(manufacturer);
	}
	
	public List<Car> findByManufacturerModel(String manufacturer, String model){
		if (manufacturer == null || manufacturer.isEmpty()) {
			throw new ManufacturerNameIsEmptyException();
		}
		if (model == null || model.isEmpty()) {
			throw new ModelNameIsEmptyException();
		}
		
		return carRepository.findByModelManufacturerNameIgnoreCase(manufacturer);
	}
	
	
	public Car findById(Integer id) {
		logger.info("findAll called");
		var optionalCar = carRepository.findById(id);
		var car = optionalCar.orElseThrow(CarNotFoundException::new);
				
		return car;
	}
	
	@Transactional
	public Car save(String manufacturer, String model, Integer mileage,	
			Integer year, Double price, String vin) {
		var carModel = modelService.findByManufacturerNameAndModelName(manufacturer, model);
		
		if(mileage == null) {
			throw new CarMileageIsNotDefinedException();
		}
		
		if(year == null) {
			throw new CarProductionYearIsNotDefinedException();
		}
		
		if(price == null) {
			throw new CarSellingPriceIsNotDefinedException();
		}
		
		if(vin == null || vin.isEmpty()) {
			throw new CarVinIsNotDefinedException();
		}
		
		var newCar = new Car();
		newCar.setModel(carModel);
		newCar.setMileage(mileage);
		newCar.setYear(year);
		newCar.setPrice(price);
		newCar.setVin(vin);
		
		var car = carRepository.save(newCar);
		publisherService.publish(CarEvent.forCreated(car));
		
		return car;
	}
	
	@Transactional
	public Car update(Integer id, String manufacturer, String model, 
			Integer mileage, Integer year, Double price, String vin) {
		var car = findById(id);
		var carModel = modelService.findByManufacturerNameAndModelName(manufacturer, model);
		
		if(mileage == null) {
			throw new CarMileageIsNotDefinedException();
		}
		
		if(year == null) {
			throw new CarProductionYearIsNotDefinedException();
		}
		
		if(price == null) {
			throw new CarSellingPriceIsNotDefinedException();
		}
		
		if(vin == null || vin.isEmpty()) {
			throw new CarVinIsNotDefinedException();
		}
		
		car.setModel(carModel);
		car.setMileage(mileage);
		car.setYear(year);
		car.setPrice(price);
		car.setVin(vin);
		
		publisherService.publish(CarEvent.forUpdated(car));
		
		return car;
	}
	
	public void deteleById(Integer id) {
		carRepository.deleteById(id);
		publisherService.publish(CarEvent.forDeleted(Car.withId(id)));
	}

}
