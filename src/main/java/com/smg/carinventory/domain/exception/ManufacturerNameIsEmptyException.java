package com.smg.carinventory.domain.exception;

public class ManufacturerNameIsEmptyException extends RuntimeException {

	private static final long serialVersionUID = 1259952449663109276L;

	public ManufacturerNameIsEmptyException() {
		super("Manufacturer name argument is empty");
	}
	
}
