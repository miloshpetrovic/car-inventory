package com.smg.carinventory.domain.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="car")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_id_generator")
	@SequenceGenerator(name="car_id_generator", sequenceName = "car_id_seq", allocationSize = 1)
	private Integer id;
	@Column(name = "mileage")
	private Integer mileage;
	@Column(name = "first_registration")
	private Integer year;
	@Column(name = "price")
	private Double price;
	@Column(name = "vin")
	private String vin;
	
	@ManyToOne
	private Model model;

	public static Car withId(Integer id) {
		var car = new Car();
		car.setId(id);
		return car;
	}
	
	public String modelName() {
		return model.getName();
	}
	
	public String manufacturerName() {
		return model.getManufacturer().getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(vin, other.vin);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vin);
	}

}
