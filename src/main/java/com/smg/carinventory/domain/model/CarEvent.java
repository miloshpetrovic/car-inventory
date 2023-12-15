package com.smg.carinventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarEvent {
	
	public static enum Event{
		CREATED, UPDATED, DELETED;
	}
	
	private Event event;
	private Car car;

	public static CarEvent forCreated(Car car) {
		return new CarEvent(Event.CREATED, car);
	}

	public static CarEvent forUpdated(Car car) {
		return new CarEvent(Event.UPDATED, car);
	}
	
	public static CarEvent forDeleted(Car car) {
		return new CarEvent(Event.DELETED, car);
	}
	
}
