package com.smg.carinventory.presentation.controller;

import java.util.List;

import com.smg.carinventory.domain.model.Car;
import com.smg.carinventory.domain.model.Manufacturer;
import com.smg.carinventory.domain.model.Model;
import com.smg.carinventory.presentation.dto.CarDTO;
import com.smg.carinventory.presentation.dto.CarDataDTO;

public class CarTestData {
	
	public static Manufacturer volvo = new Manufacturer(1, "Volvo", "Sweden", 1928, null);
	public static Manufacturer toyota = new Manufacturer(2, "Toyota", "Japan", 1928, null);
			
	public static Model s40 = new Model(1, "S40", "Hatchback", "Petrol", 105, volvo);
	public static Model s60 = new Model(2, "S60", "Hatchback", "Petrol", 115, volvo);
	public static Model s80 = new Model(3, "S80", "Sedan", "Petrol", 125, volvo);
	public static Model corolla = new Model(4, "Corolla", "Sedan", "Petrol", 115, toyota);
	public static Model yaris = new Model(5, "Yaris", "Hatchback", "Petrol", 105, volvo);
	
	public static Car volvoS40 = new Car(1, 110000, 2005, 15000.0, "erfdsd", s40);
	public static Car volvoS60 = new Car(2, 220000, 2008, 10000.0, "faecadv", s60);
	public static Car volvoS80 = new Car(3, 80000, 2015, 13000.0, "faevvxcv", s80);
	public static Car toyotaCorolla = new Car(4, 140000, 2005, 8000.0, "gragare", corolla);
	public static Car toyotaYaris = new Car(5, 110000, 2005, 5000.0, "vaewrhra", yaris);
	public static Car toyotaYarisUpdated = new Car(5, 120000, 2008, 5000.0, "vaewrhra", yaris);
	
	public static CarDTO toyotaYarisDTO = new CarDTO("Toyota", "Yaris", 110000, 2005, 5000.0, "vaewrhra");
	public static CarDTO toyotaYarisUpdatedDTO = new CarDTO("Toyota", "Yaris", 120000, 2008, 5000.0, "vaewrhra");
	public static CarDataDTO toyotaYarisDataDTO = new CarDataDTO(5, 110000, 2005, 5000.0, "vaewrhra");

	public static List<Car> allCars = List.of(volvoS40, volvoS60, volvoS80, toyotaCorolla, toyotaYaris);
	public static List<Car> volvoCars = List.of(volvoS40, volvoS60, volvoS80);
	public static List<Car> volvoS60Cars = List.of(volvoS60);
	
}
