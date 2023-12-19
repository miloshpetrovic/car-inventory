package com.smg.carinventory.application.service;

import static com.smg.carinventory.presentation.controller.CarTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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

class CarServiceTest {
	
	private CarRepository carRepository = Mockito.mock(CarRepository.class);
	private ModelService modelService = Mockito.mock(ModelService.class);
	private CarEventPublisherService publishService = Mockito.mock(CarEventPublisherService.class);
	
	private CarService carService = new CarService(carRepository, modelService, publishService);
	

	@Test
	void doesFindByManufacturerThrowsExceptionWhenArgumentIsEmpty() {
		assertThrows(ManufacturerNameIsEmptyException.class,
				() -> carService.findByManufacturer("") );
	}

	@Test
	void doesFindByManufacturerThrowsExceptionWhenArgumentIsNull() {
		assertThrows(ManufacturerNameIsEmptyException.class,
				() -> carService.findByManufacturer(null) );
	}
	
	
	@Test
	void doesFindByManufacturerPassesCorrectArgumentsToRepository() {
		when(carRepository.findByModelManufacturerNameIgnoreCase(anyString()))
		.thenReturn(Collections.emptyList());
		
		carService.findByManufacturer("volvo");	
		var manufacturerCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(carRepository, times(1)).findByModelManufacturerNameIgnoreCase(manufacturerCaptor.capture());		
		assertEquals("volvo", manufacturerCaptor.getValue());
		
	}
	
	@Test
	void doesFindByManufacturerReturnsListPassedFromRepository() {
		when(carRepository.findByModelManufacturerNameIgnoreCase(anyString()))
			.thenReturn(volvoCars);
		
		var cars = carService.findByManufacturer("volvo");	
		
		assertIterableEquals(cars, volvoCars);
		
	}
	
	
	
	@Test
	void doesFindByManufacturerModelThrowsExceptionWhenArgumentsAreEmptyOrNull() {
		assertThrows(ManufacturerNameIsEmptyException.class,
				() -> carService.findByManufacturerModel("", "model") );
		assertThrows(ManufacturerNameIsEmptyException.class,
				() -> carService.findByManufacturerModel(null, "model") );

		assertThrows(ModelNameIsEmptyException.class,
				() -> carService.findByManufacturerModel("volvo", "") );
		assertThrows(ModelNameIsEmptyException.class,
				() -> carService.findByManufacturerModel("volvo", null) );
	}

	
	
	@Test
	void doesFindByManufacturerModelPassesCorrectArgumentsToRepository() {
		when(carRepository.findByModelManufacturerNameIgnoreCaseAndModelNameIgnoreCase(anyString(), anyString()))
		.thenReturn(Collections.emptyList());
		
		carService.findByManufacturerModel("volvo", "s40");	
		var manufacturerCaptor = ArgumentCaptor.forClass(String.class);
		var modelCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(carRepository, times(1))
			.findByModelManufacturerNameIgnoreCaseAndModelNameIgnoreCase(
					manufacturerCaptor.capture(), modelCaptor.capture());		
		assertEquals("volvo", manufacturerCaptor.getValue());
		assertEquals("s40", modelCaptor.getValue());
	}
	
	@Test
	void doesFindByManufacturerModelReturnsListPassedFromRepository() {
		when(carRepository.findByModelManufacturerNameIgnoreCaseAndModelNameIgnoreCase(anyString(), anyString()))
			.thenReturn(volvoCars);
		
		var cars = carService.findByManufacturerModel("volvo", "s40");	
		
		assertIterableEquals(cars, volvoCars);
		
	}
		
	@Test
	void doesFindByIdThrowsExceptionWhenCarIsNotFound() {
		when(carRepository.findById(anyInt()))
		.thenReturn(Optional.empty());
		assertThrows(CarNotFoundException.class,
				() -> carService.findById(1) );
	}
	
	@Test
	void doesFindByIdPassesCorrectArgumentToRepository() {
		when(carRepository.findById(anyInt()))
		.thenReturn(Optional.of(volvoS40));
		
		carService.findById(1);
		var carIdCaptor = ArgumentCaptor.forClass(Integer.class);
		
		verify(carRepository, times(1)).findById(carIdCaptor.capture());
		assertEquals(1, carIdCaptor.getValue());
	}
	
	
	@Test
	void doesFindByIdReturnsCarPassedFromRepository() {
		when(carRepository.findById(anyInt()))
		.thenReturn(Optional.of(volvoS40));
		
		var car = carService.findById(1);
		
		assertEquals(volvoS40, car);
	}
	
	
	@Test
	void doesSaveThrowsExceptionWhenArgumentsAreEmptyOrNull() {
		when(modelService.findByManufacturerNameAndModelName(anyString(), anyString()))
			.thenThrow(ManufacturerNameIsEmptyException.class)
			.thenThrow(ModelNameIsEmptyException.class)
			.thenReturn(s40, s40, s40, s40, s40);
		
		assertThrows(ManufacturerNameIsEmptyException.class,
				() -> carService.save("volvo", "s40", 1234, 2010, 2000.0, "vin") );
		assertThrows(ModelNameIsEmptyException.class,
				() -> carService.save("volvo", "s40", 1234, 2010, 2000.0, "vin"));
		assertThrows(CarMileageIsNotDefinedException.class,
				() ->	carService.save("volvo", "s40", null, 2010, 2000.0, "vin") );
		assertThrows(CarProductionYearIsNotDefinedException.class,
				() -> carService.save("volvo", "s40", 1234, null, 2000.0, "vin") );
		assertThrows(CarSellingPriceIsNotDefinedException.class,
				() -> carService.save("volvo", "s40", 1234, 2010, null, "vin") );
		assertThrows(CarVinIsNotDefinedException.class,
				() -> carService.save("volvo", "s40", 1234, 2010, 2000.0, "") );
		assertThrows(CarVinIsNotDefinedException.class,
				() -> carService.save("volvo", "s40", 1234, 2010, 2000.0, null) );
	}
	
	@Test
	void doesSavePassesCarWithCorrectFieldsToRepository() {
		when(modelService.findByManufacturerNameAndModelName(anyString(), anyString()))
			.thenReturn(s40);
		
		carService.save("volvo", "s40", 1234, 2010, 2000.0, "vin");
		
		var carCaptor = ArgumentCaptor.forClass(Car.class);
		
		verify(carRepository, times(1)).save(carCaptor.capture());
		
		assertEquals("volvo", carCaptor.getValue().getModel().getManufacturer().getName().toLowerCase());
		assertEquals("s40", carCaptor.getValue().getModel().getName().toLowerCase());
		assertEquals(1234, carCaptor.getValue().getMileage());
		assertEquals(2010, carCaptor.getValue().getYear());
		assertEquals(2000.0, carCaptor.getValue().getPrice());
		assertEquals("vin", carCaptor.getValue().getVin());
	}

	@Test
	void doesSavePassesCorrectEventToPublisher() {
		when(modelService.findByManufacturerNameAndModelName(anyString(), anyString()))
			.thenReturn(s40);
		when(carRepository.save(any(Car.class)))
			.thenReturn(volvoS40);
		
		carService.save("volvo", "s40", 
				volvoS40.getMileage(), 
				volvoS40.getYear(), 
				volvoS40.getPrice(), 
				volvoS40.getVin());
		
		var carEventCaptor = ArgumentCaptor.forClass(CarEvent.class);
		
		verify(publishService, times(1)).publish(carEventCaptor.capture());
		assertEquals(volvoS40, carEventCaptor.getValue().getCar());
		assertEquals(CarEvent.Event.CREATED, carEventCaptor.getValue().getEvent());
	}
	
	
	/////
	
	
	@Test
	void doesUpdateThrowsExceptionWhenArgumentsAreEmptyOrNull() {
		when(carRepository.findById(anyInt()))
			.thenReturn(Optional.empty())
			.thenReturn(Optional.of(volvoS40))
			.thenReturn(Optional.of(volvoS40))
			.thenReturn(Optional.of(volvoS40))
			.thenReturn(Optional.of(volvoS40))
			.thenReturn(Optional.of(volvoS40))
			.thenReturn(Optional.of(volvoS40))
			.thenReturn(Optional.of(volvoS40));
		
		when(modelService.findByManufacturerNameAndModelName(anyString(), anyString()))
			.thenThrow(ManufacturerNameIsEmptyException.class)
			.thenThrow(ModelNameIsEmptyException.class)
			.thenReturn(s40, s40, s40, s40, s40);
		
		assertThrows(CarNotFoundException.class,
				() -> carService.update(31, "", "s40", 1234, 2010, 2000.0, "vin") );
		assertThrows(ManufacturerNameIsEmptyException.class,
				() -> carService.update(1, "", "s40", 1234, 2010, 2000.0, "vin") );
		assertThrows(ModelNameIsEmptyException.class,
				() -> carService.update(1, "volvo", "", 1234, 2010, 2000.0, "vin"));
		assertThrows(CarMileageIsNotDefinedException.class,
				() -> carService.update(1, "volvo", "s40", null, 2010, 2000.0, "vin") );
		assertThrows(CarProductionYearIsNotDefinedException.class,
				() -> carService.update(1, "volvo", "s40", 1234, null, 2000.0, "vin") );
		assertThrows(CarSellingPriceIsNotDefinedException.class,
				() -> carService.update(1, "volvo", "s40", 1234, 2010, null, "vin") );
		assertThrows(CarVinIsNotDefinedException.class,
				() -> carService.update(1, "volvo", "s40", 1234, 2010, 2000.0, "") );
		assertThrows(CarVinIsNotDefinedException.class,
				() -> carService.update(1, "volvo", "s40", 1234, 2010, 2000.0, null) );
	}
	
	@Test
	void doesUpdatePassesCarWithCorrectIdAndFieldsToRepositoryAndPublisher() {
		when(carRepository.findById(anyInt()))
			.thenReturn(Optional.of(volvoS40));
		when(modelService.findByManufacturerNameAndModelName(anyString(), anyString()))
			.thenReturn(s80);
		
		carService.update(1, "volvo", "s80", 1234, 2010, 2000.0, "vin");
		
		var carIdCaptor = ArgumentCaptor.forClass(Integer.class);
		var manufacturerCaptor = ArgumentCaptor.forClass(String.class);
		var modelCaptor = ArgumentCaptor.forClass(String.class);
		var carEventCaptor = ArgumentCaptor.forClass(CarEvent.class);
		
		verify(carRepository, times(1)).findById(carIdCaptor.capture());
		verify(modelService, times(1))
			.findByManufacturerNameAndModelName(manufacturerCaptor.capture(), modelCaptor.capture());
		verify(publishService, times(1)).publish(carEventCaptor.capture());

		assertEquals(1, carIdCaptor.getValue());
		assertEquals("volvo", manufacturerCaptor.getValue());
		assertEquals("s80", modelCaptor.getValue());
		assertEquals(s80, carEventCaptor.getValue().getCar().getModel());
		assertEquals(1234, carEventCaptor.getValue().getCar().getMileage());
		assertEquals(2010, carEventCaptor.getValue().getCar().getYear());
		assertEquals(2000.0, carEventCaptor.getValue().getCar().getPrice());
		assertEquals("vin", carEventCaptor.getValue().getCar().getVin());
	}

}
