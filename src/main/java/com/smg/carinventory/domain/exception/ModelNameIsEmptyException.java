package com.smg.carinventory.domain.exception;

public class ModelNameIsEmptyException extends RuntimeException {

	private static final long serialVersionUID = 1259952449663109276L;

	public ModelNameIsEmptyException() {
		super("Model name argument is empty");
	}
	
}
