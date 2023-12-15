package com.smg.carinventory.domain.exception;

public class CarSellingPriceIsNotDefinedException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public CarSellingPriceIsNotDefinedException() {
		super("Car selling price is not defined");
	}
	
}
