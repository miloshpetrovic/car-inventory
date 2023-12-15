package com.smg.carinventory.domain.exception;

public class ModelNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3004092871026767286L;

	public ModelNotFoundException() {
		super("Model is not found");
	}
	
}
