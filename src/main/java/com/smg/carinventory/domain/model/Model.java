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
@Table(name="model")
@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Model {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_id_generator")
	@SequenceGenerator(name="model_id_generator", sequenceName = "model_id_seq", allocationSize = 1)
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "body_type")
	private String bodyType;
	@Column(name = "fuel_type")
	private String fuelType;
	@Column(name = "power")
	private Integer power;
	
	@ManyToOne
	private Manufacturer manufacturer;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		return Objects.equals(name, other.name);
	}
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
}
