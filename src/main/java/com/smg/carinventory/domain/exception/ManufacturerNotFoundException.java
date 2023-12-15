package com.smg.carinventory.domain.exception;

public class ManufacturerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public ManufacturerNotFoundException() {
		super("Manufacturer is not found");
	}
	
}
