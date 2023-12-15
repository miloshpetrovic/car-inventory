package com.smg.carinventory.domain.exception;

public class CarVinIsNotDefinedException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public CarVinIsNotDefinedException() {
		super("Car vin is not defined");
	}
	
}
