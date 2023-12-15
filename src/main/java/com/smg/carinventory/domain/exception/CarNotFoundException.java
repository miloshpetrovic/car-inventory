package com.smg.carinventory.domain.exception;

public class CarNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public CarNotFoundException() {
		super("Car is not found");
	}
	
}
