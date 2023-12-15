package com.smg.carinventory.domain.exception;

public class CarMileageIsNotDefinedException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public CarMileageIsNotDefinedException() {
		super("Car mileage is not defined");
	}
	
}
