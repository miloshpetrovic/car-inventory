package com.smg.carinventory.presentation.controller;

import static com.smg.carinventory.presentation.controller.CarTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;

import com.smg.carinventory.application.service.CarService;

class CarControllerTest {

	
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
