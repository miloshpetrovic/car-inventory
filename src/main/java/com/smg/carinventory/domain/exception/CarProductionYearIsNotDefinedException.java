package com.smg.carinventory.domain.exception;

public class CarProductionYearIsNotDefinedException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public CarProductionYearIsNotDefinedException() {
		super("Car production year is not defined");
	}
	
}
