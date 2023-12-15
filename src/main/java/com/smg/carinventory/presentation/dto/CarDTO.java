package com.smg.carinventory.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

	private String manufacturer;
	private String model;
	private Integer mileage;
	private Integer year;
	private Double price;
	private String vin;
	
}
