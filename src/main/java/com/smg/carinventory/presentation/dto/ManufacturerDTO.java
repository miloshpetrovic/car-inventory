package com.smg.carinventory.presentation.dto;

import lombok.Data;

@Data
public class ManufacturerDTO {
	
	private String name;
	private String country;
	private Integer fromYear;
	private Integer toYear;

}
