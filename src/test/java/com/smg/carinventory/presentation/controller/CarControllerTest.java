package com.smg.carinventory.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;

import com.smg.carinventory.application.service.CarService;
import com.smg.carinventory.domain.model.Car;
import com.smg.carinventory.domain.model.Manufacturer;
import com.smg.carinventory.domain.model.Model;
import com.smg.carinventory.presentation.dto.CarDTO;
import com.smg.carinventory.presentation.dto.CarDataDTO;

class CarControllerTest {
	
	private static Manufacturer volvo = new Manufacturer(1, "Volvo", "Sweden", 1928, null);
	private static Manufacturer toyota = new Manufacturer(2, "Toyota", "Japan", 1928, null);
			
	private static Model s40 = new Model(1, "S40", "Hatchback", "Petrol", 105, volvo);
	private static Model s60 = new Model(2, "S60", "Hatchback", "Petrol", 115, volvo);
	private static Model s80 = new Model(3, "S80", "Sedan", "Petrol", 125, volvo);
	private static Model corolla = new Model(4, "Corolla", "Sedan", "Petrol", 115, toyota);
	private static Model yaris = new Model(5, "Yaris", "Hatchback", "Petrol", 105, volvo);
	
	private static Car volvoS40 = new Car(1, 110000, 2005, 15000.0, "erfdsd", s80);
	private static Car volvoS60 = new Car(2, 220000, 2008, 10000.0, "faecadv", s40);
	private static Car volvoS80 = new Car(3, 80000, 2015, 13000.0, "faevvxcv", s60);
	private static Car toyotaCorolla = new Car(4, 140000, 2005, 8000.0, "gragare", corolla);
	private static Car toyotaYaris = new Car(5, 110000, 2005, 5000.0, "vaewrhra", yaris);
	private static Car toyotaYarisUpdated = new Car(5, 120000, 2008, 5000.0, "vaewrhra", yaris);
	
	private static CarDTO toyotaYarisDTO = new CarDTO("Toyota", "Yaris", 110000, 2005, 5000.0, "vaewrhra");
	private static CarDTO toyotaYarisUpdatedDTO = new CarDTO("Toyota", "Yaris", 120000, 2008, 5000.0, "vaewrhra");
	private static CarDataDTO toyotaYarisDataDTO = new CarDataDTO(5, 110000, 2005, 5000.0, "vaewrhra");

	private List<Car> allCars = List.of(volvoS40, volvoS60, volvoS80, toyotaCorolla, toyotaYaris);
	private List<Car> volvoCars = List.of(volvoS40, volvoS60, volvoS80);
	private List<Car> volvoS60Cars = List.of(volvoS60);
	
	private CarService carService = Mockito.mock(CarService.class);
	private CarController carController = new CarController(carService);
	
	@Test
	void isAllCarListSizeSameAsOneReturnedByService() {
		when(carService.findAll()).thenReturn(allCars);
		var result = carController.getAllCars();
		assertEquals(allCars.size(), result.size());
	}

	@Test
	void isALLCarSListEmptyAsOneReturnedByService() {
		when(carService.findAll()).thenReturn(Collections.emptyList());
		var result = carController.getAllCars();
		assertEquals(0, result.size());
	}
	
	@Test
	void areALLCarsEntityModelFieldValuesCorrespondToCarFieldValues() {
		when(carService.findAll()).thenReturn(allCars);
		var result = carController.getAllCars();
		for(int i = 0; i < result.size(); i++) {
			var entityModel = result.get(i);
			var carDTO = entityModel.getContent();
			var car = allCars.get(i);
			
			assertEquals(car.getMileage(), carDTO.getMileage());
			assertEquals(car.getPrice(), carDTO.getPrice());
			assertEquals(car.getVin(), carDTO.getVin());
			assertEquals(car.getYear(), carDTO.getYear());
			
			var entityModelSelfLink = entityModel.getLink("self");
			var entityModelModelLink = entityModel.getLink("model");
			
			assertEquals(Link.of(CarController.CAR_URI_TEMPLATE)
					.expand(car.getId()).withRel("self"), 
					entityModelSelfLink.get());
			assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
					.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
							car.getModel().getName().toLowerCase()).withRel("model"), 
					entityModelModelLink.get());
			
		}
	}
	
	@Test
	public void areManufacturerCarsEntityModelFieldValuesCorrespondToCarFieldValues() {
		when(carService.findByManufacturer(anyString())).thenReturn(volvoCars);
		var result = carController.getAllCarsOfManufacturer("volvo");
		for(int i = 0; i < result.size(); i++) {
			var entityModel = result.get(i);
			var carDTO = entityModel.getContent();
			var car = volvoCars.get(i);
			
			assertEquals(car.getMileage(), carDTO.getMileage());
			assertEquals(car.getPrice(), carDTO.getPrice());
			assertEquals(car.getVin(), carDTO.getVin());
			assertEquals(car.getYear(), carDTO.getYear());
			
			var entityModelSelfLink = entityModel.getLink("self");
			var entityModelModelLink = entityModel.getLink("model");
			
			assertEquals(Link.of(CarController.CAR_URI_TEMPLATE)
					.expand(car.getId()).withRel("self"), 
					entityModelSelfLink.get());
			assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
					.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
							car.getModel().getName().toLowerCase()).withRel("model"), 
					entityModelModelLink.get());
			
		}
	}
	
	@Test
	public void areManufacturerModelCarsEntityModelFieldValuesCorrespondToCarFieldValues() {
		when(carService.findByManufacturerModel(anyString(), anyString())).thenReturn(volvoS60Cars);
		var result = carController.getAllCarsOfManufacturerModel("volvo", "s60");
		for(int i = 0; i < result.size(); i++) {
			var entityModel = result.get(i);
			var carDTO = entityModel.getContent();
			var car = volvoS60Cars.get(i);
			
			assertEquals(car.getMileage(), carDTO.getMileage());
			assertEquals(car.getPrice(), carDTO.getPrice());
			assertEquals(car.getVin(), carDTO.getVin());
			assertEquals(car.getYear(), carDTO.getYear());
			
			var entityModelSelfLink = entityModel.getLink("self");
			var entityModelModelLink = entityModel.getLink("model");
			
			assertEquals(Link.of(CarController.CAR_URI_TEMPLATE)
					.expand(car.getId()).withRel("self"), 
					entityModelSelfLink.get());
			assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
					.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
							car.getModel().getName().toLowerCase()).withRel("model"), 
					entityModelModelLink.get());
			
		}
	}

	@Test
	public void isCarEntityModelFieldValuesCorrespondToCarFieldValues() {
		when(carService.findById(anyInt())).thenReturn(toyotaCorolla);
		var entityModel = carController.getCar(4);
	
		var carDTO = entityModel.getContent();
		var car = toyotaCorolla;
				
		assertEquals(car.getMileage(), carDTO.getMileage());
		assertEquals(car.getPrice(), carDTO.getPrice());
		assertEquals(car.getVin(), carDTO.getVin());
		assertEquals(car.getYear(), carDTO.getYear());
				
		var entityModelModelLink = entityModel.getLink("model");
				
	
		assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
				.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
						car.getModel().getName().toLowerCase()).withRel("model"), 
							entityModelModelLink.get());
	}
	
	@Test
	public void areCorrectArgumentsPassedToServiceDuringCarCreation() {
		when(carService.save(anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyString()))
			.thenReturn(toyotaYaris);

		carController.createCar(toyotaYarisDTO);
		
		var manufacturerCaptor = ArgumentCaptor.forClass(String.class);
		var modelCaptor = ArgumentCaptor.forClass(String.class);
		var mileageCaptor = ArgumentCaptor.forClass(Integer.class);
		var yearCaptor = ArgumentCaptor.forClass(Integer.class);
		var	priceCaptor = ArgumentCaptor.forClass(Double.class);
		var vinCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(carService, times(1)).save(manufacturerCaptor.capture(), modelCaptor.capture(), 
				mileageCaptor.capture(), yearCaptor.capture(), priceCaptor.capture(), vinCaptor.capture());

		assertEquals(toyotaYarisDTO.getManufacturer(), manufacturerCaptor.getValue());
		assertEquals(toyotaYarisDTO.getModel(), modelCaptor.getValue());
		assertEquals(toyotaYarisDTO.getMileage(), mileageCaptor.getValue());
		assertEquals(toyotaYarisDTO.getYear(), yearCaptor.getValue());
		assertEquals(toyotaYarisDTO.getPrice(), priceCaptor.getValue());
		assertEquals(toyotaYarisDTO.getVin(), vinCaptor.getValue());
	}
	
	@Test
	public void isCarEntityModelCorrectAfterCarCreation() {
		when(carService.save(anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyString()))
			.thenReturn(toyotaYaris);		
		
		var entityModel = carController.createCar(toyotaYarisDTO);
	
		var carDataDTO = entityModel.getContent();
		var car = toyotaYaris;
				
		assertEquals(car.getMileage(), carDataDTO.getMileage());
		assertEquals(car.getPrice(), carDataDTO.getPrice());
		assertEquals(car.getVin(), carDataDTO.getVin());
		assertEquals(car.getYear(), carDataDTO.getYear());
				
		var entityModelSelfLink = entityModel.getLink("self");
		var entityModelModelLink = entityModel.getLink("model");
		
		assertEquals(Link.of(CarController.CAR_URI_TEMPLATE)
				.expand(car.getId()).withRel("self"), 
				entityModelSelfLink.get());
		assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
				.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
						car.getModel().getName().toLowerCase()).withRel("model"), 
				entityModelModelLink.get());		
	}
	@Test
	public void areCorrectArgumentsPassedToServiceDuringManufacturerModelCarCreation() {
		when(carService.save(anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyString()))
		.thenReturn(toyotaYaris);
		
		carController.createCarForConcreteManufacturerModel(toyotaYarisDataDTO, "toyota", "yaris");
		
		var manufacturerCaptor = ArgumentCaptor.forClass(String.class);
		var modelCaptor = ArgumentCaptor.forClass(String.class);
		var mileageCaptor = ArgumentCaptor.forClass(Integer.class);
		var yearCaptor = ArgumentCaptor.forClass(Integer.class);
		var	priceCaptor = ArgumentCaptor.forClass(Double.class);
		var vinCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(carService, times(1)).save(manufacturerCaptor.capture(), modelCaptor.capture(), 
				mileageCaptor.capture(), yearCaptor.capture(), priceCaptor.capture(), vinCaptor.capture());
		
		assertEquals("toyota", manufacturerCaptor.getValue());
		assertEquals("yaris", modelCaptor.getValue());
		assertEquals(toyotaYarisDataDTO.getMileage(), mileageCaptor.getValue());
		assertEquals(toyotaYarisDataDTO.getYear(), yearCaptor.getValue());
		assertEquals(toyotaYarisDataDTO.getPrice(), priceCaptor.getValue());
		assertEquals(toyotaYarisDataDTO.getVin(), vinCaptor.getValue());
	}
	
	@Test
	public void isManufacturerModelCarEntityModelCorrectAfterCarCreation() {
		when(carService.save(anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyString()))
		.thenReturn(toyotaYaris);		
		
		var entityModel = carController.createCarForConcreteManufacturerModel(toyotaYarisDataDTO, "toyota", "yaris");
		
		var carDataDTO = entityModel.getContent();
		var car = toyotaYaris;
		
		assertEquals(car.getMileage(), carDataDTO.getMileage());
		assertEquals(car.getPrice(), carDataDTO.getPrice());
		assertEquals(car.getVin(), carDataDTO.getVin());
		assertEquals(car.getYear(), carDataDTO.getYear());
		
		var entityModelSelfLink = entityModel.getLink("self");
		var entityModelModelLink = entityModel.getLink("model");
		
		assertEquals(Link.of(CarController.CAR_URI_TEMPLATE)
				.expand(car.getId()).withRel("self"), 
				entityModelSelfLink.get());
		assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
				.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
						car.getModel().getName().toLowerCase()).withRel("model"), 
				entityModelModelLink.get());		
	}

	
	
	@Test
	public void areCorrectArgumentsPassedToServiceDuringCarUpdate() {
		when(carService.update(anyInt(), anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyString()))
		.thenReturn(toyotaYarisUpdated);
		
		carController.updateCar(toyotaYarisUpdatedDTO, 5);
		
		var idCaptor = ArgumentCaptor.forClass(Integer.class);
		var manufacturerCaptor = ArgumentCaptor.forClass(String.class);
		var modelCaptor = ArgumentCaptor.forClass(String.class);
		var mileageCaptor = ArgumentCaptor.forClass(Integer.class);
		var yearCaptor = ArgumentCaptor.forClass(Integer.class);
		var	priceCaptor = ArgumentCaptor.forClass(Double.class);
		var vinCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(carService, times(1)).update(idCaptor.capture(), manufacturerCaptor.capture(),
				modelCaptor.capture(), mileageCaptor.capture(), yearCaptor.capture(), 
				priceCaptor.capture(), vinCaptor.capture());
		
		assertEquals(5, idCaptor.getValue());
		assertEquals(toyotaYarisUpdatedDTO.getManufacturer(), manufacturerCaptor.getValue());
		assertEquals(toyotaYarisUpdatedDTO.getModel(), modelCaptor.getValue());
		assertEquals(toyotaYarisUpdatedDTO.getMileage(), mileageCaptor.getValue());
		assertEquals(toyotaYarisUpdatedDTO.getYear(), yearCaptor.getValue());
		assertEquals(toyotaYarisUpdatedDTO.getPrice(), priceCaptor.getValue());
		assertEquals(toyotaYarisUpdatedDTO.getVin(), vinCaptor.getValue());
	}
	
	@Test
	public void isManufacturerModelCarEntityModelCorrectAfterCarUpdate() {
		when(carService.update(anyInt(), anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyString()))
			.thenReturn(toyotaYarisUpdated);
		
		var entityModel = carController.updateCar(toyotaYarisUpdatedDTO, 5);
		
		var carDataDTO = entityModel.getContent();
		var car = toyotaYarisUpdated;
		
		assertEquals(car.getMileage(), carDataDTO.getMileage());
		assertEquals(car.getPrice(), carDataDTO.getPrice());
		assertEquals(car.getVin(), carDataDTO.getVin());
		assertEquals(car.getYear(), carDataDTO.getYear());
		
		var entityModelModelLink = entityModel.getLink("model");

		assertEquals(Link.of(CarController.MANUFACTURER_MODEL_URI_TEMPLATE)
				.expand(car.getModel().getManufacturer().getName().toLowerCase(), 
						car.getModel().getName().toLowerCase()).withRel("model"), 
				entityModelModelLink.get());		
	}
}
